(ns game-of-life.life-test
  (:require [clojure.test :refer :all]
            [game-of-life.life :refer :all]))

(deftest create-grid-test
  (is (= (create-grid 2 2) [["." "."] ["." "."]])))

