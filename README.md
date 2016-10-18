# CS547-Assignment02

## Synopsis
This Java program provides performs test case prioritisation with a given set
of configurations and a test suite using both hill climbing and genetic
algorithms, allowing for comparison and benchmarking.

The program allows for the tweaking of parameters within these algorithms as
part of the benchmarking.

## Author
Aidan O'Grady (201218150) - wlb12153@uni.strath.ac.uk

## Motivation
This project is a submission for Assignment 02 for class CS547 Advanced Topics
in Computer Science for the MEng Computer Science course in the University of
Strathclyde, Glasgow.

## How to Run
```java -jar CS547-Assignment02-AidanOGrady.jar <properties> <dataset>```

### Properties
 * size: the size of the TestCase list in a chromosome
 * random.limit: How many iterations random search should perform.
 * stall: How long Hill Climb and GA stall on unimproved results until halting.
 * benchmark: The number of times to run each search/
 * hillclimb.steps: The number of steps the algorithm takes per climb.
 * ga.population: The size of population in genetic algorithm.
 * ga.elitism: (Double from 0 to 1) The subset of top chromosomes to accept as
parents.
 * ga.crossover: (Double from 0 to 1) The probability of parents performing
 crossover.
 * ga.mutation: (Double from 0 to 1) The probability of mutation
 * ga.tournament: Number of chromosomes in tournament selection.


## Output
The console will display a summary of the results in each search. The ```log```
directory will contain detailed results of each search.
