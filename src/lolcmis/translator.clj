;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

; LOLCMIS (LOLCODE + CMIS) translator

(ns lolcmis.translator
  (:require [instaparse.core       :as insta]
            [clojure.tools.logging :as log]
            [clojure.pprint        :as pp]
            [lolcmis.parser        :as lp]
            [lolcmis.runtime       :as rt]))

; Translator functions
(defn- print-ast
  "Helper function for dumping the AST that's passed in."
  [& args]
  (pp/pprint args))

(defn- program
  [& args]
  (list 'do args))

(defn- header
  [& args]
  (if (= 0 (count args))
    [:Header 1.2]
    [:Header (first args)]))

; Functions for cleaning up the AST.
(def ^:private transform-function-map
  {
    :Header              header
    :Statement           identity
    :Literal             identity
    :StringLiteral       str
    :StringCharacter     str
    :EscapedDoubleQuote  str
    :IntegerLiteral      #(Long/parseLong %)       ; ####TODO: consider using BigInteger...
    :FloatLiteral        #(Double/parseDouble %)   ; ####TODO: consider using BigDecimal...
    :TrueLiteral         #(identity true)
    :FalseLiteral        #(identity false)
    :BooleanLiteral      identity
    :VoidLiteral         #(identity nil)
    :Expression          identity
  })

(defn- translate-ast-to-clojure
  [ast]
  ; Do nothing (yet)
  )

(defn translate
  "Translates a LOLCMIS program (or fragment, if a rule is provided) into a series of Clojure forms."
  ([source]      (translate source :Program))
  ([source rule] ()
    (let [clean-ast (insta/transform transform-function-map (lp/parser source :start rule))]
      (print-ast clean-ast)   ; ####TEST!!!!
      (translate-ast-to-clojure clean-ast))))

