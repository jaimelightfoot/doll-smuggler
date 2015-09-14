(ns doll-smuggler.core-test
  (:require [clojure.test :refer :all]
            [doll-smuggler.core :refer :all]))
	
(def atomic-object-dolls 
  [{:name "luke"    :weight 9   :value 150}
   {:name "anthony" :weight 13  :value 35} 
   {:name "candice" :weight 153 :value 200}
   {:name "dorothy" :weight 50  :value 160}
   {:name "puppy"   :weight 15  :value 60}
   {:name "thomas"  :weight 68  :value 45}
   {:name "randal"  :weight 27  :value 60} 
   {:name "april"   :weight 39  :value 40}
   {:name "nancy"   :weight 23  :value 30}
   {:name "bonnie"  :weight 52  :value 10} 
   {:name "marc"    :weight 11  :value 70}
   {:name "kate"    :weight 32  :value 30}
   {:name "tbone"   :weight 24  :value 15}
   {:name "tranny"  :weight 48  :value 10}
   {:name "uma"     :weight 73  :value 40}
   {:name "grumpkin" :weight 42  :value 70}
   {:name "dusty"    :weight 43  :value 75}
   {:name "grumpy"   :weight 22  :value 80}
   {:name "eddie"    :weight 7   :value 20}
   {:name "tory"     :weight 18  :value 12}
   {:name "sally"    :weight 4   :value 50}
   {:name "babe"     :weight 30  :value 10}])
   
(deftest atomic-doll-set 
  "Tests input given in problem statement on GitHub page:  https://github.com/micahalles/doll-smuggler."
  (testing 
    "Given the set of data from Micah Alles' github page (the Atomic Object challenge
	description), check to see that the expected output is reached.  "
    (let [[value indexes] (m (-> atomic-object-dolls count dec) 400 atomic-object-dolls)]
      (is (= (set indexes) #{0 1 2 3 4 6 10 15 16 17 18 20}))
	  (is (= value 1030))
      (is (= (reduce + (map (comp :weight atomic-object-dolls) indexes)) 396))
      )
    )
)

(def one-huge-doll-set
  [{:name "Monolith" :weight 12000 :value 500000}])
  
(def doll-set-one-overweight 
  [{:name "Monolith" :weight 12000 :value 500000}
   {:name "Tiny" :weight 12 :value 10}
   {:name "Micro" :weight 2 :value 5000}])

(deftest one-huge-doll
	"Given a set of one doll with a weight over the carrying capacity, 
	should not pack that doll.  
	Given another set with two acceptable dolls and a too-large doll,
	should pack only the two acceptable dolls"
  (testing "Should reject too-large item in doll set and return empty set"
    (let [[value indexes] (m (-> one-huge-doll-set count dec) 50 one-huge-doll-set)]
      (is (= (set indexes) #{}))
	  (is (= value 0))
      (is (= (reduce + (map (comp :weight one-huge-doll-set) indexes)) 0)))
  ) 
  (testing "Should reject too-large item and pack other two"
    (let [[value indexes] (m (-> doll-set-one-overweight count dec) 50 doll-set-one-overweight)]
      (is (= (set indexes) #{1 2}))
	  (is (= value 5010))
      (is (= (reduce + (map (comp :weight doll-set-one-overweight) indexes)) 14)))
  ) 
)

(def all-dolls-should-fit-set 
  [{:name "Larry" :weight 5  :value 100}
   {:name "Mo"    :weight 5  :value 100}
   {:name "Curly" :weight 5  :value 100}])

(deftest all-dolls-should-fit
  "Given a set of dolls whose total weight is below the given 
   carrying capacity, should return all dolls to be packed"
  (testing "Set of dolls below weight limit should all fit into purse"
    (let [[value indexes] (m (-> all-dolls-should-fit-set count dec) 400 all-dolls-should-fit-set)]
      (is (= (set indexes) #{0 1 2}))
	  (is (= value 300))
      (is (= (reduce + (map (comp :weight all-dolls-should-fit-set) indexes)) 15)))
    )
)

;;================================================================
;; For future improvements 
(deftest bad-csv-input
  "Given a csv file with wrong type information in various columns, 
   or a ragged input (nils in certain cells), test error handling"
   (testing 
     "Header row (used for keys) not present"
   )
   (testing 
     "Non-string doll name"
   )
   (testing 
     "Non-integer doll weight"
   )
   (testing 
     "Non-integer doll value"
   )
)

(deftest csv-file-not-found
  "Given a bad file path or a file path to directory without test.csv, 
  test error handling"
   (testing 
     "Bad file path"
   )
   (testing 
     "File path to dir without test.csv"
   )
)

(deftest bad-carrying-capacity
  "The user can input whatever carrying capacity they want.  
  Of course, this is inviting trouble.  Let's check to see if they 
  entered in something that makes sense (right type, positive number). "
  (testing 
    "Non-integer carrying capacity"
  )
  (testing 
    "Negative carrying capacity"
  )
)

(def null-doll-set
  [{}{}])
(deftest null-doll-set
   "Tests algorithm response given a null set of data"
  (testing 
    "Given empty set of data, should respond with 0 dolls, value and weight. "
    ;;(let [[value indexes] (m (-> null-doll-set count dec) 400 null-doll-set)]
    ;;  (is (= (set indexes) #{}))
	;;  (is (= value 0))
    ;;  (is (= (reduce + (map (comp :weight null-doll-set) indexes)) 0))
      )
    )
)
