package flashcards.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import flashcards.App;
import flashcards.model.Card;
import flashcards.model.DeckManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        bestCardButton.setText(allDeck.getBestCard().getQuestionContent(0).getData());
        Button worstCardButton = (Button) root.lookup("#worstCardButton");
        worstCardButton.setText(allDeck.getWorstCard().getQuestionContent(0).getData());
        Label bestDeckLabel = (Label) root.lookup("#bestDeckLabel");
        bestDeckLabel.setText(allDeck.getBestDeck().getName());
        Label worstDeckLabel = (Label) root.lookup("#worstDeckLabel");
        worstDeckLabel.setText(allDeck.getWorstDeck().getName());
        PieChart pieChart = (PieChart) root.lookup("#globalPieChart");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Bonnes réponses", allDeck.getRightAnswers()),
                new PieChart.Data("Mauvaises réponses", allDeck.getWrongAnswers()));
        pieChart.setData(pieChartData);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Global Stats");
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
