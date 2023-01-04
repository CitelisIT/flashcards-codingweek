package flashcards.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.checkerframework.checker.index.qual.IndexFor;

import flashcards.App;
import flashcards.model.DeckManager;
import javafx.css.Size;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.Node;

public class EditCreationController implements Observer, Initializable {

    private DeckManager allDeck;
    private int activeDeck;
    private int activeCard;
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
        System.out.println(allDeck);
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation", allDeck);
    }

    public void addQuestionText() {
        buttonPressed = questionAddTextButton;
        react();
    }

    public void addQuestionMedia() {
        buttonPressed = questionAddMediaButton;
        react();
    }

    public void addAnswerText() {
        buttonPressed = answerAddTextButton;
        react();
    }

    public void addAnswerMedia() {
        buttonPressed = answerAddMediaButton;
        react();
    }

    public void validate() {
        for (int index = 1; index < VboxQuestion.getChildren().size() - 1; index++) {
            Node child = VboxQuestion.getChildren().get(index);
            if (child instanceof TextField) {
                allDeck.getDeck(activeDeck).getCard(activeCard).setQuestionContent(index,
                        ((TextField) child).getText());
            }
        }
        allDeck.triggerObserver();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void react() {
        if (buttonPressed != null) {
            buttonPressed.setStyle("-fx-background-color: lightgreen");
        }
        if (buttonPressed.equals(questionAddTextButton)) {
            TextField questionText = new TextField();
            questionText.setPromptText("Texte");
            VBox questionTextVbox = VboxQuestion;
            questionTextVbox.getChildren().add(questionTextVbox.getChildren().size() - 1, questionText);
        } else if (buttonPressed.equals(questionAddMediaButton)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(VboxQuestion.getScene().getWindow());
            Path path = Paths.get(file.getAbsolutePath());
        } else if (buttonPressed.equals(answerAddTextButton)) {
            TextField answerText = new TextField();
            answerText.setPromptText("Texte");
            VBox answerTextVbox = VboxAnswer;
            answerTextVbox.getChildren().add(answerTextVbox.getChildren().size() - 1, answerText);
        } else if (buttonPressed.equals(answerAddMediaButton)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(VboxAnswer.getScene().getWindow());
            Path path = Paths.get(file.getAbsolutePath());
        }
    }
}
