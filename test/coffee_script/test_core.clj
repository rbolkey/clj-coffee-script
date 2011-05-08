(ns coffee-script.test-core
  "Tests for the core package."
  (:require [clojure.java.io :as io])
  (:use [clojure.test :only [deftest is testing]]
	[coffee-script.core :only [compile-coffee with-coffee-compiler]]))

(deftest test-compiler-coffee
  (testing "Builtin CoffeeScript Compiler"
    (is (= "var number;\nnumber = 42;"
           (compile-coffee "number = 42" {:bare true})))
    (is (= "(function() {\n  var x;\n  x = [1, 2, 3];\n}).call(this);\n"
           (compile-coffee "x = [1,2,3]"))))
  (testing "External CoffeeScript Compiler"
    (is (= "(function() {\n  var number;\n  number = 42;\n}).call(this);\n"
         (with-coffee-compiler (io/resource "coffee_script/coffee-script.js")
           (compile-coffee "number = 42"))))
    (is (= "var x;\nx = [1, 2, 3];"
         (with-coffee-compiler (io/resource "coffee_script/coffee-script.js")
           (compile-coffee "x = [1,2,3]" {:bare true}))))))
