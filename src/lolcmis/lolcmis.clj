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
  (:require [instaparse.core :as insta]
            [clojure.tools.logging :as log]))

; LOLCMIS grammar, partially based on the LOLCODE grammars at http://forum.lolcode.com/viewtopic.php?id=318
(def ^:private lolcmis-grammar "
  (* Program structure *)
  Program               = <SkipLine*> Header StatementList <'KTHXBYE'> <SkipLine*>
  Header                = <'HAI'> <NewLine> |
                          <'HAI'> <Whitespace> FloatLiteral <NewLine>
  StatementList         = Statement*

  (* Statements *)
  Statement             = <SkipLine> |
                          ImportStatement |
                          OutputStatement |
                          InputStatement |
                          VariableDeclaration |
                          Assignment
  SkipLine              = Comment |
                          NewLine
  Comment               = 'BTW' Whitespace AnyText? NewLine
  ImportStatement       = <'CAN HAZ'> <Whitespace> Identifier <'?'> <NewLine>
  OutputStatement       = <'VISIBLE'> <Whitespace> (Identifier | Literal) <NewLine>
  InputStatement        = <'GIMMEH'> <Whitespace> Identifier <NewLine>
  VariableDeclaration   = <'I HAS A'> <Whitespace> Identifier (<Whitespace> (<'ITZ'> <Whitespace> Expression | <'ITZ A'> <Whitespace> Type))? <NewLine>
  Assignment            = Identifier <Whitespace> <'R'> <Whitespace> Expression

  (* Non-statements *)
  Expression            = Identifier |
                          Literal |
                          CastExpression
  CastExpression        = Identifier <Whitespace> <'IS NOW A'> <Whitespace> Type
  Literal               = StringLiteral |
                          IntegerLiteral |
                          FloatLiteral |
                          BooleanLiteral |
                          VoidLiteral
  StringLiteral         = <DoubleQuote> (EscapedDoubleQuote | StringCharacter)* <DoubleQuote>
  BooleanLiteral        = TrueLiteral | FalseLiteral

  (* Almost-terminals *)
  NewLine               = OptionalWhitespace ('\\n' | '\\r' | '\\r\\n' | '\\u0085' | '\\u2028' | '\\u2029' | '\\u000B')
  Identifier            = !ReservedWord #'[_\\p{Alpha}]\\w*'

  (* Terminals *)
  AnyText               = #'.*'
  Whitespace            = #'[ \\t]+'
  OptionalWhitespace    = #'[ \\t]*'
  IntegerLiteral        = #'\\d+'
  FloatLiteral          = #'\\d+\\.\\d+'
  TrueLiteral           = 'WIN'
  FalseLiteral          = 'LOSE'
  VoidLiteral           = 'NOOB'
  Type                  = 'YARN' | 'NUMBR' | 'NUMBAR' | 'TROOF' | 'NOOB'
  DoubleQuote           = '\"'
  EscapedDoubleQuote    = '\\\"'
  StringCharacter       = #'[^\"^\n^\r]'

  (* Reserved words *)
  ReservedWord          = BooleanLiteral | VoidLiteral | Type
  ")


; LOLCMIS parser
(def ^:private lolcmis-parser (insta/parser lolcmis-grammar))

(defn parse-lolcmis
  "Parses a LOLCMIS program (or fragment, if a rule is provided).  May return multiple parse trees."
  ([source]      (parse-lolcmis source :Program))
  ([source rule] (insta/parses lolcmis-parser source :start rule)))

(defn number-of-asts
  "Returns the total number of parse trees for the given program (or fragment, if a rule is provided)."
  ([source]      (number-of-asts source :Program))
  ([source rule] (count (insta/parses lolcmis-parser source :start rule))))


; LOLCMIS interpreter
(defn- get-var
  "Gets the value of a LOLCMIS variable."
  [var-name]
  (let [variable (find-var (symbol "lolprogram" var-name))]
    (if (nil? variable)
      (throw (Exception. (str "AINT GOT NO " var-name)))
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
                "NUMBR"  (set-var var-name (Long/parseLong       value))
                "NUMBAR" (set-var var-name (Double/parseDouble   value))   ; ####TODO: consider using BigDecimal...
                "TROOF"  (set-var var-name (Boolean/parseBoolean value))
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
;    :VariableDeclaration print-ast
    :Expression          identity
  })

(defn eval-lolcmis
  "Evaluates (interprets) a LOLCMIS program (or fragment, if a rule is provided)."
  ([source]      (eval-lolcmis source :Program))
  ([source rule] (do (insta/transform interpreter-function-map (lolcmis-parser source :start rule)) nil)))












; Reference grammar copied and modified from http://forum.lolcode.com/viewtopic.php?id=318

(comment
; Grammar definition and parser function
(def ^:private lolcmis-grammar "
  Program               = Header BodyStatementList 'KTHXBAI'
  Header                = 'HAI' T_STMT_END |
                          'HAI' T_IDENTIFIER T_STMT_END
  BodyStatementList     = BodyStatement |
                          BodyStatement BodyStatementList
  BodyStatement         = Statement (* |
                          FunctionDefinition T_STMT_END *)
  Statement             = LoopDefinition T_STMT_END |
                          ConditionalStatement T_STMT_END |
                          SwitchStatement T_STMT_END |
                          Declaration T_STMT_END |
                          Assignment T_STMT_END |
                          Expression T_STMT_END |
                          InputStatement T_STMT_END
(*
  FunctionDefinition    = 'HOW DUZ I' T_IDENTIFIER FunctionArgs T_STMT_END FunctionStatementList 'IF U SAY SO'
  FunctionArgs          = 'YR' T_IDENTIFIER |
                          FunctionArgs 'AN' 'YR' T_IDENTIFIER
  FunctionStatementList = FunctionStatement |
                          FunctionStatement FunctionStatementList
  FunctionStatement     = Statement |
                          'GTFO' T_STMT_END |
                          'FOUND YR' Expression T_STMT_END
*)
  LoopDefinition        = 'IM IN YR' T_IDENTIFIER T_IDENTIFIER 'YR' T_IDENTIFIER LoopCondition T_STMT_END LoopStatementList 'IM OUTTA YR' T_IDENTIFIER
  LoopCondition         = 'TIL' Expression |
                          'WILE' Expression
  LoopStatementList     = LoopStatement |
                          LoopStatement LoopStatementList
  LoopStatement         = Statement |
                          'GTFO' T_STMT_END
  ConditionalStatement  = 'O RLY?' T_STMT_END 'YA RLY' StatementList ConditionalElseList 'OIC' T_STMT_END
  ConditionalElseList   = 'MEBBE' Expression T_STMT_END StatementList ConditionalElseList |
                          'NO WAI' StatementList
  SwitchStatement       = 'WTF?'' T_STMT_END CaseList
  CaseList              = 'OMG' Literal T_STMT_END StatementList CaseList |
                          'OMGWTF' T_STMT_END StatementList 'OIC' |
                          'OIC'
  Declaration           = 'I HAS A' T_IDENTIFIER |
                          'I HAS A' T_IDENTIFIER 'ITZ' Expression |
                          'I HAS A' T_IDENTIFIER 'ITZ' 'A' Type
  Assignment            = T_IDENTIFIER 'R' Expression
  InputStatement        = 'GIMMEH' T_IDENTIFIER T_STMT_END
  Expression            = (* FunctionCall | *)
                          CastExpression |
                          T_IDENTIFIER |
                          Literal
(*
  FunctionCall          = T_IDENTIFIER ArgumentsWithStart |
                          'BOTH SAEM' ArgumentsWithStart |
                          T_CAST T_IDENTIFIER TypeWithAs
*)
  TypeWithAs            = Type |
                          'A' Type
(*
  ArgumentsWithStart    = Arguments |
                          'OF' Arguments
  Arguments             = Expression |
                          Expression Expression |
                          Expression 'AN' Expression |
                          ExtendedArgs
  ExtendedArgs          = Expression 'MKAY' |
                          Expression ExtendedArgs |
                          Expression 'AN' ExtendedArgs
*)
  CastExpression        = T_IDENTIFIER 'IS NOW A' Type
  Type                  = 'YARN' |
                          'NUMBR' |
                          'NUMBAR' |
                          'TROOF' |
                          'NOOB'
  Literal               = T_STRING_LITERAL |
                          T_INT_LITERAL |
                          T_FLOAT_LITERAL |
                          'WIN' |
                          'LOSE' |
                          'NOOB'

  T_STMT_END            = '\\n' | '\\r' | '\\r\\n'
  T_IDENTIFIER          = #'\\S+'
  T_STRING_LITERAL      = #'\".*\"'
  T_INT_LITERAL         = #'\\d+'
  T_FLOAT_LITERAL       = #'\\d+\\.\\d+'
  ")
)
