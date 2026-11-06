Version 9: Neues Design in Ozeanblau, Smaragd-Türkis und Anthrazit. Die Funktionen aus Version 8 bleiben unverändert.

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


## Neu in Version 6

- Fortschrittsbalken mit Prozentzahl bei Metadatenprüfung und Verschiebungen.
- Geschätzte Restzeit nach einigen bearbeiteten Dateien, auf Grundlage der aktuellen Sitzung.
- Beim Einlesen zeigt die App die Anzahl bereits gefundener Dateien; eine Prozentzahl ist erst nach Kenntnis der Gesamtmenge seriös möglich.
- Die Anzeige setzt keine gespeicherten Vorgänge zurück. Bitte vor dem Update eine laufende Verarbeitung pausieren und den gespeicherten Stand abwarten.


## Version 7: Kachel-Startbildschirm
Die Startseite enthält vier große Kacheln: Fotos sortieren, Vorschau, Freigeben und Dropbox. Die vorhandenen Funktionen und lokalen Fortschrittsdaten bleiben unverändert. Keine Hintergrundverarbeitung bei geschlossenem Browser. Die App kann über Chrome als PWA installiert werden.

WICHTIG: Vor dem Update einen laufenden Vorgang pausieren. Alle sieben Dateien im Wurzelverzeichnis des GitHub-Repositories ersetzen; keine Unterordner anlegen. Danach App neu laden.


## Version 8 – neues Design
Warme Pflaumen-, Rosé- und Cremefarben, größere Startkacheln, neues App-Icon und überarbeitete Beschriftungen. Die Dropbox-Verarbeitungslogik in app.js wurde gegenüber Version 7 nicht verändert. Bei einem laufenden Vorgang bitte zuerst pausieren und den lokalen Stand sichern. Nach dem Update Browserseite aktualisieren.
