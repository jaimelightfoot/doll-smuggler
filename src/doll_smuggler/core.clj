(ns doll-smuggler.core
  (:require [clojure.java.io :as io]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (with-open [rdr (io/reader "test.txt")]
  (doseq [line (line-seq rdr)]
    (println line))))
 