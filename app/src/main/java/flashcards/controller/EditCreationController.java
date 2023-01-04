package flashcards.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.checkerframework.checker.index.qual.IndexFor;

import flashcards.App;
import flashcards.model.Card;
import flashcards.model.DeckManager;
import javafx.css.Size;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.Node;

public class EditCreationController implements Observer, Initializable {

    private DeckManager allDeck;
    private int activeDeck = 0;
    private int activeCard = 0;
    private Button buttonPressed;
    private Button questionAddTextButton = new Button("Question textuelle");
    private Button questionAddMediaButton = new Button("Question multimedia");
    private Button answerAddTextButton = new Button("Reponse textuelle");
    private Button answerAddMediaButton = new Button("Reponse multimedia");
    @FXML
    private VBox VboxQuestion = new VBox();
    @FXML
    private VBox VboxAnswer = new VBox();
    @FXML
    private TextField questionText = new TextField();
    @FXML
    private TextField answerText = new TextField();

    public EditCreationController(DeckManager allDeck) {
        this.allDeck = allDeck;
        System.out.println(allDeck.getDeck(0).getCards());
        System.out.println(allDeck);
    }

    public void switchToHomeCreation() throws IOException {
        allDeck.triggerObserver();
        App.setRoot("homeCreation", allDeck);
    }

    public void addQuestionText() {
        buttonPressed = questionAddTextButton;
        allDeck.triggerObserver();
    }

    public void addQuestionMedia() {
        buttonPressed = questionAddMediaButton;
        allDeck.triggerObserver();
    }

    public void addAnswerText() {
        buttonPressed = answerAddTextButton;
        allDeck.triggerObserver();
    }

    public void addAnswerMedia() {
        buttonPressed = answerAddMediaButton;
        allDeck.triggerObserver();
    }

    public void validate() {
        for (int index = 1; index < VboxQuestion.getChildren().size() - 1; index++) {
            Node child = VboxQuestion.getChildren().get(index);
            if (child instanceof TextField) {
                System.out.println(allDeck.getDeck(activeDeck));
                System.out.println(allDeck.getDeck(activeDeck).getCards());
                if (allDeck.getDeck(activeDeck).getCard(activeCard).getQuestion().size() > index - 1) {
                    allDeck.getDeck(activeDeck).getCard(activeCard).setQuestionContent(index - 1,
                            ((TextField) child).getText());
                } else {
                    allDeck.getDeck(activeDeck).getCard(activeCard)
                            .addQuestionContentText(((TextField) child).getText());
                }
            }
        }
        allDeck.triggerObserver();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (allDeck.getDeck(0).getCards().isEmpty()) {
            System.out.println("Coucou !");
            allDeck.getDeck(0).add(new Card());
            System.out.println(allDeck.getDeck(0).getCards());
        }
        allDeck.addObserver(this);
    }

    @Override
    public void react() {
        if (buttonPressed != null) {
            buttonPressed.setStyle("-fx-background-color: lightgreen");

            if (buttonPressed.equals(questionAddTextButton)) {
                TextField questionText = new TextField();
                questionText.setPromptText("Texte");
                VBox questionTextVbox = VboxQuestion;
                questionTextVbox.getChildren().add(questionTextVbox.getChildren().size() - 1, questionText);
            } else if (buttonPressed.equals(questionAddMediaButton)) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(VboxQuestion.getScene().getWindow());
                if (file == null) {
                    return;
                }
                Path path = Paths.get(file.getAbsolutePath());
                Label questionMedia = new Label(path.toString());
                VBox questionMediaVbox = VboxQuestion;
                questionMediaVbox.getChildren().add(questionMediaVbox.getChildren().size() - 1, questionMedia);
            } else if (buttonPressed.equals(answerAddTextButton)) {
                TextField answerText = new TextField();
                answerText.setPromptText("Texte");
                VBox answerTextVbox = VboxAnswer;
                answerTextVbox.getChildren().add(answerTextVbox.getChildren().size() - 1, answerText);
            } else if (buttonPressed.equals(answerAddMediaButton)) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(VboxAnswer.getScene().getWindow());
                if (file == null) {
                    return;
                }
                Path path = Paths.get(file.getAbsolutePath());
                Label answerMedia = new Label(path.toString());
                VBox answerMediaVbox = VboxAnswer;
                answerMediaVbox.getChildren().add(answerMediaVbox.getChildren().size() - 1, answerMedia);
            }
            buttonPressed = null;
        }
    }
}
