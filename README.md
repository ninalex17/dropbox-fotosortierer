# Meine Fotosortierung – Version 5

## Installation auf GitHub Pages
Alle sieben Dateien aus diesem ZIP in das Stammverzeichnis des bestehenden GitHub-Repositories hochladen und gleichnamige Dateien ersetzen. Keine ZIP-Datei selbst hochladen. Nach der Veröffentlichung die Seite neu laden.

## Android-Icon
In Chrome die veröffentlichte App öffnen, Menü ⋮ > App installieren oder Zum Startbildschirm hinzufügen. Der Service Worker und das Web-App-Manifest sind enthalten.

## Neu in V5
- Dropbox-Ordner direkt in der App auswählen (Ordnerbrowser).
- Screenshots, WhatsApp-Medien und GIFs standardmäßig vom Verschieben zurückstellen. Die Klassifikation basiert auf Namen/Pfaden und kann unvollständig sein.
- Alle per Dropbox-Content-Hash exakt identischen Duplikate mit einem Tipp markieren; jede Löschung erfordert separate Zustimmung und erneute Prüfung der verbleibenden Kopie.
- Bestehende Unterbrechungs-/Fortsetzungsfunktion beibehalten.

## Sicherheitsgrenzen
- Der Content-Hash ist keine Bilderkennungsfunktion: Komprimierte oder neu kodierte Kopien haben andere Hashes.
- Duplikate werden nur innerhalb des eingelesenen Quellordners verglichen, nicht automatisch in der ganzen Dropbox.
- Nicht alle Kamera-/WhatsApp-Dateien lassen sich allein am Dateinamen sicher zuordnen.
- Der gespeicherte Vorgang bleibt nur im selben Browser/Browserprofil bestehen, solange die lokalen Browserdaten erhalten bleiben.
- Die App läuft bei geschlossenem Browser nicht weiter.
- Vor größeren Löschungen Backup erstellen und zuerst mit kleinem Testordner prüfen.
