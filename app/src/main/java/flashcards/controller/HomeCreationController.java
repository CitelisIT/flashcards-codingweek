package flashcards.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

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
import javafx.stage.FileChooser;
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

    public HomeCreationController(DeckManager allDeck) {
        this.allDeck = allDeck;
    }

    public void switchToHomeLearning() throws IOException {
        App.setRoot("homeLearning", allDeck);
    }

    public void switchToEditCreation() throws IOException {
        App.setRoot("editCreation", allDeck);
    }

    public void selectNewOrImport() {
        buttonPressed = addButton;
        buttonPressed.setStyle(null);
        allDeck.triggerObserver();
        ;

    }

    public void importDeck() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import");
        fileChooser.getExtensionFilters()
                .addAll(new ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(listDeck.getScene().getWindow());
        if (file != null) {
            allDeck.addDeck(allDeck.importdeckManager(file.getPath()));
        }
        allDeck.triggerObserver();
    }

    public void exportDeck() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export");
        fileChooser.getExtensionFilters()
                .addAll(new ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showSaveDialog(listDeck.getScene().getWindow());
        allDeck.exportdeckManager(allDeck.getDeck(activeDeck), file.toPath());
    }

    public void deleteDeck() {
        allDeck.removeDeck(0);
        allDeck.triggerObserver();
    }

    public void newDeck() throws IOException {
        Deck newDeck = new Deck();
        allDeck.addDeck(newDeck);
        allDeck.triggerObserver();
        switchToEditCreation();
    }

    public void saveAll() {
        allDeck.save();
        System.exit(0);
    }

    public void editDeck() throws IOException {
        switchToEditCreation();
    }

    @Override
    public void react() {
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
                deckij.setOnAction(event -> {
                    buttonPressed.setStyle(null);
                    buttonPressed = deckij;
                    deckij.setStyle("-fx-background-color: lightgreen");
                    buttonBar.requestLayout();
                    System.out.println(buttonPressed.getStyle());
                    deckButtonPress(index);

                });
                ligne.getChildren().add(deckij);
                j++;
            } else {
                j = 0;
                i++;
                HBox ligne = new HBox();
                Button pileij = new Button(allDeck.getDeck(k).getName());
                ligne.getChildren().add(pileij);
                j++;
                listDeck.getChildren().add(ligne);
            }
        }
        buttonBar.getButtons().clear();
        if (buttonPressed.equals(addButton)) {
            buttonPressed.setStyle("-fx-background-color: lightgreen");
            displayedName.setText(currentName);
            displayedDesc.setText(currentDesc);
            ButtonBar.setButtonData(newD, ButtonData.APPLY);
            ButtonBar.setButtonData(importD, ButtonData.APPLY);
            buttonBar.getButtons().addAll(newD, importD);
        } else if (buttonPressed == null) {

        } else {
            displayedName.setText(currentName);
            displayedDesc.setText(currentDesc);
            ButtonBar.setButtonData(exportD, ButtonData.APPLY);
            ButtonBar.setButtonData(editD, ButtonData.APPLY);
            ButtonBar.setButtonData(deleteD, ButtonData.APPLY);
            buttonBar.getButtons().addAll(editD, exportD, deleteD);
        }
    }

    public void deckButtonPress(int i) {
        currentName = allDeck.getDeck(i).getName();
        currentDesc = allDeck.getDeck(i).getDescription();
        activeDeck = i;
        allDeck.triggerObserver();
        ;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allDeck.addObserver(this);
        buttonBar.setStyle("null");
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
                deckij.setOnAction(event -> {
                    buttonPressed = deckij;
                    buttonPressed.setStyle("-fx-background-color: lightgreen");
                    deckButtonPress(index);
                });
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
    }
}
