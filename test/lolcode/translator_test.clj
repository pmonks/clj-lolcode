;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
; International License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/4.0/ or send a letter to Creative
; Commons, PO Box 1866, Mountain View, CA 94042, USA.
;

(ns lolcode.translator-test
  (:use midje.sweet
        lolcode.translator
        lolcode.test-helper)
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
