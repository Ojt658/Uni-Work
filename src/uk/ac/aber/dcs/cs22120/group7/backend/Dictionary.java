package uk.ac.aber.dcs.cs22120.group7.backend;

import com.google.gson.Gson;
import javafx.scene.control.Alert;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The dictionary class holds the information and methods for the main dictionary object.
 * PracticeList objects inherit the properties of Dictionary.
 *
 * @author Ollie, Sergiusz
 * @version 1.3
 */
public class Dictionary {

    /* Keeping two lists so that we don't have to reorder the same list each time we change mode */
    ArrayList<Word> englishDictionary;
    ArrayList<Word> welshDictionary;

    /**
     * The constructor initializes the lists for the dictionaries
     */
    public Dictionary() {
        englishDictionary = new ArrayList<>();
        welshDictionary = new ArrayList<>();
    }

    /**
     * Method that allows the dictionary ordered by english to be retrieved.
     *
     * @return the dictionary ordered by english
     */
    public ArrayList<Word> getEnglishDictionary() {
        return englishDictionary;
    }

    /**
     * Method that allows the dictionary ordered by welsh to be retrieved.
     *
     * @return the dictionary ordered by welsh
     */
    public ArrayList<Word> getWelshDictionary() {
        return welshDictionary;
    }

    /**
     * Read-Only method that will return a random word in the dictionary
     *
     * @return word object from dictionary
     */
    public Word getRandomWord() {
        Random random = new Random();
        return englishDictionary.get(random.nextInt(englishDictionary.size()));
    }

    /**
     * Load the data retrieved from the Json file into the Dictionary object
     *
     * @param dictionary Words added to Dictionary object.
     */
    public void load(ArrayList<Word> dictionary) {
        englishDictionary.addAll(dictionary);
        welshDictionary.addAll(dictionary);
        orderByWelsh();
    }

    /**
     * Saves data to the json file
     *
     * @param file is the json file
     */
    public void save(String file) {
        try (Writer fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            gson.toJson(englishDictionary, fw);
        } catch (IOException e) {
            System.out.println(e.getStackTrace().toString());
        }
    }

    /**
     * Sorts the dictionary by English words
     */
    private void orderByEnglish() {
        Word.setDictType('E');
        Collections.sort(englishDictionary);
    }

    /**
     * Sorts the dictionary by Welsh words
     */
    private void orderByWelsh() {
        Word.setDictType('W');
        Collections.sort(welshDictionary);
    }

    /**
     * This method allows addition to the Dictionary and Practice List objects.
     * Doesn't allow addition of same words to Dictionary/Practice List.
     *
     * @param word gets added to the Dictionary.
     */
    public void addWord(Word word) {
        //If the word already exists within the dictionary, do not add it...
        if (word != null) {
            if (englishDictionary.contains(word)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Word repetition");
                alert.setHeaderText("Word already in the practice list");
                alert.setContentText("");
                alert.showAndWait();
                //... otherwise add it to the dictionary and sort the list
            } else {
                welshDictionary.add(word);
                orderByWelsh();
                englishDictionary.add(word);
                orderByEnglish();
            }
        }
    }
}