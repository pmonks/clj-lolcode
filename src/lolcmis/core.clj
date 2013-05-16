;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcmis.core
  (:require [clojure.string      :as s]
            [lolcmis.parser      :as lp]
            [lolcmis.interpreter :as li])
  (:use [clojure.tools.cli :only [cli]]
        [clojure.pprint :only [pprint]])
  (:gen-class))

(defn -main
  "Interpret (evaluate) or parse a LOLCMIS program from the command line."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (let [[options args banner] (cli args
                                   ["-a" "--ast"  "Print the AST for the program, instead of interpreting it." :default false :flag true]
                                   ["-h" "--help" "Show help" :default false :flag true])]
    (let [ast      (:ast  options)
          help     (:help options)
          filename (first args)]
      (if (or help (nil? filename))
        (println (str banner "\n Args\t\tDesc\n ----\t\t----\n filename\tThe filename of the LOLCMIS program to interpret.\n"))
        (let [source (slurp filename)]
          (if ast
            (pprint (lp/parses source))
            (li/interpret source)))))))
