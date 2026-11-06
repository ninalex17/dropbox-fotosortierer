# Dropbox Fotosortierer – Version 2 (Fortsetzen)

Statische Web-App für GitHub Pages, ohne Serverkosten. Die App nutzt Dropbox OAuth PKCE und benötigt keinen App Secret.

## Aktualisierung bei GitHub

Die Dateien `index.html`, `app.js`, `sw.js`, `manifest.webmanifest` im Repository-Hauptordner durch die Dateien dieser ZIP ersetzen. Nicht die ZIP selbst hochladen und nicht einen zusätzlichen Unterordner anlegen. GitHub Pages benötigt ggf. 1–5 Minuten. Seite anschließend neu laden; ggf. Browsercache leeren.

## Neuer Ablauf

1. Dropbox anmelden (App Key wird im Browser gespeichert, OAuth-Token nur für die Sitzung).
2. **Neue Analyse starten**: Alle Seiten der Dropbox-Ordnerliste werden gelesen. Nach jeder Seite speichert die App Einträge und Cursor in IndexedDB.
3. Bei Unterbrechung Seite erneut öffnen, anmelden und **Gespeicherten Vorgang fortsetzen** wählen.
4. Nach vollständiger Analyse Vorschau prüfen, Freigabe setzen und **Bestätigte Dateien verschieben** klicken.
5. Nach jedem einzelnen Verschiebeversuch wird der Stand in IndexedDB gespeichert. Nach Unterbrechung erneut anmelden und **Fortsetzen** wählen.
6. Dateien, deren Quelle nach einer Unterbrechung fehlt, gelten nur dann als erledigt, wenn am erwarteten Ziel derselbe Dropbox-Inhaltshash nachgewiesen wird. Existierende Zielnamen werden nicht überschrieben.
7. **Pausieren** stoppt nach dem laufenden Dropbox-Schritt. **Gespeicherten Stand verwerfen** löscht nur die lokale Liste, keine Dropbox-Dateien.

## Grenzen und Vorsicht

- Der Fortschritt wird **nur auf diesem Gerät, in diesem Browser und für diese Website** gespeichert. Privatmodus, Browserdaten-Löschen, anderes Gerät oder anderes Browserprofil können ihn entfernen. Die bereits abgeschlossenen Dropbox-Verschiebungen bleiben bestehen.
- Dropbox-Login muss nach Sitzungsende ggf. erneuert werden. Die App setzt den Vorgang **nicht selbsttätig im Hintergrund** fort; Browser/Handy müssen während der Bearbeitung aktiv bleiben.
- Der Zeitstempel stammt weiterhin zuerst aus dem Dateinamen, sonst aus Dropbox `client_modified`/`server_modified`. EXIF/QuickTime-Aufnahmedatum wird **nicht** ausgelesen. Bei kritischen Fotos vor dem Verschieben kontrollieren.
- Duplikate werden nur innerhalb des Quellordners anhand `content_hash` erkannt; eine Kopie bleibt erhalten. Die Duplikatfunktion erfordert manuelle Auswahl und Freigabe; vor Löschung wird geprüft, ob die behaltene Kopie noch unter dem erfassten Pfad liegt und denselben Inhalt hat. Wenn die Originalkopie inzwischen verschoben wurde, wird die Löschung aus Sicherheitsgründen verweigert. Für Duplikate nach Verschiebungen daher neu analysieren und die behaltene Kopie separat überprüfen.
- Die App prüft Ziel-Dateinamen beim Verschieben, aber scannt **nicht** den gesamten Zielordner auf Duplikate. Es gibt keine EXIF-Auswertung, keine automatische Konfliktumbenennung und keine Synchronisierung über mehrere Geräte.
- Fehlerhafte Dateien bleiben im Fortschritt mit Fehlerstatus und können nach Prüfung erneut versucht werden.
- Erst mit einem kleinen Testordner prüfen und eine zusätzliche Datensicherung behalten.
