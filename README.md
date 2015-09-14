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

Input:

    max weight: 400

    available dolls:

    name    weight value
    luke        9   150
    anthony    13    35
    candice   153   200
    dorothy    50   160
    puppy      15    60
    thomas     68    45
    randal     27    60
    april      39    40
    nancy      23    30
    bonnie     52    10
    marc       11    70
    kate       32    30
    tbone      24    15
    tommy      48    10
    uma        73    40
    grumpkin   42    70
    dusty      43    75
    grumpy     22    80
    eddie       7    20
    tory       18    12
    sally       4    50
    babe       30    10

Result:

    packed dolls:

    name    weight value
    sally       4    50
    eddie       7    20
    grumpy     22    80
    dusty      43    75
    grumpkin   42    70
    marc       11    70
    randal     27    60
    puppy      15    60
    dorothy    50   160
    candice   153   200
    anthony    13    35
    luke        9   150

Hint:

* read this - http://en.wikipedia.org/wiki/Knapsack_problem



# doll-smuggler

Doll-smuggler is a program written in Clojure.  It solves the problem described below:  

You are a drug trafficker. Every day you meet with a different nice older lady (the mule) and find out how much weight she can carry in her handbag. You then meet with your supplier who has packed various drugs into a myriad of collectible porcelain dolls. Once packed with drugs, each of the precious dolls has a unique combination of weight and street value. Sometimes your supplier has more dolls than the nice lady can carry, though space in her handbag is never an issue. Your job is to choose which dolls the kind soul will carry, maximizing street value, while not going over her weight restriction.

This is a variation of the Knapsack problem (http://en.wikipedia.org/wiki/Knapsack_problem), which is NP-complete and is often solved with a dynamic programming approach.  

# Installation

To use the program, you must have leiningen.  Go to http://leiningen.org/ to install it.  

The source code for this project can be found at https://github.com/jaimelightfoot/doll-smuggler/

Once you have a local copy of the files on your computer, open the command line and navigate to the project director (i.e. ../../../doll-smuggler)

This program makes use of two additional clojure libraries, parse-csv and semantics-csv.  Run 'lein deps' from the command line.  

# Usage

## Input Data (CSV)
This program takes a CSV file, containing the available dolls (along with their respective weights and values), parses them, and displays them on a console.  

First, let's cover what's expected of the CSV file.  The file should contain all of your dolls, with the first row being a header file.  The first row must contain "name,weight,value".  The following rows must contain the name (as a String), weight (as an Integer) and value (as an Integer).  You may include comments in your document denoted by #, e.g. "#Available dolls listed below", as long as they are on their own row.    

Here is an example of what the CSV file should look like.  

	name,weight,value
	Luke,9,150
	Anthony,13,35
	Candice,153,200
	and so on...

I would like to include validation for the CSV file (detecting wrong types, allowing floats, etc.) but for the time being (or rather, with the time allowed), an Excel file has been provided.  Fill this out, and then go File, Save As and select "CSV" as the type.  

The CSV file MUST be saved in the resources/ directory!

### Why CSV?
 
CSV is the input method, rather than a set of random data, or tediously typing in the input line-by-line, because it works well with financial records, and a lot of financial (and logistical) documentation is often stored in CSV format, or easily converted to CSV format.  CSV provides relatively common and convenient way toinput data.  Our hypothetical dealer might not be using Quickbooks, but Excel/OL Calc/etc. are fairly common and this adds more compatibility (and value to the end user).  

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

# References
	
# Future Improvements

# About

## License

Copyright © 2015 Jaime Lightfoot

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
