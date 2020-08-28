package uk.ac.aber.dcs.cs22120.group7.gui;

import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import uk.ac.aber.dcs.cs22120.group7.backend.Dictionary;
import uk.ac.aber.dcs.cs22120.group7.backend.Main;
import uk.ac.aber.dcs.cs22120.group7.backend.PracticeList;
import uk.ac.aber.dcs.cs22120.group7.backend.Word;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is the class that displays and runs the the three types of User Tests.
 * They are:- multiple choice, typing and matching.
 *
 * @author Ollie, Dominik, Ferenc, David, Sergiusz
 * @version 2.1
 */
public class TestYourself extends WindowTemplate {

    static int state = 1;
    private static float totalUserScore = 0; //Used in random test to keep the running score
    private static int totalNumberOfTests = 1;
    private int matchNumber = 1; // Used in userTestThree to count current number of matches the user is on
    private String answer = ""; // Used in userTestOne to find the correct answer's button
    private static boolean isRandom = false;
    private final Random rand = new Random();
    private static final ArrayList<Word> seenWords = new ArrayList<>();


    PracticeList practiceList;
    Dictionary dictionary;

    /**
     * The constructor takes in the main application calls the super constructor.
     *
     * @param testYourself is the application
     */
    public TestYourself(Main testYourself, PracticeList practiceList, Dictionary dictionary) {
        super(testYourself);
        this.practiceList = practiceList;
        this.dictionary = dictionary;
    }

    /**
     * This method implements and returns the centre display required by WindowTemplate.
     * Could use state system to run the individual tests in this class
     *
     * @param dictType the mode of the dictionary
     * @return the centre display
     */
    @Override
    public Pane centre(char dictType) {
        return switch (state) { // Menu
            case 1 -> menu(dictType); // test1
            case 2 -> userTestOne(dictType); // test2
            case 3 -> userTestTwo(dictType); // test3
            case 4 -> userTestThree(); // random tests
            case 5 -> randomTests();
            case 6 -> feedback();
            default -> new Pane();
        };
    }

    /**
     * Creates a menu containing both versions of dictionary, practice list, tests and flashcards
     *
     * @param dictType Type of dictionary
     * @return the display with correct content.
     */
    private Pane menu(char dictType) {
        Label label = new Label();
        label.setId("text-color");

        //Informs the user if there are not enough words in practice list to do a test
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Practice list too empty");
        alert.setHeaderText("Not enough words in practice list");

        //Loads userTestOne and displays it on the window
        Button test1 = new Button("Multiple Choice");

        //CSS and JavaFX layout styling
        test1.setPrefSize(300, 500);
        test1.setMaxSize(700, 300);
        test1.setPadding(new Insets(0, 0, 100, 0));
        test1.setId("button-multiple-choice");
        test1.setOnAction(e -> {
            //If the practice list is empty, don't change window and inform the user...
            if (practiceList.getEnglishDictionary().size() < 1) {
                alert.setContentText("Please, make sure you add at least 1 word to the practice list");
                alert.showAndWait();
                //... or load userTestOne's window
            } else {
                seenWords.removeAll(seenWords);
                isRandom = false;
                totalUserScore = 0;
                state = 2;
                app.changeScene();
            }
        });

        //Loads userTestTwo and displays it on the window
        Button test2 = new Button("Typing Translation");

        //CSS and JavaFX layout styling
        test2.setPrefSize(300, 500);
        test2.setMaxSize(700, 300);
        test2.setPadding(new Insets(0, 0, 100, 0));
        test2.setId("button-typing-translation");

        test2.setMinSize(100, 100);
        test2.setOnAction(e -> {
            //If the practice list is empty, don't change window and inform the user...
            if (practiceList.getEnglishDictionary().size() < 1) {
                alert.setContentText("Please, make sure you add at least 1 word to the practice list");
                alert.showAndWait();
                //... or load userTestTwo's window
            } else {
                seenWords.removeAll(seenWords);
                isRandom = false;
                totalUserScore = 0;
                state = 3;
                app.changeScene();
            }
        });

        //Loads userTestThree and displays it on the window
        Button test3 = new Button("Matching Words");

        //CSS and JavaFX layout styling
        test3.setPrefSize(300, 500);
        test3.setMaxSize(700, 300);
        test3.setPadding(new Insets(0, 0, 100, 0));
        test3.setId("button-matching-words");

        test3.setMinSize(100, 100);
        test3.setOnAction(e -> {
            //If the practice list has lest than four words, don't change window and inform the user...
            if (practiceList.getEnglishDictionary().size() < 4) {
                // Throw error message in popup
                alert.setContentText("Please, make sure you add at least 4 word to the practice list");
                alert.showAndWait();
                //... or load userTestThree's window
            } else {
                isRandom = false;
                totalUserScore = 0;
                state = 4;
                app.changeScene();
            }
        });

        //Load the window responsible for FR10, selecting random tests and giving user feedback
        Button test4 = new Button("Random Tests");

        //CSS and JavaFX layout styling
        test4.setPrefSize(300, 500);
        test4.setMaxSize(700, 300);
        test4.setId("buttons");

        test4.setOnAction(e -> {
            //If the practice list has lest than four words, don't change window and inform the user...
            if (practiceList.getEnglishDictionary().size() < 4) {
                // Throw error message in popup
                alert.setContentText("Please, make sure you add at least 4 word to the practice list");
                alert.showAndWait();
                //... or load userTestThree's window
            } else {
                isRandom = true;
                totalUserScore = 0;
                state = 5;
                app.changeScene();
            }
        });

        //Add all test navigation buttons together for display

        HBox options = new HBox(20);
        options.setAlignment(Pos.BASELINE_CENTER);
        options.setPadding(new Insets(50, 0, 10, 0));
        options.getChildren().addAll(test1, test2, test3, test4);

        //Text stating which language is being translated
        switch (dictType) {
            case 'E' -> label.setText("English Tests");
            case 'W' -> label.setText("Welsh Tests");
        }

        //Add all components of the test menu to a layout and display it
        VBox test = new VBox(20);
        test.setPadding(new Insets(10, 10, 10, 10));
        test.getChildren().addAll(label, options);
        return test;
    }

    /**
     * Test that provides six options with only one being correct.
     *
     * @param dictType Type of dictionary
     * @return the display with correct content.
     */
    private Pane userTestOne(char dictType) {

        //Creates a skip button including CSS styling.
        Button skipButton = new Button("Skip");
        skipButton.setId("buttons");

        //Creates a informative label with JavaFX and CSS included.
        Label test1Info = new Label("Select The Translation");
        test1Info.setAlignment(Pos.TOP_CENTER);
        test1Info.setId("text-color");

        Label currentQuestion = new Label();
        currentQuestion.setId("text-color");
        //If userTestOne is running as a part of random test, totalUserScore/5 keeps current score.
        if (isRandom)
            currentQuestion.setText("Question number " + totalNumberOfTests + "        " + "Current Score: " + totalUserScore + "/5");
        //Horizontal box for the Number of current question
        VBox informationBox = new VBox(20);
        informationBox.setAlignment(Pos.CENTER);
        informationBox.getChildren().addAll(currentQuestion, test1Info);

        //makes sure that words are unique(non-repeating)
        Word randomTestWord = practiceList.getRandomWord();
        if (seenWords.size() < practiceList.getEnglishDictionary().size()) {
            while (seenWords.contains(randomTestWord)) {
                randomTestWord = practiceList.getRandomWord();
            }
        }

        seenWords.add(randomTestWord);
        AtomicReference<Word> testWord = new AtomicReference<>(randomTestWord);
        //Creates an array that hold all incorrect answers.
        AtomicReference<Word>[] answers = new AtomicReference[6];
        for (int i = 0; i < 5; i++) {
            answers[i] = new AtomicReference<>();
            answers[i].set(dictionary.getRandomWord());
        }
        answers[5] = new AtomicReference<>();
        answers[5].set(randomTestWord);

        Label word = new Label();
        word.setId("text-color");
        word.setPadding(new Insets(30, 20, 20, 20));
        word.setText(testWord.toString());

        //Feedback label with horizontal box and CSS
        Label feedback = new Label();
        feedback.setId("text-color");

        HBox buttonsBox = new HBox();
        HBox wordBox = new HBox(20);
        wordBox.setAlignment(Pos.CENTER);
        wordBox.getChildren().add(word);

        //Array of 6 buttons (options)
        Button[] buttons = new Button[6];
        for (int button = 0; button < 6; button++) {
            buttons[button] = new Button();
            buttons[button].setId("buttons");
            buttons[button].setPrefSize(200, 300);
            buttons[button].setMaxSize(500, 660);
        }

        // Needed to find the correct answer in the other language
        if (dictType == 'E') answer = randomTestWord.getWelsh();
        if (dictType == 'W') answer = randomTestWord.getEnglish();

        for (int button = 0; button < 6; button++) {
            int finalButton = button;
            buttons[button].setOnAction(e -> {
                //If the user selects the correct translation
                if (answers[finalButton].get().isTranslation(testWord.get().getWelsh())) {
                    buttons[finalButton].setId("button-selected");
                    feedback.setText("correct");
                    TestYourself.totalUserScore++;
                    state = 2;
                    //... otherwise display that it was incorrect
                } else {
                    feedback.setText("Incorrect, correct answer: " + answer);
                }

                //Disables all buttons
                buttons[0].setDisable(true);
                buttons[1].setDisable(true);
                buttons[2].setDisable(true);
                buttons[3].setDisable(true);
                buttons[4].setDisable(true);
                buttons[5].setDisable(true);

                correctAnswer(buttons); //Finds button with correct answer
                skipButton.setText("Next");
            });
        }
        //if the test is part of the random test, it changes the scene back to random test otherwise it continues with endless test 1
        skipButton.setOnAction(e -> {
            if (isRandom) {
                if (TestYourself.totalNumberOfTests == 5) {
                    state = 6;
                } else {
                    state = rand.nextInt(3) + 2;
                    totalNumberOfTests++;
                    if (seenWords.size() == practiceList.getEnglishDictionary().size()) seenWords.removeAll(seenWords);
                }
            } else {
                state = 2;
            }
            app.changeScene();
        });

        //Shuffles the buttons
        ArrayList<Button> shuffledButtons = new ArrayList<>(Arrays.asList(buttons));
        Collections.shuffle(shuffledButtons);
        //takes a different translation based on the language of the test word.
        if (dictType == 'W') {
            word.setText(testWord.get().getWelsh());
            for (int i = 0; i < 5; i++) {
                buttons[i].setText(answers[i].get().getEnglish());
            }
            buttons[5].setText(testWord.get().getEnglish());
        } else {
            word.setText(testWord.get().getEnglish());
            for (int i = 0; i < 5; i++) {
                buttons[i].setText(answers[i].get().getWelsh());
            }
            buttons[5].setText(testWord.get().getWelsh());
        }
        //feedback button modification
        Pane FeedbackSpacerL = new Pane();
        Pane FeedbackSpacerR = new Pane();
        HBox.setHgrow(FeedbackSpacerL, Priority.ALWAYS);
        HBox.setHgrow(FeedbackSpacerR, Priority.ALWAYS);

        HBox feedbackBox = new HBox(20);
        feedbackBox.getChildren().addAll(FeedbackSpacerL, feedback, FeedbackSpacerR);

        //skip button button modification
        Pane skipButtonSpacer = new Pane();
        HBox.setHgrow(skipButtonSpacer, Priority.ALWAYS);

        HBox skipButtonBox = new HBox(20);
        skipButtonBox.getChildren().addAll(skipButtonSpacer, skipButton);
        skipButtonBox.setPadding(new Insets(20));

        HBox target = new HBox();
        target.setAlignment(Pos.CENTER);
        target.getChildren().add(word);

        for (Button i : shuffledButtons) {
            buttonsBox.getChildren().add(i);
        }
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsBox.setSpacing(20);

        Label noWords = new Label("No more words in the practice list.");
        noWords.setId("text-color");
        noWords.setAlignment(Pos.CENTER);

        VBox testPage = new VBox(20);

        //makes sure that all remaining words in the practice list are unique
        if (seenWords.size() <= practiceList.getEnglishDictionary().size()) {
            if (isRandom) testPage.getChildren().add(currentQuestion);
            testPage.getChildren().addAll(informationBox, target, buttonsBox, skipButtonBox, feedbackBox);
        } else {
            if (!isRandom) {
                skipButton.setText("Reset");
                skipButtonBox.getChildren().remove(skipButtonSpacer);
                skipButtonBox.setAlignment(Pos.CENTER);
                testPage.getChildren().addAll(noWords, skipButtonBox);
            }
            seenWords.removeAll(seenWords);
        }

        testPage.setAlignment(Pos.CENTER);
        return testPage;
    }

    /**
     * Test that provides a word in english and requires user to type in the correct translation
     * in welsh or vice-versa.
     *
     * @param dictType Type of dictionary
     * @return the display with correct content.
     */
    private Pane userTestTwo(char dictType) {
        // Initialise page and information text
        VBox testPage = new VBox(20);
        testPage.setAlignment(Pos.CENTER);
        Label test2Info = new Label("Translate The Word");
        test2Info.setAlignment(Pos.TOP_CENTER);
        test2Info.setId("text-color");

        HBox informationBox = new HBox(20);
        informationBox.setAlignment(Pos.CENTER);
        informationBox.getChildren().addAll(test2Info);

        // Get a random test word that hasn't been seen before
        Word randomTestWord = practiceList.getRandomWord();
        if (seenWords.size() < practiceList.getEnglishDictionary().size()) {
            while (seenWords.contains(randomTestWord)) {
                randomTestWord = practiceList.getRandomWord();
            }
        }
        seenWords.add(randomTestWord);

        // Label for the test word and positioning spacers
        Label word = new Label();
        word.setId("text-color");
        word.setPadding(new Insets(30, 20, 20, 20));
        Pane wordSpacerL = new Pane();
        Pane wordSpacerR = new Pane();
        HBox.setHgrow(wordSpacerL, Priority.ALWAYS);
        HBox.setHgrow(wordSpacerR, Priority.ALWAYS);
        HBox wordBox = new HBox(20);
        wordBox.getChildren().addAll(wordSpacerL, word, wordSpacerR);

        // Enabling the word to be seen in the lambdas and setting the text according to the test settings
        AtomicReference<Word> testWord = new AtomicReference<>(randomTestWord);
        if (dictType == 'E') word.setText(testWord.get().getEnglish());
        else if (dictType == 'W') word.setText(testWord.get().getWelsh());

        // Labels for displaying feedback and the correct answer if the user gets it wrong
        Label feedback = new Label();
        feedback.setId("text-color");
        Label correctAnswer = new Label();
        correctAnswer.setId("text-color");
        Pane inputSpacerL = new Pane();
        Pane inputSpacerR = new Pane();
        HBox.setHgrow(inputSpacerL, Priority.ALWAYS);
        HBox.setHgrow(inputSpacerR, Priority.ALWAYS);

        // Creating the skip/next button
        Button skipButton = new Button("Skip");
        skipButton.setId("buttons");

        // Creating the input for the test
        TextField input = new TextField();
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {  // When the word is submitted
            String inputtedWord = input.getText();
            if (testWord.get().isTranslation(inputtedWord)) { // If it is correct
                feedback.setText("Correct");
                TestYourself.totalUserScore++;
            } else {   // If it is incorrect display the correct translation according to the language setting
                feedback.setText("Incorrect");
                if (dictType == 'E')
                    correctAnswer.setText("            Correct answer is " + testWord.get().getWelsh());
                else if (dictType == 'W')
                    correctAnswer.setText("           Correct answer is " + testWord.get().getEnglish());
            }
            submit.setDisable(true); // Stop them trying again this go
            skipButton.setText("Next");
        });

        skipButton.setOnAction(e -> { // If the skip button is pressed
            if (isRandom) { // If in random tests
                if (TestYourself.totalNumberOfTests == 5) { // If random tests is finished display the feedback screen
                    state = 6;
                } else {  // Else get a random test
                    state = rand.nextInt(3) + 2;
                    totalNumberOfTests++;
                    // Automatically reset the seen words so that the random words don't crash
                    if (seenWords.size() == practiceList.getEnglishDictionary().size()) seenWords.removeAll(seenWords);
                }
            } else { // If not random tests, set up a new test
                state = 3;
            }
            app.changeScene();
        });

        // Setup the boxes for displaying the input options, feedback and skip/next button
        HBox inputBox = new HBox(20);
        inputBox.setPadding(new Insets(30, 0, 30, 0));
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setId("top-theme");
        inputBox.getChildren().addAll(inputSpacerL, input, submit, inputSpacerR);

        Pane feedbackSpacerL = new Pane();
        Pane feedbackSpacerR = new Pane();
        HBox.setHgrow(feedbackSpacerL, Priority.ALWAYS);
        HBox.setHgrow(feedbackSpacerR, Priority.ALWAYS);
        HBox feedbackBox = new HBox(20);
        feedbackBox.getChildren().addAll(feedbackSpacerL, feedback, correctAnswer, feedbackSpacerR);

        Pane skipButtonSpacer = new Pane();
        HBox.setHgrow(skipButtonSpacer, Priority.ALWAYS);
        HBox skipButtonBox = new HBox(20);
        skipButtonBox.getChildren().addAll(skipButtonSpacer, skipButton);
        skipButtonBox.setPadding(new Insets(20));

        // If seen all the words in the practice list, ask if the ser wants to restart
        Label noWords = new Label("No more words in the practice list.");
        noWords.setId("text-color");
        noWords.setAlignment(Pos.CENTER);

        // For random tests - display the current question number and score
        Label currentQuestion = new Label();
        currentQuestion.setId("text-color");
        currentQuestion.setText("Question number " + totalNumberOfTests + "        " + "Current Score: " + totalUserScore + "/5");

        if (seenWords.size() <= practiceList.getEnglishDictionary().size()) {
            if (isRandom) testPage.getChildren().add(currentQuestion); // If random tests, add the progress display
            testPage.getChildren().addAll(informationBox, wordBox, inputBox, feedbackBox, skipButtonBox);
        } else {
            if (!isRandom) { // If in random tests, automatically remove the seen words, otherwise ask if the user wants to reset
                skipButton.setText("Reset");
                skipButtonBox.getChildren().remove(skipButtonSpacer);
                skipButtonBox.setAlignment(Pos.CENTER);
                testPage.getChildren().addAll(noWords, skipButtonBox);
            }
            seenWords.removeAll(seenWords);
        }
        return testPage;
    }

    /**
     * Test that provides 4 pairs of words with randomized locations. User needs to match the correct pairs.
     *
     * @return the display with correct content.
     */
    private Pane userTestThree() {
        matchNumber = 1; // Keep track of how many correct matches have taken place in the test

        VBox testPage = new VBox(20); // Initialise the Pane for the display

        // Setup the Label for the random test feedback
        Label currentQuestion = new Label();
        currentQuestion.setAlignment(Pos.TOP_CENTER);
        currentQuestion.setId("text-color");
        if (isRandom)
            currentQuestion.setText("Question number " + totalNumberOfTests + "        " + "Current Score: " + totalUserScore + "/5");

        // Display instructions of how to use the test to the user
        Label instructionLabel = new Label("Select Translated Matching Pairs");
        instructionLabel.setAlignment(Pos.CENTER);
        instructionLabel.setId("text-color");

        VBox informationBox = new VBox(20); // Group the two labels together vertically
        informationBox.setAlignment(Pos.TOP_CENTER);
        informationBox.getChildren().addAll(currentQuestion, instructionLabel);

        // Initialise the arrays to hold the words and the buttons
        ArrayList<Word> words = new ArrayList<>();
        Button[] buttons = new Button[8];

        Word testWord;
        for (int button = 0; button < 4; button++) {
            do { // Get 4 random unique words to display on the buttons
                testWord = practiceList.getRandomWord();
            } while (words.contains(testWord));
            words.add(testWord);

            /* Initialise each button with: an ID, an Object/associated word, a pref and max size, and the text to display
             * +4 added to set 2 buttons in the same loop, the first with the English and the second with the Welsh
             * */
            buttons[button] = new Button();
            buttons[button + 4] = new Button();
            buttons[button].setId("buttons");
            buttons[button + 4].setId("buttons");
            buttons[button].setUserData(words.get(button));
            buttons[button + 4].setUserData(words.get(button));
            buttons[button].setText(words.get(button).getEnglish());
            buttons[button + 4].setText(words.get(button).getWelsh());
            buttons[button].setPrefSize(160, 100);
            buttons[button + 4].setPrefSize(160, 100);
            buttons[button].setMaxSize(360, 300);
            buttons[button + 4].setMaxSize(360, 300);
        }

        // Randomise the position of the buttons within the array so they are displayed randomly
        Collections.shuffle(Arrays.asList(buttons));

        // Setup the Pane to group the 8 buttons in a grid
        GridPane buttonRows = new GridPane();
        buttonRows.setHgap(20);
        buttonRows.setVgap(20);
        buttonRows.setPadding(new Insets(20, 20, 20, 0));
        buttonRows.setId("background-userTestThree");
        buttonRows.setAlignment(Pos.CENTER);

        for (int index = 0; index < 8; index++) { // Add the buttons to the GridPane
            if (index < 4) { // The first 4 are set to the top row
                buttonRows.add(buttons[index], index + 1, 0);
            } else { // The next 4 are set to the bottom row
                buttonRows.add(buttons[index], (index - 3), 1);
            }
        }

        // Create skip/next button and set it's position on the page
        Button skipButton = new Button("Skip");
        skipButton.setId("buttons");
        skipButton.setOnAction(e -> {
            if (isRandom) { // If during a random test
                if (TestYourself.totalNumberOfTests == 5) { // If the random tests are finished, show the feedback screen
                    state = 6;
                } else { // Else get a random test using the states
                    state = this.rand.nextInt(3) + 2;
                    totalNumberOfTests++;
                }
            } else { // Else get a new matching word problem
                state = 4;
            }
            app.changeScene(); // Update the scene
        });

        HBox skipButtonBox = new HBox(); // For positioning the skip button and setting the padding
        skipButtonBox.setPadding(new Insets(20));
        skipButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
        skipButtonBox.getChildren().add(skipButton);

        // Setup variables to enable the comparison of buttons and enable the feedback of incorrect words
        ArrayList<Word> incorrectWords = new ArrayList<>();
        AtomicReference<Word> wordOne = new AtomicReference<>(new Word("", "", ""));
        AtomicReference<Word> wordTwo = new AtomicReference<>(new Word("", "", ""));
        int[] indexes = new int[2]; // To hold the indexes of the buttons to match

        VBox feedback = new VBox();
        feedback.setAlignment(Pos.BOTTOM_CENTER);

        // Loop through buttons checking for click events
        for (int index = 0; index < 8; index++) {
            int finalIndex = index;
            buttons[index].setOnAction(e -> {
                // Check for matches in the buttons clicked
                matchButtonResult(wordOne, wordTwo, instructionLabel, buttons, finalIndex, indexes, skipButton, incorrectWords, feedback);
            });
        }
        // Add the elements and return the page to be displayed
        testPage.getChildren().addAll(informationBox, buttonRows, skipButtonBox, feedback);
        return testPage;
    }


    /**
     * This method is called when a button is clicked.
     * If it is the first button, it stores the data and index of the button.
     * If it is the second button pressed, it checks the data from that button against the first.
     *
     * @param wordOne is the variable to store the data from button one.
     * @param wordTwo is the variable to store the data from button two.
     * @param instructionLabel is passed in to give feedback ("correct / incorrect") to the user.
     * @param buttons is the array of 8 buttons, and their data.
     * @param index is the index of the button clicked.
     * @param indexes is the stored index of the two buttons.
     * @param skipButton is passed so that the text can be changed to next when complete.
     * @param incorrectWords is the ArrayList of words that the user gets wrong.
     * @param feedback Is the label for the correct translations of the words once the user finishes.
     */
    private void matchButtonResult(AtomicReference<Word> wordOne, AtomicReference<Word> wordTwo, Label instructionLabel,
                                   Button[] buttons, int index, int[] indexes, Button skipButton, ArrayList<Word> incorrectWords, VBox feedback) {
        if (!wordOne.get().equals(new Word("", "" , ""))) { // If there is data from button one, it is the second click
            indexes[1] = index; // set index and data of button 2
            wordTwo.set((Word)buttons[indexes[1]].getUserData());
            if (wordOne.get().isTranslation(wordTwo.get().getWelsh())) { // Compare the data from both buttons
                instructionLabel.setText("Correct");
                totalUserScore += 0.25; // Increment the score 1/4 for each match
            } else { // If it's in correct, add both words to the incorrect words list
                instructionLabel.setText("Incorrect");
                if (!incorrectWords.contains(wordOne.get())) incorrectWords.add(wordOne.get());
                if (!incorrectWords.contains(wordTwo.get())) incorrectWords.add(wordTwo.get());
            }

            linkButtonColor(buttons[indexes[0]], buttons[indexes[1]]); // Connect the colours for the two buttons
            if (matchNumber == 5){ // If all buttons are matched display the feedback
                if (incorrectWords.size() != 0) { // If there are some incorrect words display the correct translations
                    StringBuilder correctTranslationsText = new StringBuilder();
                    for (Word incorrectWord : incorrectWords) {
                        correctTranslationsText.append(incorrectWord.getEnglish()).append(" - ").append(incorrectWord.getWelsh()).append("      ");
                    }
                    Label correctTranslationsTitle = new Label("Correct translations:");
                    Label correctTranslations = new Label(correctTranslationsText.toString());
                    correctTranslations.setId("text-color");
                    correctTranslationsTitle.setId("text-color");
                    feedback.getChildren().addAll(correctTranslationsTitle, correctTranslations); // Add the translations to the screen
                }
                skipButton.setText("Next");
                matchNumber = 0; // Reset for the next question
            }
            wordOne.set(new Word("", "", ""));
        } else { // If first click of button, set the index and data from button and disable the button
            indexes[0] = index;
            wordOne.set((Word)buttons[indexes[0]].getUserData()); // Get data from button
            buttons[index].setDisable(true);
        }
    }


    /**
     * Finds the correct answer in userTestOne and Highlights it's button
     *
     * @param buttons takes in an array that has all buttons of userTestOne
     */
    private void correctAnswer(Button[] buttons) {
        for (Button correctButton : buttons) {
            // If the answer is the same as the buttons text, correct button is found
            if (correctButton.getText().equals(answer)) {
                correctButton.setId("button-selected"); //Highlight the button with a red border
            }
        }
    }

    /**
     * Sets the two selected buttons in userTestThree and assigns them both matching colours
     * and then disables the buttons.
     *
     * @param buttonOne First selected button
     * @param buttonTwo Second selected button
     */
    private void linkButtonColor(Button buttonOne, Button buttonTwo) {

        String cssColorLink = switch (matchNumber) {

            case 1 -> "matchbutton-one"; // First word match

            case 2 -> "matchbutton-two"; // Second word match

            case 3 -> "matchbutton-three"; // Third word match

            case 4 -> "matchbutton-four"; // Four word match
            default -> "";
        };

        // Disable and link update the css link to second matched button
        buttonOne.setDisable(true);
        buttonOne.setId(cssColorLink);
        buttonTwo.setDisable(true);
        buttonTwo.setId(cssColorLink);

        matchNumber++; //Increment the current match number the user is on
    }

    /**
     * User is probided with a sequence of 5 randomly chosen tests (1 through 3), and is given feedback
     * after each test; including his/her/their score.
     *
     * @return the display with correct content.
     */
    private Pane randomTests() {
        isRandom = true;
        seenWords.removeAll(seenWords); // Reset the seen words
        HBox testPage = new HBox(20);
        testPage.setPadding(new Insets(10, 10, 10, 10));

        // Setup the start button for the random tests
        Button startButton = new Button("Start");
        startButton.setAlignment(Pos.CENTER);
        startButton.setId("buttons");
        startButton.setOnAction(e -> { // When click, pick a random test
            totalNumberOfTests = 1;
            state = rand.nextInt(3) + 2;
            app.changeScene();
        });
        testPage.getChildren().addAll(startButton);
        testPage.setAlignment(Pos.CENTER);
        return testPage;
    }


    /**
     * Provides the user with feedback.
     *
     * @return the display with correct content.
     */
    private Pane feedback() {
        // Setup the feedback page for the random tests
        HBox testPage = new HBox(20);
        Label feedback = new Label();
        feedback.setId("text-color");
        feedback.setFont(new Font(22));
        feedback.setText("Your score is " + totalUserScore + "/" + totalNumberOfTests); // Display the score
        testPage.getChildren().add(feedback);
        testPage.setAlignment(Pos.CENTER);
        return testPage;
    }
}
