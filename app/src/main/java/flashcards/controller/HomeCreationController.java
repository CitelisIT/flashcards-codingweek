package flashcards.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import flashcards.App;
import flashcards.model.Card;
import flashcards.model.Deck;
import flashcards.model.FlashcardManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class HomeCreationController implements Observer, Initializable {

    private String currentName;
    private String currentDescription;
    private FlashcardManager flashcardManager;
    private int activeDeck;
    private Button buttonPressed;

    private Button newDeck = new Button("Nouveau Deck");
    private Button importDeck = new Button("Importer JSON");
    private Button editDeck = new Button("Modifier");
    private Button deleteDeck = new Button("Supprimer");
    private Button exportDeck = new Button("Exporter");
    private Button statistics = new Button("Statistiques");

    @FXML
    private ButtonBar buttonBar;
    @FXML
    private Button addButton;
    @FXML
    private VBox listDeck;
    @FXML
    private Label displayedDescription;
    @FXML
    private Label displayedName;

    public HomeCreationController(FlashcardManager flashcardManager) {
        this.flashcardManager = flashcardManager;
    }

    /**
     * Switch the view for the Learning view. This method is
     * called when the "Mode Apprentissage" menu Item is pressed.
     */
    public void switchToHomeLearning() throws IOException {
        App.setRoot("homeLearning", this.flashcardManager, 0);
    }

    /**
     * Switch the view for the edit view. This method is
     * called when the "Edit" or "New" button is pressed.
     */
    public void switchToEditCreation() throws IOException {
        App.setRoot("editCreation", this.flashcardManager, this.activeDeck);
    }

    /**
     * Selects the "New" or "Import" option in the "Add" button. This method is
     * called when the "Add" button is pressed.
     */
    public void selectNewOrImport() {
        this.currentName = "Ajout de pile";
        this.currentDescription = "Créer une pile ou en importer une au format JSON";
        this.buttonPressed = this.addButton;
        this.buttonPressed.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;");
        react();
    }

    /**
     * Imports a deck from a JSON file. This method is called when the "Import deck"
     * button is pressed.
     * 
     * @throws JsonSyntaxException   if there is a syntax error in the JSON file
     * @throws JsonIOException       if there is an error reading the JSON file
     * @throws FileNotFoundException if the file is not found
     */
    public void importDeck() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un JSON");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(this.listDeck.getScene().getWindow());
        if (file != null) {
            this.flashcardManager.addDeck(this.flashcardManager.importdeckManager(file.getPath()));
        }
        react();
    }

    /**
     * Exports the currently active deck to a JSON file. This method is called when
     * the "Export deck" button is pressed.
     * 
     * @throws IOException if there is an error writing to the file
     */
    public void exportDeck() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter sous format JSON");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showSaveDialog(this.listDeck.getScene().getWindow());
        if (file != null) {
            this.flashcardManager.exportdeckManager(this.flashcardManager.getDeck(this.activeDeck), file.toPath());
        }
    }

    /**
     * Deletes the currently active deck. This method is called when the "Delete
     * deck" button is pressed.
     */
    public void deleteDeckeck() {
        this.flashcardManager.removeDeck(this.activeDeck);
        this.activeDeck = 0;
        selectNewOrImport();
    }

    /**
     * Creates a pop-up containing the stats of a certain date. This method is
     * called when the "Stats" button is
     * pressed.
     * 
     * @throws IOException if there is an error switching to the "stats" view
     */
    public void statsDeck() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/deckStats.fxml"));
        Parent root = fxmlLoader.load();

        Button bestCardButton = (Button) root.lookup("#bestCardButton");
        bestCardButton.setText(flashcardManager.getDeck(this.activeDeck).getBestCard().getQuestionContent(0).getData());
        bestCardButton.setOnAction(e -> {
            try {
                showCard(flashcardManager.getDeck(this.activeDeck).getBestCard());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button worstCardButton = (Button) root.lookup("#worstCardButton");
        worstCardButton
                .setText(this.flashcardManager.getDeck(this.activeDeck).getWorstCard().getQuestionContent(0).getData());
        worstCardButton.setOnAction(e -> {
            try {
                showCard(this.flashcardManager.getDeck(this.activeDeck).getWorstCard());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        PieChart pieChart = (PieChart) root.lookup("#deckPieChart");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Bonnes réponses", this.flashcardManager.getDeck(this.activeDeck).getRightAnswers()),
                new PieChart.Data("Mauvaises réponses",
                        this.flashcardManager.getDeck(this.activeDeck).getWrongAnswers()));
        pieChart.setData(pieChartData);

        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setTitle("Statistiques de la pile");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Shows a card in a pop-up.
     * 
     * @param card the card to show
     * @throws IOException if there is an error switching to the "card" view
     */
    public void showCard(Card card) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/card.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setTitle("Carte");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        root.requestFocus();

        Button questionAnswerButton = (Button) root.lookup("#questionAnswerButton");
        questionAnswerButton.setStyle(
                "-fx-pref-width: 490px; -fx-pref-height: 338px;  -fx-font-size: 20px; -fx-font-weight: bold ; -fx-text-alignment: center;");
        questionAnswerButton.setText(card.getQuestionContent(0).getData());
        questionAnswerButton.setWrapText(true);
        questionAnswerButton.setOnAction(e -> {
            if (questionAnswerButton.getText().equals(card.getQuestionContent(0).getData())) {
                questionAnswerButton.setText(card.getAnswerContent(0).getData());
                questionAnswerButton.setStyle(
                        "-fx-pref-width: 490px; -fx-pref-height: 338px;  -fx-font-size: 16px ; -fx-text-alignment: center;");
            } else {
                questionAnswerButton.setText(card.getQuestionContent(0).getData());
                questionAnswerButton.setStyle(
                        "-fx-pref-width: 490px; -fx-pref-height: 338px;  -fx-font-size: 20px; -fx-font-weight: bold ; -fx-text-alignment: center;");
            }
        });
        Button goBackButton = (Button) root.lookup("#goBackButton");
        goBackButton.setOnAction(e -> {
            stage.close();
        });

    }

    /**
     * Creates a new deck and switches to the "editCreation" view. This method is
     * called when the "New deck" button is pressed.
     * 
     * @throws IOException if there is an error switching to the "editCreation" view
     */
    public void newDeck() throws IOException {
        Deck newDeck = new Deck();
        this.flashcardManager.addDeck(newDeck);
        newDeck.setName(newDeck.getName() + this.flashcardManager.getDeckManagerSize());
        this.activeDeck = this.flashcardManager.sortByName(newDeck);
        react();
        switchToEditCreation();
    }

    /**
     * Saves the current state of the decks and exits the program. This method is
     * called when the "Save and Quit" button is pressed.
     */
    public void saveAll() {
        this.flashcardManager.save();
        System.exit(0);
    }

    /**
     * Switches to the "editCreation" view. This method is called when the "Edit
     * deck" button is pressed.
     * 
     * @throws IOException if there is an error switching to the "editCreation" view
     */
    public void editDeck() throws IOException {
        switchToEditCreation();
    }

    /**
     * This method is called when there is a change in the deck manager. It updates
     * the list of decks displayed in the main screen,
     * and updates the buttons displayed in the button bar based on the currently
     * selected deck (if any).
     *
     * @param none
     * @return void
     */
    @Override
    public void react() {
        flashcardManager.save();
        // Clear the list of decks in the UI
        this.listDeck.getChildren().clear();
        // Add a new row to the list with the add button
        HBox initLigne = new HBox(this.addButton);
        this.listDeck.getChildren().add(initLigne);
        // Initialize variables to keep track of current row and column
        int i = 0;
        int j = 1;
        // Iterate through all decks in the deck manager
        for (int k = 0; k < this.flashcardManager.getDeckManagerSize(); k++) {
            // If there is still space on the current row, add the deck button to it
            HBox line;
            if (j == 6) {
                i++;
                j = 0;
                line = new HBox();
                this.listDeck.getChildren().add(line);
            } else {
                line = (HBox) this.listDeck.getChildren().get(i);
            }

            Button deckij = new Button(this.flashcardManager.getDeck(k).getName());
            deckij.setId(Integer.toString(k));
            deckij.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;");
            // If this button is the one that was previously pressed, highlight it
            if (this.buttonPressed != null) {
                if (deckij.getId().equals(this.buttonPressed.getId())) {
                    deckij.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;-fx-background-color: lightblue");
                }
            }
            // Set the action for when this button is pressed
            int index = k;
            // deckij.setPrefSize(130.0, 100.0);
            deckij.setOnAction(event -> {
                this.buttonPressed.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;");
                this.buttonPressed = deckij;
                deckij.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;-fx-background-color: lightblue");
                this.buttonBar.requestLayout();
                this.displayedName.setText("Nouvelle pile");
                this.displayedDescription.setText("Ajouter une description à votre pile");
                deckButtonPress(index);
            });
            line.getChildren().add(deckij);
            HBox.setMargin(deckij, new Insets(10, 0, 0, 10));
            j++;
        }
        // Clear the buttons in the button bar
        this.buttonBar.getButtons().clear();
        if (buttonPressed != null) {
            // If the add button is pressed, show the new and import buttons
            if (this.buttonPressed.equals(this.addButton)) {
                this.displayedName.setText("Nouvelle pile");
                this.displayedDescription.setText("Ajouter une description à votre pile");
                this.buttonPressed
                        .setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;-fx-background-color: lightblue");
                this.displayedName.setText(this.currentName);
                this.displayedDescription.setText(this.currentDescription);
                ButtonBar.setButtonData(this.newDeck, ButtonData.APPLY);
                ButtonBar.setButtonData(this.importDeck, ButtonData.APPLY);
                this.buttonBar.getButtons().addAll(this.newDeck, this.importDeck);
            }
            // If a deck button is pressed, show the edit, export, delete and stats buttons
            else {
                this.displayedName.setText(this.currentName);
                this.displayedDescription.setText(this.currentDescription);
                ButtonBar.setButtonData(this.exportDeck, ButtonData.APPLY);
                ButtonBar.setButtonData(this.editDeck, ButtonData.APPLY);
                ButtonBar.setButtonData(this.deleteDeck, ButtonData.APPLY);
                ButtonBar.setButtonData(this.statistics, ButtonData.APPLY);
                this.buttonBar.getButtons().addAll(this.editDeck, this.exportDeck, this.deleteDeck, this.statistics);
            }
        }
    }

    /**
     * Updates the name and description of the currently selected deck and triggers
     * an update of the view. This method is called when a deck button is pressed.
     * 
     * @param i the index of the selected deck
     */
    public void deckButtonPress(int i) {
        this.currentName = this.flashcardManager.getDeck(i).getName();
        this.currentDescription = this.flashcardManager.getDeck(i).getDescription();
        this.activeDeck = i;
        react();
    }

    /**
     * Initializes the view. This method is called when the view is first loaded.
     * 
     * @param location  the location of the view
     * @param resources the resources used to load the view
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add this object as an observer of the flashcardManager object.
        this.flashcardManager.addObserver(this);
        // Set the style of the buttonBar object to "null".
        this.buttonBar.setStyle("null");
        // Set up action event handlers for the newDeck, importDeck, editDeck,
        // deleteDeck,
        // exportD and stats objects.
        this.newDeck.setOnAction(event -> {
            try {
                newDeck();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.importDeck.setOnAction(event -> {
            try {
                importDeck();
            } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        this.editDeck.setOnAction(event -> {
            try {
                editDeck();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        this.deleteDeck.setOnAction(event -> deleteDeckeck());
        this.exportDeck.setOnAction(event -> {
            try {
                exportDeck();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.statistics.setOnAction(event -> {
            try {
                statsDeck();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Clear the list of children for the listDeck object.
        this.listDeck.getChildren().clear();
        // Create a new HBox object called initLigne and add it to the listDeck object.
        HBox initLigne = new HBox(this.addButton);
        addButton.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;");
        this.listDeck.getChildren().add(initLigne);
        // Initialize loop variables.
        int i = 0;
        int j = 1;
        // Iterate through all decks in the deck manager
        for (int k = 0; k < this.flashcardManager.getDeckManagerSize(); k++) {
            // If there is still space on the current row, add the deck button to it
            HBox line;
            if (j == 6) {
                i++;
                j = 0;
                line = new HBox();
                this.listDeck.getChildren().add(line);
            } else {
                line = (HBox) this.listDeck.getChildren().get(i);
            }
            Button deckij = new Button(this.flashcardManager.getDeck(k).getName());
            deckij.setId(Integer.toString(k));
            // If this button is the one that was previously pressed, highlight it
            // Set the action for when this button is pressed
            int index = k;
            deckij.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;");

            deckij.setOnAction(event -> {
                this.buttonPressed = deckij;
                deckij.setStyle("-fx-background-color: lightblue");
                this.buttonBar.requestLayout();
                this.displayedName.setText("Nouvelle pile");
                this.displayedDescription.setText("Ajouter une description à votre pile");
                deckButtonPress(index);
            });
            line.getChildren().add(deckij);
            HBox.setMargin(deckij, new Insets(10, 0, 0, 10));
            j++;
        }
    }
}
