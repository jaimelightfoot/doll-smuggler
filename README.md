# doll-smuggler

Doll-smuggler is a program written in Clojure.  It solves the problem described below:  

You are a drug trafficker. Every day you meet with a different nice older lady (the mule) and find out how much weight she can carry in her handbag. You then meet with your supplier who has packed various drugs into a myriad of collectible porcelain dolls. Once packed with drugs, each of the precious dolls has a unique combination of weight and street value. Sometimes your supplier has more dolls than the nice lady can carry, though space in her handbag is never an issue. Your job is to choose which dolls the kind soul will carry, maximizing street value, while not going over her weight restriction.

**Write a function in the [Clojure](http://clojure.org/) programming language** which given:

* a set of dolls with a name and unique weight and value combination
* an overall weight restriction

Produces the optimal set of drug-packed porcelain dolls which:

* are within the total weight restriction
* maximize the total street value of drugs delivered

Requirements:

* use leiningen - https://github.com/technomancy/leiningen
* include multiple high-level test cases to validate your solution (like the one included below)
* provide instructions in a README for running your test suite from the command line

This is a variation of the Knapsack problem (http://en.wikipedia.org/wiki/Knapsack_problem), which is NP-complete and is often solved with a dynamic programming approach.  

# Installation

To use the program, you must have leiningen.  Go to http://leiningen.org/ to install it.  

The source code for this project can be found at https://github.com/jaimelightfoot/doll-smuggler/

Once you have a local copy of the files on your computer, open the command line and navigate to the project director (i.e. ../../../doll-smuggler)

This program makes use of two additional clojure libraries, parse-csv and semantics-csv.  Run 'lein deps' from the command line.  

# Usage

## Input Data (CSV)
This program takes a CSV file containing the names of available dolls (along with their respective weights and values), parses them, and displays them on a console.  

First, let's cover what's expected of the CSV file.  The file should contain all of your dolls' information, with the first row being a header file.  The first row must contain "name,weight,value".  The following rows must contain the name (as a String), weight (as an Integer) and value (as an Integer).  You may include comments in your document denoted by #, e.g. "#Available dolls listed below", as long as they are on their own row.    

Here is an example of what the CSV file should look like.  

	name,weight,value
	Luke,9,150
	Anthony,13,35
	Candice,153,200
	#and so on...

I would like to include validation for the CSV file (detecting wrong types, allowing floats, etc.) but for the time being (or rather, with the time allowed), an Excel file has been provided.  Fill this out, and then go File, Save As and select "CSV" as the type.  

The CSV file MUST be saved in the resources/ directory as 'test.csv'!

### Why CSV?
 
CSV is my chosen input method, rather than a set of random data, or tediously typing in the input line-by-line, because it works well with financial records, and a lot of financial (and logistical) documentation is often stored in CSV format, or easily converted to CSV format.  CSV provides relatively common and convenient way toinput data.  Our hypothetical dealer might not be using Quickbooks, but Excel/OL Calc/etc. are fairly common and this adds more compatibility (and value to the end user).  

## Running the program

In a command prompt, navigate to the doll-smuggler directory.  Type "lein run" to begin the program.  

You will be prompted for the maximum carrying capacity of your granny.  Following that, the program will return the dolls you should pack (from the set of dolls provided in the CSV file), their total street value, their total weight, and some analytics to consider in your next deal.  

Example output:  

	------------------------------------------------
	Welcome to kiloGrammaMule
	------------------------------------------------
	Please place your CSV file of available dolls in doll-smuggler/resources once yo
	u receive information from your dealer.

	List of dolls begins with these names:
	Luke, Anthony, Candice, Dorothy, Puppy

	Please enter the granny's carrying capacity (in kilograms):
	400
	------------------------------------------------
	RESULTS
	------------------------------------------------
	Pack these dolls:
			Luke
			Anthony
			Candice
			Dorothy
			Puppy
			Randal
			Marc
			Grumpkin
			Dusty
			Grumpy
			Eddie
			Sally
	Total street value:     $1030
	Total weight:           396 kilograms (out of 400)
	------------------------------------------------
	ANALYTICS
	------------------------------------------------
	You've still got 4 kilograms of weight capacity left!
	Ask your dealer for another doll like:  Sally (w: 4), to optimize your old lady'
	s carrying capacity.
	------------------------------------------------

# Other

## References and About
Being new to Clojure (and Lisp-like languages and functional programming), I first found a mathematical solution (cse.unl.edu link) to fully understand the algorithm (or rather, one possible algorithm).  I tried to mimic this approach with strictly my own code, and had quite a bit of difficulty, largely in part to my newness to Clojure.  

I decided to study the Rosetta Code solution to the problem.  I didn't fully understand it at first (the "no" and "yes" was especially confusing), but determined to write my own algorithm, tried developing my own version.  Through repeated tries, my attempts at my own algorithm improved, and I understood the original algorithm better.  I reached a point where (from increased knowledge or increased familiarity with RC's answer), my algorithm and the RC algorithm looked more and more similar.  While the code's similarity to the Rosetta Code original makes it look like little work was done, the multiple iterations of (trying to) write my own algorithm taught me a lot more than is evident in my solution.  The pages of notes that I took to understand each line of RC's algorithm exceed the length of their algorithm by a couple hundred lines.  I intend this section of the README to be an acknowledgement of that 'unseen' effort.  

The additional comments in the algorithm function are to show understanding of the code (that it wasn't just copy/paste with variable name changes).  The variable name changes were to make the code more readable in English, so you don't have to divine what "sn" means, for example ("set of dolls with the current doll not included").  

While this was definitely a challenging problem for me, I appreciated it and learned a lot (some of it completely new, some of it expanded knowledge):  Dynamic programming, a few random pieces of Java (since taken out of core.clj), different classifications of complexity (NP-complete, etc.), 
polynomial time, and a ton of Clojure things, of course:  syntax; symbols; structures; maps; vectors; atoms; a few detours into what happens behind the scenes with memoize, csv parsing, etc; a couple hundred misplaced parenthesis (in C it's foo(bar) not (foo bar)...);
keys; sorting; file parsing; testing; bindings; and many more.  

Acknowledged Reference Material:  
- Rosetta Code's Clojure solution to the knapsack problem:  http://rosettacode.org/wiki/Knapsack_problem/0-1#Clojure
- Helpful step-by-step explanation of the knapsack problem:  http://cse.unl.edu/~goddard/Courses/CSCE310J/Lectures/Lecture8-DynamicProgramming.pdf
- Stack Overflow
- Clojure-doc.org
- Wikipedia
- and several blog posts of Clojure programmers and enthuiasts.  

## Future Improvements
A list of things I'd like to implement, given more time and my budding familiarity with Clojure:  
- Output results into CSV file for dealer's records.  
- Validate CSV columns (names should be strings, weight and value should be integers)
- Support float values for weight and value
- In Analytics part of output, show value added by suggested doll.  
- Catch invalid answers for input of carrying capacity
- Add message to let user know that CSV read was successful or not
- Add timing to show how fast the calculation was
- Filter out too-heavy dolls from the start so time isn't wasted trying to calculate them as an input.  

## About

## License

Copyright © 2015 Jaime Lightfoot

Distributed under the Eclipse Public License either version 1.0 or any later version.
