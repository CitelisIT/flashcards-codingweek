package flashcards.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import flashcards.App;
import flashcards.model.DeckManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class HomeLearningController implements Observer, Initializable{
    private DeckManager allDeck;

    @FXML
    private ChoiceBox<String> strategyChoiceBox;

    @FXML
    private Spinner<Integer> nbCardSpinner;

    public HomeLearningController(DeckManager allDeck) {
        this.allDeck = allDeck;
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation", allDeck, 0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> values = FXCollections.observableArrayList("Génération aléatoire","Combler ses lacunes","Valider ses acquis");
        this.strategyChoiceBox.setItems(values);
        this.strategyChoiceBox.setValue("Génération aléatoire");
        this.nbCardSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 10));
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub
    }
}
