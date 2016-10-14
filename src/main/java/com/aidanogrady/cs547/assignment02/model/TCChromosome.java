package com.aidanogrady.cs547.assignment02.model;

import java.util.*;

/**
 * The chromosome used for test case prioritisation. Each chromosome is a
 * permutation of tests in the test suite being examined, being a set of the
 * tests performed in the specific order.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public class TCChromosome implements Comparable<TCChromosome> {
    /**
     * Random generator to ensure the chaos required.
     */
    private static final Random RAND = new Random();

    /**
     * The test cases in the order of being executed. It is important to note
     * that uniqueness and order is required.
     */
    private List<TestCase> candidate;

    /**
     * The fitness of this candidate solution.
     */
    private double fitness;

    /**
     * Constructor.
     *
     * @param candidate the set of tests in this solution.
     */
    public TCChromosome(List<TestCase> candidate) {
        this.candidate = candidate;
        fitness = calculateFitness();
    }

    /**
     * Calculates the fitness of this solution.
     *
     * @return fitness
     */
    private double calculateFitness() {
        double fitness = 1.0;

        int faults = candidate.get(0).getNumOfFaults();

        double sum = 0;
        for (int i = 1; i <= faults; i++) {
            int fit = 1;
            for (TestCase tc : candidate) {
                if (tc.getFaults().contains(i)) {
                    break;
                }
                fit++;
            }
            sum += fit;
        }
        fitness -= sum / (faults * candidate.size());
        fitness += 1.0 / (2 * candidate.size());
        return 1.0 - fitness;
    }

    /**
     * Returns the candidate solution of this chromosome.
     *
     * @return candidate
     */
    public List<TestCase> getCandidate() {
        return candidate;
    }

    /**
     * Returns this candidate solution's fitness value
     *
     * @return fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Performs a crossover, combining this and the given parent. The crossover
     * is based on the given offset, determining where the split occers.
     *
     * @param parent the other parent to produce offspring
     * @param offset where the crossover is performed
     * @return chromosome produced by parents
     */
    public TCChromosome crossover(TCChromosome parent, int offset) {

        List<TestCase> newCandidate = new ArrayList<>(candidate.subList(0, offset));
        List<TestCase> parentCandidate = parent.getCandidate();

        int i = 0;
        while (newCandidate.size() < parentCandidate.size() && i < parentCandidate.size()) {
            TestCase test = parentCandidate.get(i);

            if (!newCandidate.contains(test))
                newCandidate.add(test);
            i++;
        }

        if (newCandidate.size() != candidate.size()) {
            System.out.println("Crossover isn't working!");
            return null;
        }
        return new TCChromosome(new ArrayList<>(newCandidate));
    }

    /**
     * Returns a new version of this solution that has been given a minor,
     * random alteration to its data.
     *
     * @return randomly altered chromosome
     */
    public TCChromosome mutate(List<TestCase> testCases) {
        List<TestCase> copy = new ArrayList<>(candidate);

        int i = RAND.nextInt(candidate.size() - 1);
        TestCase newTestCase = testCases.get(RAND.nextInt(testCases.size()));
        while (copy.contains(newTestCase))
            newTestCase = testCases.get(RAND.nextInt(testCases.size()));
        copy.set(i, newTestCase);

        return new TCChromosome(copy);
    }

    /**
     * Returns the neighbouring chromosomes.
     *
     * @return neighbours
     */
    public List<TCChromosome> getNeighbours(List<TestCase> testCases) {
        List<TCChromosome> neighbours = new ArrayList<>();
        for (int i = 0; i < candidate.size(); i++) {
            for (TestCase tc : testCases) {
                List<TestCase> copy = new ArrayList<>(candidate);
                if (!copy.contains(tc))
                    copy.set(i, tc);
                neighbours.add(new TCChromosome(copy));
            }
        }
        return neighbours;
    }

    /**
     * Generates a random chromosome based on the number of cases to include and
     * the test suite to take from.
     *
     * @param size the number of cases to include
     * @param cases the cases to select from
     * @return random chromosome
     */
    public static TCChromosome generateChromosome(int size, List<TestCase> cases) {
        List<TestCase> newCandidate = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TestCase testCase = cases.get(RAND.nextInt(cases.size()));
            if (newCandidate.contains(testCase))
                i--;
            else
                newCandidate.add(testCase);
        }
        return new TCChromosome(newCandidate);
    }


    @Override
    public int compareTo(TCChromosome o) {
        if (o.getFitness() > this.getFitness()) // this is a better solution
            return -1;
        if (this.getFitness() > o.getFitness()) // this is a worse solution
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return this.getCandidate() + " (" + this.getFitness() + ")";
    }
}
