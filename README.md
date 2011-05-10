# clj-coffee-script

*clj-coffee-script* is a Clojure wrapper for compiling
 [CoffeeScript](http://www.coffeescript.org) source files into JavaScript.

## Usage

    (require '[coffee-script.core :as cs])

    ;; Compiles the CoffeeScript string "number = 42" into its
    ;; JavaScript form:
    ;; 
    ;;            (function() {
    ;;              var number;
    ;;              number = 42;
    ;;            }).call(this);
    ;;
    (cs/compile-coffee "number = 42")

    ;; Compiles the CoffeeScript string "x = [1,2,3]" into its
    ;; JavaScript form.  The CoffeeScript compiler is passed the
    ;; optional options map as a JSON string, in this case without the
    ;; top-level function safety wrapper:
    ;;
    ;;          var x;
    ;;          x = [1, 2, 3];
    ;; 
    (cs/compile-coffee "x = [1,2,3]" {:bare true})

    ;; If there is a problem with the built-in CoffeeScript compiler,
    ;; you can use an external version of the compiler by using the
    ;; with-coffee-compiler macro.  Simply pass it a variable that
    ;; can be coerced into a reader via clojure.java.io/Reader.
    ;;
    (with-coffee-compiler (clojure.java.io/file "/path/to/coffee-script.js")
        (cs/compile-coffee "number = -42 if opposite"))

    ;; The default compiler options can be changed by binding the
    ;; *default-coffee-options* symbol to a different value.
    (with-binding [*default-compile-options* {:bare true}]
        (cs/compile-coffee "square = (x) -> x * x"))

## Installing

clj-coffee-script is available via
[clojars](http://clojars.org/clj-coffee-script).  If you are using
[Leiningen](https://github.com/technomancy/leiningen), you can add the
package to your project by including the following dependency in your
project.clj file:

     [clj-coffee-script "1.0,0"]

## License

Copyright (C) 2011 Richard Bolkey

Distributed under the Eclipse Public License, the same as Clojure.
