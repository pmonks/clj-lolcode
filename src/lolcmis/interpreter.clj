;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

; LOLCMIS (LOLCODE + CMIS) interpreter

(ns lolcmis.interpreter
  (:require [instaparse.core       :as insta]
            [clojure.tools.logging :as log]
            [lolcmis.parser        :as lp]))

; Interpreter implementation functions
(defn- get-var
  "Gets the value of a LOLCMIS variable."
  [var-name]
  (let [variable (find-var (symbol "lolprogram" var-name))]
    (if (nil? variable)
      (throw (Exception. (str "AINT GOT NO " var-name " FOR U!!1")))
      (var-get variable))))

(defn- set-var
  "Sets the value of a LOLCMIS variable to the specified value."
  [var-name value]
  (do
    (log/debug (str "Setting lolprogram/" var-name " to " (if (nil? value) "<nil>" value)))
    (intern 'lolprogram (symbol var-name) value)))

(defn- initialise
  "Initialises the LOLCMIS interpreter."
  [& args]
  (create-ns 'lolprogram)        ; Create a dedicated namespace for the program itself
  (set-var "IT" nil)             ; Define and initialise special variable "IT"
  (log/debug "LOL interpreter initialised"))

(defn- print-ast
  "Prints the AST - useful as a placeholder in the interpreter-functions map to see what, precisely, will be passed to the function."
  [& args]
  (println args))

(defn- true-literal
  "Returns true, regardless of the arguments."
  [& args]
  true)

(defn- false-literal
  "Returns false, regardless of the arguments."
  [& args]
  false)

(defn- void-literal
  "Returns nil, regardless of the arguments."
  [& args]
  nil)

(defn- output-statement
  "OutputStatement implementation."
  [& args]
  (let [token-type (first (first args))]
    (case token-type
      :Literal
        (print (second (first args)))
      :Identifier
        (print (get-var (second (first args)))))
    (flush)))

(defn- input-statement
  "InputStatement implementation."
  [& args]
  (set-var (second (first args)) (read-line)))

(defn- variable-declaration
  "VariableDeclaration implementation."
  [& args]
  (let [var-name (second (first args))]
    (if (= (count args) 1)
      (set-var var-name nil)   ; Variable was declared but not initialised
      (let [variable-declaration-type (first (second args))]
        (case variable-declaration-type
          :Type
            (let [data-type (second (second args))]
              (case data-type
                "YARN"   (set-var var-name "")
                "NUMBR"  (set-var var-name 0)
                "NUMBAR" (set-var var-name 0.0)
                "TROOF"  (set-var var-name false)
                "NOOB"   (set-var var-name nil)))
          :Literal
            (set-var var-name (second (second args)))
          :Identifier
            (set-var var-name (get-var (second (second args))))
          :CastExpression
            (let [value     (second (second (second args)))
                  cast-type (second (nth (second args) 2))]
              (case cast-type
                "YARN"   (set-var var-name (str                  value))
                "NUMBR"  (set-var var-name (Long/parseLong       value))   ; ####TODO: handle cast failures
                "NUMBAR" (set-var var-name (Double/parseDouble   value))   ; ####TODO: handle cast failures, consider using BigDecimal...
                "TROOF"  (set-var var-name (Boolean/parseBoolean value))   ; ####TODO: handle cast failures
                "NOOB"   (set-var var-name value))))))))

; The map of tokens to functions for the interpreter.
(def ^:private interpreter-function-map
  {
    :Header              initialise
    :StringLiteral       str
    :StringCharacter     str
    :EscapedDoubleQuote  str
    :IntegerLiteral      #(Long/parseLong %)
    :FloatLiteral        #(Double/parseDouble %)   ; ####TODO: consider using BigDecimal...
    :TrueLiteral         true-literal
    :FalseLiteral        false-literal
    :BooleanLiteral      identity
    :VoidLiteral         void-literal
    :OutputStatement     output-statement
    :InputStatement      input-statement
    :VariableDeclaration variable-declaration
    :Expression          identity
  })

; The interpreter itself
(defn interpret
  "Evaluates (interprets) a LOLCMIS program (or fragment, if a rule is provided)."
  ([source]      (interpret source :Program))
  ([source rule] (do (insta/transform interpreter-function-map (lp/parser source :start rule)) nil)))





