package uk.ac.aber.dcs.cs22120.group7.tests;

import org.junit.jupiter.api.Assertions;
import uk.ac.aber.dcs.cs22120.group7.backend.Main;
import org.junit.jupiter.api.Test;
import uk.ac.aber.dcs.cs22120.group7.backend.Word;

/**
 *  This JUnit class is responsible for testing Main class functionality
 *
 * @author Ferenc, Sergiusz, Filip
 * @version 1.2
 */

public class MainTests {

    Main main = new Main();
    /**
     * Checks if word is added despite being similar
     */
    @Test
    public void testWordRepetition() {
        Word w1 = new Word("aa", "aa", "nm");
        Word w2 = new Word("aa","aa", "nf");
        main.addWord(w1);
        if(!main.isWordRepeated(w2))
        main.addWord(w2);
        Assertions.assertTrue(main.getDictionaryList().getEnglishDictionary().contains(w2), "Word wasn't added");
    }

    /**
     * Checks if isWordRepeated prevents adding two the same words
     */
    @Test
    public void testWordRepetition2(){
        Word w1 = new Word("aa", "aa", "nm");
        Word w2 = new Word("aa", "aa", "nm");
        int size = main.getDictionaryList().getEnglishDictionary().size();
        main.addWord(w1);
        if(!main.isWordRepeated(w2))
            main.addWord(w2);
        Assertions.assertEquals(size + 1, main.getDictionaryList().getEnglishDictionary().size(), "Word repetition doesn't work");
    }

    /**
     * Checks if program correctly add user input to BOTH dictionaries
     */
    @Test
    public void addedToBoth() {
        Word w2 = new Word("aa", "aa", "nf");
        main.addWord(w2);
        Assertions.assertEquals(main.getPracticeList().getEnglishDictionary().get(0), main.getDictionaryList().getEnglishDictionary().get(0));
    }
}
