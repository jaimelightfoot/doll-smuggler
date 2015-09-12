(ns doll-smuggler.core
  (:require [clojure.java.io :as io]
			[clojure-csv.core :refer [parse-csv]]))

(defn read-file
  "open and read the csv file
  Takes a CSV as a char sequence or string, and returns a lazy sequence of 
  vectors of strings; each vector corresponds to a row, and each string is 
  one field from that row. Be careful to ensure that if you read lazily from 
  a file or some other resource that it remains open when the sequence is consumed.
  http://stackoverflow.com/questions/23717331/clojure-lein-run-unable-to-resolve-symbol
  http://stackoverflow.com/questions/19645160/using-clojure-csv-core-to-parse-a-huge-csv-file"
  [fname]
  (with-open [file (io/reader fname)]
    (parse-csv (slurp fname))))
	
	;;close file?
			
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println (clojure.string/join "\n" (read-file "test.csv"))))