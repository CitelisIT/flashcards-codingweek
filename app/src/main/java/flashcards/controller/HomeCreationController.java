package flashcards.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import flashcards.App;
import flashcards.model.Deck;
import flashcards.model.DeckManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomeCreationController implements Observer, Initializable {

    @FXML
    private Label displayedName;
    @FXML
    private Label displayeDesc;
    @FXML
    private Button addButton;
    @FXML
    private VBox listDeck;

    private DeckManager allDeck;
    private boolean deckButtonPressed;
    private Button buttonPressed;

    @FXML
    private ButtonBar buttonBar;

    private Button newD = new Button("Nouveau Deck");
    private Button importD = new Button("Importer deck");

    private Button editD = new Button("Edit deck");
    private Button deleteD = new Button("Delete deck");
    private Button exportD = new Button("Export deck");

    public HomeCreationController() {
    }

    public void switchToHomeLearning() throws IOException {
        App.setRoot("homeLearning");
    }

    public void switchToEditCreation() throws IOException {
        App.setRoot("editCreation");
    }

    public void selectNewOrImport() {
        buttonPressed = addButton;
        react();

    }

    public void importDeck() {

    }

    public void exportDeck() {

    }

    public void deleteDeck() {

        allDeck.removeDeck(0);
    }

    public void newDeck() throws IOException {
        Deck newDeck = new Deck();
        System.out.println(newDeck.toString());
        allDeck.addDeck(newDeck);
        switchToEditCreation();
    }

    public void editDeck() {

    }

    @Override
    public void react() {
        if (buttonPressed != null) {
            buttonPressed.setStyle("-fx-background-color: lightgreen");
            displayedName.setText(displayedName.getText());
            displayeDesc.setText(displayeDesc.getText());
        }

        listDeck.getChildren().clear();
        HBox initLigne = new HBox(addButton);
        listDeck.getChildren().add(initLigne);
        int i = 0;
        int j = 1;
        for (int k = 0; k < allDeck.getDeckManagerSize(); k++) {
            if (j < 6) {
                HBox ligne = (HBox) listDeck.getChildren().get(i);
                Button deckij = new Button(allDeck.getDeck(k).getName() + i + j);
                deckij.setId(allDeck.getDeck(k).getName());
                int index = k;
                deckij.setOnAction(event -> deckButtonPress(index));
                ligne.getChildren().add(deckij);
                j++;
            } else {
                j = 0;
                i++;
                HBox ligne = new HBox();
                Button pileij = new Button(allDeck.getDeck(k).getName() + i + j);
                ligne.getChildren().add(pileij);
                j++;
                listDeck.getChildren().add(ligne);
            }
        }
        buttonBar.getButtons().clear();
        if (buttonPressed.equals(addButton)) {
            ButtonBar.setButtonData(newD, ButtonData.APPLY);
            ButtonBar.setButtonData(importD, ButtonData.APPLY);
            buttonBar.getButtons().addAll(newD, importD);
            // buttonBar.getParent().requestLayout();
        }
    }

    public void deckButtonPress(int i) {
        buttonPressed = (Button) listDeck.getScene().lookup(allDeck.getDeck(i).getName());
        displayedName.setText(allDeck.getDeck(i).getName());
        displayeDesc.setText(allDeck.getDeck(i).getDescription());
        buttonBar.getButtons().addAll(editD, deleteD, importD);
        System.out.println("pressed" + displayeDesc.getText() + displayedName.getText());

        react();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allDeck = new DeckManager("Nouveau Deck");
        displayedName = new Label("Choisisez une pile");
        displayeDesc = new Label();
        buttonBar.setStyle("null");
        newD.setOnAction(event -> {
            try {
                newDeck();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        importD.setOnAction(event -> importDeck());
        editD.setOnAction(event -> editDeck());
        deleteD.setOnAction(event -> deleteDeck());
        exportD.setOnAction(event -> exportDeck());
    }
}
