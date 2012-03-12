(ns simulator.field
  (:require [clojure.set :as set])
  (:import 
    (javax.swing JFrame JPanel Timer)
    (java.awt Color)
    (java.awt.event ActionListener )
    (java.awt Dimension)))

;;Draws the full grid, first argument is a graphics object.
;;The swing graphics object doesall the heavy lifting here. 
(defn draw-grid 
  [g columns rows position]
  (do
    ;;configure the g objects color : NOTE this is statefull on g.
    ;;It might be nice to get an ephemarl graphics object wrapper if 
    ;;we wanted to be purely functional here.... ?
    (.setColor g Color/GRAY)
    (doall (map #(.drawLine g  (position %1) 0 (position %1) (position rows))(range 0 columns)))
    (doall (map #(.drawLine g  0 (position %1) (position columns) (position %1))(range 0 rows)))))

;;Helper method, similar to draw-grid.
(defn draw-box 
  [g position x y color]
  (.setColor g color)
  (let [size (position 1)]
    (.fillRect g (position x) (position y) size size)))

;;x is the 1st coordinate in a cell.  Mostly syntactic sugar.
(defn x
  [cell]
  (nth cell 0))

;;Similar to the "x" method above. 
(defn y
  [cell]
  (nth cell 1))

;;Similar to the draw-grid method.
(defn draw-cells
  [g position cells color]
  (doall (map #(draw-box g position (x %1 ) (y %1) color) cells) ))

;;Returns true iff a cell is visible.
;;That is... true if x coordinate of a cell is < col+2, and y coord < rows + 2
(defn visible?
  [rows columns cell]
  (and (< (x cell) (+ columns 2)) (< (y cell) (+ rows 2))))

;;This is the true "starting" point for the application.
;;inputs are 
;;(1)  
;;(2) 
;;(3) A map of options.
(defn field-panel 
  [engine seed options]
  (let [columns (options :columns)
        rows (options :rows)
        position  #(* %1 (options :cellsize))
        ;;Not sure what this atomic seed object should be, nor why
	;;it is converted to a thread-safe atom.  Does the simulation run
	;;in parallel ? 
	new-cells (atom seed)
        previous-cells (atom ())
	;;"partial": We can now call visible with only one argument - the cell. 
        visible-cell? (partial visible? rows columns)
        ;;Bind the "panel" to a proxy JPANEL implementing ACTIONLISTENER
	;;The proxy form takes class interface1 interface2 interface3 ... as inputs, 
	;;and then defines several inner functions which implement/extend
	;;the functionality of those class/interfaces.
	panel (proxy [JPanel ActionListener] []
		;;Override paintComponent. 
                (paintComponent [g]
                  (let [removed (set/difference @previous-cells @new-cells)
                        added (set/difference @new-cells @previous-cells)]
                    (draw-cells g position removed Color/WHITE)
                    (draw-cells g position added Color/BLACK)
                    (draw-grid g columns rows position)))
		;;Implement the mandatory actionPerformed method of ActionListener.
                (actionPerformed [e]
                  (swap! previous-cells (fn [_] (deref new-cells)))
                  (swap! new-cells 
                    #(set (filter visible-cell? (engine %))))
                  (.repaint this)))]
    (doto panel
      (.setPreferredSize (Dimension. (position columns)  (position rows))))))

;; Sets up the jframe , i.e. the GUI for the whole game. 
(defn field-frame 
  [panel]
  (doto (JFrame. "Game of Life")
    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    ;;the content is here in the panel. 
    (.add panel)
    (.setBackground Color/WHITE)
    .pack
    .show))

;;Arguments are the 'engine' and the 'seed' 
;;This is a polymorphic function - the function is dispatched 
;;via the various implementations. 
(defn run-game 
  ;;This one takes 2 args, the engine + seed.
  ([engine seed]  
    (run-game engine seed  
      {:columns 50 :rows 50 :speed 100 :cellsize 10}))
  ;;This one takes 3 args, an options map as the 3rd arg.
  ([engine seed options] 
    (let [panel (field-panel engine seed options)
          frame (field-frame panel)
          ;;This Swing timer will refresh the GUI intermittently.
	  ;;by calling the action performed method on the panel.
	  timer (Timer. (options :speed) panel)]
      (.start timer))))


