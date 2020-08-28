package uk.ac.aber.dcs.cs22120.group7.backend;

/**
 * A class to store information and methods for practice lists. It inherits most of its content from Dictionary
 *
 * @author Ollie
 * @version 1.3
 */
public class PracticeList extends Dictionary {

    /**
     * When a practice list is created, it calls the super constructor to initialize the variables.
     */
    public PracticeList() {
        super();
    }

    /**
     * This method removes a word from the practice list.
     *
     * @param word gets removed.
     */
    public void removeWord(Word word) {
        welshDictionary.remove(word);
        englishDictionary.remove(word);
    }
}