;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
; International License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/4.0/ or send a letter to Creative
; Commons, PO Box 1866, Mountain View, CA 94042, USA.
;

(ns lolcode.file-based-parser-test
  (:use midje.sweet
        clojure.pprint
        lolcode.parser
        clojure.java.io
        lolcode.test-helper)
  (:require [instaparse.core :as insta]))

(println "\n☔️ Running tests on Clojure" (clojure-version) "/ JVM" (System/getProperty "java.version") (str "(" (System/getProperty "java.vm.name") " v" (System/getProperty "java.vm.version") ")"))

(println "---- FILE BASED PARSER TESTS ----")

(def ^:private valid-programs-directory (file "test/valid"))
(def ^:private valid-test-programs (filter #(.endsWith (.toUpperCase (.getName %)) ".LOL") (file-seq valid-programs-directory)))
(doall (map #(do (println "Parsing" (.getPath %) "...") (fact (can-parse-file? % :Program true) => true)) valid-test-programs))

(def ^:private invalid-programs-directory (file "test/invalid"))
(def ^:private invalid-test-programs (filter #(.endsWith (.toUpperCase (.getName %)) ".LOL") (file-seq invalid-programs-directory)))
(doall (map #(do (println "Parsing" (.getPath %) "...") (fact (can-parse-file? %) => false)) invalid-test-programs))
