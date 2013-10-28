;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(ns lolcmis.translator-test
  (:use midje.sweet
        lolcmis.translator
        lolcmis.test-helper)
  (:require [instaparse.core :as insta]))

(println "---- STRING LITERAL TRANSLATOR TESTS ----")

(facts
  (translate "
              HAI 1.0
              KTHXBYE
             ")
  =>
             nil  ;####TODO!!!!

  (translate "
              HAI 1.1
              CAN HAZ STDIO?
              VISIBLE \"HAI WORLD!\"
              KTHXBYE
             ")
  =>
             nil  ;####TODO!!!!

  (translate "
              HAI 1.2
              CAN HAZ STDIO?
              CAN HAZ CIMS?
              I HAS A VAR ITZ 1
              VISIBLE \"VAR IZ \"
              VISIBLE VAR
              KTHXBYE
             ")
  =>
             nil  ;####TODO!!!!

  (translate "
              HAI
              CAN HAZ STDIO?
              CAN HAZ CIMS?
              I HAS A VAR ITZ 1
              VISIBLE \"VAR IZ \"
              VISIBLE VAR
              BOTH SAEM VAR AN 1
              O RLY?
                YA RLY
                  VISIBLE \"VAR IZ 1\"
                MEBBE BOTH SAEM VAR AN 2
                  VISIBLE \"VAR IZ 2\"
                MEBBE BOTH SAEM VAR AN 3
                NO WAI
                  VISIBLE \"VAR IZ NOT 1 OR 2 OR 3!!!1\"
              OIC              
              KTHXBYE
             ")
  =>
             nil  ;####TODO!!!!
)
