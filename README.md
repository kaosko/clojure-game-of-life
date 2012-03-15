# clojure-game-of-life

An implementation of Conway's game of live together with a Swing GUI. Both written in clojure. 

## Usage

with leiningen (https://github.com/technomancy/leiningen):

    cd clojure-game-of-life 
    lein repl

### Starting the game

 1. from project folder:

        lein repl   

 2. from the repl:
 
        (life gosper-glider-gun)
        
This comes with five example configurations pre-defined: 

 - box
 - boat
 - blinker
 - glider
 - (and best of all) gosper-glider-gun

You can also manually specify the set of points life takes. For example,

    (life #{[3 1] [3 2] [3 3]}) 
    
makes a blinker.


## Installation

git clone git@github.com:jayunit100/clojure-game-of-life.git

## License

Original source code: Copyright (C) 2010 Sebastian Benz
The source has since been updated by Colbert Sesanker, Matt Fenwick, and Jay Vyas.

Distributed under the Eclipse Public License, the same as Clojure.
