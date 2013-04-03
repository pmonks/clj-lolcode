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
  (:require [com.lithinos.amotoen.core :as a]))

; Grammar definition and parser
(def lolcmis-grammar {
  ; General rules
  :_*                     '(* (| :Whitespace :EndOfLine))
  :Whitespace             '(| \space \tab)
  :EndOfLine              '(| \newline \return)
  :EndOfInput             :$
  :Alpha                  (a/lpegs '| "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
  :Numeric                (a/lpegs '| "0123456789")
  :Numerics               [:Numeric '(* :Numeric)]
  :AlphaNumeric           (a/lpegs '| "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")
  :AlphaNumerics          [:AlphaNumeric '(* :AlphaNumeric)]
  :QuestionMark           \?
  :ExclamationMark        \!
  :DoubleQuote            \"
  :EscapedDoubleQuote     [\\ \"]
  :StringLiteralChar      '(| :EscapedDoubleQuote (% :DoubleQuote))
  :StringLiteralChars     '(* :StringLiteralChar)
  :StringLiteral          [:DoubleQuote :StringLiteralChars :DoubleQuote]
;  :StringLiteral          [:DoubleQuote :DoubleQuote]
  :NumberLiteral          :Numerics

  ; LOLCODE specific rules
  :Identifier             [:Alpha '(* :AlphaNumeric)]
  :LOLProgram             [:_* :Hai :_* :Imports :_* :Statements :_* :KThxBye :_* :EndOfInput]
  :Hai                    (vec "HAI")
  :Imports                '(* [:_* :CanHaz])
  :Statements             '(* [:_* :Statement])
  :KThxBye                (vec "KTHXBYE")
  :CanHaz                 [(vec "CAN HAZ ") :Identifier :QuestionMark]
  :Statement              '(| :Visible)
  :Visible                [(vec "VISIBLE ") :_* :StringLiteral]
  })

(defn parse-lolcmis
  "Parses a LOLCMIS program (or fragment, if a rule is provided), returning the AST or nil if parsing fails."
  ([source]      (parse-lolcmis source :LOLProgram))
  ([source rule] (a/pegasus rule lolcmis-grammar (a/wrap-string source))))


; Interpreter
(defn- noop [ast] "")

(defn- string-literal-chars
  [ast]
  (apply str (map :StringLiteralChar ast)))

(defn- string-literal
  [ast]
  (:StringLiteralChars (second ast)))

(defn- visible
  [ast]
  (println (:StringLiteral (nth ast 2))))

(def #^{:private true} lolcmis-grammar-fns {
  :StringLiteralChars string-literal-chars
  :StringLiteral      string-literal
  :Visible            visible
  })

(defn eval-lolcmis
  "Evaluates the given LOLCMIS program."
  [source]
  (a/post-process :LOLProgram lolcmis-grammar (a/wrap-string source) lolcmis-grammar-fns))
