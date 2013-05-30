;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcmis.parser-test
  (:use midje.sweet
        lolcmis.parser
        lolcmis.test-helper)
  (:require [instaparse.core :as insta]))

(println "---- STRING LITERAL BASED PARSER TESTS ----")

(facts "String literals can be parsed."
  (can-parse? "" :StringLiteral) => false
  (can-parse? "\"" :StringLiteral) => false
  (can-parse? "\"\"" :StringLiteral) => true
  (can-parse? "\"\\\"\"" :StringLiteral) => true
  (can-parse? "HAI!" :StringLiteral) => false
  (can-parse? "\"HAI!\"" :StringLiteral) => true
  (can-parse? "\"HAI LOLCMIS! CAN HAZ STIRNG LITS??\"" :StringLiteral) => true
  (can-parse? "\"Here is an escaped double quote: \\\" followed by some more text.\"" :StringLiteral) => true
  (can-parse? "\"Here are a pair of escaped double quotes: \\\"HAI!\\\"\"" :StringLiteral) => true
  (can-parse? "\"String literal containing \\an escaped backslash\"" :StringLiteral) => true
  (can-parse? "\"String literal containing \nan escaped newline\"" :StringLiteral) => true
  (can-parse? "\"String literal containing
an explicit newline\"" :StringLiteral) => true
)

(facts "Integer literals can be parsed."
  (can-parse? "" :IntegerLiteral) => false
  (can-parse? "\"" :IntegerLiteral) => false
  (can-parse? " " :IntegerLiteral) => false
  (can-parse? "abcd" :IntegerLiteral) => false
  (can-parse? "-" :IntegerLiteral) => false
  (can-parse? "0" :IntegerLiteral) => true
  (can-parse? "12" :IntegerLiteral) => true
  (can-parse? "-12" :IntegerLiteral) => true
  (can-parse? "-0" :IntegerLiteral) => true
  (can-parse? "1234567890" :IntegerLiteral) => true
  (can-parse? "1234567890a" :IntegerLiteral) => false
  (can-parse? "a1234567890" :IntegerLiteral) => false
  (can-parse? "12345.67890" :IntegerLiteral) => false
)

(facts "Float literals can be parsed."
  (can-parse? "" :FloatLiteral) => false
  (can-parse? "\"" :FloatLiteral) => false
  (can-parse? " " :FloatLiteral) => false
  (can-parse? "abcd" :FloatLiteral) => false
  (can-parse? "-" :FloatLiteral) => false
  (can-parse? "0" :FloatLiteral) => false
  (can-parse? "12" :FloatLiteral) => false
  (can-parse? "-12" :FloatLiteral) => false
  (can-parse? "1234567890" :FloatLiteral) => false
  (can-parse? "1234567890a" :FloatLiteral) => false
  (can-parse? "a1234567890" :FloatLiteral) => false
  (can-parse? "0.0" :FloatLiteral) => true
  (can-parse? "12.34" :FloatLiteral) => true
  (can-parse? "12345.67890" :FloatLiteral) => true
  (can-parse? "-12.34" :FloatLiteral) => true
  (can-parse? "-0.0" :FloatLiteral) => true
)

(facts "Boolean literals can be parsed."
  (can-parse? "" :BooleanLiteral) => false
  (can-parse? " " :BooleanLiteral) => false
  (can-parse? "1" :BooleanLiteral) => false
  (can-parse? "abcd" :BooleanLiteral) => false
  (can-parse? "win" :BooleanLiteral) => false
  (can-parse? "lose" :BooleanLiteral) => false
  (can-parse? "WIN" :BooleanLiteral) => true
  (can-parse? " WIN" :BooleanLiteral) => false
  (can-parse? "WIN " :BooleanLiteral) => false
  (can-parse? "FAIL" :BooleanLiteral) => true
  (can-parse? " FAIL" :BooleanLiteral) => false
  (can-parse? "FAIL " :BooleanLiteral) => false
)

(facts "Void literals can be parsed."
  (can-parse? "" :VoidLiteral) => false
  (can-parse? " " :VoidLiteral) => false
  (can-parse? "1" :VoidLiteral) => false
  (can-parse? "abcd" :VoidLiteral) => false
  (can-parse? "noob" :VoidLiteral) => false
  (can-parse? "NOOB" :VoidLiteral) => true
  (can-parse? " NOOB" :VoidLiteral) => false
  (can-parse? " NOOB " :VoidLiteral) => false
)

(facts "Identifiers can be parsed."
  (can-parse? "" :Identifier) => false
  (can-parse? " " :Identifier) => false
  (can-parse? "1" :Identifier) => false
  (can-parse? "\"" :Identifier) => false
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
  (can-parse? "ABCD\"" :Identifier) => false
  (can-parse? "ABCD+_@#$-9" :Identifier) => false
)

(facts "Single line comments can be parsed."
  (can-parse? "" :SingleLineComment) => false
  (can-parse? " " :SingleLineComment) => false
  (can-parse? "BTW" :SingleLineComment) => true
  (can-parse? "BTWabcd" :SingleLineComment) => false
  (can-parse? "BTWabcd\n" :SingleLineComment) => false
  (can-parse? "BTW " :SingleLineComment) => true
  (can-parse? "BTW abcd" :SingleLineComment) => true
  (can-parse? "BTW abcd    " :SingleLineComment) => true
  (can-parse? "BTW abcd\t" :SingleLineComment) => true
  (can-parse? "BTW abcd 121431 @%&()-=+~`/,.<>';:\\[]{}_ 208941234 \" 2134097" :SingleLineComment) => true
  (can-parse? "BTW VISIBLE GIMMEH I HAS A ITZ YARN NUMBR NUMBAR TROOF NOOB WIN LOSE" :SingleLineComment) => true
)

(facts "Multi line comments can be parsed."
  (can-parse? "" :MultiLineComment) => false
  (can-parse? " " :MultiLineComment) => false
  (can-parse? "OBTW" :MultiLineComment) => false
  (can-parse? "OBTW TLDR" :MultiLineComment) => true
  (can-parse? "OBTW TLDR" :MultiLineComment) => true
  (can-parse? "OBTW\nTLDR" :MultiLineComment) => true
  (can-parse? "OBTW \nTLDR" :MultiLineComment) => true
  (can-parse? "OBTW
TLDR" :MultiLineComment) => true
  (can-parse? "OBTW Here is a multi-line comment on a single line TLDR" :MultiLineComment) => true
  (can-parse? "OBTW\nHere is a multi-line comment\rTLDR" :MultiLineComment) => true
  (can-parse? "OBTW\n Here \nis \na\n multi-line\n comment\n\r\n\r\r\nTLDR" :MultiLineComment) => true
  (can-parse? "OBTW\n abcd 121431 @%&()-=+~`/,.<>';:\\[]{}_ 208941234 \" 2134097\rTLDR" :MultiLineComment) => true
  (can-parse? "OBTW
VISIBLE
  GIMMEH
    I HAS A
ITZ
YARN
NUMBR
NUMBAR
TROOF
NOOB
WIN
LOSE
TLDR" :MultiLineComment) => true
)

(facts "Import statements can be parsed."
  (can-parse? "" :ImportStatement) => false
  (can-parse? " " :ImportStatement) => false
  (can-parse? "CAN HAZ" :ImportStatement) => false
  (can-parse? " CAN HAZ" :ImportStatement) => false
  (can-parse? "CAN HAZ ?" :ImportStatement) => false
  (can-parse? "CAN HAZ STDIO" :ImportStatement) => false
  (can-parse? "CAN HAZ STDIO?" :ImportStatement) => true
  (can-parse? "CAN HAZ CIMS?" :ImportStatement) => true
)

(facts "Output statements can be parsed."
  (can-parse? "" :OutputStatement) => false
  (can-parse? " " :OutputStatement) => false
  (can-parse? "VISIBLE" :OutputStatement) => false
  (can-parse? "VISIBLE " :OutputStatement) => false
  (can-parse? " VISIBLE" :OutputStatement) => false
  (can-parse? "VISIBLE \"\"" :OutputStatement) => true
  (can-parse? "VISIBLE \"HAI WORLD!\"" :OutputStatement) => true
  (can-parse? "VISIBLE 1234" :OutputStatement) => true
  (can-parse? "VISIBLE 1234.5678" :OutputStatement) => true
  (can-parse? "VISIBLE WIN" :OutputStatement) => true
  (can-parse? "VISIBLE LOSE" :OutputStatement) => true
  (can-parse? "VISIBLE NOOB" :OutputStatement) => true
  (can-parse? "VISIBLE ABCD" :OutputStatement) => true
)

(facts "Input statements can be parsed."
  (can-parse? "" :InputStatement) => false
  (can-parse? " " :InputStatement) => false
  (can-parse? "GIMMEH" :InputStatement) => false
  (can-parse? "GIMMEH " :InputStatement) => false
  (can-parse? " GIMMEH" :InputStatement) => false
  (can-parse? "GIMMEH \"\"" :InputStatement) => false
  (can-parse? "GIMMEH \"HAI WORLD!\"" :InputStatement) => false
  (can-parse? "GIMMEH 1234" :InputStatement) => false
  (can-parse? "GIMMEH 1234.5678" :InputStatement) => false
  (can-parse? "GIMMEH WIN" :InputStatement) => false     ; Reserved word
  (can-parse? "GIMMEH FAIL" :InputStatement) => false    ; Reserved word
  (can-parse? "GIMMEH NOOB" :InputStatement) => false    ; Reserved word
  (can-parse? "GIMMEH ABCD" :InputStatement) => true
  (can-parse? "GIMMEH MY_VAR" :InputStatement) => true
  (can-parse? "GIMMEH _MY_VAR" :InputStatement) => true
)

(facts "Variable declarations can be parsed."
  (can-parse? "" :VariableDeclaration) => false
  (can-parse? " " :VariableDeclaration) => false
  (can-parse? "I HAS A" :VariableDeclaration) => false
  (can-parse? "I HAS A " :VariableDeclaration) => false
  (can-parse? "I HAS A ABCD" :VariableDeclaration) => true
  (can-parse? "I HAS A NOOB" :VariableDeclaration) => false
  (can-parse? "I HAS A ABCD ITZ \"HAI WORLD!\"" :VariableDeclaration) => true
  (can-parse? "I HAS A PIE ITZ 3" :VariableDeclaration) => true
  (can-parse? "I HAS A PIE ITZ 3" :VariableDeclaration) => true
  (can-parse? "I HAS A ABCD ITZ A YARN" :VariableDeclaration) => true
  (can-parse? "I HAS A _ABCD ITZ EFGH" :VariableDeclaration) => true
  (can-parse? "I HAS A ABCD ITZ EFGH IS NOW A INVALID" :VariableDeclaration) => false
  (can-parse? "I HAS A ABCD ITZ EFGH IS NOW A NOOB" :VariableDeclaration) => true
)

(facts "Cast expressions can be parsed."
  (can-parse? "" :CastExpression) => false
  (can-parse? " " :CastExpression) => false
  (can-parse? "MY_VAR IS" :CastExpression) => false
  (can-parse? "MY_VAR IS NOW" :CastExpression) => false
  (can-parse? "MY_VAR IS NOW A" :CastExpression) => false
  (can-parse? "MY_VAR IS NOW A FOO" :CastExpression) => false
  (can-parse? "MY_VAR IS NOW A YARN" :CastExpression) => true
  (can-parse? "MY_VAR IS NOW A NUMBR" :CastExpression) => true
  (can-parse? "MY_VAR IS NOW A NUMBAR" :CastExpression) => true
  (can-parse? "MY_VAR IS NOW A TROOF" :CastExpression) => true
  (can-parse? "MY_VAR IS NOW A NOOB" :CastExpression) => true
)

(facts "Assignments can be parsed."
  (can-parse? "" :Assignment) => false
  (can-parse? " " :Assignment) => false
  (can-parse? "ABCD R" :Assignment) => false
  (can-parse? "ABCD R 1" :Assignment) => true
  (can-parse? "_EFGHI R NOOB" :Assignment) => true
  (can-parse? "ZZ167 R WIN" :Assignment) => true
  (can-parse? "ZZ167 R LOSE" :Assignment) => true
  (can-parse? "KITTEH R \"CUTE\"" :Assignment) => true
  (can-parse? "ABCD R EFGHI" :Assignment) => true
  (can-parse? "ABCD R EFGHI IS NOW A INVALID" :Assignment) => false
  (can-parse? "ABCD R EFGHI IS NOW A YARN" :Assignment) => true
  (can-parse? "ABCD R EFGHI IS NOW A NUMBR" :Assignment) => true
)

(facts "Equals expressions can be parsed."
  (can-parse? "" :EqualsExpression) >= false
  (can-parse? " " :EqualsExpression) >= false
  (can-parse? "BOTH SAEM" :EqualsExpression) => false
  (can-parse? "BOTH SAEM VAR" :EqualsExpression) => false
  (can-parse? "BOTH SAEM VAR AN 1" :EqualsExpression) => true
  (can-parse? "BOTH SAEM _VAR AN \"KITTEHZ!!\"" :EqualsExpression) => true
  (can-parse? "BOTH SAEM _VAR AN OTHER_VAR" :EqualsExpression) => true
)

(facts "Not equals expressions can be parsed."
  (can-parse? "" :NotEqualsExpression) >= false
  (can-parse? " " :NotEqualsExpression) >= false
  (can-parse? "DIFFRINT" :NotEqualsExpression) => false
  (can-parse? "DIFFRINT VAR" :NotEqualsExpression) => false
  (can-parse? "DIFFRINT VAR AN 1" :NotEqualsExpression) => true
  (can-parse? "DIFFRINT _VAR AN \"KITTEHZ!!\"" :NotEqualsExpression) => true
  (can-parse? "DIFFRINT _VAR AN OTHER_VAR" :NotEqualsExpression) => true
)

(facts "And expressions can be parsed."
  (can-parse? "" :AndExpression) >= false
  (can-parse? " " :AndExpression) >= false
  (can-parse? "BOTH" :AndExpression) => false
  (can-parse? "BOTH OF VAR" :AndExpression) => false
  (can-parse? "BOTH OF WIN AN WIN" :AndExpression) => true
  (can-parse? "BOTH OF _VAR AN OTHER_VAR" :AndExpression) => true
)

(facts "Or expressions can be parsed."
  (can-parse? "" :OrExpression) >= false
  (can-parse? " " :OrExpression) >= false
  (can-parse? "EITHER" :OrExpression) => false
  (can-parse? "EITHER OF VAR" :OrExpression) => false
  (can-parse? "EITHER OF VAR AN 1" :OrExpression) => true
  (can-parse? "EITHER OF _VAR AN OTHER_VAR" :OrExpression) => true
)

(facts "Addition expressions can be parsed."
  (can-parse? "" :AdditionExpression) >= false
  (can-parse? " " :AdditionExpression) >= false
  (can-parse? "SUM" :AdditionExpression) => false
  (can-parse? "SUM OF" :AdditionExpression) => false
  (can-parse? "SUM OF VAR" :AdditionExpression) => false
  (can-parse? "SUM OF VAR AN" :AdditionExpression) => false
  (can-parse? "SUM OF VAR AN 1" :AdditionExpression) => true
  (can-parse? "SUM OF _VAR AN OTHER_VAR" :AdditionExpression) => true
)

(fact "Max expressions can be parsed."
  (can-parse? "" :MaxExpression) >= false
  (can-parse? " " :MaxExpression) >= false
  (can-parse? "BIGGR" :MaxExpression) >= false
  (can-parse? "BIGGR OF" :MaxExpression) >= false
  (can-parse? "BIGGR OF VAR" :MaxExpression) >= false
  (can-parse? "BIGGR OF VAR AN" :MaxExpression) >= false
  (can-parse? "BIGGR OF VAR AN 1" :MaxExpression) >= true
  (can-parse? "BIGGR OF VAR AN OTHER_VAR" :MaxExpression) >= true
)

(fact "Min expressions can be parsed."
  (can-parse? "" :MinExpression) >= false
  (can-parse? " " :MinExpression) >= false
  (can-parse? "SMALLR" :MinExpression) >= false
  (can-parse? "SMALLR OF" :MinExpression) >= false
  (can-parse? "SMALLR OF VAR" :MinExpression) >= false
  (can-parse? "SMALLR OF VAR AN" :MinExpression) >= false
  (can-parse? "SMALLR OF VAR AN 1" :MinExpression) >= true
  (can-parse? "SMALLR OF VAR AN OTHER_VAR" :MinExpression) >= true
)

(facts "If-only conditionals can be parsed."
  (can-parse? "" :Conditional) => false
  (can-parse? "BTW" :Conditional) => false
  (can-parse? "BOTH SAEM" :Conditional) => false
  (can-parse? "BOTH SAEM VAR AN 1" :Conditional) => false
  (can-parse? "BOTH SAEM VAR AN 1\nO RLY?" :Conditional) => false
  (can-parse? "O RLY?\nOIC" :Conditional) => false
  (can-parse? "BOTH SAEM VAR AN 1\nO RLY?\rYA RLY\nOIC" :Conditional) => true
  (can-parse? "BOTH SAEM VAR AN \"KITTEHZ\",O RLY?,YA RLY,OIC" :Conditional) => true
  (can-parse? "BOTH SAEM VAR_1 AN VAR_2
    O RLY?
      YA RLY

    OIC" :Conditional) => true
)

(facts "If-else conditionals can be parsed."
  (can-parse? "BOTH SAEM VAR1 AN VAR2\nO RLY?\nYA RLY\nNO" :Conditional) => false
  (can-parse? "BOTH SAEM VAR AN 1\nO RLY?\nYA RLY\nNOWAI" :Conditional) => false
  (can-parse? "BOTH SAEM VAR AN \"KITTEHZ!!1\"\nO RLY?\nYA RLY\nWAI" :Conditional) => false
  (can-parse? "BOTH SAEM _VAR AN 3.14156,O RLY?,YA RLY,NO WAI" :Conditional) => false
  (can-parse? "BOTH SAEM _VAR AN _VAR,O RLY?,NO WAI" :Conditional) => false
  (can-parse? "BOTH SAEM _VAR AN _VAR,O RLY?,NO WAI,OIC" :Conditional) => false
  (can-parse? "BOTH SAEM _VAR AN _VAR,O RLY?,NO WAI,OIC" :Conditional) => false
  (can-parse? "BOTH SAEM _VAR AN _VAR\rO RLY?\rYA RLY\nNO WAI\nOIC" :Conditional) => true
  (can-parse? "BOTH SAEM VAR1 AN VAR2
    O RLY?
YA RLY
NO WAI
OIC" :Conditional) => true
)


