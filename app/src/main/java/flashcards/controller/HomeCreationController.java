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
import flashcards.model.DeckManager;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class HomeCreationController implements Observer, Initializable {

    @FXML
    private Label displayedName;
    private String currentName;
    @FXML
    private Label displayedDesc;
    private String currentDesc;
    @FXML
    private Button addButton;
    @FXML
    private VBox listDeck;

    private DeckManager allDeck;
    private int activeDeck;
    private Button buttonPressed;

    @FXML
    private ButtonBar buttonBar;

    private Button newD = new Button("Nouveau Deck");
    private Button importD = new Button("Importer deck");

    private Button editD = new Button("Edit deck");
    private Button deleteD = new Button("Delete deck");
    private Button exportD = new Button("Export deck");

    private Button stats = new Button("Stats");

    public HomeCreationController(DeckManager allDeck) {
        this.allDeck = allDeck;
    }

    /**
     * Switch the view for the Learning view. This method is
     * called when the "Mode Apprentissage" menu Item is pressed.
     */
    public void switchToHomeLearning() throws IOException {
        App.setRoot("homeLearning", allDeck, 0);
    }

    /**
     * Switch the view for the edit view. This method is
     * called when the "Edit" or "New" button is pressed.
     */
    public void switchToEditCreation() throws IOException {
        App.setRoot("editCreation", allDeck, activeDeck);
    }

    /**
     * Selects the "New" or "Import" option in the "Add" button. This method is
     * called when the "Add" button is pressed.
     */
    public void selectNewOrImport() {
        currentName = "Nouvelle pile";
        currentDesc = "Décrivez votre pile";
        buttonPressed = addButton;
        buttonPressed.setStyle(null);
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
        fileChooser.setTitle("Import");
        fileChooser.getExtensionFilters()
                .addAll(new ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(listDeck.getScene().getWindow());
        if (file != null) {
            allDeck.addDeck(allDeck.importdeckManager(file.getPath()));
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
        fileChooser.setTitle("Export");
        fileChooser.getExtensionFilters()
                .addAll(new ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showSaveDialog(listDeck.getScene().getWindow());
        if (file != null) {
            allDeck.exportdeckManager(allDeck.getDeck(activeDeck), file.toPath());
        }
    }

    /**
     * Deletes the currently active deck. This method is called when the "Delete
     * deck" button is pressed.
     */
    public void deleteDeck() {
        allDeck.removeDeck(activeDeck);
        activeDeck = 0;
        buttonPressed = addButton;
        react();
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
        bestCardButton.setText(allDeck.getDeck(activeDeck).getBestCard().getQuestionContent(0).getData());
        Button worstCardButton = (Button) root.lookup("#worstCardButton");
        worstCardButton.setText(allDeck.getDeck(activeDeck).getWorstCard().getQuestionContent(0).getData());
        PieChart pieChart = (PieChart) root.lookup("#deckPieChart");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Bonnes réponses", allDeck.getDeck(activeDeck).getRightAnswers()),
                new PieChart.Data("Mauvaises réponses", allDeck.getDeck(activeDeck).getWrongAnswers()));
        pieChart.setData(pieChartData);
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setTitle("Deck Stats");
        stage.setScene(new Scene(root));
        stage.show();
        bestCardButton.setOnAction(e -> {
            try {
                showCard(allDeck.getBestCard());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        worstCardButton.setOnAction(e -> {
            try {
                showCard(allDeck.getWorstCard());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
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
        stage.setTitle("Card");
        stage.setScene(new Scene(root));
        stage.show();
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
        allDeck.addDeck(newDeck);
        newDeck.setName(newDeck.getName() + allDeck.getDeckManagerSize());
        activeDeck = allDeck.getDeckManagerSize() - 1;
        react();
        switchToEditCreation();
    }

    /**
     * Saves the current state of the decks and exits the program. This method is
     * called when the "Save and Quit" button is pressed.
     */
    public void saveAll() {
        allDeck.save();
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
        // Clear the list of decks in the UI
        listDeck.getChildren().clear();
        // Add a new row to the list with the add button
        HBox initLigne = new HBox(addButton);
        listDeck.getChildren().add(initLigne);
        // Initialize variables to keep track of current row and column
        int i = 0;
        int j = 1;
        // Iterate through all decks in the deck manager
        for (int k = 0; k < allDeck.getDeckManagerSize(); k++) {
            // If there is still space on the current row, add the deck button to it
            HBox ligne;
            if (j == 6) {
                i++;
                j = 0;
                ligne = new HBox();
                listDeck.getChildren().add(ligne);
            } else {
                ligne = (HBox) listDeck.getChildren().get(i);
            }

            Button deckij = new Button(allDeck.getDeck(k).getName());
            deckij.setId(allDeck.getDeck(k).getName());
            // If this button is the one that was previously pressed, highlight it
            if (buttonPressed != null) {
                if (deckij.getId().equals(buttonPressed.getId())) {
                    deckij.setStyle("-fx-background-color: lightgreen");
                }
            }
            // Set the action for when this button is pressed
            int index = k;
            deckij.setPrefSize(130.0, 100.0);
            deckij.setOnAction(event -> {
                buttonPressed.setStyle(null);
                buttonPressed = deckij;
                deckij.setStyle("-fx-background-color: lightgreen");
                buttonBar.requestLayout();
                displayedName.setText("Nouvelle pile");
                displayedDesc.setText("Décrivez votre pile");
                deckButtonPress(index);
            });
            ligne.getChildren().add(deckij);
            HBox.setMargin(deckij, new Insets(10, 0, 0, 10));
            j++;
        }
        // Clear the buttons in the button bar
        buttonBar.getButtons().clear();
        if (buttonPressed != null)

        {
            // If the add button is pressed, show the new and import buttons
            if (buttonPressed.equals(addButton)) {
                displayedName.setText("Nouvelle pile");
                displayedDesc.setText("Décrivez votre pile");
                buttonPressed.setStyle("-fx-background-color: lightgreen");
                displayedName.setText(currentName);
                displayedDesc.setText(currentDesc);
                ButtonBar.setButtonData(newD, ButtonData.APPLY);
                ButtonBar.setButtonData(importD, ButtonData.APPLY);
                buttonBar.getButtons().addAll(newD, importD);
            }

            // If a deck button is pressed, show the edit, export, delete and stats buttons
            else {
                displayedName.setText(currentName);
                displayedDesc.setText(currentDesc);
                ButtonBar.setButtonData(exportD, ButtonData.APPLY);
                ButtonBar.setButtonData(editD, ButtonData.APPLY);
                ButtonBar.setButtonData(deleteD, ButtonData.APPLY);
                ButtonBar.setButtonData(stats, ButtonData.APPLY);
                buttonBar.getButtons().addAll(editD, exportD, deleteD, stats);
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
        currentName = allDeck.getDeck(i).getName();
        currentDesc = allDeck.getDeck(i).getDescription();
        activeDeck = i;
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
        // Add this object as an observer of the allDeck object.
        allDeck.addObserver(this);
        // Set the style of the buttonBar object to "null".
        buttonBar.setStyle("null");
        // Set up action event handlers for the newD, importD, editD, deleteD,
        // exportD and stats objects.
        newD.setOnAction(event -> {
            try {
                newDeck();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        importD.setOnAction(event -> {
            try {
                importDeck();
            } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        editD.setOnAction(event -> {
            try {
                editDeck();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        deleteD.setOnAction(event -> deleteDeck());
        exportD.setOnAction(event -> {
            try {
                exportDeck();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stats.setOnAction(event -> {
            try {
                statsDeck();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Clear the list of children for the listDeck object.
        listDeck.getChildren().clear();
        // Create a new HBox object called initLigne and add it to the listDeck object.
        HBox initLigne = new HBox(addButton);
        listDeck.getChildren().add(initLigne);
        // Initialize loop variables.
        int i = 0;
        int j = 1;
        // Iterate through all decks in the deck manager
        for (int k = 0; k < allDeck.getDeckManagerSize(); k++) {
            // If there is still space on the current row, add the deck button to it
            HBox ligne;
            if (j == 6) {
                i++;
                j = 0;
                ligne = new HBox();
                listDeck.getChildren().add(ligne);
            } else {
                ligne = (HBox) listDeck.getChildren().get(i);
            }

            Button deckij = new Button(allDeck.getDeck(k).getName());
            deckij.setId(allDeck.getDeck(k).getName());
            // If this button is the one that was previously pressed, highlight it
            // Set the action for when this button is pressed
            int index = k;
            deckij.setPrefSize(130.0, 100.0);
            deckij.setOnAction(event -> {
                buttonPressed = deckij;
                deckij.setStyle("-fx-background-color: lightgreen");
                buttonBar.requestLayout();
                displayedName.setText("Nouvelle pile");
                displayedDesc.setText("Décrivez votre pile");
                deckButtonPress(index);
            });
            ligne.getChildren().add(deckij);
            HBox.setMargin(deckij, new Insets(10, 0, 0, 10));
            j++;

        }

    }
}
