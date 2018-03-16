(ns game-of-life.life
  (:require [quil.core :as quil]))

(defn create-grid
  "Creates a 2D grid of the specified dimensions"
  [x y]
  (repeat y (repeat x ".")))

