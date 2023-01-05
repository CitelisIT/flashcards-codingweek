package flashcards.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import flashcards.App;
import flashcards.model.DeckManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeLearningController implements Observer, Initializable {
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

    public void switchToStats() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/globalStats.fxml"));
        Parent root = fxmlLoader.load();
        Button bestCardButton = (Button) root.lookup("#bestCardButton");
        // bestCardButton.setText(allDeck.getBestCard().getQuestion().toString());
        bestCardButton.setText("tmp test");
        Button worstCardButton = (Button) root.lookup("#worstCardButton");
        // worstCardButton.setText(allDeck.getWorstCard().getQuestion().toString());
        worstCardButton.setText("tmp test");
        Label bestDeckLabel = (Label) root.lookup("#bestDeckLabel");
        // bestDeckLabel.setText(allDeck.getBestDeck().getName());
        bestDeckLabel.setText("tmp test");
        Label worstDeckLabel = (Label) root.lookup("#worstDeckLabel");
        // worstDeckLabel.setText(allDeck.getWorstDeck().getName());
        worstDeckLabel.setText("tmp test");
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Global Stats");
        stage.setScene(new Scene(root));
        stage.show();
        bestCardButton.setOnAction(e -> {
            // showCard(allDeck.getBestCard());
        });
        worstCardButton.setOnAction(e -> {
            // showCard(allDeck.getWorstCard());
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> values = FXCollections.observableArrayList("Génération aléatoire", "Combler ses lacunes",
                "Valider ses acquis");
        this.strategyChoiceBox.setItems(values);
        this.strategyChoiceBox.setValue("Génération aléatoire");
        this.nbCardSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 10));
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub
    }
}
