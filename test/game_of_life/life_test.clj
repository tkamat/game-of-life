(ns game-of-life.life-test
  (:require [clojure.test :refer :all]
            [game-of-life.life :refer :all]))

(def mock-grid-empty [["." "." "."] ["." "." "."] ["." "." "."]])
(def mock-grid-blinker [["." "X" "."] ["." "X" "."] ["." "X" "."]])
(def mock-grid-star [["." "X" "."] ["X" "X" "X"] ["." "X" "."]])

(deftest create-grid-test
  (is (= mock-grid-empty (create-grid 3 3))))

(deftest assoc-live-cell-test
  (is (= [["X" "." "."] ["." "." "."] ["." "." "."]]
         (assoc-live-cell [0 0] mock-grid-empty))))

(deftest assoc-dead-cell-test
  (is (= [["." "." "."] ["." "X" "."] ["." "X" "."]]
         (assoc-dead-cell [0 1] mock-grid-blinker))))

(deftest is-alive-test
  (is (= true (is-alive? [0 1] mock-grid-blinker))))

(deftest get-neighbors-test
  (is (= #{[0 0] [0 1] [0 2] [1 0] [1 2] [2 0] [2 1] [2 2]}
         (get-neighbors [1 1] mock-grid-blinker))))

(deftest get-neighbors-in-corner-test
  (is (= #{[0 1] [1 1] [1 0]}
         (get-neighbors [0 0] mock-grid-blinker))))

(deftest count-live-neighbors-test
  (is (= 2 (count-live-neighbors [1 1] mock-grid-blinker))))

(deftest kill-underpopulated-cells-test
  (is (= {:dead #{[0 1] [2 1]} :alive #{}}
         (kill-underpopulated-cells mock-grid-blinker {:alive #{} :dead #{}}))))

(deftest kill-overpopulated-cells-test
  (is (= {:dead #{[1 1]} :alive #{}}
         (kill-overpopulated-cells mock-grid-star {:alive #{} :dead #{}}))))

(deftest reproduce-empty-cells-test
  (is (= {:alive #{[1 0] [1 2]} :dead #{}}
         (reproduce-empty-cells mock-grid-blinker {:alive #{} :dead #{}}))))

(deftest step-through-generation-blinker-test
  (is (= [["." "." "."] ["X" "X" "X"] ["." "." "."]]
         (step-through-generation mock-grid-blinker))))

(deftest step-through-generation-star-test
  (is (= [["X" "X" "X"] ["X" "." "X"] ["X" "X" "X"]]
         (step-through-generation mock-grid-star))))








