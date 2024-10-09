package assignment;

import java.io.*;
import java.util.*;

public class RandomWriter implements TextProcessor {
    private static StringBuffer content;
    private static StringBuffer outputText;
    private static HashMap<String, HashMap<Character, Integer>> frequencyMap;
    private static Random rand;
    private static int k;

    /**
     * Ensure that passed arguments are valid and if so, call methods to read, generate, and write text
     *
     * @param args String[] indicating the source file name, output file name, level of k analysis, and desired output length
     */
    public static void main(String[] args) throws IOException {

        //if less than expected number of arguments are passed, throw an error
        if (args.length < 4) {
            System.err.println("Expected four or more arguments.");
            throw new IllegalArgumentException();
        }

        //if any argument is null, throw an error
        if (args[0] == null || args[1] == null || args[2] == null || args[3] == null) {
            System.err.println("Null argument passed.");
            throw new NullPointerException();
        }

        String source = args[0];
        String result = args[1];
        int k;
        int length;

        //if k or length is not able to be parsed into integer, throw an error
        try {
            k = Integer.parseInt(args[2]);
            length = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Third/fourth argument not an integer.");
            throw new IllegalArgumentException();
        }

        //ensure input file can be read
        RandomWriter randomWriter = (RandomWriter) createProcessor(k);
        randomWriter.readText(source);

        //ensure k and length are non-negative and k is less than the length of content
        if (k < 0 || length < 0 || content.length() <= k) {
            System.err.println("k or length is invalid.");
            throw new IllegalArgumentException();
        }

        //if k = 0, create a text of randomly chosen characters
        if (k == 0) {
            randomWriter.kEqualToZero(length);
        }

        //check validity of output
        randomWriter.checkOutput(result);

        //build frequencyTable
        randomWriter.createFrequency(content.toString(), k);

        //generate randomly written text
        randomWriter.generateText(length);

        //write the generated text to the output file
        randomWriter.writeText(result, length);
    }

    public static TextProcessor createProcessor(int level) {
        return new RandomWriter(level);
    }

    /**
     * Instantiate private instance variables
     *
     * @param level int representing the level of k analysis
     */
    private RandomWriter(int level) {
        content = new StringBuffer();
        frequencyMap = new HashMap<>();
        outputText = new StringBuffer();
        rand = new Random();
        k = level;
    }

    /**
     * Read the file, if able, and set class variable content to the text inside file
     *
     * @param inputFilename String representing the name of the file to be read
     */
    public void readText(String inputFilename) throws IOException {
        try {
            File sourceFile = new File(inputFilename);
            //check if the input is a valid file and exists, if not throw an exception
            if (!sourceFile.isFile() || !sourceFile.exists()) {
                System.err.println("Source file is not valid or does not exist.");
                throw new IllegalArgumentException();
            }

            //if valid and exists, read each character and append to content
            FileReader fileReader = new FileReader(inputFilename);
            int ch;
            while ((ch = fileReader.read()) != -1) {
                content.append((char) ch);
            }
            fileReader.close();
        } catch (IOException e) {
            System.err.println("Error while reading file.");
            throw e;
        }
    }

    /**
     * Check the validity of the output file, if not create the new file
     *
     * @param outputFilename String representing the name of the file to be opened/written to
     */
    public void checkOutput(String outputFilename) throws IOException {
        File resultFile = new File(outputFilename);

        //if exists, check if it can be written to, if not create file
        //catch any potential IOExceptions and print error statement
        try {
            if (resultFile.canWrite() || resultFile.createNewFile() && resultFile.canWrite()) {
                System.out.println("File created.");
            } else {
                System.err.println("File cannot be created.");
                throw new IllegalArgumentException();
            }
        } catch (IOException e) {
            System.err.println("Error while creating output file.");
            throw e;
        }
    }

    /**
     * Write the generated output text into the output file
     *
     * @param outputFilename String representing the name of the file to be opened/written to
     * @param length         int representing the desired length of the output text
     */
    public void writeText(String outputFilename, int length) throws IOException {
        FileWriter fileWriter = null;

        //write the generated text into the output file
        //catch any potential IOExceptions and print error statement
        try {
            fileWriter = new FileWriter(outputFilename);
            fileWriter.write(outputText.toString());
        } catch (IOException e) {
            System.err.println("Error while writing to file.");
            throw e;
        }
        fileWriter.close();
    }

    /**
     * Generate the output text to be written in output file
     *
     * @param length int representing the desired length of the output text
     */
    public void generateText(int length) throws IOException {

        //if the file is empty, return an empty output text
        if (length == 0) {
            return;
        }

        //choose a random index in content to use as starting index for the initial seed
        int randomIndex = rand.nextInt(content.length() - k);
        String seed = content.substring(randomIndex, randomIndex + k);

        outputText.append(seed);

        while (outputText.length() < length) {
            //retrieve the value  for the respective seed, which is a hashmap type
            HashMap<Character, Integer> charMap = frequencyMap.get(seed);

            //retrieve a random character from the value of this hashmap and append to outputText
            char nextChar = getRandomChar(charMap);
            outputText.append(nextChar);

            //redefine the seed by removing its first character and appending the recently added character
            seed = seed.substring(1) + nextChar;

            //if at any point, seed does not exist in the map (reaches the end of content), choose a new random seed using the method above
            if (!frequencyMap.containsKey(seed)) {
                randomIndex = rand.nextInt(content.length() - k);
                seed = content.substring(randomIndex, randomIndex + k);
            }
        }
    }

    /**
     * Retrieve a random character that follows a seed based on the probabilistic model frequencyMap
     *
     * @param charMap HashMap representing the characters and their respective frequencies that appear after a specific seed in the text
     * @return char that represents a randomly chosen character that follows the seed
     */
    public char getRandomChar(HashMap<Character, Integer> charMap) {

        //sum up the total amount of characters that follow the seed
        int totalChar = 0;
        for (int count : charMap.values()) {
            totalChar += count;
        }

        //generate a random index to return the random character
        int randomIndex = rand.nextInt(totalChar);
        int sum = 0;

        //iterate through the characters until the amount of characters passed so far exceeds randomIndex (meaning just enough characters have passed before the chosen character)
        for (char ch : charMap.keySet()) {
            sum += charMap.get(ch);
            if (sum > randomIndex) {
                return ch;
            }
        }

        //if no random character is found, return the ascii character of the random index
        return (char) randomIndex;
    }

    /**
     * Build a 2D HashMap that organizes all possible seeds and all the characters and the frequencies that follow it
     * HashMap structure looks like {seed: {character: frequency}}
     *
     * @param source String representing the input file
     * @param k      int representing the level of analysis
     */
    public void createFrequency(String source, int k) throws IOException {

        //iterate through all the characters from source text, leaving one character at the end so that all seeds in the map have at least one following character
        for (int i = 0; i <= source.length() - k - 1; i++) {
            String seed = source.substring(i, i + k);
            char nextChar = source.charAt(i + k);

            //if the seed is not already a key, put a new key-value pair with the seed and a new hashmap
            if (!frequencyMap.containsKey(seed)) frequencyMap.put(seed, new HashMap<>());

            //if inner hashmap does not already contain a value, put a new key-value pair with character and new frequency of 1
            //add one to the value of the inner hashmap
            HashMap<Character, Integer> charMap = frequencyMap.get(seed);
            charMap.put(nextChar, charMap.getOrDefault(nextChar, 0) + 1);
        }
    }

    /**
     * In the case k = 0, generate an output only of completely random characters from the input text
     *
     * @param length int representing the desired length of the output text
     */
    private void kEqualToZero(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(content.length());
            outputText.append(content.charAt(randomIndex));
        }
    }

    /**
     * @return class variable frequencyMap of type HashMap
     */
    public HashMap<String, HashMap<Character, Integer>> getFrequencyMap() {
        return frequencyMap;
    }

    /**
     * @return class variable content of type String
     */
    public String getContent() {
        return content.toString();
    }
}
