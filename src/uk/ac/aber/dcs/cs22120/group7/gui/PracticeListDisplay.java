package uk.ac.aber.dcs.cs22120.group7.gui;

import javafx.scene.control.Label;
import uk.ac.aber.dcs.cs22120.group7.backend.Main;
import uk.ac.aber.dcs.cs22120.group7.backend.PracticeList;
import uk.ac.aber.dcs.cs22120.group7.backend.Word;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * This is the class that displays and allows edits to the User's Practice List.
 *
 * @author Ollie
 * @version 1.2
 */
public class PracticeListDisplay extends DictionaryDisplay {

    /**
     * The constructor takes in the main application and calls the super constructor (DictionaryDisplay).
     *
     * @param practiceListDisplay is the application
     */
    public PracticeListDisplay(Main practiceListDisplay, PracticeList practiceList) {
        super(practiceListDisplay, practiceList);
    }

    /**
     * This method overrides the super, as we want it to display Practice List rather than Dictionary.
     *
     * @param dictType is the mode of the dictionary
     * @return the string label
     */
    @Override
    String setLabel(char dictType) {
        String label = "";
        if (dictType == 'E') label = "English Practice List";
        else if (dictType == 'W') label = "Welsh Practice List";
        return label;
    }

    /**
     * This method overrides the super, as when in the practice list clicking on a word removes it rather than adding.
     *
     * @param dictionaryBox is the display box that it will be put in.
     * @param dictionary    is the filtered (from the search) dictionary information
     */
    @Override
    void onClick(VBox dictionaryBox, FilteredList<Word> dictionary) {

        //Display the practice list if the dictionary is not empty...
        ListView<Word> welshDictionary = new ListView<>(dictionary);
        dictionaryBox.setPrefWidth(400);
        dictionaryBox.getChildren().addAll(welshDictionary);
        welshDictionary.setOnMouseClicked(event -> {
            app.removeFromPracticeList(welshDictionary.getSelectionModel().getSelectedItem());
            app.changeScene();
        });
    }
}