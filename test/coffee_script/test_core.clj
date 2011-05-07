(ns coffee-script.test-core
  ""
  (:use [clojure.test :only [deftest is]]
	[coffee-script.core :only [compile-coffee]]))

(deftest test-compile-coffee
  (is (= "var number;\nnumber = 42;" (compile-coffee "number = 42"))))
  