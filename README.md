# clojure-game-of-life

An implementation of Conway's game of live together with a Swing GUI. Both written in clojure. 

## Usage

with leinigen (https://github.com/technomancy/leiningen):

cd clojure-game-of-life 
lein repl

### Starting the game

;;This appears to work, but the Jpanel does essentially nothing. 
;;Note that, however, we do see a little black square at coordinates 1,2 
;;this, the "seed" is probably just a set of points of starting cells that are alive.

;;To investigate : 
;;What is the engine (arg2)? 
;;Why does the program do nothing ?
repl> (run-game #{} #{[1 2]})



## Installation

git clone git@github.com:sebastianbenz/clojure-game-of-life.git

## License

Copyright (C) 2010 Sebastian Benz

Distributed under the Eclipse Public License, the same as Clojure.
