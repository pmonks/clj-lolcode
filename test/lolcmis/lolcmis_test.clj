;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcmis.lolcmis-test
  (:use midje.sweet
        clojure.pprint
        lolcmis.lolcmis
        clojure.java.io)
  (:require [instaparse.core :as insta]))

(defn- print-ast
  ([source]      (pprint (parse-lolcmis source)))
  ([source rule] (pprint (parse-lolcmis source rule))))

(defn- can-parse?
  ([source]      (not (insta/failure? (parse-lolcmis source))))
  ([source rule] (not (insta/failure? (parse-lolcmis source rule)))))

(defn- can-parse-file?
  ([file]      (can-parse? (slurp file)))
  ([file rule] (can-parse? (slurp file) rule)))

(facts "String literals are parsed correctly."
  (can-parse? "" :StringLiteral) => false
  (can-parse? "\"" :StringLiteral) => false
  (can-parse? "\"\"" :StringLiteral) => true
  (can-parse? "HAI!" :StringLiteral) => false
  (can-parse? "\"HAI!\"" :StringLiteral) => true
  (can-parse? "\"HAI LOLCMIS! CAN HAZ STIRNG LITS??\"" :StringLiteral) => true
  (can-parse? "\"Here is an escaped double quote: \\\"\"" :StringLiteral) => true
  (can-parse? "\"Here are a pair of escaped double quotes: \\\"HAI!\\\"\"" :StringLiteral) => true
)

(facts "Integer literals are parsed correctly."
  (can-parse? "" :IntegerLiteral) => false
  (can-parse? "\"" :IntegerLiteral) => false
  (can-parse? " " :IntegerLiteral) => false
  (can-parse? "abcd" :IntegerLiteral) => false
  (can-parse? "0" :IntegerLiteral) => true
  (can-parse? "12" :IntegerLiteral) => true
  (can-parse? "1234567890" :IntegerLiteral) => true
  (can-parse? "1234567890a" :IntegerLiteral) => false
  (can-parse? "a1234567890" :IntegerLiteral) => false
  (can-parse? "12345.67890" :IntegerLiteral) => false
)

(facts "Float literals are parsed correctly."
  (can-parse? "" :FloatLiteral) => false
  (can-parse? "\"" :FloatLiteral) => false
  (can-parse? " " :FloatLiteral) => false
  (can-parse? "abcd" :FloatLiteral) => false
  (can-parse? "0" :FloatLiteral) => false
  (can-parse? "12" :FloatLiteral) => false
  (can-parse? "1234567890" :FloatLiteral) => false
  (can-parse? "1234567890a" :FloatLiteral) => false
  (can-parse? "a1234567890" :FloatLiteral) => false
  (can-parse? "0.0" :FloatLiteral) => true
  (can-parse? "12.34" :FloatLiteral) => true
  (can-parse? "12345.67890" :FloatLiteral) => true
)

(facts "Boolean literals are parsed correctly."
  (can-parse? "" :BooleanLiteral) => false
  (can-parse? " " :BooleanLiteral) => false
  (can-parse? "1" :BooleanLiteral) => false
  (can-parse? "abcd" :BooleanLiteral) => false
  (can-parse? "win" :BooleanLiteral) => false
  (can-parse? "lose" :BooleanLiteral) => false
  (can-parse? "WIN" :BooleanLiteral) => true
  (can-parse? " WIN" :BooleanLiteral) => false
  (can-parse? "WIN " :BooleanLiteral) => false
  (can-parse? "LOSE" :BooleanLiteral) => true
  (can-parse? " LOSE" :BooleanLiteral) => false
  (can-parse? "LOSE " :BooleanLiteral) => false
)

(facts "Void literals are parsed correctly."
  (can-parse? "" :VoidLiteral) => false
  (can-parse? " " :VoidLiteral) => false
  (can-parse? "1" :VoidLiteral) => false
  (can-parse? "abcd" :VoidLiteral) => false
  (can-parse? "noob" :VoidLiteral) => false
  (can-parse? "NOOB" :VoidLiteral) => true
  (can-parse? " NOOB" :VoidLiteral) => false
  (can-parse? " NOOB " :VoidLiteral) => false
)

(facts "Identifiers are parsed correctly."
  (can-parse? "" :Identifier) => false
  (can-parse? " " :Identifier) => false
  (can-parse? "1" :Identifier) => false
  (can-parse? "_" :Identifier) => true
  (can-parse? "a" :Identifier) => true
  (can-parse? "abcd" :Identifier) => true
  (can-parse? " abcd" :Identifier) => false
  (can-parse? "abcd " :Identifier) => false
  (can-parse? " abcd " :Identifier) => false
  (can-parse? "_abcd" :Identifier) => true
  (can-parse? "ABCD123" :Identifier) => true
  (can-parse? "_ABCD" :Identifier) => true
  (can-parse? "123ABCD" :Identifier) => false
)

(facts "Comments are parsed correctly."
  (can-parse? "" :Comment) => false
  (can-parse? " " :Comment) => false
  (can-parse? "BTW" :Comment) => false
  (can-parse? "BTW" :Comment) => false
  (can-parse? "BTWabcd" :Comment) => false
  (can-parse? "BTWabcd\n" :Comment) => false
  (can-parse? "BTW abcd\n" :Comment) => true
  (can-parse? "BTW abcd 121431 @%&()-=+~`/,.<>';:\\[]{}_ 208941234 \" 2134097\r" :Comment) => true
)

(facts "Import statements are parsed correctly."
  (can-parse? "" :ImportStatement) => false
  (can-parse? " " :ImportStatement) => false
  (can-parse? "CAN HAZ" :ImportStatement) => false
  (can-parse? " CAN HAZ" :ImportStatement) => false
  (can-parse? "CAN HAZ ?" :ImportStatement) => false
  (can-parse? "CAN HAZ STDIO" :ImportStatement) => false
  (can-parse? "CAN HAZ STDIO?\n" :ImportStatement) => true
  (can-parse? "CAN HAZ CIMS?\r" :ImportStatement) => true
)

(facts "Output statements are parsed correctly."
  (can-parse? "" :OutputStatement) => false
  (can-parse? " " :OutputStatement) => false
  (can-parse? "VISIBLE" :OutputStatement) => false
  (can-parse? "VISIBLE " :OutputStatement) => false
  (can-parse? " VISIBLE" :OutputStatement) => false
  (can-parse? "VISIBLE \"\"\n" :OutputStatement) => true
  (can-parse? "VISIBLE \"HAI WORLD!\"\r" :OutputStatement) => true
  (can-parse? "VISIBLE 1234\r\n" :OutputStatement) => true
  (can-parse? "VISIBLE 1234.5678\n" :OutputStatement) => true
  (can-parse? "VISIBLE WIN\n" :OutputStatement) => true
  (can-parse? "VISIBLE LOSE\n" :OutputStatement) => true
  (can-parse? "VISIBLE NOOB\n" :OutputStatement) => true
  (can-parse? "VISIBLE ABCD\n" :OutputStatement) => true
)

(facts "Input statements are parsed correctly."
  (can-parse? "" :InputStatement) => false
  (can-parse? " " :InputStatement) => false
  (can-parse? "GIMMEH" :InputStatement) => false
  (can-parse? "GIMMEH " :InputStatement) => false
  (can-parse? " GIMMEH" :InputStatement) => false
  (can-parse? "GIMMEH \"\"\n" :InputStatement) => false
  (can-parse? "GIMMEH \"HAI WORLD!\"\r" :InputStatement) => false
  (can-parse? "GIMMEH 1234\r\n" :InputStatement) => false
  (can-parse? "GIMMEH 1234.5678\n" :InputStatement) => false
  (can-parse? "GIMMEH WIN\n" :InputStatement) => false
  (can-parse? "GIMMEH LOSE\n" :InputStatement) => false
  (can-parse? "GIMMEH NOOB\n" :InputStatement) => false
  (can-parse? "GIMMEH ABCD\n" :InputStatement) => true
  (can-parse? "GIMMEH MY_VAR\n" :InputStatement) => true
  (can-parse? "GIMMEH _MY_VAR\n" :InputStatement) => true
)

(def ^:private valid-programs-directory (file "test/valid"))
(def ^:private valid-test-programs (filter #(.endsWith (.getName %) ".LOL") (file-seq valid-programs-directory)))
(doall (map #(do (println "Parsing" (.getPath %) "...") (fact (can-parse-file? %) => true)) valid-test-programs))

(def ^:private invalid-programs-directory (file "test/invalid"))
(def ^:private invalid-test-programs (filter #(.endsWith (.getName %) ".LOL") (file-seq invalid-programs-directory)))
(doall (map #(do (println "Parsing" (.getPath %) "...") (fact (can-parse-file? %) => false)) invalid-test-programs))

;(print-ast "HAI 1.2\nVISIBLE \"HAI WRLD!\"\nVISIBLE \"BAI WRLD!\"\nKTHXBYE")


(comment
(print-ast "VISIBLE NOOB" :OutputStatement)
(print-ast "HAI\nKTHXBYE")
(print-ast "HAI\nVISIBLE \"HAI WRLD!\"\nKTHXBYE")
)
