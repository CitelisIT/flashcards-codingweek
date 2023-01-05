package flashcards.controller;

import java.net.URL;
import java.util.ResourceBundle;

import flashcards.model.Card;
import flashcards.model.DeckManager;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class GameLearningController implements Observer, Initializable {

    private DeckManager flashcardManager;
    private int activeDeck;
    private int activeCard;

    private int timer;
    private Timeline timeline;

    @FXML
    private ProgressBar timerProgressBar;
    @FXML
    private Button goodAnswerButton;
    @FXML
    private Button badAnswerButton;
    @FXML
    private Label gameDeckTitle;
    @FXML
    private Label gameStatus;

    // TODO Réfléchir à l'implémentation des médias par rapport au timer

    public GameLearningController(DeckManager flashcardManager, int activeDeck) {
        this.flashcardManager = flashcardManager;
        this.activeDeck = activeDeck;
    }

    // new Deck dans le HomeLearning
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.flashcardManager.setDefaultGame(this.flashcardManager.getDeck(this.activeDeck));
        this.goodAnswerButton.setVisible(false);
        this.badAnswerButton.setVisible(false);
        react();
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
                        timeline.stop();
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void goodAnswer() {
        flashcardManager.getCard(activeDeck, activeCard).incrRightCount();
        goodAnswerButton.setVisible(false);
        badAnswerButton.setVisible(false);
        react();
    }

    public void badAnswer() {
        flashcardManager.getCard(activeDeck, activeCard).incrRightCount();
        goodAnswerButton.setVisible(false);
        badAnswerButton.setVisible(false);
        react();
    }

    @Override
    public void react() {
        Card currentCard = this.flashcardManager.getGame().getCurrentCard();
        this.gameDeckTitle.setText(this.flashcardManager.getDeck(activeDeck).getName());
        int currentQuestion = this.flashcardManager.getGame().getCurrentCardIndex() + 1;
        int nbQuestions = this.flashcardManager.getGame().getSequenceCards().size();
        this.gameStatus.setText(currentQuestion + "/" + nbQuestions);
        timerPlay();
    }
}
