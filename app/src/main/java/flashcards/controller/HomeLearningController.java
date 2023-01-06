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
import flashcards.model.FlashcardManager;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeLearningController implements Observer, Initializable {
    private FlashcardManager flashcardManager;
    private HashMap<String, ArrayList<Deck>> dico;
    private Button buttonPressed;
    private String currentDeckKey;
    private int currentDeckIndex;

    @FXML private ChoiceBox<String> strategyChoiceBox;
    @FXML private Spinner<Integer> nbCardSpinner;
    @FXML private VBox rightPannel;
    @FXML private Accordion accordion;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label strategyLabel;
    @FXML private Label nbCardLabel;
    @FXML private Button startButton;

    public HomeLearningController(FlashcardManager flashcardManager) {
        this.flashcardManager = flashcardManager;
        this.dico = new HashMap<String, ArrayList<Deck>>();
        this.buttonPressed = null;
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation", this.flashcardManager, 0);
    }

    public void startGame() throws IOException {
        int nbCards = this.nbCardSpinner.getValue();
        String chosenAlgo = this.strategyChoiceBox.getValue();
        Game game = new Game(nbCards, chosenAlgo, getCurrentDeck());
        this.flashcardManager.setGame(game);
        App.setRoot("gameLearning", this.flashcardManager, 0);
    }

    public void switchToStats() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/globalStats.fxml"));
        Parent root = fxmlLoader.load();

        Button bestCardButton = (Button) root.lookup("#bestCardButton");
        bestCardButton.setText(this.flashcardManager.getBestCard().getQuestionContent(0).getData());
        bestCardButton.setOnAction(e -> {
            try {
                showCard(this.flashcardManager.getBestCard());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button worstCardButton = (Button) root.lookup("#worstCardButton");
        worstCardButton.setText(this.flashcardManager.getWorstCard().getQuestionContent(0).getData());
        worstCardButton.setOnAction(e -> {
            try {
                showCard(this.flashcardManager.getWorstCard());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Label bestDeckLabel = (Label) root.lookup("#bestDeckLabel");
        bestDeckLabel.setText(this.flashcardManager.getBestDeck().getName());

        Label worstDeckLabel = (Label) root.lookup("#worstDeckLabel");
        worstDeckLabel.setText(this.flashcardManager.getWorstDeck().getName());

        PieChart pieChart = (PieChart) root.lookup("#globalPieChart");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Bonnes réponses", this.flashcardManager.getRightAnswers()),
                new PieChart.Data("Mauvaises réponses", this.flashcardManager.getWrongAnswers()));
        pieChart.setData(pieChartData);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Statistiques sur l'ensemble des piles");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showCard(Card card) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/card.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setTitle("Card");
        stage.setScene(new Scene(root));
        stage.show();

        Button questionAnswerButton = (Button) root.lookup("#questionAnswerButton");
        questionAnswerButton.setText(card.getQuestionContent(0).getData());
        questionAnswerButton.setOnAction(e -> {
            if (questionAnswerButton.getText().equals(card.getQuestionContent(0).getData())) {
                questionAnswerButton.setText(card.getAnswerContent(0).getData());
            } else {
                questionAnswerButton.setText(card.getQuestionContent(0).getData());
            }
        });

        Button goBackButton = (Button) root.lookup("#goBackButton");
        goBackButton.setOnAction(e -> { stage.close(); });
    }

    public void fillDico() {
        for (Deck deck : this.flashcardManager.getDeckList()) {
            if (this.dico.containsKey(deck.getName().substring(0, 1))) {
                this.dico.get(deck.getName().substring(0, 1)).add(deck);
            } else {
                this.dico.put((String) deck.getName().substring(0, 1), new ArrayList<Deck>(Collections.singletonList(deck)));
            }
        }
    }

    public Deck getCurrentDeck() {
        return this.dico.get(this.currentDeckKey).get(this.currentDeckIndex);
    }

    public VBox makeGrid(ArrayList<Deck> listDeck, int nbColumn) {
        VBox table = new VBox();
        for (int i = 0; i < listDeck.size(); i++) {
            int numLine = i / nbColumn;
            int numColumn = i % nbColumn;
            if (numColumn == 0) {
                HBox line = new HBox();
                table.getChildren().add(line);
            }
            
            Button deckButton = new Button(listDeck.get(i).getName());
            deckButton.setId(Integer.toString(i));

            HBox line = (HBox) table.getChildren().get(numLine);
            line.getChildren().add(deckButton);
            
            HBox.setMargin(deckButton, new Insets(10, 0, 0, 10));
            deckButton.setPrefSize(130.0, 100.0);

            deckButton.setOnAction(event -> {
                this.buttonPressed = deckButton;
                this.currentDeckIndex = Integer.parseInt(deckButton.getId());
                this.currentDeckKey = deckButton.getText().substring(0, 1);
                this.titleLabel.setText(deckButton.getText());
                this.descriptionLabel.setText(getCurrentDeck().getDescription());
                react();
            });
        }
        return table;
    }

    public void affDeck() {
        this.accordion.getPanes().clear();
        for (Entry entry : this.dico.entrySet()) {
            TitledPane pane = new TitledPane((String) entry.getKey(), makeGrid((ArrayList<Deck>) entry.getValue(), 4));
            this.accordion.getPanes().add(pane);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rightPannel.getChildren().clear();
        if (this.flashcardManager.getDeckManagerSize() > 0) {
            ObservableList<String> values = FXCollections.observableArrayList("Génération aléatoire","Combler ses lacunes","Valider ses acquis");
            this.strategyChoiceBox.setItems(values);
            this.strategyChoiceBox.setValue("Génération aléatoire");
            this.nbCardSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 10));
            fillDico();
            affDeck();
            this.rightPannel.getChildren().add(new Label("Selectionner une pile"));
        } else {
            this.rightPannel.getChildren().add(new Label("Aucune pile"));
        }
    }

    @Override
    public void react() {
        this.rightPannel.getChildren().clear();
        this.rightPannel.getChildren().add(this.titleLabel);
        this.rightPannel.getChildren().add(this.descriptionLabel);
        this.rightPannel.getChildren().add(this.strategyLabel);
        this.rightPannel.getChildren().add(this.strategyChoiceBox);
        this.rightPannel.getChildren().add(this.nbCardLabel);
        this.rightPannel.getChildren().add(this.nbCardSpinner);
        this.rightPannel.getChildren().add(this.startButton);
    }
}
