package com.aidanogrady.cs547.assignment02.search;

import com.aidanogrady.cs547.assignment02.model.TestCase;

import java.util.List;
import java.util.Properties;

/**
 * Search defines how the various search algorithms are utilised.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public interface Search {
    /**
     * Searches the given test cases for the best solution.
     *
     * @param props the properties of the search
     * @param cases the test cases to be used
     */
    Result search(Properties props, List<TestCase> cases);

    /**
     * Runs a benchmark, performing multiple tests and seeing the results.
     *
     * @param props the properties of the benchmark
     * @param cases the test cases to be used
     */
    void benchmark(Properties props, List<TestCase> cases);
}
