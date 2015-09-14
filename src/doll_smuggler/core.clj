(ns doll-smuggler.core
  (:require [clojure.java.io :as io]
		   [clojure-csv.core :as csv]
		   [semantic-csv.core :as sc :refer :all]
   :use	[clojure.pprint]))
	
	
;;============================================================================================
;;	FILE INPUT
;;============================================================================================
(defn read-file
  "Open and read CSV file placed in ./resources dir of clojure project.  
  Please see documentation for formatting of CSV file.  
  
  Uses parse-csv from clojure-csv.core and is also used in get-doll-csv function"
  [fname]
  
  (with-open [file (io/reader fname)]
    (doall (parse-csv (slurp fname))))
)

;;TODO add output csv?
(defn get-doll-csv
  "Makes use of clojure-csv.core and semantic-csv.core to read in from
  a CSV file (following the formatting outlined in the documentation) and
  create a vector of maps.  First line of Excel file is used as keys, and 
  second and third (weight and value) columns are cast as Integers.
  
  CSV is the input method, rather than a set of random data, or tediously typing 
  in the input line-by-line, because it works well with financial records, and a 
  lot of financial (and logistical) documentation is often in CSV format, or easily
  converted to CSV format.  This provides an relatively common and convenient way to
  input data."
  [array location]

  (into [] (with-open [in-file (io/reader location)]
    (->>
      (csv/parse-csv in-file)
      remove-comments
      mappify 
      (cast-with #(Integer/parseInt %) {:only [:weight :value]})
      doall)))
)

;;============================================================================================
;;	DOLL-PACKING ALGORITH (reference: http://rosettacode.org/wiki/Knapsack_problem/0-1#Clojure)
;;============================================================================================

(defn doll-could-fit
  [possible-doll weight-limit]
  (< possible-doll weight-limit)
)

(declare mm)

(defn m [index weight-limit available-dolls]
  "Algorithm from Rosetta Code (http://rosettacode.org/wiki/Knapsack_problem/0-1#Clojure)).  
   Mathematical notation from http://cse.unl.edu/~goddard/Courses/CSCE310J/Lectures/Lecture8-DynamicProgramming.pdf
   See 'References and About' section of the README file for more information about the algorithm.  
   
   After checking for invalid conditions, the algorithm considers if the doll (at a given row/vector of the 
   vector of available dolls) can fit, and if so, what the consequences are on the weight and the value.  
   Given that information, it returns the optimal set of [value [array-of-selected-dolls]].  It is helped by 
   memoization, which prevents unnecessary repetition of previous work."
  (cond
    ;; Testing for invalid index or weight-limit.  If so, return a set with [(value = 0) [null dollset]]
    (or (< index 0) (= weight-limit 0)) [0 []]
    ;; If our inputs are good, continue to the algorithm
	:else
	;; Locally bind the values at each index of the available-dolls array for each 
	;; key (:weight and :value) and assign those values to doll-weight and doll-value integers.  
	(let [{doll-weight :weight doll-value :value} (get available-dolls index)]
	  ;; Check to see if it's even a possibility, given the current doll's weight
      (if (< doll-weight weight-limit)
	    ;; Locally bind a call to the m(index, weight) function to a set containing 
		;; [total-value dollset-resulting-in-that-value]
        (let [[val-ifnotpack dollset-ifnotpack]  (mm (dec index) weight-limit available-dolls)	  
             [val-ifpack dollset-ifpack]   (mm (dec index) (- weight-limit doll-weight) available-dolls)]
		  ;; If the current doll's value + dollset so far is greater than not packing it, obviously, pack it
		  ;; Mathematically, if bi + B[i-1,w-wi] > B[i-1,w]
          (if (> (+ val-ifpack doll-value) val-ifnotpack)
		    ;; if true, return [value dollset] with new doll value and index added
			;; Mathematically, m[i,w] = bi + m[i-1,w- wi]
            [(+ val-ifpack doll-value) (conj dollset-ifpack index)] 
			;; else, forget it.  Return an older [value dollset] without this current doll considered
			;; Mathematically, m[i,w] = m[i-1,w]
            (mm (dec index) weight-limit available-dolls)))  ;;m[i,w] = m[i-1,w]
		;; else, let's not even bother.  Mathematically, m[i,w] = B[i-1,w]
		(mm (dec index) weight-limit available-dolls))))
)

;; Helps improve performance of recursive calls to algorithm by saving previous results, 
;; rather than duplicating efforts.  
(def mm (memoize m))

(defn total-packed-weight
  "Simple function to determine the total weight of packed dolls.  Turned into 
  a wrapper function after repeated code reuse."
	[packed-doll-set indexes-of-packed-dolls]
	(reduce + (map (comp :weight packed-doll-set) indexes-of-packed-dolls))
)

;;============================================================================================
;;	ANALYTICS
;;============================================================================================

;;TODO:  display extra value added, and/or pick best doll
(defn find-similar-doll 
  "From the 'check-if-underpacked' function, if there is still some amount of carrying capacity
  left, we check the original list of dolls and find the best match (as data for a hypothetical
  scenario where the dealer has duplicate dolls, and/or we know how to better optimize next time."
  [weight-difference available-dolls]
 
  (print "Ask your dealer for another doll like:  ")
  (doall
    (for [ i (range 0 (-> available-dolls count))]
      (let [{doll-weight :weight doll-value :value ni :name} (get available-dolls i)]
	    (if (<= doll-weight weight-difference)
	      (do 
		    (print (str ni " (w: "doll-weight"), ")))))))
  (print "to optimize your old lady's carrying capacity.\n")
)

(defn check-if-underpacked
  "Checks to see if there is any room left after all the dolls have been packed, 
  and finds a doll that could fill that space.  This is of course completely hypothetical,
  but might be useful in a case where your dealer offers duplicates or could in the future,
  with knowledge that you have some carrying capacity left." 
  [weight-limit packed-weight available-dolls indexes]
 
  (if (not= (count indexes) (count available-dolls))
    (if (> (- weight-limit packed-weight) 0)
      (do 
	    (println (str "You've still got "(- weight-limit packed-weight) " kilograms of weight capacity left!"))
	    (find-similar-doll (- weight-limit packed-weight) available-dolls))
	  (println "Nice job!  100% utilization of granny's carrying capacity!"))
	(println "You packed all of your items and you still have "(- weight-limit packed-weight) 
	        " kilograms of weight capacity left.  Time to expand!  Buy buy buy!"))
)

;;============================================================================================
;;	COMMAND PROMPT OUTPUT FORMATTING
;;============================================================================================
(defn print-divider
  []
  (println "------------------------------------------------")
)

(defn make-header
  [string]
  (print-divider)
  (println string)
  (print-divider)
)
 
;;============================================================================================
;;	MAIN
;;============================================================================================ 
(defn -main
  "Main function of doll-smuggler program.  Pulls doll information from a CSV file, parses it using 
  a dynamic programming algorithm, reports the results, and offers improvements for the next order."
  []
  
  (make-header "Welcome to kiloGrammaMule") 
 
  (def location "resources/test.csv")
 
  (if (not (.exists (as-file location)))
	(println "Please place your CSV (test.csv) file of available dolls in doll-smuggler/resources once you receive information from your dealer, and restart the program.\n")
    (do 
	  (println "\nCSV file read successfully.\n")
	  (def available-dolls (get-doll-csv available-dolls location))
	  
	  (def max-doll-print 5)
	  (if (< (count available-dolls) max-doll-print)
		(def max-doll-print (count available-dolls)))
	  
	  (println "Your list of dolls begins with these names: \n(visual verification that correct file is being used): ") 
	  (let [names(map (comp :name available-dolls) (range 0 max-doll-print))]
		(print  (clojure.string/join ", " names)))
	  (println "...")
	  
	  (println "\nPlease enter the granny's carrying capacity (in kilograms):  ")
	  ;; TODO catch invalid answers
	  (def weight-limit 
		(try	
		  (read-string (read-line))
		  (catch Exception e (println "Caught exception:" e))))
		  
	  ;;TODO add timing
		  
	  ;; TODO filter out obviously too-heavy dolls
	  (let [[value indexes] (m (-> available-dolls count dec) weight-limit available-dolls) ;; value
			 names(map (comp :name available-dolls) indexes)]
		(do 
		   (make-header "RESULTS")
		   (println (str "Pack these dolls:\n\t" (clojure.string/join "\n\t" names)))
		   (println (str "Total street value:\t$" value))
		   (def packed-weight (total-packed-weight available-dolls indexes))
		   (println (str "Total weight:\t\t" packed-weight " kilograms (out of " weight-limit")"))

		   (make-header "ANALYTICS")
		   (check-if-underpacked weight-limit packed-weight available-dolls indexes)
		   (print-divider)))
	  )
	)
)
