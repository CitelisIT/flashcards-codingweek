package flashcards.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.units.qual.A;

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
    private Button validationButton = new Button("Valider");
    @FXML
    private VBox VboxQuestion = new VBox();
    @FXML
    private VBox VboxAnswer = new VBox();
    @FXML
    private TextField questionText = new TextField();
    @FXML
    private TextField answerText = new TextField();
    @FXML
    private TextField name = new TextField();
    @FXML
    private TextField description = new TextField();

    public EditCreationController(DeckManager allDeck, int activeDeck) {
        this.allDeck = allDeck;
        this.activeDeck = activeDeck;
    }

    public void switchToHomeCreation() throws IOException {
        allDeck.triggerObserver();
        App.setRoot("homeCreation", allDeck, 0);
    }

    public void addQuestionText() {
        buttonPressed = questionAddTextButton;
        allDeck.getCard(activeDeck, activeCard).addQuestionContentText("");
        allDeck.triggerObserver();
    }

    public void addQuestionMedia() {
        buttonPressed = questionAddMediaButton;
        allDeck.getCard(activeDeck, activeCard).addQuestionContentMultimedia("", "");
        allDeck.triggerObserver();
    }

    public void addAnswerText() {
        buttonPressed = answerAddTextButton;
        allDeck.getCard(activeDeck, activeCard).addAnswerContentText("");
        allDeck.triggerObserver();
    }

    public void addAnswerMedia() {
        buttonPressed = answerAddMediaButton;
        allDeck.getCard(activeDeck, activeCard).addAnswerContentMultimedia("", "");
        allDeck.triggerObserver();
    }

    public void validationClick() {
        buttonPressed = validationButton;
        allDeck.triggerObserver();
    }

    public void validate() throws IOException {
        updateModel();
        allDeck.triggerObserver();
        switchToHomeCreation();
        System.out.println(allDeck.getDeck(activeDeck).getName());
    }

    public void updateModel() {
        if (!name.getText().isEmpty()) {
            allDeck.getDeck(activeDeck).setName(name.getText());
            System.out.println(allDeck.getDeck(activeDeck).getName());
            System.out.println(name.getText());
        } else {
            allDeck.getDeck(activeDeck).setName("Deck sans nom");
        }
        if (!description.getText().isEmpty()) {
            allDeck.getDeck(activeDeck).setDescription(description.getText());
        } else {
            allDeck.getDeck(activeDeck).setDescription("Deck sans description");
        }

        for (int index = 1; index < VboxQuestion.getChildren().size() - 1; index++) {
            Node child = VboxQuestion.getChildren().get(index);
            if (child instanceof TextField) {
                allDeck.getCard(activeDeck, activeCard).setQuestionContent(index - 1,
                        ((TextField) child).getText());
            }
            if (child instanceof Label) {
                allDeck.getCard(activeDeck, activeCard).setQuestionContent(index - 1,
                        ((Label) child).getText());
                allDeck.getCard(activeDeck, activeCard).setQuestionContentType(index - 1,
                        getFileType(((Label) child).getText()));
            }
        }

        for (int index = 1; index < VboxAnswer.getChildren().size() - 1; index++) {
            Node child = VboxAnswer.getChildren().get(index);
            if (child instanceof TextField) {
                System.out.println(((TextField) child).getText());
                allDeck.getCard(activeDeck, activeCard).setAnswerContent(index - 1,
                        ((TextField) child).getText());
            }
            if (child instanceof Label) {
                allDeck.getCard(activeDeck, activeCard).setAnswerContent(index - 1,
                        ((Label) child).getText());
                allDeck.getCard(activeDeck, activeCard).setAnswerContentType(index - 1,
                        getFileType(((Label) child).getText()));

            }
        }
    }

    public static String getFileType(String filePath) {
        Path path = Paths.get(filePath);

        // Vérifiez si le fichier est de type son en utilisant la méthode
        // probeContentType de la classe Files
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (contentType != null && contentType.startsWith("audio")) {
            return "SON";
        }

        // Vérifiez si le fichier est de type vidéo en utilisant la méthode
        // probeContentType de la classe Files
        if (contentType != null && contentType.startsWith("video")) {
            return "VIDEO";
        }

        // Vérifiez si le fichier est de type image en utilisant la méthode
        // probeContentType de la classe Files
        if (contentType != null && contentType.startsWith("image")) {
            return "IMAGE";
        }

        return "INCONNU";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (allDeck.getDeck(0).getCards().isEmpty()) {
            allDeck.getDeck(0).add(new Card());
        }
        allDeck.addObserver(this);

        name.setText(allDeck.getDeck(activeDeck).getName());
        description.setText(allDeck.getDeck(activeDeck).getDescription());
        VboxQuestion.getChildren().remove(1);
        for (int i = 0; i < allDeck.getCard(activeDeck, activeCard).getQuestion().size(); i++) {
            if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("TEXT")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size() - 1,
                        new TextField(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));
                System.out.println("TEXT:" + allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData());

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("IMAGE")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size() - 1,
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("SON")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size() - 1,
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("VIDEO")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size() - 1,
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            }

        }
        VboxAnswer.getChildren().remove(1);
        for (int j = 0; j < allDeck.getCard(activeDeck, activeCard).getAnswer().size(); j++) {
            if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType().equals("TEXT")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size() - 1,
                        new TextField(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("IMAGE")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size() - 1,
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType().equals("SON")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size() - 1,
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("VIDEO")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size() - 1,
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            }
        }
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
