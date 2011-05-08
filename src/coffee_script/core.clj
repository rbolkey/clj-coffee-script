(ns coffee-script.core
  "ADD DESCRIPTION!!"
  (:require [clojure.java.io :as io])
  (:use [clojure.contrib.json :only  (json-str)])
  (:import (org.mozilla.javascript Context ContextAction ContextFactory JavaScriptException Scriptable)))

(def ^{:doc "Default options for compiling coffee script"} *default-coffee-options*
     {:bare false})

(def global-scope
  (let [x (delay (with-open [script (io/reader (io/resource "coffee_script/coffee-script.js"))]
                   (. (ContextFactory/getGlobal)
                      (call (reify ContextAction
                              (run [this context]
                                (do (.setOptimizationLevel context -1))
                                (let [x (.initStandardObjects context)]
                                  (.evaluateReader context x script "coffee-script.js" 0 nil)
                                  x)))))))]
    #(force x)))

(defn compile-coffee
  ""
  ([context scope source opts]
     (do (. scope (put "coffeeScriptSource" scope source)))
     (. context (evaluateString scope
                                (format "CoffeeScript.compile(coffeeScriptSource, %s);"
                                        (json-str opts))
                                "lein-coffee-script" 0 nil)))
  ([context source opts]
     (compile-coffee context
                     (doto (. context (newObject (global-scope))) (.setParentScope (global-scope)))
                     source opts))
  ([source opts]
     (try (compile-coffee (Context/enter) source opts)
          (finally (Context/exit))))
  ([source]
     (compile-coffee source {})))

(defn coffee [project]
  "Compile coffee files"
  )
