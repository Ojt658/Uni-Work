package uk.ac.aber.dcs.cs22120.group7.gui;

import javafx.scene.text.Font;
import uk.ac.aber.dcs.cs22120.group7.backend.Main;
import uk.ac.aber.dcs.cs22120.group7.backend.PracticeList;
import uk.ac.aber.dcs.cs22120.group7.backend.Word;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This is the class that displays the Flashcards FR8.
 *
 * @author Sergiusz
 * @version 1.1
 */
public class FlashCards extends WindowTemplate {

    PracticeList practiceListRemain;
    boolean endOf = false;
    ArrayList<Word> seenWords = new ArrayList<>();

    /**
     * The constructor takes in the main application and calls the super constructor.0
     *
     * @param flashCard is the application
     */
    public FlashCards(Main flashCard, PracticeList practiceList) {
        super(flashCard);
        practiceListRemain = practiceList;
    }

    /**
     * This method implements and returns the centre display required by WindowTemplate
     *
     * @param dictType the mode of the dictionary
     * @return the centre display
     */
    @Override
    public Pane centre(char dictType) {

        //Text stating which language is being translated

        Label label = new Label();
        label.setAlignment(Pos.TOP_LEFT);
        label.setId("text-color");

        switch (dictType) {
            case 'E' -> label.setText("English to Welsh");
            case 'W' -> label.setText("Welsh to English");
        }

        HBox infoBar = new HBox(20);
        infoBar.setPadding(new Insets(10, 10, 10, 10));
        infoBar.setAlignment(Pos.TOP_LEFT);
        infoBar.getChildren().addAll(label);

        //Title of flashcards window

        Label flashcardLabel = new Label("Flashcards");
        //CSS and JavaFX
        flashcardLabel.setAlignment(Pos.CENTER_LEFT);
        flashcardLabel.setFont(Font.font("", 20));
        flashcardLabel.setId("text-color");

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(flashcardLabel);

        // Acquire a random word and it's translation from the practice list and display it

        Label flashCard = new Label();
        flashCard.setId("text-color");

        Label flashcardReveal = new Label();
        flashcardReveal.setId("text-color");

        // Checks if practice list is empty...
        AtomicReference<Word> testWord = new AtomicReference<>();

        //... if the practice list is empty, inform the user...
        if (seenWords.size() == practiceListRemain.getEnglishDictionary().size()) {
            flashCard.setText("NO WORD REMAINING");
            endOf = true;

            //...otherwise get a random word from the practice list
        } else {

            //Find a word that hasn't been displayed on the flashcards already
            do {
                testWord.set(practiceListRemain.getRandomWord());
            } while (seenWords.contains(testWord.get()));
            seenWords.add(testWord.get());
            if (dictType == 'E') flashCard.setText(testWord.get().getEnglish());
            else if (dictType == 'W') flashCard.setText(testWord.get().getWelsh());
        }

        HBox wordShow = new HBox(20);
        wordShow.setAlignment(Pos.CENTER);
        wordShow.getChildren().addAll(flashCard, flashcardReveal);

        // Button which reveals label with translation
        Button revealButton = new Button("click to see Translation");
        revealButton.setAlignment(Pos.CENTER);
        revealButton.setId("buttons");

        revealButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                //Checks which language is being translated and displays the translated word
                if (dictType == 'E') flashcardReveal.setText(testWord.get().getWelsh());
                else if (dictType == 'W') flashcardReveal.setText(testWord.get().getEnglish());
            }
        });

        HBox showTranslation = new HBox(20);
        showTranslation.setAlignment(Pos.CENTER);
        showTranslation.getChildren().addAll(revealButton);

        // Next button which gets a new word, it changes to restart button when all practice words have been shown
        Button nextButton = new Button();
        nextButton.setText("Next word");
        nextButton.setAlignment(Pos.BOTTOM_CENTER);
        nextButton.setId("buttons");
        nextButton.setOnAction(e -> {
            if (endOf) {
                app.changeScene(); // if all words been used restart class
            } else {
                flashcardReveal.setText("");
                // Checking how many words are remaining and randomizing new word
                if (seenWords.size() == practiceListRemain.getEnglishDictionary().size()) {
                    flashCard.setText("NO WORD REMAINING");
                    endOf = true;
                    revealButton.setVisible(false);
                    nextButton.setText("Restart");
                } else {
                    do {
                        testWord.set(practiceListRemain.getRandomWord());
                    } while (seenWords.contains(testWord.get()));
                    seenWords.add(testWord.get());
                    if (dictType == 'E') flashCard.setText(testWord.get().getEnglish());
                    else if (dictType == 'W') flashCard.setText(testWord.get().getWelsh());
                }
            }
        });

        HBox nextWord = new HBox(20);
        nextWord.setAlignment(Pos.CENTER);
        nextWord.getChildren().addAll(nextButton);

        // Main display setup
        VBox mainDisplay = new VBox(20);
        mainDisplay.getChildren().addAll(infoBar, topBar, wordShow, showTranslation, nextWord);

        return mainDisplay;
    }
}