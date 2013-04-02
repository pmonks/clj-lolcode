;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(defproject lolcmis "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :description "LOLCMIS interpreter"
  :url "https://github.com/pmonks/lolcmis"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0 Unported License."
            :url "http://creativecommons.org/licenses/by-sa/3.0/"}
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.2"]
                 [com.lithinos/amotoen "0.3.0-SNAPSHOT"]
                ]
  :profiles {:dev {:dependencies [
                                  [midje "1.5.1"]
                                 ]}}
  :main lolcmis.core)
