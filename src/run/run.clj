(ns run.run
  (:require [engine.game :refer [gosper-glider-gun tick]] )
  (:require [simulator.field :as field]))

(defn life [game]
  "Choose between games: box, boat, blinker, glider and gosper-glider-gun"
  (field/run-game tick game))

;; Start the game when `lein run` is used on the command line
(defn -main [& args]
  (life gosper-glider-gun))
