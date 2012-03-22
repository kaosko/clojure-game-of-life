(ns engine.game
   (:require [clojure.set :as set]))

;; constants for game balance
(def under-population 1)
(def over-population 4)
(def parent-count 3)

; constants defining interesting patterns or configurations
;   based on the visual effects they produce
(def box #{[2 1] [2 2] [1 1] [1 2]}) 
(def boat #{[1 1] [2 1] [1 2] [3 2] [2 3]})
(def blinker #{[3 1] [3 2] [3 3]}) 
(def glider #{[2 0] [0 1] [2 1] [1 2] [2 2]}) 
(def gosper-glider-gun #{[1 5] [2 5] [1 6] [2 6]  
                         [11 5] [11 6] [11 7] [12 4] [12 8] [13 3] [13 9] [14 3] [14 9]
                         [15 6] [16 4] [16 8] [17 5] [17 6] [17 7] [18 6]
                         [21 3] [21 4] [21 5] [22 3] [22 4] [22 5] [23 2] [23 6] [25 1] [25 2] [25 6] [25 7] 
                         [35 3] [35 4] [36 3] [36 4]})


;([-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1])
;this is all of the points adjacent or caddy-corner to a given point (relative coordinates)
(def neighbour-offsets 
  (let [digits (range -1 2)]            ; digits is now (list -1 0 1)
  (for [x digits y digits               ; ... both x and y get each of [-1, 0, 1] ...
        :let [value [x y]]              ; :let allows you to give a name to a value inside a (for ...)
        :when (not (= value [0 0])) ]   ; :when filters out generated values
    value)))


; given a cell, get all of its neighbours
(defn all-neighbours
  [cell]
  (for [offset neighbour-offsets]  ; start with all of the "relative" offsets (each of which is a pair i.e. [1,0])
    (map + offset cell)))          ; and add each of those relative offsets to the original cell
                                   ;   remember that map can do cool stuff in Clojure
                                   ;   so (map + [1,1] [3,4]) -> [4,5]


; given a bunch of living cells, and a specific cell, find all of that cell's living neighbours (0 - 8)
(defn alive-neighbours
  [cells cell]
  (filter #(contains? cells %)     ; remove all of the neighbours that aren't alive
          (all-neighbours cell)))  ; get the coordinates of the 8 neighbouring cells of the cell-of-interest


; given a bunch of living cells, and a specific cell, find all of that cell's dead neighbours (0 - 8)
(defn dead-neighbours
  [cells cell]
  (filter #(not (contains? cells %))  ; remove all of the dead neighbours 
          (all-neighbours cell)))     ; get all 8 of the neighbours of the cell-of-interest


; remove (kill) cells with too few neighbours or too many
(defn regulate  
  [cells]
  (filter #(let [alive-neighbour-count (count (alive-neighbours cells %))]
            (and                                           ; if there are:
              (> alive-neighbour-count under-population)   ;   more than 1 living neighbour 
              (< alive-neighbour-count over-population)))  ;   AND fewer than 4 living neighbours
          cells))                                          ; then let the cell LIVE!!!!


; given a bunch of living cells, find ALL of the dead neighbors
(defn dead-neighbour-cells
  [cells]
  (reduce set/union                        ; We want uniqe set of neighbors made by union...
          (for [c cells]                   ; for :: a X product over a list is the list itself! 
					   ; running the  dead-neighbors function once for a given "cell")  
            (dead-neighbours cells c))))    ; -> emit each value returned by dead-neighbors
					    ;  -> "reduce" them into a unique list.


; given a bunch of living cells, create some new cells
; turn SOME of the dead neighbours into living neighbours, if:
;   the dead cell has exactly 3 living neighbours
(defn reproduce
  [cells]
  (filter #(= parent-count                         ; need exactly 3 parents from the living neighbours
              (count (alive-neighbours cells %)))  ; 
          (dead-neighbour-cells cells)))           ; starting with the living cells, find all of the dead neighbours


; one "time" unit in the game involves:
;   creating new cells in medium-populated areas
;   destroying cells in over- and under-populated areas
(defn tick [cells] 
  (set/union 
    (set (reproduce cells)) 
    (set (regulate cells))))
