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
              HAI
              KTHXBYE
             ")
  =>
             '(do (require '[lolcmis.runtime :as rt]) (rt/initialise))
)
