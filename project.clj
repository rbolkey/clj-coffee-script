(defproject clj-coffee-script "1.0.0"
  :description "Clojure wrapper for the CoffeeScript compiler."
  :url "http://www.github.com/rbolkey/clj-coffee-script"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [rhino/js "1.7R2"]]
  :dev-dependencies [[lein-difftest "1.3.1"]
                     [swank-clojure "1.3.0"]]
  :hooks [leiningen.hooks.difftest])
