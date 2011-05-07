(defproject clj-coffee-script "1.0.0-SNAPSHOT"
  :description "Clojure wrapper for the CoffeeScript compiler."
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [rhino/js "1.7R2"]]
  :dev-dependencies [[lein-difftest "1.3.1"]
                     [swank-clojure "1.3.0"]]
  :hooks [leiningen.hooks.difftest])
