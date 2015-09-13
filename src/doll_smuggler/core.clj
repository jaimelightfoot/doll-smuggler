(ns doll-smuggler.core
  (:require [clojure.java.io :as io]
		   [clojure-csv.core :as csv]
		   [semantic-csv.core :as sc :refer :all]
   :use	[clojure.pprint]))
	
(defn read-file
  "Open and read CSV file placed in ./resources dir of clojure project.  
  Please see documentation for formatting of CSV file.  
  
  Uses parse-csv from clojure-csv.core"
  [fname]
  (with-open [file (io/reader fname)]
    (doall (parse-csv (slurp fname)))))
	
	;;TODO add timing
	
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(declare mm) ;forward decl for memoization function
 
 ;; define a function "m" with inputs i and w.  index?  and???
(defn m [index w available-dolls]
  (cond
    (or (< index 0) (= w 0)) [0 []]		;;if (i < 0) then return ??
    
	:else
	(let [{wi :weight vi :value} (get available-dolls index)]
	  ;; if valid weight, let's consider the possibilities
      (if (< wi w)
	  
        (let [[val-notpack dollset-notpack]  (mm (dec index) w available-dolls)	  
             [val-yespack dollset-yespack]   (mm (dec index) (- w wi) available-dolls)]
		  ;; if the current doll's value + our OK'd dollset so far is greater than not packing it, obviously, pack it
          (if (> (+ val-yespack vi) val-notpack)
		    ;; if true, return [value dollset] with new doll value and index added
            [(+ val-yespack vi) (conj dollset-yespack index)] 
			;; else, forget it.  Return an older [value dollset] without this current doll considered
            (mm (dec index) w available-dolls);;[val-notpack dollset-notpack]
		  )
		)
		;; else, let's not even bother.  The result of this mm(i, w) call is actually the result of mm(i-1, w)
		(mm (dec index) w available-dolls)	;;B[i,w] = B[i-1,w] // wi > w
	  )
	)
  )
)

(def mm (memoize m))

;;TODO simplify?  are we receiving the packed dolls already?
(defn total-packed-weight
	[packed-doll-set indexes-of-packed-dolls]
	;;(pprint packed-doll-set)
	(reduce + (map (comp :weight packed-doll-set) indexes-of-packed-dolls))
)

(defn find-optimization 
  "TODO add comments"
  [weight-difference available-dolls]
 
  (def addition "none")
 
  (print "Ask your dealer for another doll like:  ")
  (doall
    (for [ i (range 0 (-> available-dolls count))]
      (let [{wi :weight vi :value ni :name} (get available-dolls i)]
	    (if (<= wi weight-difference)
	      (do 
		    ;;(into [] (concat [1 2] [3 4] [5 6])
		    ;; TODO  (conj extra-value vi)  display increased value
		    (print (str ni " (w: "wi"), ")))))))
  (print " to optimize your old lady's carrying capacity.\n")
  ;;TODO(println extra-value);;(apply min extra-value))
)

(defn check-underpack
  "TODO add comments" 
	[weight-limit packed-weight available-dolls]
	
	(if (> (- weight-limit packed-weight) 0)
	  (do 
	    (println (str "You've still got "(- weight-limit packed-weight) " kilograms of weight capacity left!"))
		(find-optimization (- weight-limit packed-weight) available-dolls)
	  )
	  (println "100% capacity... nice job, free trader"))
  )
  
  
;;TODO get working
;;TODO add output csv?
(defn get-doll-csv
  "TODO add comments"
	[location]
	(into [] (with-open [in-file (io/reader "resources/test.csv")]
     (->>
       (csv/parse-csv in-file)
       remove-comments
       mappify 
       (cast-with #(Integer/parseInt %) {:only [:weight :value]})
       doall)
	  )
	)
)
  
(defn -main
  "I don't do a whole lot ... yet."
  []
  
  ;;TODO:  add successful read message
  (def available-dolls (into [] (with-open [in-file (io/reader "resources/test.csv")]
     (->>
       (csv/parse-csv in-file)
       remove-comments
       mappify 
       (cast-with #(Integer/parseInt %) {:only [:weight :value]})
       doall))))
  
  (println "\n\n------------------------------------------------")
  (println "------------------------------------------------")
  (println "Welcome to kiloGrammaMule")  
  (println "------------------------------------------------")
  (println "Please place your CSV file of available dolls in doll-smuggler/resources once you receive information from your dealer.")
    
  (def max-doll-print 5)
  (if (< (count available-dolls) max-doll-print)
    (def max-doll-print (count available-dolls)))
  
  (println "\nList of dolls begins with these names: ") 
  (let [names(map (comp :name available-dolls) (range 0 max-doll-print))]
	(println  (clojure.string/join ", " names)))
  
  (println "\nPlease enter the granny's carrying capacity (in kilograms):  ")
  ;; TODO catch invalid answers
  (def weight-limit 
    (try			;;add 'integer?'
      (read-string (read-line))
      (catch Exception e nil)))

	  (println (assert (every? (fn [x] (and (contains? x "weight") (contains? x "value"))) available-dolls))
;; TODO filter out obvious no-gos

  (let [[value indexes] (m (-> available-dolls count dec) weight-limit available-dolls) ;; value
         names(map (comp :name available-dolls) indexes)]
	(do 
	   (println "\n------------------------------------------------")
	   (println "RESULTS")
	   (println "------------------------------------------------")
	   (println (str "Pack these dolls:\n\t" (clojure.string/join "\n\t" names)))
	   (println (str "Total street value:\t$" value))
	   (println (str "Total weight:\t\t" (total-packed-weight available-dolls indexes) " kilograms (out of " weight-limit")"))
	   (println "------------------------------------------------")
	   (println "SUGGESTIONS")
	   (println "------------------------------------------------")
	   (check-underpack weight-limit (total-packed-weight available-dolls indexes) available-dolls)
	   (println "------------------------------------------------")
	)
  )
)