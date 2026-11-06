# Meine Erinnerungen – Android 0.1 (Quellcode)

Dies ist eine **erste native Android-Testversion**, kein fertiges Produkt und noch keine APK.
Sie ist unabhängig von der bestehenden GitHub-Pages-Web-App. Die vorhandene Browser-Fortschrittsdatenbank wird **nicht** automatisch in diese Android-App übernommen.

## Bereits umgesetzt
- Native Android-Oberfläche mit sichtbarem Dropbox-Anmeldestatus.
- OAuth 2.0 Authorization Code + PKCE im externen Browser; `token_access_type=offline`.
- Dropbox-Refresh-Token mit Android Keystore (AES-GCM) verschlüsselt gespeichert.
- Automatisches Erneuern eines kurzlebigen Dropbox-Access-Tokens bei API-Aufrufen.
- Dropbox-Quellordner mit Unterordnern **nur lesend** auflisten; Anzeige der Zahl erkannter Medien.
- Keine Verschiebe- oder Löschfunktion: Testmodus zum Schutz deiner Bilder.

## Noch NICHT umgesetzt
- Installierbare APK ist nicht enthalten; das Projekt wurde in dieser Umgebung nicht kompiliert/getestet.
- Dauerhafte Scan-Fortschrittsspeicherung, Pause/Fortsetzen und Hintergrunddienst.
- Bilddaten-/EXIF-Auswertung, Verschieben, Duplikate und Löschfreigabe.
- Xiaomi-Favoriten, KI-Bildbewertung, Fotobuchauswahl und Export.
- Die native App übernimmt den laufenden Browser-Job nicht. **Bitte dort keine Browserdaten löschen.**

## Einmalige Einrichtung am Windows-PC
1. Android Studio installieren und **Open** > diesen entpackten Projektordner wählen.
2. In deiner Dropbox Developer Console bei der **bestehenden Dropbox-App** unter OAuth 2 > Redirect URIs zusätzlich `fotoarchiv://oauth` eintragen. Die bisherige GitHub-Pages-Redirect-URI unbedingt bestehen lassen.
3. In die Datei `gradle.properties` eine neue Zeile `DROPBOX_APP_KEY=DEIN_OEFFENTLICHER_APP_KEY` schreiben. Nur den **App key**, niemals das App secret verwenden. `gradle.properties` mit dem Schlüssel nicht öffentlich hochladen.
4. Gradle synchronisieren lassen. Ein Android-Handy per USB-Debugging verbinden und auf **Run** drücken oder über **Build > Build APK(s)** eine Test-APK erzeugen.
5. App starten, **Mit Dropbox verbinden** wählen und Berechtigung erteilen. Dann `/Kamera-Uploads` lesend prüfen.

### Hinweise
- Die Dropbox-App muss PKCE unterstützen (bei scoped Apps üblich). Manche Dropbox-Konfigurationen akzeptieren keine benutzerdefinierten Redirect-URIs; in diesem Fall muss der OAuth-Callback vor dem ersten Test auf eine bestätigte HTTPS-App-Link-Lösung umgestellt werden. Nicht versuchen, Zugangsdaten in die App einzutragen.
- Bei sehr vielen Dateien kann die erste lesende Prüfung länger dauern. **Sie läuft derzeit nur, solange die App aktiv ist**, nicht zuverlässig bei gesperrtem Bildschirm.
- Eine bestehende Web-App-Sitzung wird dadurch nicht verändert.
- Vor produktiven Verschiebungen und Löschungen werden separate Tests mit einem kleinen Testordner benötigt.


## Ohne Android Studio: GitHub Actions (empfohlen)

1. Auf GitHub ein **neues Repository** anlegen, vorzugsweise **Private** (die APK ist ein privater Test).
2. Den **Inhalt** dieses Projektordners in das Repository hochladen, einschließlich `.github/workflows/android-debug.yml`. Nicht den umschließenden Ordner als einzige Ebene hochladen.
3. In GitHub unter **Settings → Secrets and variables → Actions → New repository secret** den Namen `DROPBOX_APP_KEY` und als Wert den öffentlichen App-Key der eigenen Dropbox-Entwickler-App eintragen. **Kein App Secret!**
4. In der Dropbox Developer Console unter OAuth Redirect URIs `fotoarchiv://oauth` zusätzlich eintragen, sofern Dropbox diese URI akzeptiert. Die bisherige Web-App-Redirect-URI nicht entfernen.
5. Unter **Actions → Android Test-APK → Run workflow** den Build starten. Nach grünem Erfolg den Lauf öffnen und unter **Artifacts → Fotoarchiv-Test-APK** herunterladen. ZIP entpacken und `app-debug.apk` auf dem Android-Handy installieren.
6. Test: Anmeldung und `/Kamera-Uploads` nur lesend prüfen. Es werden keine Fotos verändert.

**Wichtig:** Dies ist ein vorbereiteter Build-Workflow. Ein erfolgreicher Build ist noch nicht bestätigt. GitHub Actions kann auf einem neuen Repository zunächst freigeschaltet werden müssen. Der Build benötigt Internetzugriff auf Android-/Gradle-Abhängigkeiten. Der App-Key ist öffentlich im APK enthalten; er ist **kein Geheimnis**. Ein Secret schützt lediglich davor, ihn zusätzlich im öffentlichen Repository abzulegen.

**APK-Signierung:** Diese erste Debug-APK wird automatisch mit einem temporären Debug-Schlüssel signiert. Für spätere Updates/Veröffentlichung ist ein dauerhaft sicher verwahrter Release-Signierschlüssel notwendig; Debug-APKs verschiedener Builds lassen sich ggf. nicht direkt übereinander installieren.
