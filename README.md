# clojure-game-of-life

An implementation of Conway's game of live together with a Swing GUI. Both written in clojure. 

## Usage

with leinigen (https://github.com/technomancy/leiningen):

cd clojure-game-of-life 
lein repl

### Starting the game

 1. lein repl from project folder

 2. (life gosper-glider-gun) ;; life takes box, boat, blinker, glider and best of all gosper-glider-gun

 Note: you can also manually specify the set of points life takes: (life #{[3 1] [3 2] [3 3]}) makes a blinker.


## Installation

git clone git@github.com:sebastianbenz/clojure-game-of-life.git

## License

Copyright (C) 2010 Sebastian Benz

Distributed under the Eclipse Public License, the same as Clojure.
