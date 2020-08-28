package uk.ac.aber.dcs.cs22120.group7.backend;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import uk.ac.aber.dcs.cs22120.group7.gui.*;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the main class in the application and holds the main logic that leads to what the user sees.
 * It connects the back-end code to the JavaFX GUI and launches the application.
 *
 * @author Ollie, Sergiusz, David
 * @version 2.3
 */
public class Main extends Application {

    private Dictionary dictionary;
    private PracticeList practiceList;

    // Keeps track of what window the GUI is on
    private int state;
    private Stage window;

    private double windowHeight; // Window Height resolution
    private double windowWidth; // Window Width resolution

    /**
     * The constructor initializes the state of the application and loads in the dictionary data from the Json files.
     */
    public Main() {
        state = 1;
        dictionary = new Dictionary();
        practiceList = new PracticeList();
        dictionary.load(loadJson("src/uk/ac/aber/dcs/cs22120/group7/resources/dictionary.json"));
        Word.setDictType('E');
        practiceList.load(loadJson("src/uk/ac/aber/dcs/cs22120/group7/resources/practiceList.json"));
        Word.setDictType('E');
        windowHeight = 700;
        windowWidth = 700;
    }

    /**
     * Set the state of the program (i.e. which screen the user is currently on).
     *
     * @param state becomes the new state of the program.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Read-only method to get the dictionary
     *
     * @return applications only dictionary list object
     */
    public Dictionary getDictionaryList() {
        return dictionary;
    }

    /**
     * Read-only method for to get the applications practice list
     *
     * @return applications only practice list object
     */
    public PracticeList getPracticeList() {
        return practiceList;
    }

    /**
     * This method loads the Json from the file into either the stored dictionary or practice list
     *
     * @param file to be loaded from.
     * @return the list of Words to be added to the dictionary / practice list
     */
    private ArrayList<Word> loadJson(String file) {
        ArrayList<Word> words = new ArrayList<>();
        try (Reader fr = new FileReader(file, StandardCharsets.UTF_8)) { // Open file
            Gson gson = new Gson();

            Word[] loadedWords = gson.fromJson(fr, Word[].class); // Load words from file into an array, using the Word class as a blueprint
            words.addAll(Arrays.asList(loadedWords));  // Add words to ArrayList as they are easier to work with

        } catch (IOException e) { // Catch file and other exceptions
            e.printStackTrace();
        }
        return words;
    }

    /**
     * This method saves the dictionary and practice list contents into separate json files
     */
    private void saveJson() {
        dictionary.save("src/uk/ac/aber/dcs/cs22120/group7/resources/dictionary.json");
        practiceList.save("src/uk/ac/aber/dcs/cs22120/group7/resources/practiceList.json");
    }

    /**
     * Adds word to both the dictionary and the practice list if it is not already added.
     *
     * @param word gets added to both dictionary / practice list
     */
    public void addWord(Word word) {
        char hold = Word.getDictType(); // Make sure the reordering of the dictionaries doesn't affect the current mode
        dictionary.addWord(word);
        practiceList.addWord(word);
        Word.setDictType(hold);
    }

    /**
     * Add word to practice list
     *
     * @param word to be added
     */
    public void addToPracticeList(Word word) {
        char hold = Word.getDictType(); // Make sure the reordering of the dictionaries doesn't affect the current mode
        practiceList.addWord(word);
        Word.setDictType(hold);
    }

    /**
     * Remove word from practice list
     *
     * @param word to be removed
     */
    public void removeFromPracticeList(Word word) {
        practiceList.removeWord(word);
    }

    /**
     * Checks if word is already in the dictionary
     */
    public boolean isWordRepeated(Word word) {
        if (dictionary.getEnglishDictionary().contains(word)) return true;
        else return false;
    }

    /**
     * Method for JavaFX code that creates a popup box to enter new word information.
     * It is called by the 'Add Word' button in DictionaryDisplay.
     * <p>
     * This method calls the addWord method to add information to the dictionary and practice list
     *
     * @param theme    char stating the current applications color scheme
     * @param fontSize int value stating the current applications text size
     */
    public void addWordBox(char theme, int fontSize) {
        //Initialize addWordBox stage
        Stage popup = new Stage();
        popup.setResizable(false);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(window.getScene().getWindow());

        //Two text fields to allow user to input an English/Welsh word
        TextField english = new TextField("English");
        TextField welsh = new TextField("Welsh");

        final ToggleGroup toggleGroup = new ToggleGroup();

        //Initializing selection options that assign a word type
        RadioButton masc = new RadioButton("Masc");
        masc.setToggleGroup(toggleGroup);

        RadioButton fem = new RadioButton("Fem");
        fem.setToggleGroup(toggleGroup);

        RadioButton other = new RadioButton("Other");
        other.setToggleGroup(toggleGroup);

        RadioButton verb = new RadioButton("Verb");
        verb.setToggleGroup(toggleGroup);

        //CSS and JavaFX layout styling for word type selection options
        masc.setId("text-color");
        fem.setId("text-color");
        other.setId("text-color");
        verb.setId("text-color");

        masc.setPadding(new Insets(10));
        fem.setPadding(new Insets(10));
        other.setPadding(new Insets(10));
        verb.setPadding(new Insets(10));

        //Initialize the button responsible for adding the inputted word to the dictionary
        Button add = new Button("+");
        add.setMinSize(30, 30);
        add.setPadding(new Insets(0));

        /*
         * Lambda expression below checks if the user has all the options filled out before the program adds the word.
         * It will ensure the user has selected a word type (masculine, feminine etc.)
         */
        add.setOnAction(e -> {
            Word newWord = null;

            //If the user hasn't inputted both English and Welsh text fields...
            if (english.getText().equals("") || welsh.getText().equals("") || english.getText().equals("English") || welsh.getText().equals("Welsh")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input missing");
                alert.setHeaderText("Translation missing");
                alert.setContentText("Please, make sure you input English and Welsh translation of the word");
                alert.showAndWait();

                //...if both text fields aren't empty check if a word type has been selected...
            } else {
                if (masc.isSelected()) {
                    newWord = new Word(english.getText(), welsh.getText(), "nm");
                } else if (fem.isSelected()) {
                    newWord = new Word(english.getText(), welsh.getText(), "nf");
                } else if (other.isSelected()) {
                    newWord = new Word(english.getText(), welsh.getText(), "other");
                } else if (verb.isSelected()) {
                    newWord = new Word(english.getText(), welsh.getText(), "verb");
                    //...if no word type has been selected, throw up an alert boxes informing the user...
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Input missing");
                    alert.setHeaderText("No word type selected");
                    alert.setContentText("Please, select the correct word type");
                    alert.showAndWait();
                }

                //...then check if the word already exists in the dictionary...
                if (newWord != null) {
                    //...if it does exist, inform the user and don't add it...
                    if (isWordRepeated(newWord)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Word repetition");
                        alert.setHeaderText("Word already in the dictionary");
                        alert.showAndWait();
                    } else if (newWord.getEnglish().length() > 60 && newWord.getWelsh().length() > 60) {
                        //...if the word is too long, inform the user and don't add it...
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Word input error");
                        alert.setHeaderText("Word too long");
                        alert.showAndWait();
                    } else {
                        //...if the word doesn't exist in the dictionary, add it
                        addWord(newWord);
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Success");
                        alert.setHeaderText("Word added successfully");
                        alert.showAndWait();
                        changeScene();
                        popup.close();
                    }
                }
            }
        });

        // Setup popup window with all components
        StackPane addPane = new StackPane();
        addPane.setPadding(new Insets(5, 10, 10, 10));
        addPane.getChildren().add(add);

        GridPane wordTypes = new GridPane();
        wordTypes.addColumn(0, masc, other);
        wordTypes.addColumn(1, fem, verb);
        wordTypes.setPadding(new Insets(0, 40, 20, 0));

        HBox boxesAndAdd = new HBox(10);
        boxesAndAdd.getChildren().addAll(wordTypes, addPane);
        boxesAndAdd.setId("top-theme");

        VBox wordAddition = new VBox(20);
        wordAddition.setPadding(new Insets(20));
        wordAddition.getChildren().addAll(english, welsh, boxesAndAdd);
        wordAddition.setId("background-theme");

        // Window configuration
        popup.setTitle("Add Word");
        Scene addWordScene = new Scene(wordAddition);
        windowStyling(addWordScene, theme, fontSize); //Call method that applies CSS styling to the addWordBox

        popup.setScene(addWordScene);
        popup.show();
    }

    /**
     * Changes what is displayed on the screen according to what state the program is currently in.
     * Keeps the default borders, but the centre pane is changed.
     * This is called whenever the screen needs to be updated.
     */
    public void changeScene() {

        // Sets maximum and minimum size of the window
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenBounds.getHeight();
        screenBounds.getWidth();

        window.setMinHeight(500);
        window.setMinWidth(500);
        window.setMaxWidth(screenBounds.getWidth());
        window.setMaxHeight(screenBounds.getHeight());

        // Gets user's machine Operating System and adjust window size
        String OS = System.getProperty("os.name").toLowerCase();

        //Separate resolution settings for Windows and Mac
        if (window.getHeight() >= 500 && window.getWidth() >= 500) {
            if (OS.contains("windows")) {
                windowWidth = window.getWidth() - 16;
                windowHeight = window.getHeight() - 39;
            } else {
                windowWidth = window.getWidth();
                windowHeight = window.getHeight() - 22;
            }
        }

        // Get default display and borders

        WindowTemplate template = new DictionaryDisplay(this, dictionary);
        BorderPane border = template.windowSetup();
        border.setId("background-theme");

        // Get the centre pane of the window depending on the state
        switch (state) {
            case 1: // Main / Dictionary display
                border.setCenter(template.centre(Word.getDictType()));
                break;
            case 2:// Practice list display
                template = new PracticeListDisplay(this, practiceList);
                border.setCenter(template.centre(Word.getDictType()));
                break;
            case 3: // Testing display
                template = new TestYourself(this, practiceList, dictionary);
                border.setCenter(template.centre(Word.getDictType()));
                break;
            case 4: // Flashcard display
                template = new FlashCards(this, practiceList);
                border.setCenter(template.centre(Word.getDictType()));
                break;
            case 5: // Settings display
                template = new Settings(this);
                border = template.windowSetup(); // Different to default borders
                border.setCenter(template.centre(Word.getDictType()));
                break;
        }

        // Window Configuration
        Scene scene = new Scene(border, windowWidth, windowHeight);

        //Call method that applies CSS styling to the application
        windowStyling(scene, template.getThemeStyle(), template.getFontSize());

        window.setScene(scene);
        window.show();
        window.setOnCloseRequest(event -> {
            saveJson();
            window.close();
        });
    }

    /**
     * This method is the link between the Cascading Style Sheet code withing the css package
     * and this application's JavaFX.
     * <p>
     * It will link either the light or dark mode style sheet depending on the
     * character variable "theme" which is dictated within the settings class.
     *
     * @param theme    Light or dark mode character to link the corresponding CSS file
     * @param fontSize Font Size integer to load correct text size
     */
    private void windowStyling(Scene scene, char theme, int fontSize) {
        if (theme == 'L') {
            //Load the light theme
            scene.getStylesheets().add("/uk/ac/aber/dcs/cs22120/group7/css/AppStyleSheetLight.css");
        } else if (theme == 'D') {
            //Load the dark theme
            scene.getStylesheets().add("/uk/ac/aber/dcs/cs22120/group7/css/AppStyleSheetDark.css");
        }

        //Adds a specific stylesheet that will change the font size of the application
        switch (fontSize) {
            case 12:
                scene.getStylesheets().add("/uk/ac/aber/dcs/cs22120/group7/css/font_12.css");
                break;
            case 14:
                scene.getStylesheets().add("/uk/ac/aber/dcs/cs22120/group7/css/font_14.css");
                break;
            case 16:
                scene.getStylesheets().add("/uk/ac/aber/dcs/cs22120/group7/css/font_16.css");
                break;
            case 18:
                scene.getStylesheets().add("/uk/ac/aber/dcs/cs22120/group7/css/font_18.css");
                break;
            default:
                break;
        }
    }

    /**
     * This starts up the main window at launch.
     *
     * @param stage becomes the main window.
     * @throws Exception when error with the JavaFX.
     */
    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setTitle("Welsh Learner Dictionary");
        changeScene();
    }

    /**
     * This main method is where the application is launched from.
     *
     * @param args the arguments from command line
     */
    public static void main(String[] args) {
        launch(args);
    }
}