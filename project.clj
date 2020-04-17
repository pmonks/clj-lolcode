;
; Copyright © 2013 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
; International License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/4.0/ or send a letter to Creative
; Commons, PO Box 1866, Mountain View, CA 94042, USA.
;

(defproject lolcmis "0.1.0-SNAPSHOT"
  :description "LOLCODE parser and interpreter in Clojure"
  :url "https://github.com/pmonks/clj-lolcode"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0 Unported License."
            :url "https://creativecommons.org/licenses/by-sa/4.0/"}
  :min-lein-version "2.9.3"
  :dependencies [[org.clojure/clojure            "1.10.1"]
                 [org.clojure/tools.cli          "1.0.194"]
                 [org.clojure/tools.trace        "0.7.10"]
                 [org.clojure/tools.logging      "1.0.0"]
                 [instaparse                     "1.4.10"]
                 [ch.qos.logback/logback-classic "1.2.3"]]
  :profiles {:dev {:dependencies [[midje "1.9.9"]]
                   :plugins      [[lein-midje "3.2.1"]]}}
  :main lolcmis.core)
