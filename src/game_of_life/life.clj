(ns game-of-life.life)

(defn create-grid
  "Returns a 2D grid of the specified dimensions"
  [x y]
  (vec (repeat x (vec (repeat y ".")))))

(defn assoc-live-cell
  "Returns a new grid with the specified live cell"
  [cell grid]
  (assoc-in grid cell "X"))

(defn assoc-dead-cell
  "Returns a new grid with the specified dead cell"
  [cell grid]
  (assoc-in grid cell "."))

(defn populate-grid
  "Takes a set of cells and a grid and returns a grid with the given cells
  populated."
  [cells grid]
  (reduce (fn [grid cell] (assoc-live-cell cell grid))
          grid
          cells))

(defn is-alive?
  "Returns true if the specified cell of the grid contains an alive cell,
  false if otherwise"
  [cell grid]
  (if (= (get-in grid cell) "X")
    true
    false))

(def neighbor-directions
  "A set of all of the possible directions to move"
  (let [directions (range -1 2)]
    (for [x directions
          y directions
          :when (not (and (= x 0) (= y 0)))]
      [x y])))

(defn get-neighbors
  "Returns a set of the orthagonal and diagonal neighbors of the given cell"
  [[x y :as cell] grid]
  (->> neighbor-directions
       (map (fn [[xd yd]] [(+ x xd) (+ y yd)]))
       (filter #(get-in grid %))
       set))

(defn count-live-neighbors
  "Returns a the number of live neighbors of a given cell"
  [cell grid]
  (count (filter #(is-alive? % grid) (get-neighbors cell grid))))

(defn create-cell-list
  "Creates a list of cell coordinates from the grid"
  [grid]
  (set (for [x (range 0 (count grid))
             y (range 0 (count grid))]
         [x y])))

(defn kill-underpopulated-cells
  "Removes any live cells from the grid that have less than 2 living neighbors,
  and returns the changes in the grid-state map"
  [grid grid-state]
  (let [cell-list (create-cell-list grid)]
    (reduce (fn [grid-state cell]
              (if (and (is-alive? cell grid)
                       (< (count-live-neighbors cell grid) 2))
                (update grid-state :dead #(conj % cell))
                grid-state))
            grid-state
            cell-list)))

(defn kill-overpopulated-cells
  "Removes any live cells from the grid that have more than 3 living neighbors,
  and returns the changes in the grid-state map"
  [grid grid-state]
  (let [cell-list (create-cell-list grid)]
    (reduce (fn [grid-state cell]
              (if (and (is-alive? cell grid)
                       (> (count-live-neighbors cell grid) 3))
                (update grid-state :dead #(conj % cell))
                grid-state))
            grid-state
            cell-list)))

(defn reproduce-empty-cells
  "Enlivens any dead cells from the grid that have exactly 3 live neighbors, and
  returns the changes in the grid-state map"
  [grid grid-state]
  (let [cell-list (create-cell-list grid)]
    (reduce (fn [grid-state cell]
              (if (and (not (is-alive? cell grid))
                       (= (count-live-neighbors cell grid) 3))
                (update grid-state :alive #(conj % cell))
                grid-state))
            grid-state
            cell-list)))

(defn apply-grid-functions
  [grid]
  (->> {:dead #{} :alive #{}}
       (kill-underpopulated-cells grid)
       (kill-overpopulated-cells grid)
       (reproduce-empty-cells grid)))

(defn step-through-generation
  "Steps through a single generation by applying the killing and reproduction
  functions to the grid, returns a new grid"
  [grid]
  (let [grid-state (apply-grid-functions grid)]
    (reduce (fn [grid dead-cell] (assoc-dead-cell dead-cell grid))
            (reduce (fn [grid alive-cell] (assoc-live-cell alive-cell grid))
                    grid
                    (:alive grid-state))
     (:dead grid-state))))

