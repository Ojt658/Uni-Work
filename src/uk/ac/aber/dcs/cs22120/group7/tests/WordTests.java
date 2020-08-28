package uk.ac.aber.dcs.cs22120.group7.tests;

import uk.ac.aber.dcs.cs22120.group7.backend.Word;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 *  This JUnit class is responsible for testing Word class functionality
 *
 * @author Ferenc, Sergiusz, Filip
 * @version 1.2
 */
public class WordTests {

    private Word testVerb, testMNoun, testFNoun;

    /**
     * Setting up base variables to make tests on
     */
    @BeforeEach
    public void setup() {
        testVerb = new Word("accept", "derbyn", "verb");
        testMNoun = new Word("abbey", "abaty", "nm");
        testFNoun = new Word("action", "gweithred", "nf");
    }

    /**
     * Checks if verb returns word with "to "
     */
    @Test
    public void checkEnglishVerbPrinting() {
        Assertions.assertEquals("to accept", testVerb.getEnglish(), "Incorrect printing of verb");
    }

    /**
     * Checking if words give the correct word type
     */
    @Test
    public void checkNounPrinting() {
        Assertions.assertEquals("masculine noun", testMNoun.getWordType(), "Incorrect printing of masculine noun");
        Assertions.assertEquals("feminine noun", testFNoun.getWordType(), "Incorrect printing of feminine noun");
    }

    /**
     * Checks if is equals method working properly
     */
    @Test
    public void checkIfEqual() {
        Assertions.assertTrue(testVerb.equals(testVerb) && !testVerb.equals(testMNoun));
    }

}
