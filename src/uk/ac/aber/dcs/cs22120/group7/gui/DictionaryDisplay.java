package uk.ac.aber.dcs.cs22120.group7.gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import uk.ac.aber.dcs.cs22120.group7.backend.Dictionary;
import uk.ac.aber.dcs.cs22120.group7.backend.Main;
import uk.ac.aber.dcs.cs22120.group7.backend.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This is the class that displays and allows edits to the Main Dictionary.
 *
 * @author Ollie, David
 * @version 1.2
 */
public class DictionaryDisplay extends WindowTemplate {

    //Two lists used to display the dictionary in both languages
    private ObservableList<Word> englishData;
    private ObservableList<Word> welshData;

    //Search string used to find words in both dictionaries
    private static String search;

    /**
     * The constructor takes in the main application, calls the super constructor and initializes the variables.
     *
     * @param dictionaryDisplay is the application
     */
    public DictionaryDisplay(Main dictionaryDisplay, Dictionary dictionary) {
        super(dictionaryDisplay);
        englishData = FXCollections.observableArrayList();
        welshData = FXCollections.observableArrayList();
        englishData.addAll(dictionary.getEnglishDictionary());
        welshData.addAll(dictionary.getWelshDictionary());
    }

    /**
     * This method allows the Label above the dictionary to be changed.
     *
     * @param dictType is the mode of the dictionary
     * @return the string label
     */
    String setLabel(char dictType) {
        String label = "";
        if (dictType == 'E') label = "English Dictionary";
        else if (dictType == 'W') label = "Welsh Dictionary";
        return label;
    }

    /**
     * This method implements and returns the centre display required by WindowTemplate
     *
     * @param dictType the mode of the dictionary
     * @return the centre display
     */
    @Override
    public Pane centre(char dictType) {
        Label label = new Label();
        label.setId("text-color");

        TextField searchBar = new TextField(search);

        //Button that allows that loads the addWordBox for users to create a new word
        Button addWord = new Button("Add Word");

        addWord.setId("buttons");
        addWord.setMinWidth(50);
        addWord.setOnAction(e ->
                app.addWordBox(themeStyle, fontSize)
        );

        //Left empty space formatting
        Pane spacerL = new Pane();
        HBox.setHgrow(spacerL, Priority.ALWAYS);
        spacerL.setMinWidth(25);

        //Right empty space formatting
        Pane spacerR = new Pane();
        HBox.setHgrow(spacerR, Priority.ALWAYS);
        spacerR.setMinWidth(25);

        VBox dictionaryBox = new VBox(20);

        FilteredList<Word> dictionary;

        switch (dictType) {
            case 'E': // English mode
                dictionary = new FilteredList<>(englishData, s -> true);
                label.setText(setLabel(dictType));

                // Search the English list
                searchBar.textProperty().addListener(obs -> { // Wait for change in the search bar
                    String filter = searchBar.getText();
                    search = filter;
                    if (filter == null || filter.length() == 0) {
                        dictionary.setPredicate(null); // i.e. if no search filter don't shrink the list
                    } else {
                        dictionary.setPredicate(s -> s.englishContains(filter)); // Shrink the list according to words that start with the filter
                    }
                });

                if (search != null) dictionary.setPredicate(s -> s.englishContains(search));

                // Transfer filtered list to clickable and get event
                onClick(dictionaryBox, dictionary);

                break;
            case 'W': // Welsh mode
                dictionary = new FilteredList<>(welshData, s -> true);
                label.setText(setLabel(dictType));

                // Search the Welsh list
                searchBar.textProperty().addListener(obs -> { // Wait for change in the search bar
                    String filter = searchBar.getText();
                    search = filter;
                    if (filter == null || filter.length() == 0) {
                        dictionary.setPredicate(null); // i.e. if no search filter don't shrink the list
                    } else {
                        dictionary.setPredicate(s -> s.welshContains(filter)); // Shrink the list according to words that start with the filter
                    }
                });

                if (search != null) dictionary.setPredicate(s -> s.welshContains(search));

                // Transfer filtered list to clickable and get event
                onClick(dictionaryBox, dictionary);

                break;
        }

        HBox top = new HBox(20);
        top.getChildren().addAll(label, spacerL, addWord, spacerR, searchBar);

        //Add everything to the display
        VBox main = new VBox(20);
        main.setPadding(new Insets(10, 10, 10, 10));
        main.getChildren().addAll(top, dictionaryBox);

        return main;
    }

    /**
     * This method turns the Dictionary information into a displayable list and defines what happens when an element is clicked.
     *
     * @param dictionaryBox is the display box that it will be put in.
     * @param dictionary    is the filtered (from the search) dictionary information
     */
    void onClick(VBox dictionaryBox, FilteredList<Word> dictionary) {

        //Display the dictionary list if the dictionary is not empty...
        ListView<Word> dictionaryList = new ListView<>(dictionary);
        dictionaryBox.setPrefWidth(400);
        dictionaryBox.getChildren().addAll(dictionaryList);
        dictionaryList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    app.addToPracticeList(dictionaryList.getSelectionModel().getSelectedItem());
                }
            }
        });
    }
}