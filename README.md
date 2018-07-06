# ing-sw-2018-Fabrizio-Fino-Frigerio
Uso del jar:
```
> java -jar sagrada-1.0-SNAPSHOT-jar-with-dependencies.jar -option
```
Opzioni supportate:
```
-s avvia il server
-g avvia direttamente la GUI
-c avvia direttamente la CLI
```
File necessari per il funzionamento:
```
mappe.json
settings.json (autogenerato se non presente)
```
Le opzioni del server sono caricate dal file settings.json, e' possibile configurare la durata del timer in secondi della lobby e la durata in minuti del tempo di timeout di un giocatore.
