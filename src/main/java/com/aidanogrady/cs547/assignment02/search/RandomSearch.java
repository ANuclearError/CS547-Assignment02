package com.aidanogrady.cs547.assignment02.search;

import com.aidanogrady.cs547.assignment02.model.TCChromosome;
import com.aidanogrady.cs547.assignment02.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * Searches the space at random, making little use of the concepts of fitness or
 * a local search.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public class RandomSearch implements Search {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomSearch.class);

    @Override
    public Result search(Properties props, List<TestCase> cases) {
        int setSize = Integer.parseInt(props.getProperty("size"));
        int limit = Integer.parseInt(props.getProperty("random.limit"));

        TCChromosome best = null;
        int i = 0;
        while (i <= limit && (best == null || best.getFitness() > 0)) {
            i++;

            TCChromosome next = TCChromosome.generateChromosome(setSize, cases);
            if (best == null || best.getFitness() > next.getFitness()) {
                best = next;
                LOGGER.info(i + ". new best: " + best);
            }
        }
        return new RandomResult(best, i);
    }

    @Override
    public void benchmark(Properties props, List<TestCase> cases) {
        int runs = Integer.parseInt(props.getProperty("benchmark"));
        LOGGER.info("Starting RandomSearch benchmark");

        double totalFitness = 0.0;
        double totalAttempts = 0.0;
        long totalTime = 0;

        for (int i = 1; i <= runs; i++) {
            LOGGER.info("Starting run " + i);

            long time = System.currentTimeMillis();
            RandomResult result = (RandomResult) search(props, cases);
            time = System.currentTimeMillis() - time;

            totalFitness += result.result.getFitness();
            totalAttempts += result.runs;
            totalTime += time;

            LOGGER.info("Finished run " + i + " in " + time + "ms");
            LOGGER.info("Search took " + result.runs + " attempts");
            LOGGER.info("Result: " + result);
        }
        LOGGER.info("Finished RandomSearch benchmark");

        LOGGER.info("Average fitness: " + totalFitness / runs);
        LOGGER.info("Average attempts: " + totalAttempts / runs);
        LOGGER.info("Total time: " + totalTime + " ms");
        LOGGER.info("Average time: " + totalTime / runs + " ms");
    }


    /**
     * Data class for the results of this search.
     *
     * @author Aidan O'Grady
     * @since 0.2
     */
    private class RandomResult implements Result {
        /**
         * The result the search returned.
         */
        final TCChromosome result;

        /**
         * The number of attempts the search took.
         */
        final int runs;

        /**
         * Constructs a new RandomResult.
         *
         * @param result the result returned by the search
         * @param runs the number of searches tried
         */
        public RandomResult(TCChromosome result, int runs) {
            this.result = result;
            this.runs = runs;
        }
    }
}
