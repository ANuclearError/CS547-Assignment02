package com.aidanogrady.cs547.assignment02.search;

import com.aidanogrady.cs547.assignment02.model.TCChromosome;
import com.aidanogrady.cs547.assignment02.model.TCPopulation;
import com.aidanogrady.cs547.assignment02.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * The Genetic Algorithm search is not a local search, but instead polls various
 * different potential solutions, and uses them to continuously create better
 * ones until the correct solution is found.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public class GeneticAlgorithmSearch implements Search {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticAlgorithmSearch.class);

    @Override
    public Result search(Properties props, List<TestCase> cases) {
        int limit = Integer.parseInt(props.getProperty("ga.limit"));

        TCPopulation population = new TCPopulation(props, cases);

        TCChromosome best = population.getFittest();

        int generations = 1;
        int stalls = 0;
        LOGGER.info("Generation " + generations + ". " + best);
        while (best.getFitness() > 0 && stalls < limit) {
            population.evolve();

            if (population.getFittest().getFitness() == best.getFitness())
                stalls++;
            else if (population.getFittest().getFitness() < best.getFitness())
                stalls = 0;
            best = population.getFittest();
            generations++;
            LOGGER.info("Generation " + generations + ". Best: " + best + " Average: " + population.getAverageFitness());
        }
        return new GAResult(best, generations);
    }

    @Override
    public void benchmark(Properties props, List<TestCase> cases) {
        int runs = Integer.parseInt(props.getProperty("benchmark"));
        LOGGER.info("Starting HillClimb benchmark");

        double totalFitness = 0.0;
        long totalTime = 0;

        int totalGenerations = 0;

        for (int i = 1; i <= runs; i++) {
            LOGGER.info("Starting run " + i);

            long time = System.currentTimeMillis();
            GAResult result = (GAResult) search(props, cases);
            time = System.currentTimeMillis() - time;

            totalFitness += result.result.getFitness();
            totalTime += time;

            totalGenerations += result.generations;


            LOGGER.info("Finished run " + i + " in " + time + "ms");
            LOGGER.info("Result took " + result.generations + " generations.");
            LOGGER.info("Result: " + result);
        }
        LOGGER.info("Finished GeneticAlgorithmSearch benchmark");

        double avgFitness = totalFitness / runs;
        LOGGER.info("Average fitness: " + avgFitness);
        LOGGER.info("Average generations: " + totalGenerations / runs);

        long avgTime = totalTime / runs;
        LOGGER.info("Total time: " + totalTime + " ms");
        LOGGER.info("Average time: " + avgTime + " ms");
    }

    /**
     * Data class for the results of this search.
     *
     * @author Aidan O'Grady
     * @since 0.2
     */
    private class GAResult implements Result {
        /**
         * The chromosome the search returned.
         */
        final TCChromosome result;

        /**
         * The number of generations the population went through.
         */
        final int generations;

        /**
         * Constructs a new GAResult.
         *
         * @param result the result of the search
         * @param generations the number of generations of the search
         */
        GAResult(TCChromosome result, int generations) {
            this.result = result;
            this.generations = generations;
        }
    }
}
