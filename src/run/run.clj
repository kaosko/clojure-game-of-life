(ns run.run
  (:use [engine.game])
  (:use [simulator.field]))

(defn life [game]
 "Choose between games: box, boat, blinker, glider and gosper-glider-gun"
  (run-game tick game))

