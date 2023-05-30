# Sagrada - Boardgame

This repo contains the final project for the Computer Science and Engineering
Bachelor Degree course [*PROVA FINALE (INGEGNERIA DEL SOFTWARE)*
(085923 - A.Y. 2017/18)][course] of Politecnico di Milano. The goal of the
project is to implement the boardgame [Sagrada][sagrada].

* Assignement: [ITA](assignment/Requisiti.pdf), [ENG summary](assignment/Requirements.md)

## Main Features

* Complete rules of the game
* RMI Networking
* Socket Networking
* Management of network failure: disconnection & reconnection
* Graphical user interface (GUI)
* Command line interface (CLI)
* Handling multiple matches at the same time on the same server


How to start the game:
```
> java -jar sagrada-1.0-SNAPSHOT-jar-with-dependencies.jar -option
```
List of options:
```
-s start the server
-g start the client GUI
-c start the client CLI
```
File necessari per il funzionamento:
```
mappe.json
settings.json (autogenerato se non presente)
```
Le opzioni del server sono caricate dal file settings.json,
e' possibile configurare la durata del timer in secondi della lobby
e la durata in minuti del tempo di timeout di un giocatore.

Server configuration is loaded from "settings.json" file (which is
autogenerated in case it is not present). It is possible to configure
lobby expiration timer and maximum turn duration, in seconds.


[course]: https://www4.ceda.polimi.it/manifesti/manifesti/controller/ManifestoPublic.do?EVN_DETTAGLIO_RIGA_MANIFESTO=evento&aa=2017&k_cf=225&k_corso_la=358&k_indir=II3&codDescr=085923&lang=IT&semestre=2&anno_corso=3&idItemOfferta=133689&idRiga=221825
[sagrada]: https://boardgamegeek.com/boardgame/199561/sagrada
