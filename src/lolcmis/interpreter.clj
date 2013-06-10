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
            [lolcmis.runtime       :as rt]
            [lolcmis.translator    :as lt]))

; The interpreter itself
(defn interpret
  "Evaluates (interprets) a LOLCMIS program."
  [source]
  (eval (lt/translate source)))





