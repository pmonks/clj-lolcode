;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

; LOLCMIS (LOLCODE + CMIS) parser

(ns lolcmis.parser
  (:require [instaparse.core       :as insta]
            [clojure.tools.logging :as log]
            [lolcmis.grammar       :as lg]))

(def parser (insta/parser lg/lolcmis-grammar))

(defn parses
  "Parses a LOLCMIS program (or fragment, if a rule is provided).  May return multiple parse trees."
  ([source]      (parses source :Program))
  ([source rule] (insta/parses parser source :start rule)))

(defn number-of-asts
  "Returns the total number of parse trees for the given program (or fragment, if a rule is provided)."
  ([source]      (number-of-asts source :Program))
  ([source rule] (count (parses source rule))))
