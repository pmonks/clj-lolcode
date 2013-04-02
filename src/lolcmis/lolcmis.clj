;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

; Implementation of LOLCMIS (LOLCODE + CMIS) interpreter

(ns lolcmis.lolcmis
  (:require [com.lithinos.amotoen.core :as amotoen]))

(def lolcmis-grammar {
  ; General rules
  :_*                     '(* (| :Whitespace :EndOfLine))
  :Whitespace             '(| \space \tab)
  :EndOfLine              '(| \newline \return)
  :EndOfInput             :$
  :Alpha                  (amotoen/lpegs '| "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
  :Numeric                (amotoen/lpegs '| "0123456789")
  :AlphaNumeric           (amotoen/lpegs '| "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")
  :AlphaNumerics          [:AlphaNumeric '(* :AlphaNumeric)]
  :QuestionMark           \?
  :ExclamationMark        \!

  ; LOLCODE specific rules
  :Identifier             [:Alpha '(* :AlphaNumeric)]
  :LOLProgram             [:_* :Hai :_* :Imports :_* :KThxBye :_* :EndOfInput]
  :Hai                    (vec "HAI")
  :Imports                '(* [:_* :CanHaz])
  :KThxBye                (vec "KTHXBYE")
  :CanHaz                 [(vec "CAN HAZ ") :Identifier :QuestionMark]
  })

(defn parse-lolcmis
  "Parses a LOLCMIS program (or fragment, if a rule is provided), returning the AST or nil if parsing fails."
  ([source]      (parse-lolcmis source :LOLProgram))
  ([source rule] (amotoen/pegasus rule lolcmis-grammar (amotoen/wrap-string source))))
