(ns run.run
  (:use [engine.game])
  (:use [simulator.field]))

(defn life [game]
  "Choose between games: box, boat, blinker, glider and gosper-glider-gun"
  (run-game tick game))

;; Start the game when `lein run` is used on the command line
(defn -main [& args]
  (life gosper-glider-gun))
