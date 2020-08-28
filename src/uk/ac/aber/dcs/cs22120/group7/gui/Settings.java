package uk.ac.aber.dcs.cs22120.group7.gui;

import uk.ac.aber.dcs.cs22120.group7.backend.Main;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * This is the class that displays the User Settings.
 *
 * @author Ollie, David
 * @version 1.5
 */
public class Settings extends WindowTemplate {

    private static char theme = 'L'; //Setting specific variable which holds the current color scheme of the program

    /**
     * The constructor takes in the main application and calls the super constructor.
     *
     * @param settingsWindow is the application
     */
    public Settings(Main settingsWindow) {
        super(settingsWindow);
    }

    /**
     * This method overrides the parent WindowTemplate "Pane centre" method
     * to adhere to the Settings window UI design and functionality.
     *
     * @param dictType the mode of the dictionary
     * @return the centre display
     */
    @Override
    public Pane centre(char dictType) {
        theme = getThemeStyle();

        // Top Bar implementation
        Label settingsLabel = new Label("Settings");
        settingsLabel.setId("text-color");
        Pane spacer = new Pane();

        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(250);

        Button presets = new Button("Reset Settings"); // Resets application to light mode and size 11 font
        presets.setOnAction(e -> {
            setFontSize(11);
            theme = 'L';        // Applies light mode to local value so that the color buttons below are updated
            setTheme(theme);   // Sends the light mode character to window template to be updated globally
            app.changeScene();
        });
        presets.setId("buttons");

        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.getChildren().addAll(settingsLabel, spacer, presets);

        // Implementation of changing font sizes

        Label textSettingLabel = new Label("Font Size       ");
        textSettingLabel.setId("text-color");

        // Selection function of changing font sizes
        ComboBox<Integer> fontSizes = new ComboBox();
        fontSizes.setPromptText("Select");
        fontSizes.getItems().addAll(11, 12, 14, 16, 18);

        HBox textSetting = new HBox(20);
        textSetting.setPadding(new Insets(10, 10, 10, 10));
        textSetting.getChildren().addAll(textSettingLabel, fontSizes);


        //Apply settings implementation
        Button applyButton = new Button("Apply");

        //Applies changes that have been made in the settings to the rest of the program
        applyButton.setOnAction(e -> {
            if (fontSizes.getValue() != null) setFontSize(fontSizes.getValue());
            setTheme(theme);
            app.changeScene();
        });
        applyButton.setId("buttons");

        HBox applyBox = new HBox(20);
        applyBox.setPadding(new Insets(10, 200, 10, 200));
        applyBox.getChildren().add(applyButton);

        //Implementation of changing the application's color schemes

        Label colorSettingLabel = new Label("Color Mode       Select: ");
        colorSettingLabel.setId("text-color");

        Button darkMode = new Button();
        Button lightMode = new Button();

        // If the current theme is on "dark mode" then highlight the darkMode button
        if (theme == 'D') {
            darkMode.setId("dark-button-selected");
            lightMode.setId("light-button");

            //...otherwise, highlight the lightMode button
        } else if (theme == 'L') {
            darkMode.setId("dark-button");
            lightMode.setId("light-button-selected");
        }

        //Change application to dark color scheme if user clicks "Apply"
        darkMode.setPrefSize(40, 40);
        darkMode.setOnAction(e -> {
            theme = 'D';
            darkMode.setId("dark-button-selected");
            lightMode.setId("light-button");
        });

        //Change application to light color scheme if user clicks "Apply"
        lightMode.setPrefSize(40, 40);
        lightMode.setOnAction(e -> {
            theme = 'L';
            darkMode.setId("dark-button");
            lightMode.setId("light-button-selected");
        });

        //Add color scheme settings together into a layout
        HBox colorSetting = new HBox(20);
        colorSetting.setPadding(new Insets(10, 10, 10, 10));
        colorSetting.getChildren().addAll(colorSettingLabel, darkMode, lightMode);

        // Collected elements grouped into one VBox for the Scene
        VBox mainDisplay = new VBox(20);
        mainDisplay.getChildren().addAll(topBar, textSetting, colorSetting, applyBox);
        return mainDisplay;
    }

    /**
     * This method overrides the default template displayed, as the settings page doesn't have a bottom bar.
     * (Could change this to override the bottom bar if we want to add the apply button to that)
     *
     * @return the settings page layout
     */
    @Override
    public BorderPane windowSetup() {

        Pane top = topTemplate();

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(0, 0, 30, 0));
        layout.setTop(top);
        layout.setId("background-theme");

        return layout;
    }
}