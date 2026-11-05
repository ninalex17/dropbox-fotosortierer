# Dropbox Fotosortierer – erste Testversion

Dies ist eine **statische** Web-App ohne eigenen Server. Sie muss trotzdem auf einer HTTPS-Adresse bereitgestellt werden, damit Dropbox OAuth PKCE funktioniert (z. B. kostenlos über GitHub Pages oder Cloudflare Pages). `file://` funktioniert nicht.

## Einrichtung
1. Die vier Web-Dateien (`index.html`, `app.js`, `manifest.webmanifest`, `sw.js`) auf einem HTTPS-Static-Host veröffentlichen.
2. Unter https://www.dropbox.com/developers/apps eine neue **Scoped access / Full Dropbox** App anlegen. **Full Dropbox** ist erforderlich, wenn beliebige Quellordner ausgewählt werden sollen. Die App nur für das eigene Konto verwenden.
3. Im Dropbox-App-Dashboard unter Permissions `files.metadata.read`, `files.content.read`, `files.content.write` aktivieren. Die Berechtigungen vor der ersten Autorisierung setzen.
4. Unter OAuth 2 Redirect URIs die exakte URL der veröffentlichten `index.html`-Seite eintragen (einschließlich abschließendem `/`, falls vorhanden).
5. In der Web-App den **App Key** eintragen und „Mit Dropbox anmelden“ wählen. **Niemals den App Secret in die Web-App eintragen.**
6. Quellordner `/Kamera-Uploads` und Zielordner `/Fotos und Videos` belassen, dann „Ordner vollständig einlesen“ wählen.
7. Die Vorschau sorgfältig prüfen. Erst danach einzeln Verschiebungen und ggf. Duplikate bestätigen.

## Grenzen dieser ersten Version
- Datumszuordnung: aus dem Dateinamen, sonst Dropbox `client_modified`/`server_modified`. **EXIF/QuickTime-Aufnahmedatum wird noch nicht gelesen.** Deshalb ist die Sortierung nicht für alle Dateien zuverlässig. Bei kritischen Daten noch nicht verschieben.
- Duplikate werden nur **innerhalb des gewählten Quellordners** über Dropbox `content_hash` erkannt. Es wird nur der zweite und jeder weitere Treffer als Duplikat markiert; die erste Kopie bleibt. Bei mehr als 300 Treffern zeigt die App die ersten 300 zur Löschfreigabe.
- Zielordner wird noch nicht auf Duplikate geprüft.
- Die App verarbeitet Dateiänderungen nacheinander. Bei vielen Dateien dauert dies länger. Bildschirm/Browser offen lassen.
- Bei gleichen Namen im Ziel wird nicht überschrieben; der Eintrag wird übersprungen.
- Kein Wiederaufnehmen unterbrochener Operationen; nach Unterbrechung erneut scannen.
- Die App verwendet ein kurzlebiges OAuth-Token im Browser-Session-Speicher und keinen App Secret. Nach Ablauf des Tokens erneut anmelden.
- Die App ist ein funktionaler Prototyp, nicht vollständig getestet gegen ein Dropbox-Konto. Vor größeren Aktionen mit einem kleinen Testordner prüfen.
