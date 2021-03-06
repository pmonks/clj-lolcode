;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
; International License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/4.0/ or send a letter to Creative
; Commons, PO Box 1866, Mountain View, CA 94042, USA.
;

(ns lolcode.grammar)

; LOLCODE grammar
; LOLCODE 1.2 specification available at http://archive.is/HKS3
; Some pre-existing LOLCODE grammars are at:
; * http://forum.lolcode.com/viewtopic.php?id=318
; * https://github.com/jynnantonix/lolcode/blob/master/BNFGrammar.txt
(def lolcode-grammar "
  (* Program structure *)
  <Program>                = Skip* Header ImportStatementList StatementList <Footer> (Skip | <OptionalWhitespace>)*
  Header                   = <OptionalWhitespace 'HAI'> (<EndOfStatement> | <Whitespace> FloatLiteral <EndOfStatement>)
  ImportStatementList      = (Skip | (<OptionalWhitespace> ImportStatement <EndOfStatement>))*
  StatementList            = (Skip | Statement)*
  Footer                   = OptionalWhitespace 'KTHXBYE' EndOfStatement?

  (* Statements *)
  <Skip>                   = <BlankLine> | (<OptionalWhitespace> Comment <EndOfStatement?>)
  <Statement>              = <OptionalWhitespace>
                             (OutputStatement |
                              InputStatement |
                              VariableDeclaration |
                              Assignment |
                              Conditional |
                              CastExpression)
                             <EndOfStatement>
  Comment                  = SingleLineComment | MultiLineComment
  <SingleLineComment>      = <'BTW'> (<Whitespace> NotNewLine*)?
  <MultiLineComment>       = <'OBTW' (Whitespace | NewLine)> (!'TLDR' (NotNewLine | NewLine)*)? <'TLDR'>
(*  <MultiLineComment>       = <'OBTW'> (Whitespace | NewLine) ((Whitespace | NewLine)* (NotNewLine | NewLine))? <'TLDR'> *)
  ImportStatement          = <'CAN HAZ'> <Whitespace> Identifier <'?'>
  OutputStatement          = <'VISIBLE'> <Whitespace> Expression
  InputStatement           = <'GIMMEH'> <Whitespace> Identifier
  VariableDeclaration      = <'I HAS A'> <Whitespace> Identifier (<Whitespace> (<'ITZ' Whitespace> Expression | <'ITZ A' Whitespace> Type))?
  Assignment               = Identifier <Whitespace 'R' Whitespace> Expression
  Conditional              = IfClause <Skip*> ElseIfClause* <Skip*> ElseClause? <Skip* OptionalWhitespace 'OIC'>
  IfClause                 = Expression <EndOfStatement Skip* OptionalWhitespace 'O RLY?' EndOfStatement Skip* OptionalWhitespace 'YA RLY' EndOfStatement> StatementList
  ElseIfClause             = <OptionalWhitespace 'MEBBE' (Whitespace | NewLine)> BooleanExpression <EndOfStatement> StatementList
  ElseClause               = <OptionalWhitespace 'NO WAI' EndOfStatement> StatementList

  (* Expressions *)
  <Expression>             = Identifier |
                             Literal |
                             CastExpression |
                             BooleanExpression |
                             MathematicalExpression
  CastExpression           = Identifier <Whitespace 'IS NOW A' Whitespace> Type
  <Literal>                = StringLiteral |
                             IntegerLiteral |
                             FloatLiteral |
                             BooleanLiteral |
                             VoidLiteral
  StringLiteral            = <DoubleQuote> (EscapedDoubleQuote | StringCharacter)* <DoubleQuote>
  <BooleanLiteral>         = TrueLiteral | FalseLiteral
  <BooleanExpression>      = EqualsExpression |
                             NotEqualsExpression |
                             AndExpression |
                             OrExpression
  EqualsExpression         = <'BOTH SAEM' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  NotEqualsExpression      = <'DIFFRINT' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  AndExpression            = <'BOTH OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  OrExpression             = <'EITHER OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  MathematicalExpression   = AdditionExpression |
                             SubtractionExpression |
                             MultiplicationExpression |
                             DivisionExpression |
                             ModulusExpression |
                             MaxExpression |
                             MinExpression
  AdditionExpression       = <'SUM OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  SubtractionExpression    = <'DIFF OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  MultiplicationExpression = <'PRODUKT OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  DivisionExpression       = <'QUOSHUNT OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  ModulusExpression        = <'MOD OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  MaxExpression            = <'BIGGR OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression
  MinExpression            = <'SMALLR OF' Whitespace> Expression <Whitespace 'AN' Whitespace> Expression

  (* Almost-terminals *)
  EndOfStatement           = OptionalWhitespace (NewLine | ',')
  BlankLine                = OptionalWhitespace (NewLine | ',')
  Identifier               = !ReservedWord #'[\\p{Alpha}]\\w*'

  (* Terminals *)
  NewLine                  = '\\n' | '\\r' | '\\r\\n' | '\\u0085' | '\\u2028' | '\\u2029' | '\\u000B'
  NotNewLine               = #'.'
  WhitespaceChar           = ' ' | '\\t'
  Whitespace               = WhitespaceChar+
  OptionalWhitespace       = WhitespaceChar*
  IntegerLiteral           = #'-?\\d+'
  FloatLiteral             = #'-?\\d+\\.\\d+'
  TrueLiteral              = 'WIN'
  FalseLiteral             = 'FAIL'
  VoidLiteral              = 'NOOB'
  Type                     = 'YARN' | 'NUMBR' | 'NUMBAR' | 'TROOF' | 'NOOB' (* | BUKKIT *)
  DoubleQuote              = '\"'
  EscapedDoubleQuote       = '\\\\\"'   (* Don't ask... o_O *)
  StringCharacter          = #'[^\"]'

  (* Reserved words *)
  ReservedWord             = BooleanLiteral | VoidLiteral | Type
  ")
