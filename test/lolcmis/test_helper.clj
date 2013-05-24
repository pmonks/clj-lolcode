;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcmis.test-helper
  (:use clojure.pprint
        clojure.java.io
        lolcmis.parser)
  (:require [instaparse.core :as insta]))

(defn print-ast
  ([source]      (print-ast source :Program))
  ([source rule] (pprint (parses source rule))))

(defn can-parse?
  ([source]                    (can-parse? source :Program))
  ([source rule]               (can-parse? source rule false))
  ([source rule print-failure]
    (let [asts   (parses source rule)
          result (not (insta/failure? asts))]
      (if (and print-failure (not result))
        (do
          (pprint "Parse failure:")
          (pprint (insta/get-failure asts))))
      result)))

(defn can-parse-file?
  ([file]      (can-parse-file? file :Program))
  ([file rule] (can-parse-file? file rule false))
  ([file rule print-failure]
    (let [source         (slurp file)
          parsed         (can-parse? source rule print-failure)
          number-of-asts (number-of-asts source rule)]

      (if (and print-failure (> number-of-asts 1))
        (println "Multiple ASTs!"))
      (and parsed (= number-of-asts 1))
    )))
