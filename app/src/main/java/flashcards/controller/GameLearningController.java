package flashcards.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import flashcards.App;
import flashcards.model.Card;
import flashcards.model.Content;
import flashcards.model.DeckManager;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class GameLearningController implements Observer, Initializable {

    private DeckManager flashcardManager;
    private int activeDeck;

    private int timer;
    private Timeline timeline;

    @FXML
    private ProgressBar timerProgressBar;
    @FXML
    private ProgressBar cardProgressBar;
    @FXML
    private Button goodAnswerButton;
    @FXML
    private Button badAnswerButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Label gameDeckTitle;
    @FXML
    private Label gameStatus;
    @FXML
    private Label gameScore;
    @FXML
    private VBox displayedVBox;

    // TODO Réfléchir à l'implémentation des médias par rapport au timer

    public GameLearningController(DeckManager flashcardManager, int activeDeck) {
        this.flashcardManager = flashcardManager;
        this.activeDeck = activeDeck;
        this.flashcardManager.addObserver(this);
    }

    // new Deck dans le HomeLearning
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // this.flashcardManager.setDefaultGame(this.flashcardManager.getDeck(this.activeDeck));
        this.goodAnswerButton.setVisible(false);
        this.badAnswerButton.setVisible(false);
        react();
    }

    /**
     * Switch the view for the Learning view. This method is
     * called when the "Mode Apprentissage" menu Item is pressed.
     */
    public void switchToHomeLearning() throws IOException {
        App.setRoot("homeLearning", flashcardManager, 0);
    }

    public void timerPlay() {
        timer = 0;
        timerProgressBar.setMaxWidth(Double.MAX_VALUE);
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    timerProgressBar.setProgress((double) ++timer / 10);
                    if (timer == 10) {
                        goodAnswerButton.setVisible(true);
                        badAnswerButton.setVisible(true);
                        showAnswer();
                        timeline.stop();
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void showAnswer() {
        Card displayedCard = flashcardManager.getGame().getCurrentCard();
        for (int index = 0; index < displayedCard.getAnswer().size(); index++) {
            Content answer = displayedCard.getAnswerContent(index);
            if (answer.getDataType().equals("TEXT")) {
                Label answerLabel = new Label(answer.getData());
                displayedVBox.getChildren().add(answerLabel);

            } else {
                // TODO : gestion des media LOL
            }
        }
    }

    public void showQuestion() {
        displayedVBox.getChildren().clear();
        goBackButton.setVisible(false);
        Card displayedCard = flashcardManager.getGame().getCurrentCard();
        for (int index = 0; index < displayedCard.getQuestion().size(); index++) {
            Content question = displayedCard.getQuestionContent(index);
            if (question.getDataType().equals("TEXT")) {
                Label answerLabel = new Label(question.getData());
                displayedVBox.getChildren().add(answerLabel);

            } else {
                // TODO : gestion des media LOL
            }
        }
    }

    public void goodAnswer() {
        goodAnswerButton.setVisible(false);
        badAnswerButton.setVisible(false);
        flashcardManager.updateGoodAnswer();
    }

    public void badAnswer() {
        goodAnswerButton.setVisible(false);
        badAnswerButton.setVisible(false);
        flashcardManager.updateBadAnswer();
    }

    @Override
    public void react() {
        cardProgressBar.setMaxWidth(Double.MAX_VALUE);
        cardProgressBar.setProgress((double) flashcardManager.getGame().getCurrentCardIndex()
                / flashcardManager.getGame().getSequenceCards().size());

        this.gameDeckTitle.setText(this.flashcardManager.getGame().getDeck().getName());
        int currentQuestion = this.flashcardManager.getGame().getCurrentCardIndex() + 1;
        int nbQuestions = this.flashcardManager.getGame().getSequenceCards().size();
        this.gameStatus.setText(currentQuestion + "/" + nbQuestions);
        int previousQuestion = this.flashcardManager.getGame().getCurrentCardIndex();
        int numberGoodAnswer = this.flashcardManager.getGame().getNbGoodAnswer();
        this.gameStatus.setText(currentQuestion + "/" + nbQuestions);
        this.gameScore.setText("Score : " + numberGoodAnswer + "/" + previousQuestion);

        if (flashcardManager.getGame().endOfGame()) {
            displayedVBox.getChildren().clear();
            Label fin = new Label("Fin de la Partie\n Merci d'avoir joué");
            fin.setTextAlignment(TextAlignment.CENTER);

            displayedVBox.getChildren().add(fin);
            goBackButton.setVisible(true);
        } else {
            showQuestion();
            timerPlay();
        }
    }
}
