;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(defproject lolcmis "0.1.0-SNAPSHOT"
  :description "LOLCMIS interpreter"
  :url "https://github.com/pmonks/lolcmis"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0 Unported License."
            :url "http://creativecommons.org/licenses/by-sa/3.0/"}
  :min-lein-version "2.8.1"
  :dependencies [[org.clojure/clojure            "1.10.0"]
                 [org.clojure/tools.cli          "0.4.1"]
                 [org.clojure/tools.trace        "0.7.10"]
                 [org.clojure/tools.logging      "0.4.1"]
                 [instaparse                     "1.4.10"]
                 [ch.qos.logback/logback-classic "1.2.3"]]
  :profiles {:dev {:dependencies [[midje "1.9.4"]]
                   :plugins      [[lein-midje "3.2.1"]]}}
  :main lolcmis.core)
