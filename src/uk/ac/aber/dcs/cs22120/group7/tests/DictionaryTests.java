package uk.ac.aber.dcs.cs22120.group7.tests;

import uk.ac.aber.dcs.cs22120.group7.backend.Dictionary;

import uk.ac.aber.dcs.cs22120.group7.backend.PracticeList;

import uk.ac.aber.dcs.cs22120.group7.backend.Word;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 *  This JUnit class is responsible for testing Dictionary class functionality
 *
 * @author Ferenc, Sergiusz, Filip
 * @version 1.2
 */


public class DictionaryTests {
    private PracticeList prac;
    private Dictionary dictionary;

    /**
     * Setting up base variables to make tests on
     */
    @BeforeEach
    private void setUp() {
        ArrayList<Word> words = new ArrayList<>();
        Word w1 = new Word("abbey", "abaty", "mn");
        Word w2 = new Word("adventure","antur", "fn");
        words.add(w1);
        words.add(w2);
        dictionary = new Dictionary();
        dictionary.load(words);
        prac = new PracticeList();
        prac.load(words);
    }

    /**
     * Checking if program removes word from practice list correctly
     */
    @Test
    public void removeWordFromPractiseList(){
        Word w2 = new Word("adventure","antur", "fn");
        prac.removeWord(w2);
        Assertions.assertFalse(prac.getEnglishDictionary().contains(w2), "Word removed incorrectly");
    }

    /**
     * Checking if program adds a new word to both dictionaries
     */
    @Test
    public void addWordToDictionaryTest() {
        Word wordToAdd = new Word("flood", "llif", "nm");
        dictionary.addWord(wordToAdd);
        Assertions.assertEquals(dictionary.getEnglishDictionary().get(2),wordToAdd, "Not added to english");
        Assertions.assertEquals(dictionary.getWelshDictionary().get(2),wordToAdd, "Not added to welsh");
    }

    /**
     * Checking if program sorts words according to English properly
     */
    @Test
    public void testOrderingByEnglish() {
        Word wordToAdd = new Word("about to", "ar fin", "other");
        dictionary.addWord(wordToAdd);
        Assertions.assertEquals(dictionary.getEnglishDictionary().get(1), wordToAdd, "Word not in correct place");
    }

    /**
     * Checking if program sorts words according to Welsh properly
     */
    @Test
    public void testOrderingByWelsh() {
        Word wordToAdd = new Word("reason", "achos", "other");
        dictionary.addWord(wordToAdd);
        Assertions.assertEquals(dictionary.getWelshDictionary().get(1), wordToAdd, "Word not in correct place");
    }

    /**
     * Checking if program correctly re-adds word to the practice list
     */
    @Test
    public void addWordToPracticeList(){
        Word wordToAdd = new Word("Filip", "Philip", "nm");
        dictionary.addWord(wordToAdd);
        prac.removeWord(wordToAdd);
        prac.addWord(wordToAdd);
        Assertions.assertTrue(dictionary.getEnglishDictionary().contains(wordToAdd), "Word readded incorrectly");
    }

    /**
     * Checking if program has any issues with adding a huge amount of words
     */
    @Test
    public void addThousandWords(){
        for(int i = 0; i < 998; i++){
            Word wordToAdd = new Word("Filip" + Integer.toString(i), "Philip" + Integer.toString(i), "nm");
            dictionary.addWord(wordToAdd);
        }
        Assertions.assertEquals(1000, dictionary.getEnglishDictionary().size(), "Words added incorrectly");
    }

    /**
     * Checking if program has any issues with adding a huge amount of words
     */
    @Test
    public void addTenThousandWords(){
        for(int i = 0; i < 9998; i++){
            Word wordToAdd = new Word("Filip" + Integer.toString(i), "Philip" + Integer.toString(i), "nm");
            dictionary.addWord(wordToAdd);
        }
        Assertions.assertEquals(10000, dictionary.getEnglishDictionary().size(), "Words added incorrectly");
    }

}
