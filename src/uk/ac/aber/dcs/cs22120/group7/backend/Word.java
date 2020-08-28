package uk.ac.aber.dcs.cs22120.group7.backend;

import java.util.Objects;

/**
 * This class holds all information and methods for the word objects. It is used as a blueprint for importing the Json.
 *
 * @author Ollie
 * @version 1.1
 */
public class Word implements Comparable<Word> {

    private static char dictType; //Global variable for all word objects which holds the current language selected

    private String english;
    private String welsh;
    private String wordType;

    /**
     * This constructor is used to create word objects during the application. It isn't used by the Gson method.
     *
     * @param englishWord becomes english
     * @param welshWord   becomes welsh
     * @param wordType    becomes wordType
     */
    public Word(String englishWord, String welshWord, String wordType) {
        this.english = englishWord;
        this.welsh = welshWord;
        this.wordType = wordType;
        setWordType();
    }

    /**
     * Method to get the dictionary mode. It is static, so applies to all Words
     *
     * @return type of dictionary (i.e english or welsh)
     */
    public static char getDictType() {
        return dictType;
    }

    /**
     * @param dictType becomes the mode for all Words
     *                 Method to set the dictionary mode. It is static, so applies to all Words
     */
    public static void setDictType(char dictType) {
        Word.dictType = dictType;
    }

    /**
     * Method to get the english word from the definition pair. If it is a verb, the word is preceded with 'to '.
     *
     * @return the english word
     */
    public String getEnglish() {
        StringBuilder result = new StringBuilder();
        if (wordType.equals("verb")) { // Return required formatting according to FR6
            result.append("to ").append(english);
        } else {
            result.append(english);
        }
        return result.toString();
    }

    /**
     * Method to get the welsh word with no added modifiers
     *
     * @return the english word
     */
    public String getEnglishNoVerb() {
        return english;
    }

    /**
     * Method to get the welsh word from the definition pair.
     *
     * @return the welsh word
     */
    public String getWelsh() {
        return welsh;
    }

    /**
     * Method to get the wordType word from the definition.
     *
     * @return the wordType
     */
    public String getWordType() {
        return wordType;
    }

    /**
     * This method is called in the toString, but will only be used in the first instance.
     * It changes the coded json nouns into the full words.
     */
    private void setWordType() {
        if (!wordType.equals("")) {
            if (wordType.charAt(0) == 'n') {
                if (wordType.charAt(1) == 'm') {
                    this.wordType = "masculine noun";
                } else if (wordType.charAt(1) == 'f') {
                    this.wordType = "feminine noun";
                }
            }
        }
    }

    /**
     * Method to check whether the inputs are translations of each other.
     *
     * @param word checked against this.welsh or this.english
     * @return a boolean - whether or not the two words are a translation of one another
     */
    public boolean isTranslation(String word) {
        boolean result = false;
        if (word.equals(this.getEnglish()) || word.equals(this.welsh)) {
            result = true;
        }
        return result;
    }

    /**
     * The equals method checks if two words are the same, it is used during the search algorithm.
     *
     * @param wordObject is cast to type Word and check to see if it is the same as this.
     * @return a boolean depending on whether the two objects are equal.
     */
    @Override
    public boolean equals(Object wordObject) {
        if (this == wordObject) return true;
        if (wordObject == null || getClass() != wordObject.getClass()) return false;
        Word word = (Word) wordObject;
        return Objects.equals(english, word.english) &&
                Objects.equals(welsh, word.welsh) &&
                Objects.equals(wordType, word.wordType);
    }

    /**
     * This method brings in the filter from the search bar and compares it with the start of the english word.
     *
     * @param s is the filter
     * @return true - if the starts are the same | false - if they are not
     */
    public boolean englishContains(String s) {
        return english.startsWith(s);
    }

    /**
     * This method brings in the filter from the search bar and compares it with the start of the welsh word.
     *
     * @param s is the filter
     * @return true - if the starts are the same | false - if they are not
     */
    public boolean welshContains(String s) {
        return welsh.startsWith(s);
    }

    /**
     * This method is used by the Collections.sort method.
     * It compares the objects by english word in english mode and by the welsh word if in welsh mode.
     * This method ignores the case of the words, so words with capitals at the beginning are placed with uncapitalised words.
     *
     * @param wordObject is the Word object to compare this to
     * @return an integer that is positive if the word is 'larger' or negative if the word is 'smaller' or 0 if they are the same.
     */
    @Override
    public int compareTo(Word wordObject) {
        int result;
        if (dictType == 'W') { // If ordering in terms of welsh
            result = welsh.compareToIgnoreCase(wordObject.getWelsh());
        } else { // If ordering in terms of english
            result = english.compareToIgnoreCase(wordObject.getEnglishNoVerb());
        }

        return result;
    }

    /**
     * This method returns the information from Word in a readable format. It is used by the ListView object in the uk.ac.aber.dcs.cs22120.group7.GUI.
     * In English mode, the english is first, and vice versa.
     *
     * @return a readable string with the information from Word
     */
    @Override
    public String toString() {
        setWordType();
        StringBuilder sb = new StringBuilder();
        if (dictType == 'E') {
            //English string being built to display
            sb.append(getEnglish()).append(" | ").append(getWelsh()).append(" | ").append(wordType);
        } else if (dictType == 'W') {
            //Welsh string being built to display
            sb.append(getWelsh()).append(" | ").append(getEnglish()).append(" | ").append(wordType);
        }
        return sb.toString();
    }
}