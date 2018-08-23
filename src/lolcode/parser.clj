;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcode.parser
  (:require [instaparse.core       :as insta]
            [clojure.tools.logging :as log]
            [lolcode.grammar       :as lg]))

(def ^:private parser (insta/parser lg/lolcode-grammar))

; Logic for cleaning up the AST.
(defn- header
  [& args]
  (if (= 0 (count args))
    [:Version 1.2]
    [:Version (first args)]))

(defn- import-statement-list
  [& args]
  (if (empty? args)
    [:Imports nil]
    (vec (list :Imports (vec args)))))

(defn- statement-list
  [& args]
  (if (empty? args)
    [:Statements nil]
    (vec (list :Statements (vec args)))))

(defn- ast-comment
  [& args]
  [:Comment (clojure.string/join args)])

(def parser-function-map
  {
    ; Basic translations
    :Header              header
    :ImportStatement     second
    :ImportStatementList import-statement-list    ;  #(vec (list :Imports (vec %&)))
    :StatementList       statement-list           ;  #(vec (list :Statements (vec %&)))
    :StringLiteral       str
    :StringCharacter     str
    :EscapedDoubleQuote  str
    :NotNewLine          str
    :Comment             ast-comment
    :IntegerLiteral      #(Long/parseLong %)       ; ####TODO: consider using BigInteger...
    :FloatLiteral        #(Double/parseDouble %)   ; ####TODO: consider using BigDecimal...
    :TrueLiteral         #(identity true)
    :FalseLiteral        #(identity false)
    :VoidLiteral         #(identity nil)
  })

; The parsing functions
(defn clean-parse
  "Parses a LOLCMIS program (or fragment, if a rule is provided).  Returns a single clean AST."
  ([source]      (clean-parse source :Program))
  ([source rule] (vec (insta/transform parser-function-map (parser source :start rule)))))

(defn raw-parses
  "Parses a LOLCMIS program (or fragment, if a rule is provided).  Returns one or more raw ASTs."
  ([source]      (raw-parses source :Program))
  ([source rule] (insta/parses parser source :start rule)))

(defn number-of-asts
  "Returns the total number of parse trees for the given program (or fragment, if a rule is provided)."
  ([source]      (number-of-asts source :Program))
  ([source rule] (count (raw-parses source rule))))
