package flashcards.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import flashcards.App;
import flashcards.model.Card;
import flashcards.model.Content;
import flashcards.model.FlashcardManager;
import flashcards.model.Game;
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

    private FlashcardManager flashcardManager;
    private int timer;
    private int maxTimer;
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

    public GameLearningController(FlashcardManager flashcardManager) {
        this.flashcardManager = flashcardManager;
        this.flashcardManager.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        this.timer = 0;
        this.maxTimer = this.flashcardManager.getGame().getTimer();
        this.timerProgressBar.setMaxWidth(Double.MAX_VALUE);
        this.timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    this.timerProgressBar.setProgress((double) ++this.timer / maxTimer);
                    if (timer == maxTimer) {
                        this.goodAnswerButton.setVisible(true);
                        this.badAnswerButton.setVisible(true);
                        showAnswer();
                        this.timeline.stop();
                    }
                }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public void showAnswer() {
        Card displayedCard = this.flashcardManager.getGame().getCurrentCard();
        for (int index = 0; index < displayedCard.getAnswer().size(); index++) {
            Content answer = displayedCard.getAnswerContent(index);
            if (answer.getDataType().equals("TEXT")) {
                Label answerLabel = new Label(answer.getData());
                this.displayedVBox.getChildren().add(answerLabel);

            } else {
                // TODO : gestion des media LOL
            }
        }
    }

    public void showQuestion() {
        this.displayedVBox.getChildren().clear();
        this.goBackButton.setVisible(false);
        Card displayedCard = this.flashcardManager.getGame().getCurrentCard();
        for (int index = 0; index < displayedCard.getQuestion().size(); index++) {
            Content question = displayedCard.getQuestionContent(index);
            if (question.getDataType().equals("TEXT")) {
                Label answerLabel = new Label(question.getData());
                this.displayedVBox.getChildren().add(answerLabel);

            } else {
                // TODO : gestion des media LOL
            }
        }
    }

    public void goodAnswer() {
        this.goodAnswerButton.setVisible(false);
        this.badAnswerButton.setVisible(false);
        this.flashcardManager.updateGoodAnswer();
    }

    public void badAnswer() {
        this.goodAnswerButton.setVisible(false);
        this.badAnswerButton.setVisible(false);
        this.flashcardManager.updateBadAnswer();
    }

    @Override
    public void react() {
        this.cardProgressBar.setMaxWidth(Double.MAX_VALUE);
        this.cardProgressBar.setProgress((double) this.flashcardManager.getGame().getCurrentCardIndex()
                / this.flashcardManager.getGame().getSequenceCards().size());

        this.gameDeckTitle.setText(this.flashcardManager.getGame().getDeck().getName());

        int currentQuestion = this.flashcardManager.getGame().getCurrentCardIndex() + 1;
        int nbQuestions = this.flashcardManager.getGame().getSequenceCards().size();
        if (currentQuestion >= nbQuestions) {
            this.gameStatus.setText(nbQuestions + "/" + nbQuestions);
        } else {
            this.gameStatus.setText(currentQuestion + "/" + nbQuestions);
        }

        int previousQuestion = this.flashcardManager.getGame().getCurrentCardIndex();
        int numberGoodAnswer = this.flashcardManager.getGame().getNbGoodAnswer();
        this.gameScore.setText("Score : " + numberGoodAnswer + "/" + previousQuestion);

        if (this.flashcardManager.getGame().endOfGame()) {
            this.displayedVBox.getChildren().clear();

            Label end = new Label("Fin de la Partie\nMerci d'avoir joué");
            end.setTextAlignment(TextAlignment.CENTER);

            this.displayedVBox.getChildren().add(end);
            this.goBackButton.setVisible(true);
        } else {
            showQuestion();
            timerPlay();
        }
    }
}
