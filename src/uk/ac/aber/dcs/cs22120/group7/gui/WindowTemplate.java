package uk.ac.aber.dcs.cs22120.group7.gui;

import javafx.scene.control.Alert;
import uk.ac.aber.dcs.cs22120.group7.backend.Main;
import uk.ac.aber.dcs.cs22120.group7.backend.Word;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * This class is the template for all the GUI classes. It contains methods that are necessary to building the display.
 * It is also able to control the main class, by changing the state.
 *
 * @author Ollie, David, Filip
 * @version 1.3
 */
public abstract class WindowTemplate {
    static char themeStyle = 'L';
    static int fontSize = 11;

    Main app;

    /**
     * This constructor initializes the connection to the Main class.
     *
     * @param backEndConnection is the main application
     */
    public WindowTemplate(Main backEndConnection) {
        app = backEndConnection;
    }

    /**
     * This method is a getter for changing the themes of the application
     *
     * @return single character value 'L' for Light theme and 'D' for Dark theme
     */
    public char getThemeStyle() {
        return themeStyle;
    }

    /**
     * This method is a setter for changing the themes of the application
     *
     * @param theme single character value, typically only 'L' for Light theme and 'D' for Dark theme
     */
    public void setTheme(char theme) {
        WindowTemplate.themeStyle = theme;
    }

    /**
     * This method is a getter for changing the size of text in the application
     *
     * @return integer value stating what text size the system is currently on or will be changing to
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * This method is a setter for changing changing the size of text in the application
     *
     * @param fontSize integer values that will change the text size of the application
     */
    public void setFontSize(int fontSize) {
        WindowTemplate.fontSize = fontSize;
    }

    /**
     * This method sets up the default window display (i.e. the top and bottom bars).
     * It applies JavaFX Layouts and CSS styling
     *
     * @return the default setup
     */
    public BorderPane windowSetup() {

        Pane top = topTemplate();
        Pane bottom = bottomTemplate();

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(0, 0, 0, 0));
        layout.setBottom(bottom);
        layout.setTop(top);

        return layout;
    }

    /**
     * This method enables the bar at the top to be changed in the different screens of the application
     *
     * @return the top bar for the default screen.
     */
    Pane topTemplate() {

        //Top right Home page button
        Button dictionaryButton = new Button("Dictionary");
        dictionaryButton.setPrefHeight(30);
        dictionaryButton.setPadding(new Insets(80, 0, 0, 0));
        dictionaryButton.setId("button-dictionary");
        dictionaryButton.setOnAction(e -> {
            app.setState(1);
            app.changeScene();
        });

        //Top right Settings button
        Button settings = new Button("Settings");
        settings.setPrefHeight(30);
        settings.setPadding(new Insets(80, 10, 0, 10));
        settings.setId("button-settings");
        settings.setOnAction(e -> {
            app.setState(5);
            app.changeScene();
        });

        //Enforces a layout and CSS styling to the top bar of the application
        HBox top = new HBox(20);
        top.setAlignment(Pos.BASELINE_RIGHT);
        top.setPrefHeight(65);
        top.setPadding(new Insets(5, 5, 5, 5));
        top.getChildren().addAll(dictionaryButton, settings);
        top.setId("top-theme");

        return top;
    }

    /**
     * This method enables the bar at the bottom to be changed in the different screens of the application
     *
     * @return the bottom bar for the default screen.
     */
    Pane bottomTemplate() {

        //Changes the system to translate English to Welsh
        Button english = new Button("English");

        //CSS styling and JavaFX layout
        english.setPadding(new Insets(0, 0, 60, 0));
        english.setId("button-english-to-welsh");
        english.setPrefSize(200, 80);
        english.setMaxSize(400, 150);
        english.setOnAction(e -> {
            Word.setDictType('E');
            app.changeScene();
        });

        //Changes the system to translate Welsh to English
        Button welsh = new Button("Welsh");
        //CSS styling and JavaFX layout
        welsh.setPadding(new Insets(0, 0, 60, 0));
        welsh.setId("button-welsh-to-english");
        welsh.setPrefSize(200, 80);
        welsh.setMaxSize(400, 150);
        welsh.setOnAction(e -> {
            Word.setDictType('W');
            app.changeScene();
        });

        //Changes the application screen to show the Practice list window
        Button practice_list = new Button("Practice List");
        //CSS styling and JavaFX layout
        practice_list.setPadding(new Insets(0, 0, 0, 0));
        practice_list.setWrapText(true);
        practice_list.setId("button-empty-box");
        practice_list.setPrefSize(200, 80);
        practice_list.setMaxSize(400, 150);
        practice_list.setOnAction(e -> {
            app.setState(2);
            app.changeScene();
        });

        //Changes the application screen to show the the test window
        Button tests = new Button("Tests");
        //CSS styling and JavaFX layout
        tests.setPadding(new Insets(0, 0, 0, 0));
        tests.setId("button-empty-box");
        tests.setPrefSize(200, 80);
        tests.setMaxSize(400, 150);
        tests.setOnAction(e -> {
            TestYourself.state = 1;
            app.setState(3);
            app.changeScene();
        });

        // Flashcards error popup: if there aren't enough words in practice list to run flashcards
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Practice list too empty");
        alert.setHeaderText("Not enough words in practice list");

        //Changes the application screen to show the the flashcards window
        Button flashcards = new Button("Flashcards");
        //CSS styling and JavaFX layout
        flashcards.setPadding(new Insets(0, 0, 0, 0));
        flashcards.setWrapText(true);
        flashcards.setId("button-empty-box");
        flashcards.setPrefSize(200, 80);
        flashcards.setMaxSize(400, 150);
        flashcards.setOnAction(e -> {
            if (app.getPracticeList().getEnglishDictionary().size() < 1) {
                alert.setContentText("Please, make sure you add at least 1 word to the practice list");
                alert.showAndWait();
            } else {
                app.setState(4);
                app.changeScene();
            }
        });

        HBox bottom = new HBox(20);
        bottom.setAlignment(Pos.BASELINE_CENTER);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        bottom.getChildren().addAll(english, welsh, practice_list, tests, flashcards);

        return bottom;
    }

    /**
     * Subclasses of WindowTemplate must have this method. It is necessary as it contains the main body of the display.
     *
     * @param dictType the dictionary mode
     * @return the centre display for the screen
     */
    public abstract Pane centre(char dictType);

}