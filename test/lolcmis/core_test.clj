;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcmis.core-test
  (:use midje.sweet
        clojure.pprint
        lolcmis.lolcmis
        com.lithinos.amotoen.core))

; Helper functions
(defn can-parse?
  ([source]      (try (not (nil? (parse-lolcmis source)))      (catch Error e false)))
  ([source rule] (try (not (nil? (parse-lolcmis source rule))) (catch Error e false))))

(defn print-ast
  ([source]      (pprint (parse-lolcmis source)))
  ([source rule] (pprint (parse-lolcmis source rule))))

(facts "Amotoen is ok and lolcmis grammar can be parsed."
  (first (self-check)) => true
  (first (validate lolcmis-grammar)) => true
)

(facts "Whitespace is parsed correctly."
  (can-parse? " " [:_* :EndOfInput]) => true
  (can-parse? "\t" [:_* :EndOfInput]) => true
  (can-parse? "\n" [:_* :EndOfInput]) => true
  (can-parse? "\r" [:_* :EndOfInput]) => true
  (can-parse? "            " [:_* :EndOfInput]) => true
  (can-parse? "    \r    \r   \n\n\n    \t   \r\r   \n\r\t  " [:_* :EndOfInput]) => true
  (can-parse? "a" [:_* :EndOfInput]) => false
  (can-parse? "7" [:_* :EndOfInput]) => false
  (can-parse? "." [:_* :EndOfInput]) => false
  (can-parse? "cat" [:_* :EndOfInput]) => false
  (can-parse? "dog37" [:_* :EndOfInput]) => false
  (can-parse? "BaldEagle10267" [:_* :EndOfInput]) => false
)

(facts "AlphaNumerics are parsed correctly."
  (can-parse? "" [:AlphaNumeric :EndOfInput]) => false
  (can-parse? " " [:AlphaNumeric :EndOfInput]) => false
  (can-parse? "a" [:AlphaNumeric :EndOfInput]) => true
  (can-parse? "7" [:AlphaNumeric :EndOfInput]) => true
  (can-parse? "." [:AlphaNumeric :EndOfInput]) => false
  (can-parse? " " [:AlphaNumeric :EndOfInput]) => false
  (can-parse? "" [:AlphaNumerics :EndOfInput]) => false
  (can-parse? "a" [:AlphaNumerics :EndOfInput]) => true
  (can-parse? "7" [:AlphaNumerics :EndOfInput]) => true
  (can-parse? "." [:AlphaNumerics :EndOfInput]) => false
  (can-parse? " " [:AlphaNumerics :EndOfInput]) => false
  (can-parse? "cat" [:AlphaNumerics :EndOfInput]) => true
  (can-parse? "dog37" [:AlphaNumerics :EndOfInput]) => true
  (can-parse? "BaldEagle10267" [:AlphaNumerics :EndOfInput]) => true
  (can-parse? "Bald Eagle" [:AlphaNumerics :EndOfInput]) => false
)

(facts "Identifiers are parsed correctly."
  (can-parse? "" [:Identifier :EndOfInput]) => false
  (can-parse? "A" [:Identifier :EndOfInput]) => true
  (can-parse? "A9" [:Identifier :EndOfInput]) => true
  (can-parse? "AVeryLongIdentifierName" [:Identifier :EndOfInput]) => true
  (can-parse? "9A" [:Identifier :EndOfInput]) => false
  (can-parse? "An invalid identifier name" [:Identifier :EndOfInput]) => false
)

(facts "String literals are parsed correctly."
  (can-parse? "" [:StringLiteral :EndOfInput]) => false
  (can-parse? "\"" [:StringLiteral :EndOfInput]) => false
  (can-parse? "\"\"" [:StringLiteral :EndOfInput]) => true
  (can-parse? "\"HAI!\"" [:StringLiteral :EndOfInput]) => true
  (can-parse? "\"HAI LOLCMIS! CAN HAZ STIRNG LITS??\"" [:StringLiteral :EndOfInput]) => true
  (can-parse? "\"Here is an escaped double quote: \\\"\"" [:StringLiteral :EndOfInput]) => true
  (can-parse? "\"Here are a pair of escaped double quotes: \\\"HAI!\\\"\"" [:StringLiteral :EndOfInput]) => true
)

(facts "HAI is parsed correctly."
  (can-parse? "HAI" [:Hai :EndOfInput]) => true
  (can-parse? "HI" [:Hai :EndOfInput]) => false
)

(facts "KTHXBYE is parsed correctly."
  (can-parse? "KTHXBYE" [:KThxBye :EndOfInput]) => true
  (can-parse? "KTHXBAI" [:KThxBye :EndOfInput]) => false
)

(facts "CAN HAZ <library name>? is parsed correctly."
  (can-parse? "" [:CanHaz :EndOfInput]) => false
  (can-parse? "CAN HAZ" [:CanHaz :EndOfInput]) => false
  (can-parse? "CAN HAZ?" [:CanHaz :EndOfInput]) => false
  (can-parse? "CAN HAZ ?" [:CanHaz :EndOfInput]) => false
  (can-parse? "CAN HAZ STDIO" [:CanHaz :EndOfInput]) => false
  (can-parse? "CAN HAZ STDIO?" [:CanHaz :EndOfInput]) => true
  (can-parse? "CAN HAZ CIMS?" [:CanHaz :EndOfInput]) => true
)

(facts "VISIBLE <string literal> is parsed correctly."
  (can-parse? "" [:Visible :EndOfInput]) => false
  (can-parse? "VISIBLE" [:Visible :EndOfInput]) => false
  (can-parse? "VISIBLE " [:Visible :EndOfInput]) => false
  (can-parse? "VISIBLE \"" [:Visible :EndOfInput]) => false
  (can-parse? "VISIBLE \"\"" [:Visible :EndOfInput]) => true
  (can-parse? "VISIBLE \"OH HAI WRLD!\"" [:Visible :EndOfInput]) => true
)

(facts "Full programs are parsed correctly."
  (can-parse? "") => false
  (can-parse? "HAI") => false
  (can-parse? "KTHXBYE") => false
  (can-parse? "HI") => false
  (can-parse? "BYE") => false
  (can-parse? "HAI KTHXBYE") => true
  (can-parse? "HAI\nKTHXBYE") => true
  (can-parse? "HAI\n\nKTHXBYE") => true
  (can-parse? "\nHAI\nKTHXBYE") => true
  (can-parse? "\n\n\n\n\r\r\rHAI\nKTHXBYE") => true
  (can-parse? "HAI\nHAI\nKTHXBYE") => false
  (can-parse? "HAI\nKTHXBYE\nKTHXBYE") => false
  (can-parse? "HAI\nCAN HAZ STDIO?\nKTHXBYE") => true
  (can-parse? "HAI\nCAN HAZ STDIO?\nCAN HAZ CIMS?\nKTHXBYE") => true
  (can-parse? "HAI CAN HAZ STDIO? CAN HAZ CIMS? KTHXBYE") => true
  (can-parse? "HAI CAN HAZ STDIO? CAN HAZ CIMS? CAN HAZ KITTEHZ? KTHXBYE") => true
  (can-parse? "HAI\nCAN HAZ STDIO?\nVISIBLE \"HAI WORLD!!1\"\nKTHXBYE") => true
)

;(print-ast "HAI\nCAN HAZ STDIO?\nCAN HAZ CIMS?\nKTHXBYE")
