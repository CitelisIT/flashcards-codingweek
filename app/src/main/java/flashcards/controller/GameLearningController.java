package flashcards.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import flashcards.App;
import flashcards.model.Card;
import flashcards.model.Content;
import flashcards.model.FlashcardManager;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public GameLearningController(FlashcardManager flashcardManager, int activeDeck) {
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
        App.setRoot("homeLearning", this.flashcardManager, 0);
    }

    public void timerPlay() {
        this.timer = 0;
        this.maxTimer = this.flashcardManager.getGame().getTimer();
        this.timerProgressBar.setMaxWidth(Double.MAX_VALUE);
        this.timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    this.timerProgressBar.setProgress((double) ++this.timer / this.maxTimer);
                    if (this.timer == this.maxTimer) {
                        this.goodAnswerButton.setVisible(true);
                        this.badAnswerButton.setVisible(true);
                        try {
                            showAnswer();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        this.timeline.stop();
                    }
                }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public void showAnswer() throws FileNotFoundException {
        Card displayedCard = this.flashcardManager.getGame().getCurrentCard();
        for (int index = 0; index < displayedCard.getAnswer().size(); index++) {
            Content answer = displayedCard.getAnswerContent(index);
            if (answer.getDataType().equals("TEXT")) {
                Label answerLabel = new Label(answer.getData());
                this.displayedVBox.getChildren().add(answerLabel);
            } else if (answer.getDataType().equals("IMAGE")) {
                InputStream stream = new FileInputStream(answer.getData());
                ImageView answerImageView = new ImageView(new Image(stream));
                this.displayedVBox.getChildren().add(answerImageView);
            } else {
                Media answerMedia = new Media("file:" + answer.getData());
                MediaPlayer mediaPlayer = new MediaPlayer(answerMedia);
                mediaPlayer.setAutoPlay(true);
                MediaView mediaView = new MediaView(mediaPlayer);
                if (answer.getDataType().equals("SON")) {
                    InputStream stream = getClass().getClassLoader().getResourceAsStream("sound.png");
                    ImageView answerImageView = new ImageView(new Image(stream));
                    answerImageView.setFitWidth(50);
                    answerImageView.setFitHeight(50);
                    this.displayedVBox.getChildren().add(answerImageView);
                } else {
                    mediaView.preserveRatioProperty();
                    mediaView.setFitHeight(300);
                }
                this.displayedVBox.getChildren().addAll(mediaView);
            }
        }
    }

    public void showQuestion() throws FileNotFoundException {
        boolean timerLaunched = false;
        this.displayedVBox.getChildren().clear();
        this.goBackButton.setVisible(false);
        Card displayedCard = this.flashcardManager.getGame().getCurrentCard();
        for (int index = 0; index < displayedCard.getQuestion().size(); index++) {
            Content question = displayedCard.getQuestionContent(index);
            if (question.getDataType().equals("TEXT")) {
                Label questionLabel = new Label(question.getData());
                this.displayedVBox.getChildren().add(questionLabel);
            } else if (question.getDataType().equals("IMAGE")) {
                InputStream stream = new FileInputStream(question.getData());
                ImageView questionImageView = new ImageView(new Image(stream));
                this.displayedVBox.getChildren().add(questionImageView);
            } else {
                Media questionMedia = new Media("file:" + question.getData());
                MediaPlayer mediaPlayer = new MediaPlayer(questionMedia);
                mediaPlayer.setAutoPlay(true);
                MediaView mediaView = new MediaView(mediaPlayer);
                if (question.getDataType().equals("SON")) {
                    InputStream stream = getClass().getClassLoader().getResourceAsStream("sound.png");
                    ImageView questionImageView = new ImageView(new Image(stream));
                    questionImageView.setFitWidth(50);
                    questionImageView.setFitHeight(50);
                    this.displayedVBox.getChildren().add(questionImageView);
                } else {
                    mediaView.preserveRatioProperty();
                    mediaView.setFitHeight(300);
                }
                mediaPlayer.setOnEndOfMedia(() -> {
                    timerPlay();
                });
                timerLaunched = true;
                this.displayedVBox.getChildren().addAll(mediaView);
            }
        }
        if (!timerLaunched) {
            timerPlay();
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
        this.gameStatus.setText(currentQuestion + "/" + nbQuestions);

        int previousQuestion = this.flashcardManager.getGame().getCurrentCardIndex();
        int numberGoodAnswer = this.flashcardManager.getGame().getNbGoodAnswer();
        this.gameStatus.setText(currentQuestion + "/" + nbQuestions);
        this.gameScore.setText("Score : " + numberGoodAnswer + "/" + previousQuestion);

        if (flashcardManager.getGame().endOfGame()) {
            this.gameStatus.setText(nbQuestions + "/" + nbQuestions);
            displayedVBox.getChildren().clear();
            Label fin = new Label("Fin de la Partie\n Merci d'avoir joué");
            fin.setTextAlignment(TextAlignment.CENTER);

            this.displayedVBox.getChildren().add(fin);
            this.goBackButton.setVisible(true);
        } else {
            try {
                showQuestion();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
