# Project Requirements

## Deliverables

* High-level UML diagram
* Low-level UML diagram
* Executable of the game
* Source code of the code
* Source code of the unit tests

## Game-specific requirements

Implement every rule of the game, every player need to have a unique nickname during matches.

## Game-agnostic requirements

The implementation must be a distributed system composed by a single server which can handle multiple clients. The use of the MVC pattern is mandatory.

### Server requirements

* Implemented with JavaSE
* Istantiated only one time
* Must support both RMI and socket at the same time in the same match

### Client requirements

* Implemented with JavaSE
* GUI implemented with JavaFX
* The user can choose between RMI and socket at the start of the game
* The user can choose between GUI and terminal at the start of the game

### Lobby

Once 2 players connect to the server a time-out has to start; when the time-out runs out the game begin. If 4 players connect the game starts immediatly.

### Players disconnection

If a player leave the game/lose connection, a time-out has to start; when the time-out runs out the player is considered absent and his turns are skipped. If the player reconnect, he/she can continue to play the game. If every player but one leave the match, the last online player will win the game.

### Advanced functionalities

As additional functionalities we have implemented:

* Dynamic schema cards: schema cards are loaded from file, so to add/remove/modify them is just needed to create/delete/modify files in the correct directory
* Multiple matches: a server can handle multiple matches at the same time

## Evaluation

The project has been evaluated based on:

* Design Patterns use
* Reliability
* Clarity of code and comments
* Unit Tests effectiveness
