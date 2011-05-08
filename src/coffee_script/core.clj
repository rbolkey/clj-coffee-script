(ns coffee-script.core
  "ADD DESCRIPTION!!"
  (:require [clojure.java.io :as io])
  (:use [clojure.contrib.json :only  (json-str)])
  (:import (org.mozilla.javascript Context JavaScriptException Scriptable)))

(def ^{:doc "Default options for compiling coffee script"} *default-coffee-options*
     {:bare false})

(def global-scope
  (let [x (delay (with-open [script (io/reader (io/resource "coffee_script/coffee-script.js"))]
           (let [context (doto (Context/enter) (.setOptimizationLevel -1))
                 global (.initStandardObjects context)]
             (try (.evaluateReader context global script "coffee-script.js" 0 nil)
                  (finally (Context/exit)))
             global)))]
    #(force x)))

(defn compile-coffee
  ""
  ([context scope source opts]
     (do (.put scope "coffeeScriptSource" scope source))
     (.evaluateString context scope
                      (format "CoffeeScript.compile(coffeeScriptSource, %s);"
                              (json-str opts))
                      "lein-coffee-script" 0 nil))
  ([context source opts]
     (compile-coffee context
                     (doto (.newObject context (global-scope)) (.setParentScope (global-scope)))
                     source opts))
  ([source opts]
     (try (compile-coffee (Context/enter) source opts)
          (finally (Context/exit))))
  ([source]
     (compile-coffee source {})))

(defn coffee [project]
  "Compile coffee files"
  )
