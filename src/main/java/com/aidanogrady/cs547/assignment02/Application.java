package com.aidanogrady.cs547.assignment02;

import com.aidanogrady.cs547.assignment02.model.TCChromosome;
import com.aidanogrady.cs547.assignment02.model.TestCase;
import com.aidanogrady.cs547.assignment02.search.GeneticAlgorithmSearch;
import com.aidanogrady.cs547.assignment02.search.HillClimbingSearch;
import com.aidanogrady.cs547.assignment02.search.RandomSearch;
import com.aidanogrady.cs547.assignment02.search.Search;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main application class for the system.
 *
 * @author Aidan O'Grady
 * @since 0.0
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("CS547 Assignment 01: Introductory Exercise");
        System.out.println("Author: Aidan O'Grady (201218150)");
        System.out.println();

        if (args.length < 2) {
            System.out.println("Please provide a .properties file & data file");
        } else {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(args[0]));
            } catch (IOException e) {
                System.out.println(args[0] + " cannot be parsed.");
            }
            List<TestCase> cases = readTestCasesFromFile(args[1]);
            if (validateProperties(properties) && cases != null) {
                System.out.println("Properties are valid, let's go!");
                System.out.println("No. of test cases: " + cases.size() + ".");
                System.out.println();

                Search search = new GeneticAlgorithmSearch();
                search.search(properties, cases);
             }
            }
        }
    }

    /**
     * Attempts to return the list of test cases read from the given file.
     *
     * @param filePath the file to read test cases from.
     * @return list of test cases from file if valid, otherwise null
     */
    private static List<TestCase> readTestCasesFromFile(String filePath) {
        List<TestCase> list = new ArrayList<>();

        String content;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(filePath));
            content = new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }

        // Lovely regex to split file into each test case.
        Pattern pattern = Pattern.compile("unitest[\\d+]+:\\s[v\\d+:\\s\\d]+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            // Compress whitespace and remove colons to allow easy manipulation.
            String string = matcher.group();
            string = string.replaceAll("\\s+"," ").replaceAll(":", "");
            String[] nextCase = string.split(" ");

            List<Integer> faults = new ArrayList<>();
            int length = (nextCase.length - 1) / 2;
            for (int i = 0; i < length; i++) {
                if (Integer.parseInt(nextCase[(i + 1) * 2]) > 0)
                    faults.add(i + 1);
            }
            list.add(new TestCase(nextCase[0], faults, length));
        }
        return list;
    }

    /**
     * Reads the given properties and ensures that they are valid.
     *
     * @param properties the properties being used
     * @return true if valid properties, otherwise false
     */
    private static boolean validateProperties(Properties properties) {
        String[] props = {"size", "benchmark", "random.limit",
                "ga.population", "ga.crossover", "ga.elitism", "ga.mutation",
                "ga.tournament"};

        String[] types = {"int", "int", "int", "int", "double", "double",
                "double", "int"};

        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            String value = properties.getProperty(prop);
            if (value == null) {
                System.out.println(prop + " property does not exist in file.");
                return false;
            } else {
                switch (types[i]) {
                    case "int":
                        try {
                            int val = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            System.out.println(prop + " must be " + types[i]);
                            return false;
                        }
                        break;
                    case "double":
                        try {
                            double val = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            System.out.println(prop + " must be " + types[i]);
                            return false;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

}
