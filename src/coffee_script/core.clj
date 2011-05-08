(ns coffee-script.core
  "ADD DESCRIPTION!!"
  (:require [clojure.java.io :as io])
  (:use [clojure.contrib.json :only  (json-str)])
  (:import (org.mozilla.javascript Context ContextAction ContextFactory JavaScriptException Scriptable)))

(defn- run-context-action
  ([^ContextAction action] (. (ContextFactory/getGlobal) (call action))))

(defn evaluate-compiler
  [source]
  (with-open [script (io/reader source)]
    (run-context-action
     (reify ContextAction
       (run [this context]
         (do (.setOptimizationLevel context -1))
         (let [^Scriptable x (.initStandardObjects context)]
           (.evaluateReader context x script "coffee-script.js" 0 nil)
           x))))))

(def ^{:doc "Default options for compiling coffee script"}
  *default-coffee-options*
  {:bare false})

(declare ^{:doc "The CoffeeScript compiler used for compiling."}
  *coffee-compiler*)

(def builtin-coffee-compiler
  (evaluate-compiler (io/resource "coffee_script/coffee-script.js")))

(defmacro with-coffee-compiler
  "Evaluates the body in the context of the coffee compiler coerced from the given input."
  [c & body]
  `(binding [*coffee-compiler* (evaluate-compiler ~c)]
     ~@body))

(defn compile-coffee
  "Compiles a string of CoffeeScript into its JavaScript form."
  ([^CharSequence source opts]
     (let [compiler (if (bound? (var *coffee-compiler*)) *coffee-compiler* builtin-coffee-compiler)]
       (run-context-action
        (reify ContextAction
          (run [this context]
            (let [^Scriptable scope (doto (. context (newObject compiler))
                                      (.setParentScope compiler))]
              (do (. scope (put "coffeeScriptSource" scope source)))
              (. context (evaluateString scope
                                         (format "CoffeeScript.compile(coffeeScriptSource, %s);"
                                                 (json-str (merge *default-coffee-options* opts)))
                                         "lein-coffee-script" 0 nil))))))))
  ([^CharSequence source]
     (compile-coffee source {})))
