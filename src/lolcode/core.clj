;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
; International License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/4.0/ or send a letter to Creative
; Commons, PO Box 1866, Mountain View, CA 94042, USA.
;

(ns lolcode.core
  (:require [clojure.string      :as s]
            [lolcode.parser      :as lp]
            [lolcode.interpreter :as li])
  (:use [clojure.tools.cli :only [cli]]
        [clojure.pprint :only [pprint]])
  (:gen-class))

(defn -main
  "Interpret (evaluate) or parse a LOLCMIS program from the command line."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (let [[options args banner] (cli args
                                   ["-a" "--ast"      "Print the clean AST for the program, instead of interpreting it." :default false :flag true]
                                   ["-r" "--raw-asts" "Print the raw AST(s) for the program, instead of interpreting it." :default false :flag true]
                                   ["-h" "--help"     "Show help" :default false :flag true])]
    (let [ast      (:ast      options)
          raw-asts (:raw-asts options)
          help     (:help     options)
          filename (first args)]
      (if (or help (nil? filename))
        (println (str banner "\n Args\t\tDesc\n ----\t\t----\n filename\tThe filename of the LOLCMIS program to interpret.\n"))
        (let [source (slurp filename)]
          (cond
            ast      (pprint (lp/clean-parse source))
            raw-asts (map #(do (println "\n\n**** NEXT AST ****") (pprint %) (flush)) (lp/raw-parses source))
            :else    (li/interpret source)))))))
