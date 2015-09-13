(ns doll-smuggler.core
  (:require [clojure.java.io :as io]
		   [clojure-csv.core :as csv]
		   [semantic-csv.core :as sc :refer :all]
   :use	[clojure.pprint]))
	
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
    (doall (parse-csv (slurp fname)))))


(declare mm) ;forward decl for memoization function
 
 ;; define a function "m" with inputs i and w.  index?  and???
(defn m [i w available-dolls]
  ;; COND--Takes a set of test/expr pairs. It evaluates each test one at a time.  If a test returns logical true, cond evaluates and returns
		;;the value of the corresponding expr and doesn't evaluate any of the other tests or exprs. (cond) returns nil.
  (cond
    (< i 0) [0 []]		;;if (i < 0) then return ??
    (= w 0) [0 []]		;;else if (w = 0) then return ??
    :else				;;else
	;; LET--(let [bindings*] exprs*)
	;; binding => binding-form init-expr
	;;	Evaluates the exprs in a lexical context in which the symbols in the binding-forms are bound to their respective init-exprs or parts therein.
	(let [{wi :weight vi :value} (get available-dolls i)]	 ;;soo... value in 'items' at 'i' --> weight and value?
	;;(println wi "+" vi)
											;; key binding map:  new var "wi" with weight column, new var "vi" with value column
											;; GET--Returns the value mapped to key, not-found or nil if key not present.    (get {:a 1 :b 2} :b)   ;;=> 2
      (if (> wi w)		;; if weight_current_item is greater than total weight?
        (mm (dec i) w available-dolls)	;; DEC--Returns a number one less than num.  decrement i, return mm(i-1, w)
        (let [[vn sn :as no]  (mm (dec i) w available-dolls)	;;what?  
              [vy sy :as yes] (mm (dec i) (- w wi) available-dolls)]
          (if (> (+ vy vi) vn)  ;;if new value is less than 
            [(+ vy vi) (conj sy i)] ;; conjoin i onto the end of sy:  [] conj with 0 = 0, vector of [3 0]
            no)
		)
	  )
	)
  )
)
 
;;MEMOIZE--Returns a memoized version of a referentially transparent function. The memoized version of the function keeps a cache of the mapping 
;;from arguments to results and, when calls with the same arguments are repeated often, has higher performance at the expense of higher memory use.
(def mm (memoize m))

;;TODO simplify
(defn total-packed-weight
	[packed-doll-set indexes-of-packed-dolls]
	(reduce + (map (comp :weight packed-doll-set) indexes-of-packed-dolls))
)

(defn find-optimization 
  [weight-difference available-dolls]
 
  (def addition "none")
 
  (print "Ask your dealer for another doll like:  ")
  (doall
  (for [ i (range 0 (-> available-dolls count))]
    (let [{wi :weight vi :value ni :name} (get available-dolls i)]
	  (if (<= wi weight-difference)
	    (do 
		  ;;(into [] (concat [1 2] [3 4] [5 6])
		  (print ni "(w:"wi"), ")
		;; TODO  (conj extra-value vi)  display increased value
		)
	  )
	)
  ))
  (print "to optimize your old lady's carrying capacity\n")
  ;;TODO(println extra-value);;(apply min extra-value))
)

(defn check-underpack
	[weight-limit packed-weight available-dolls]
	
	(if (> (- weight-limit packed-weight) 0)
	  (do 
	    (println "You've still got"(- weight-limit packed-weight) "kilograms of weight capacity left!")
		;;(println "Can I offer you some advice?")
		(find-optimization (- weight-limit packed-weight) available-dolls)
	  )
	  (println "100% capacity... nice job, free trader"))
  )
  
(defn get-doll-csv
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
	   
  (def available-dolls (into [] (with-open [in-file (io/reader "resources/test.csv")]
     (->>
       (csv/parse-csv in-file)
       remove-comments
       mappify 
       (cast-with #(Integer/parseInt %) {:only [:weight :value]})
       doall))))
	   
	;;(pprint available-dolls)
	
  ;;TODO, catch invalid answers
  (println "weight limit?")
  (def weight-limit 
    (try
      (read-string (read-line))
      (catch Exception e nil)))
 
  (println weight-limit)

  (let [[value indexes] 
		(m (-> available-dolls count dec) weight-limit available-dolls)	;; value
        names(map (comp :name available-dolls) indexes)]
	(do 
	   (println "------------------------------------------------")
	   (println "RESULTS")
	   (println "------------------------------------------------")
	   (println "items to pack:" (clojure.string/join ", " names))
	   (println "total value: $" value)
	   (println "total weight: " (total-packed-weight available-dolls indexes) "kilograms")
	   (println "------------------------------------------------")
	   (println "SUGGESTIONS")
	   (println "------------------------------------------------")
	   (check-underpack weight-limit (total-packed-weight available-dolls indexes) available-dolls)
    )
  )
)