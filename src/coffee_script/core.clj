(ns coffee-script.core
  "ADD DESCRIPTION!!"
  (:require [clojure.java.io :as io])
  (:use [clojure.contrib.json :only  (json-str)])
  (:import (org.mozilla.javascript Context ContextAction ContextFactory JavaScriptException Scriptable)))

(def ^{:doc "Default options for compiling coffee script"}
  *default-coffee-options*
  {:bare false})

(defn- run-context-action
  ([^ContextAction action] (. (ContextFactory/getGlobal) (call action))))

(def ^{:private true}
  global-scope
  (let [x (delay (with-open [script (io/reader (io/resource "coffee_script/coffee-script.js"))]
                   (run-context-action
                    (reify ContextAction
                      (run [this context]
                        (do (.setOptimizationLevel context -1))
                        (let [^Scriptable x (.initStandardObjects context)]
                          (.evaluateReader context x script "coffee-script.js" 0 nil)
                          x))))))]
    #(force x)))

(defn compile-coffee
  "Returns a "
  ([^CharSequence source opts]
     (run-context-action
      (reify ContextAction
        (run [this context]
          (let [^Scriptable scope (doto (. context (newObject (global-scope))) (.setParentScope (global-scope)))]
            (do (. scope (put "coffeeScriptSource" scope source)))
            (. context (evaluateString scope
                                       (format "CoffeeScript.compile(coffeeScriptSource, %s);"
                                               (json-str opts))
                                       "lein-coffee-script" 0 nil)))))))
  ([^CharSequence source]
     (compile-coffee source {})))
