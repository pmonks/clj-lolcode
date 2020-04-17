;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
; International License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/4.0/ or send a letter to Creative
; Commons, PO Box 1866, Mountain View, CA 94042, USA.
;

(ns lolcode.runtime
  (:require [instaparse.core       :as insta]
            [clojure.tools.logging :as log]))

; Interpreter implementation functions
(defn get-var
  "Gets the value of a LOLCODE variable."
  [var-name]
  (let [variable (find-var (symbol "lolprogram" var-name))]
    (if (nil? variable)
      (throw (Exception. (str "AINT GOT NO " var-name " FOR U!!1")))
      (var-get variable))))

(defn set-var
  "Sets the value of a LOLCODE variable to the specified value."
  [var-name value]
  (do
    (log/debug (str "Setting lolprogram/" var-name " to " (if (nil? value) "<nil>" value)))
    (intern 'lolprogram (symbol var-name) value)))

(defn initialise
  "Initialises the LOLCODE interpreter."
  [& args]
  (create-ns 'lolprogram)        ; Create a dedicated namespace for the program itself
  (set-var "IT" nil)             ; Define and initialise special variable "IT"
  (log/debug "LOL interpreter initialised"))

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
                "NUMBR"  (set-var var-name (Long/parseLong       value))   ; ####TODO: handle cast failures, consider using BigInteger...
                "NUMBAR" (set-var var-name (Double/parseDouble   value))   ; ####TODO: handle cast failures, consider using BigDecimal...
                "TROOF"  (set-var var-name (Boolean/parseBoolean value))   ; ####TODO: handle cast failures
                "NOOB"   (set-var var-name value))))))))

(defn- cast-expression
  "CastExpression implementation."
  [& args]
  (let [var-name  (first args)
        type      (second args)
        old-value (get-var var-name)
        new-value (case type
                    "YARN"   (str                  old-value)
                    "NUMBR"  (Long/parseLong       old-value)   ; ####TODO: handle cast failures, consider using BigInteger...
                    "NUMBAR" (Double/parseDouble   old-value)   ; ####TODO: handle cast failures, consider using BigDecimal...
                    "TROOF"  (Boolean/parseBoolean old-value)   ; ####TODO: handle cast failures
                    "NOOB"   old-value)]
    (set-var var-name new-value)
    new-value))
