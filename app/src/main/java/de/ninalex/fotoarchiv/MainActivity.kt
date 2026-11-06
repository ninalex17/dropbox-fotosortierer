package de.ninalex.fotoarchiv

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64
import org.json.JSONObject

/** First native milestone: Dropbox OAuth with offline refresh and read-only source-folder count.
 *  No files are modified or deleted in this version. */
class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("fotoarchiv", MODE_PRIVATE) }
    private lateinit var status: TextView
    private lateinit var progress: TextView
    private lateinit var source: EditText
    private lateinit var connect: Button
    private val main = Handler(Looper.getMainLooper())
    private val redirect = "fotoarchiv://oauth"
    private val appKey get() = BuildConfig.DROPBOX_APP_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        draw()
        intent?.data?.let { processRedirect(it) }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent.data?.let { processRedirect(it) }
    }

    private fun draw() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(42, 54, 42, 32)
            setBackgroundColor(Color.rgb(242, 247, 248))
        }
        val heading = TextView(this).apply { text = "Meine Erinnerungen"; textSize = 27f; setTextColor(Color.rgb(24, 59, 70)) }
        root.addView(heading)
        root.addView(TextView(this).apply { text = "Dein persönliches Fotoarchiv"; textSize = 14f; setPadding(0, 8, 0, 34) })
        status = TextView(this).apply { textSize = 18f; setPadding(0, 0, 0, 16) }
        root.addView(status)
        connect = Button(this).apply { text = "Mit Dropbox verbinden"; setOnClickListener { beginLogin() } }
        root.addView(connect)
        root.addView(TextView(this).apply { text = "QUELLORDNER"; setPadding(0, 38, 0, 6) })
        source = EditText(this).apply { setSingleLine(true); setText(prefs.getString("source", "/Kamera-Uploads")) }
        root.addView(source)
        val scan = Button(this).apply { text = "Ordner prüfen (nur lesen)"; setOnClickListener { scanFolder() } }
        root.addView(scan)
        progress = TextView(this).apply { text = "Noch keine Analyse gestartet."; setPadding(0, 24, 0, 24); textSize = 15f }
        root.addView(progress)
        root.addView(TextView(this).apply {
            text = "Version 0.1 · Sicherer Testmodus: keine Dateien werden verschoben oder gelöscht. Hintergrundverarbeitung, Fotobücher und Favoriten folgen in späteren Versionen."
            textSize = 13f
        })
        setContentView(ScrollView(this).apply { addView(root) })
        updateStatus()
    }

    private fun updateStatus() {
        val connected = SecureTokenStore(this).read() != null
        status.text = if (connected) "● Dropbox-Anmeldung gespeichert" else "● Dropbox nicht verbunden"
        status.setTextColor(if (connected) Color.rgb(0, 119, 98) else Color.rgb(170, 48, 48))
        connect.text = if (connected) "Dropbox-Konto erneut verbinden" else "Mit Dropbox verbinden"
    }

    private fun randomUrlSafe(n: Int): String {
        val bytes = ByteArray(n); SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    private fun beginLogin() {
        if (appKey.isBlank()) { message("Bitte zuerst den Dropbox App Key in gradle.properties eintragen."); return }
        val verifier = randomUrlSafe(32)
        val challenge = Base64.encodeToString(MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray()), Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        val state = randomUrlSafe(20)
        prefs.edit().putString("oauth_verifier", verifier).putString("oauth_state", state).apply()
        val url = Uri.parse("https://www.dropbox.com/oauth2/authorize").buildUpon()
            .appendQueryParameter("client_id", appKey)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("token_access_type", "offline")
            .appendQueryParameter("code_challenge", challenge)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("redirect_uri", redirect)
            .appendQueryParameter("state", state).build()
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }

    private fun processRedirect(uri: Uri) {
        if (uri.scheme != "fotoarchiv" || uri.host != "oauth") return
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        val verifier = prefs.getString("oauth_verifier", null)
        if (code.isNullOrEmpty() || state != prefs.getString("oauth_state", null) || verifier.isNullOrEmpty()) {
            message("Anmeldung abgebrochen oder Sicherheitsprüfung fehlgeschlagen."); return
        }
        // Clear one-time PKCE material before exchange; prevents accidental replay.
        prefs.edit().remove("oauth_state").remove("oauth_verifier").apply()
        progress.text = "Dropbox-Anmeldung wird abgeschlossen …"
        Thread {
            try {
                val json = postForm("https://api.dropboxapi.com/oauth2/token", mapOf(
                    "code" to code, "grant_type" to "authorization_code", "client_id" to appKey,
                    "code_verifier" to verifier, "redirect_uri" to redirect))
                val refresh = json.optString("refresh_token")
                if (refresh.isEmpty()) error("Dropbox hat keinen Refresh-Token zurückgegeben.")
                SecureTokenStore(this).write(refresh)
                main.post { updateStatus(); progress.text = "Dropbox verbunden. Du kannst den Quellordner prüfen." }
            } catch (e: Exception) { main.post { progress.text = "Anmeldung fehlgeschlagen: ${e.message}" } }
        }.start()
    }

    private fun accessToken(): String {
        val refresh = SecureTokenStore(this).read() ?: error("Bitte zuerst Dropbox verbinden.")
        val json = postForm("https://api.dropboxapi.com/oauth2/token", mapOf(
            "grant_type" to "refresh_token", "refresh_token" to refresh, "client_id" to appKey))
        return json.getString("access_token")
    }

    private fun scanFolder() {
        val path = source.text.toString().trim().trimEnd('/')
        if (!path.startsWith('/') || path == "/") { message("Bitte einen Dropbox-Unterordner angeben."); return }
        prefs.edit().putString("source", path).apply()
        progress.text = "Lese Dropbox-Ordner …"
        Thread {
            try {
                val token = accessToken()
                var cursor: String? = null
                var total = 0
                var media = 0
                var pages = 0
                do {
                    val endpoint = if (cursor == null) "list_folder" else "list_folder/continue"
                    val payload = if (cursor == null) JSONObject().put("path", path).put("recursive", true).put("limit", 1000)
                                  else JSONObject().put("cursor", cursor)
                    val result = postJson("https://api.dropboxapi.com/2/files/$endpoint", payload, token)
                    val entries = result.getJSONArray("entries")
                    for (i in 0 until entries.length()) {
                        val entry = entries.getJSONObject(i)
                        if (entry.optString(".tag") != "file") continue
                        total++
                        if (Regex("(?i)\\.(jpg|jpeg|png|heic|heif|webp|gif|mp4|mov|m4v|avi|mkv)$").containsMatchIn(entry.optString("name"))) media++
                    }
                    cursor = if (result.optBoolean("has_more")) result.getString("cursor") else null
                    pages++
                    val displayTotal = total; val displayMedia = media
                    main.post { progress.text = "Bisher gefunden: $displayTotal Dateien, davon $displayMedia Fotos/Videos. Einlesen läuft …" }
                } while (cursor != null)
                val summary = "Prüfung abgeschlossen: $total Dateien, davon $media Fotos/Videos. Es wurde nichts verändert."
                main.post { progress.text = summary }
            } catch (e: Exception) { main.post { progress.text = "Prüfung fehlgeschlagen: ${e.message}"; updateStatus() } }
        }.start()
    }

    private fun postForm(url: String, fields: Map<String, String>): JSONObject {
        val body = fields.entries.joinToString("&") { "${URLEncoder.encode(it.key, "UTF-8")}=${URLEncoder.encode(it.value, "UTF-8")}" }
        return request(url, body, "application/x-www-form-urlencoded", null)
    }
    private fun postJson(url: String, data: JSONObject, token: String) = request(url, data.toString(), "application/json", token)
    private fun request(url: String, body: String, type: String, token: String?): JSONObject {
        val conn = URL(url).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "POST"; conn.connectTimeout = 20000; conn.readTimeout = 30000
            conn.setRequestProperty("Content-Type", type)
            if (token != null) conn.setRequestProperty("Authorization", "Bearer $token")
            conn.doOutput = true
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val stream = if (conn.responseCode in 200..299) conn.inputStream else conn.errorStream
            val response = stream?.bufferedReader()?.use { it.readText() } ?: ""
            if (conn.responseCode !in 200..299) error("Dropbox HTTP ${conn.responseCode}: ${response.take(180)}")
            return JSONObject(response)
        } finally { conn.disconnect() }
    }
    private fun message(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
