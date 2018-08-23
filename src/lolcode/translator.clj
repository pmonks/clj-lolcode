;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcode.translator
  (:require [clojure.tools.logging :as log]
            [clojure.pprint        :as pp]
            [lolcode.parser        :as lp]
            [lolcode.runtime       :as rt]))

; Translator functions
(defn- print-ast
  "Helper function for dumping the AST that's passed in.  Note: wraps the input in a list."
  [& args]
  (pp/pprint args))

(def translator-function-map (merge lp/parser-function-map
  {
    ; ####TODO: add translator specific function mappings, including overrides (as needed)
  }))


(defn- translate-ast-to-clojure
  [ast]
  ; Do nothing (yet)
  )

(defn translate
  "Translates a LOLCODE program (or fragment, if a rule is provided) into a series of Clojure forms."
  ([source]      (translate source :Program))
  ([source rule]
    (let [clean-ast (lp/clean-parse source rule)]
      (pp/pprint clean-ast)   ; ####TEST!!!!
      (translate-ast-to-clojure clean-ast))))

