package com.aidanogrady.cs547.assignment02.model;

import java.util.List;

/**
 * The test case that is part of the suite
 *
 * @author Aidan O'Grady
 * @since 0.0
 */
public class TestCase {
    /**
     * The name given to this test.
     */
    private String testName;

    /**
     * The faults to be detected.
     */
    private List<Integer> faults;

    /**
     * The total number of faults that exist in system.
     */
    private int numOfFaults;

    /**
     * The number of faults that this test case detects.
     */
    private int faultsFound;

    /**
     * The coverage this test has.
     */
    private double coverage;

    /**
     * Constructs a new test case.
     *
     * @param testName the name of the test
     * @param faults the faults the test detects
     * @param numOfFaults the number of faults that could be detected
     */
    public TestCase(String testName, List<Integer> faults, int numOfFaults) {
        this.testName = testName;
        this.faults = faults;
        this.numOfFaults = numOfFaults;
        this.faultsFound = faults.size();
        this.coverage = (double) this.faultsFound / this.numOfFaults;
    }

    /**
     * Returns the name of the test.
     *
     * @return testName
     */
    public String getTestName() {
        return testName;
    }

    /**
     * Returns the faults.
     *
     * @return faults
     */
    public List<Integer> getFaults() {
        return faults;
    }

    /**
     * Retruns the number of faults that could be detected.
     *
     * @return numOfFaults
     */
    public int getNumOfFaults() {
        return numOfFaults;
    }

    /**
     * Returns the number of faults detected.
     *
     * @return faultsFound
     */
    public int getFaultsFound() {
        return faultsFound;
    }

    /**
     * Returns the coverage of this test.
     *
     * @return coverage
     */
    public double getCoverage() {
        return coverage;
    }

    @Override
    public String toString() {
        return testName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestCase testCase = (TestCase) o;

        if (numOfFaults != testCase.numOfFaults) return false;
        return faults != null ? faults.equals(testCase.faults) : testCase.faults == null;

    }

    @Override
    public int hashCode() {
        int result = faults != null ? faults.hashCode() : 0;
        result = 31 * result + numOfFaults;
        return result;
    }
}
