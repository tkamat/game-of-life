(ns game-of-life.core
  (:gen-class)
  (:require [clojure.string :as string]
            [game-of-life.life :as life :refer :all]))

(def initial-grid (create-grid 40 40))

(defn print-grid
  "Clears the screen, using ANSI control characters, then prints the given grid
  with the current generation number"
  [grid i]
  (let [esc (char 27)]
    ;; clear terminal
    (print (str esc "[2J"))
    (print (str esc "[;H"))
    ;; print grid and row numbers
    (doall (map
            (fn [n row] (print (format "%02d" n)) (println row))
            (range 0 40) grid ))
    ;; print column numbers
    (println " " (vec (concat (repeat 10 0) (repeat 10 1) (repeat 10 2) (repeat 10 3))))
    (println " " (vec (concat (range 0 10) (range 0 10) (range 0 10) (range 0 10))))
    (println i)))

(defn string->number-vector
  "Turns a string of 2 space-seperated numbers into a vector"
  [s]
  (into []
        (map #(Integer/parseInt %)
           (string/split s #" "))))

(defn generate-seed-from-user-input
  "Generates a seed from user input"
  ([]
   (do (print-grid initial-grid 0)
       (println "Please enter initial points in the format \"6 5\", finish with \"done\"")
       (generate-seed-from-user-input #{} initial-grid)))
  ([seed grid]
   (let [input (read-line)]
     (if (= "done" input)
       seed
       (let [input-vector (string->number-vector input)
             new-grid (assoc-live-cell input-vector grid)]
         (do (print-grid new-grid 0)
             (generate-seed-from-user-input (conj seed input-vector) new-grid)))))))


(defn run-game
  "Runs the game for n generations with the given seed, prints grid to terminal"
  [n seed]
  (let [grid (->> initial-grid
                  (populate-grid (generate-seed-from-user-input)))]
    (loop [i 0
           grid grid]
      (if (= i n)
        (println "finished!")
        (do (print-grid grid i)
            (recur (inc i) (step-through-generation grid)))))))

(defn -main
  "Entry point"
  [& args]
  (run-game 1000 #{[10 10] [11 10] [12 10]}))
