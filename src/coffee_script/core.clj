(ns coffee-script.core
  "ADD DESCRIPTION!!"
  (:require [clojure.java.io :as io])
  (:import (org.mozilla.javascript Context JavaScriptException Scriptable)))

(def ^{:doc "Default options for compiling coffee script"} *default-coffee-options*
     {:bare false})

(def global-scope
     (with-open [script (io/reader (io/resource "coffee_script/coffee-script.js"))]
       (let [context (doto (Context/enter) (.setOptimizationLevel -1))
	     global (.initStandardObjects context)]
	 (try (.evaluateReader context global script "coffee-script.js" 0 nil)
	      (finally (Context/exit)))
	 global)))

(defn compile-coffee
  ""
  [source]
  (let [context (Context/enter)]
    (try
      (let [cs (doto (.newObject context global-scope) (.setParentScope global-scope))]
	(.put cs "coffeeScriptSource" cs source)
	(.evaluateString context cs
			 (format "CoffeeScript.compile(coffeeScriptSource, %s);"
				 "{bare: true}")
			 "lein-coffee-script" 0 nil))
      (finally (Context/exit)))))

(defn coffee [project]
  "Compile coffee files"
  )
