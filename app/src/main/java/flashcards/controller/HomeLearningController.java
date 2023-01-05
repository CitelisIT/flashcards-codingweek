package flashcards.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import flashcards.App;
import flashcards.model.Card;
import flashcards.model.Deck;
import flashcards.model.DeckManager;
import flashcards.model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeLearningController implements Observer, Initializable {
    private DeckManager allDeck;
    private HashMap<String, ArrayList<Deck>> dico = new HashMap<String, ArrayList<Deck>>();
    private Button buttonPressed = null;
    private String currentDeckKey;
    private int currentDeckIndex;

    @FXML
    private ChoiceBox<String> strategyChoiceBox;

    @FXML
    private Spinner<Integer> nbCardSpinner;

    @FXML
    private VBox rightPannel;

    @FXML
    private Accordion accordion;

    @FXML
    private Label title;

    @FXML
    private Label description;

    @FXML
    private Button startButton;

    public HomeLearningController(DeckManager allDeck) {
        this.allDeck = allDeck;
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation", allDeck, 0);
    }

    public void startGame() throws IOException {
        int nbCards = nbCardSpinner.getValue();
        String chosenAlgo = strategyChoiceBox.getValue();
        Game game = new Game(nbCards, chosenAlgo, getCurrentDeck());
        allDeck.setGame(game);
        App.setRoot("gameLearning", allDeck, 0);
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

    public void fillDico() {
        for (Deck deck : allDeck.getDeckList()) {
            if (dico.containsKey(deck.getName().substring(0, 1))) {
                dico.get(deck.getName().substring(0, 1)).add(deck);
            } else {
                dico.put((String) deck.getName().substring(0, 1), new ArrayList<Deck>(Collections.singletonList(deck)));
            }
        }
    }

    public Deck getCurrentDeck() {
        return dico.get(currentDeckKey).get(currentDeckIndex);
    }

    public VBox makeGrid(ArrayList<Deck> listDeck, int nbColonnes) {
        System.out.println(listDeck);
        VBox table = new VBox();
        for (int i = 0; i < listDeck.size(); i++) {
            int numLigne = i / nbColonnes;
            int numColonne = i % nbColonnes;
            if (numColonne == 0) {
                HBox line = new HBox();
                table.getChildren().add(line);
            }
            Button deckButton = new Button(listDeck.get(i).getName());
            deckButton.setId(Integer.toString(i));
            HBox line = (HBox) table.getChildren().get(numLigne);
            line.getChildren().add(deckButton);
            HBox.setMargin(deckButton, new Insets(10, 0, 0, 10));
            deckButton.setPrefSize(130.0, 100.0);

            deckButton.setOnAction(event -> {
                buttonPressed = deckButton;
                currentDeckIndex = Integer.parseInt(deckButton.getId());
                currentDeckKey = deckButton.getText().substring(0, 1);
                title.setText(deckButton.getText());
                description.setText(getCurrentDeck().getDescription());
                react();
            });

        }
        return table;
    }

    public void affDeck() {
        accordion.getPanes().clear();
        for (Entry entry : dico.entrySet()) {
            TitledPane pane = new TitledPane((String) entry.getKey(), makeGrid((ArrayList<Deck>) entry.getValue(), 4));
            accordion.getPanes().add(pane);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rightPannel.getChildren().clear();

        if (allDeck.getDeckManagerSize() > 0) {
            ObservableList<String> values = FXCollections.observableArrayList("Génération aléatoire",
                    "Combler ses lacunes",
                    "Valider ses acquis");
            this.strategyChoiceBox.setItems(values);
            this.strategyChoiceBox.setValue("Génération aléatoire");
            this.nbCardSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 10));
            fillDico();
            affDeck();

            rightPannel.getChildren().add(new Label("Selectionner une pile"));
        } else {
            rightPannel.getChildren().add(new Label("Aucune pile"));
        }

    }

    @Override
    public void react() {
        rightPannel.getChildren().clear();
        rightPannel.getChildren().add(title);
        rightPannel.getChildren().add(description);
        rightPannel.getChildren().add(strategyChoiceBox);
        rightPannel.getChildren().add(nbCardSpinner);
        rightPannel.getChildren().add(startButton);
        // fillDico();
        // affDeck();

        // TODO Auto-generated method stub
    }
}
