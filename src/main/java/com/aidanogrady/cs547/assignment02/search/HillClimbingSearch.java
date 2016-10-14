package com.aidanogrady.cs547.assignment02.search;

import com.aidanogrady.cs547.assignment02.model.TCChromosome;
import com.aidanogrady.cs547.assignment02.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * Hill Climbing search operates by taking a random starting point in the search
 * space and using its fitness function to find neighboring solutions to
 * improve its results until the search is complete.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public class HillClimbingSearch implements Search {
    private static final Logger LOGGER = LoggerFactory.getLogger(HillClimbingSearch.class);

    @Override
    public Result search(Properties props, List<TestCase> cases) {
        int limit = Integer.parseInt(props.getProperty("stall"));
        int setSize = Integer.parseInt(props.getProperty("size"));

        int climbs = 1;
        int restarts = 0;

        TCChromosome best = TCChromosome.generateChromosome(setSize, cases);
        LOGGER.info(climbs + ". Best: " + best);
        while (best.getFitness() > 0) {
            climbs++;

            List<TCChromosome> neighbours = best.getNeighbours(cases);
            boolean plateau = true;

            for (TCChromosome n : neighbours) {
                if (n.getFitness() < best.getFitness()) {
                    best = n;
                    plateau = false;
                }
            }

            if (plateau) {
                int attempts = 0;
                double fitness = best.getFitness();
                while (attempts < limit && best.getFitness() == fitness) {
                    TCChromosome t = TCChromosome.generateChromosome(setSize, cases);
                    if (t.getFitness() < best.getFitness()) {
                        best = t;
                    } else {
                        attempts++;
                    }
                }
                if (attempts == 15)
                    return new HillClimbingResult(best, climbs, restarts);
                restarts++;
                LOGGER.info(climbs + ". restart number " + restarts);
            } else {
                LOGGER.info(climbs + ". new best: " + best);
            }
        }

        return new HillClimbingResult(best, climbs, restarts);
    }

    @Override
    public void benchmark(Properties props, List<TestCase> cases) {
        int runs = Integer.parseInt(props.getProperty("benchmark"));
        LOGGER.info("Starting HillClimb benchmark");

        double totalFitness = 0.0;
        long totalTime = 0;

        int totalClimbs = 0;
        int totalRestarts = 0;

        for (int i = 1; i <= runs; i++) {
            LOGGER.info("Starting run " + i);

            long time = System.currentTimeMillis();
            HillClimbingResult result = (HillClimbingResult) search(props, cases);
            time = System.currentTimeMillis() - time;

            totalFitness += result.result.getFitness();
            totalTime += time;
            totalClimbs += result.climbs;
            totalRestarts += result.restarts;

            LOGGER.info("Finished run " + i + " in " + time + "ms");
            LOGGER.info("Result took " + result.climbs + " climbs and " + result.restarts + " restarts.");
            LOGGER.info("Result: " + result);
        }
        LOGGER.info("Finished HillClimbingSearch benchmark");

        LOGGER.info("Average fitness: " + totalFitness / runs);
        LOGGER.info("Average climbs: " + totalClimbs / runs);
        LOGGER.info("Average restarts: " + totalRestarts / runs);

        long avgTime = totalTime / runs;
        LOGGER.info("Total time: " + totalTime + " ms");
        LOGGER.info("Average time: " + avgTime + " ms");
    }

    /**
     * The data class for the results of this search.
     *
     * @author Aidan O'Grady
     * @since 0.2
     */
    private class HillClimbingResult implements Result {
        /**
         * The result the search returned.
         */
        final TCChromosome result;

        /**
         * The number of climbs taken in the search.
         */
        final int climbs;

        /**
         * The number of random restarts required.
         */
        final int restarts;

        /**
         * Constructs a new HillClimbingResult object.
         *
         * @param result the result of the search
         * @param climbs the number of climbs taken
         * @param restarts the number of restarts
         */
        public HillClimbingResult(TCChromosome result, int climbs, int restarts) {
            this.result = result;
            this.climbs = climbs;
            this.restarts = restarts;
        }
    }

}
