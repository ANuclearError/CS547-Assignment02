package com.aidanogrady.cs547.assignment02.model;

import java.util.*;

/**
 * The population is a set of chromosomes either randomly created or spawned by
 * previous generations. It has the ability to evolve future generations by
 * combining the best solutions available.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public class TCPopulation {
    /**
     * The list of test cases to use to create chromosomes.
     */
    private List<TestCase> testCases;

    /**
     * The population
     */
    private List<TCChromosome> population;

    /**
     * The size of the population
     */
    private int size;

    /**
     * The rate of elitism
     */
    private double elitism;

    /**
     * Crossover probability
     */
    private double crossover;

    /**
     * The probability of mutation occurring.
     */
    private double mutation;

    /**
     * The number of combatants in a tournament.
     */
    private int tournament;

    /**
     * The number of tests each set should include.
     */
    private final int setSize;

    /**
     * RNG for mutation, crossover, etc.
     */
    private Random random;

    /**
     * Constructor.
     *
     * @param properties the configuration of this population
     * @param testCases the test cases to be used in generation chromosomes
     */
    public TCPopulation(Properties properties, List<TestCase> testCases) {
        this.testCases = testCases;
        this.size = Integer.parseInt(properties.getProperty("ga.population"));
        this.elitism = Double.parseDouble(properties.getProperty("ga.elitism"));
        this.crossover = Double.parseDouble(properties.getProperty("ga.crossover"));
        this.mutation = Double.parseDouble(properties.getProperty("ga.mutation"));
        this.tournament = Integer.parseInt(properties.getProperty("ga.tournament"));
        this.setSize = Integer.parseInt(properties.getProperty("size"));

        this.population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            population.add(TCChromosome.generateChromosome(setSize, testCases));
        }
        Collections.sort(population);

        random = new Random();
    }

    /**
     * Returns the chromosome with the lowest fitness.
     *
     * @return best solution thus far.
     */
    public TCChromosome getFittest() {
        return population.get(0);
    }

    /**
     * Returns the average fitness of this population.
     *
     * @return average fitness.
     */
    public double getAverageFitness() {
        double fitness = 0.0;
        for (TCChromosome c : population)
            fitness += c.getFitness();
        return fitness / size;
    }

    /**
     * Returns the chromosome with the highest fitness.
     *
     * @return best solution thus far.
     */
    public TCChromosome getLeastFit() {
        return population.get(size - 1);
    }

    /**
     * Evolves the current population into a new one with better fitness.
     */
    public void evolve() {
        int eliteSize = (int) Math.round(size * elitism);

        List<TCChromosome> nextGen = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TCChromosome father = selectParent(eliteSize);
            TCChromosome mother = selectParent(eliteSize);

            TCChromosome son;
            TCChromosome daughter;

            if (random.nextDouble() < crossover) {
                int offset = random.nextInt(mother.getCandidate().size());
                son = father.crossover(mother, offset);
                daughter = mother.crossover(father, offset);

            } else {
                son = father;
                daughter = mother;
            }

            if (random.nextDouble() < mutation) {
                son = son.mutate(testCases);
            }

            if (random.nextDouble() < mutation) {
                daughter = daughter.mutate(testCases);
            }

            nextGen.add(son);
            if (i != (size - 1)) { // Handle odd size pop
                nextGen.add(daughter);
                i++;
            }
        }
        population = nextGen;
        Collections.sort(population);
    }

    /**
     * Select a random parent from the population.
     *
     * @param limit the cutoff point of where the parent should be taken from.
     * @return random chromosome
     */
    private TCChromosome selectParent(int limit) {
        TCChromosome parent = population.get(random.nextInt(limit));
        for (int i = 0; i < tournament; i++) {
            TCChromosome opponent = population.get(random.nextInt(limit));
            if (opponent.getFitness() < parent.getFitness())
                parent = opponent;
        }
        return parent;
    }
}
