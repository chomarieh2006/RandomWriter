package assignment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

public class RandomWriterTest {

    //ensure that main() method can be called without any errors when given valid arguments
    @Test
    void testCreateProcessor() throws IOException {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "3", "10"};
        assertDoesNotThrow(() -> RandomWriter.main(args));
    }

    //check that the content retrieved from the input file is same as content read
    @Test
    void testGetContent() throws IOException {
        String[] args = {"test_books/simple.txt", "output.txt", "3", "10"};
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);
        randomWriter.main(args);

        assertEquals("simple test", randomWriter.getContent());
    }

    //check exception thrown if less than 4 arguments passed
    @Test
    void testLessArgs() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "3"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception thrown if non-numeric value passed as the third argument
    @Test
    void testInvalidThirdArgument() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "notANumber", "100"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception thrown if non-numeric value passed as the fourth argument
    @Test
    void testInvalidFourthArgument() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "3", "notANumber"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });

    }

    //verify that main() executes when passed four valid arguments
    @Test
    void testFourArgs() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "0", "100"};

        assertDoesNotThrow(() -> RandomWriter.main(args));
    }

    //verify that main() executes when passed more than four valid arguments
    @Test
    void testMoreArgs() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "3", "0", "fifthArgument"};

        assertDoesNotThrow(() -> RandomWriter.main(args));
    }

    //verify that the program can handle an analysis level of 0 (random output)
    @Test
    void testKEqualZero() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "0", "100"};

        assertDoesNotThrow(() -> RandomWriter.main(args));
    }

    //verify that the program can handle a desire length of 0 (empty output)
    @Test
    void testLengthEqualZero() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "0", "0"};

        assertDoesNotThrow(() -> RandomWriter.main(args));
    }

    //check exception if bigger k than length passed
    @Test
    void testKGreaterThanLength() {
        String[] args = {"test_books/simple.txt", "output.txt", "100", "3"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //verify that the program can handle a smaller k than length (proceed as normal)
    @Test
    void testKLessThanLength() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "4", "3"};

        assertDoesNotThrow(() -> RandomWriter.main(args));
    }

    //check exception if null argument passed
    @Test
    void testNullArgs() {
        String[] args = {null, "output.txt", "4", "3"};

        assertThrows(NullPointerException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception if blank argument passed
    @Test
    void testBlankStrings() {
        String[] args = {"", "output.txt", "4", "3"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception if negative argument for k passed
    @Test
    void testNegK() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "-4", "3"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception if negative argument for length passed
    @Test
    void testNegLength() {
        String[] args = {"test_books/CatInTheHat.txt", "output.txt", "4", "-3"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception if k that is equal to or less than content length is passed
    @Test
    void testInvalidK() {
        String[] args = {"test_books/simple.txt", "output.txt", "11", "100"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception if invalid argument for length passed
    @Test
    void testInvalidLength() {
        String[] args = {"test_books/simple.txt", "output.txt", "3", "-1"};

        assertThrows(IllegalArgumentException.class, () -> {
            RandomWriter.main(args);
        });
    }

    //check exception if attempting to read from a nonexistent file
    @Test
    void testFileNotFound() {
        String invalidFile = "nonexistentfile.txt";
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);

        assertThrows(IllegalArgumentException.class, () -> {
            randomWriter.readText(invalidFile);
        });
    }

    //check exception if attempting to read from invalid file type
    @Test
    void testFileNotValid() {
        String invalidFile = "nonexistentfile.jpg";
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);

        assertThrows(IllegalArgumentException.class, () -> {
            randomWriter.readText(invalidFile);
        });
    }

    //ensure program works when passed in exisitng and valid file
    @Test
    void testFileFoundAndValid() {
        String validFile = "test_books/CatInTheHat.txt";
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);

        assertDoesNotThrow(() -> randomWriter.readText(validFile));
    }

    //check that the content read from the input file is correctly read and stored in content
    @Test
    void testContentRead() throws IOException {
        String validFile = "test_books/simple.txt";
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);
        randomWriter.readText(validFile);

        String theoContent = "simple test";
        String actualContent = randomWriter.getContent();

        assertEquals(theoContent, actualContent);
    }

    //check that validating and creating an output file does not throw any exceptions, even if not already existing
    @Test
    void testOutputFileValid() {
        String validFile = "notexistentfile.jpg";
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);

        assertDoesNotThrow(() -> randomWriter.checkOutput(validFile));
    }

    //check that writing into output file does not throw any exceptions
    @Test
    void testWriteText() {
        String validFile = "notexistentfile.jpg";
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);

        assertDoesNotThrow(() -> randomWriter.checkOutput(validFile));
    }

    //verify that built frequency map is same as hardcoded frequency map (unqiue following characters)
    @Test
    void testCreateFrequency() throws IOException {
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);

        HashMap<String, HashMap<Character, Integer>> theoMap = new HashMap<>();

        HashMap<Character, Integer> freq = new HashMap<>();
        freq.put('p', 1);
        theoMap.put("sim", freq);

        HashMap<Character, Integer> freq2 = new HashMap<>();
        freq2.put('l', 1);
        theoMap.put("imp", freq2);

        HashMap<Character, Integer> freq3 = new HashMap<>();
        freq3.put('e', 1);
        theoMap.put("mpl", freq3);

        HashMap<Character, Integer> freq4 = new HashMap<>();
        freq4.put(' ', 1);
        theoMap.put("ple", freq4);

        HashMap<Character, Integer> freq5 = new HashMap<>();
        freq5.put('t', 1);
        theoMap.put("le ", freq5);

        HashMap<Character, Integer> freq6 = new HashMap<>();
        freq6.put('e', 1);
        theoMap.put("e t", freq6);

        HashMap<Character, Integer> freq7 = new HashMap<>();
        freq7.put('s', 1);
        theoMap.put(" te", freq7);

        HashMap<Character, Integer> freq8 = new HashMap<>();
        freq8.put('t', 1);
        theoMap.put("tes", freq8);

        randomWriter.createFrequency("simple test", 3);
        HashMap<String, HashMap<Character, Integer>> actualMap = randomWriter.getFrequencyMap();

        assertEquals(theoMap, actualMap);
    }

    //verify that built frequency map is same as hardcoded frequency map (multiple following characters)
    @Test
    void testCreateFrequency2() throws IOException {
        RandomWriter randomWriter = (RandomWriter) RandomWriter.createProcessor(3);

        HashMap<String, HashMap<Character, Integer>> theoMap = new HashMap<>();

        HashMap<Character, Integer> freq = new HashMap<>();
        freq.put('i', 1);
        freq.put('k', 1);
        theoMap.put("bro", freq);

        HashMap<Character, Integer> freq2 = new HashMap<>();
        freq2.put('l', 1);
        theoMap.put("roi", freq2);

        HashMap<Character, Integer> freq3 = new HashMap<>();
        freq3.put('b', 1);
        freq3.put('s', 1);
        theoMap.put("oil", freq3);

        HashMap<Character, Integer> freq4 = new HashMap<>();
        freq4.put('r', 1);
        theoMap.put("ilb", freq4);

        HashMap<Character, Integer> freq5 = new HashMap<>();
        freq5.put('o', 1);
        theoMap.put("lbr", freq5);

        HashMap<Character, Integer> freq6 = new HashMap<>();
        freq6.put('e', 1);
        theoMap.put("rok", freq6);

        HashMap<Character, Integer> freq7 = new HashMap<>();
        freq7.put('o', 1);
        theoMap.put("oke", freq7);

        HashMap<Character, Integer> freq8 = new HashMap<>();
        freq8.put('i', 1);
        theoMap.put("keo", freq8);

        HashMap<Character, Integer> freq9 = new HashMap<>();
        freq9.put('l', 1);
        theoMap.put("eoi", freq9);

        randomWriter.createFrequency("broilbrokeoils", 3);
        HashMap<String, HashMap<Character, Integer>> actualMap = randomWriter.getFrequencyMap();

        assertEquals(theoMap, actualMap);
    }


}
