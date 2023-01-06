package flashcards.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import flashcards.App;
import flashcards.model.Card;
import flashcards.model.FlashcardManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.Node;

public class EditCreationController implements Observer, Initializable {

    private FlashcardManager flashcardManager;
    private int activeDeck = 0;
    private int activeCard = 0;
    private int activeQuestionContent = 0;
    private int activeAnswerContent = 0;

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
    @FXML
    private VBox listCard;
    @FXML
    private Button addCardButton;
    @FXML
    private Button delCardButton;
    @FXML
    private Button delContentQuestionButton;
    @FXML
    private Button delContentAnswerButton;

    public EditCreationController(FlashcardManager flashcardManager, int activeDeck) {
        this.flashcardManager = flashcardManager;
        this.activeDeck = activeDeck;
    }

    public void switchToHomeCreation() throws IOException {
        activeCard = 0;
        react();
        App.setRoot("homeCreation", flashcardManager, 0);
    }

    public void addCard() {
        flashcardManager.getDeck(activeDeck).add(new Card());
        updateModel();
        activeCard = flashcardManager.getDeck(activeDeck).getCards().size() - 1;

        react();
    }

    public void delCard() {
        if (!(flashcardManager.getDeck(activeDeck).getCards().size() == 1)) {
            flashcardManager.getDeck(activeDeck).remove(activeCard);
            activeCard--;
            if (activeCard < 0) {
                activeCard = 0;
            }
        }
        react();
    }

    public void addQuestionText() {
        buttonPressed = questionAddTextButton;
        updateModel();
        flashcardManager.getCard(activeDeck, activeCard).addQuestionContentText("Texte");
        react();
    }

    public void addQuestionMedia() {
        buttonPressed = questionAddMediaButton;
        updateModel();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(VboxQuestion.getScene().getWindow());
        if (file == null) {
            return;
        }
        Path path = Paths.get(file.getAbsolutePath());
        flashcardManager.getCard(activeDeck, activeCard).addQuestionContentMultimedia(path.toString(),
                getFileType(path.toString()));
        react();
    }

    public void delQuestionContent() {
        delContentQuestionButton.setStyle(null);
        activeQuestionContent = Integer.parseInt(delContentQuestionButton.getId());
        if (flashcardManager.getCard(activeDeck, activeCard).getQuestion().size() != 1) {
            flashcardManager.getCard(activeDeck, activeCard).getQuestion().remove(activeQuestionContent);
            activeQuestionContent = 0;
        }
        react();
    }

    public void delAnswerContent() {
        delContentAnswerButton.setStyle(null);
        activeAnswerContent = Integer.parseInt(delContentAnswerButton.getId());
        if (flashcardManager.getCard(activeDeck, activeCard).getAnswer().size() != 1) {
            flashcardManager.getCard(activeDeck, activeCard).getAnswer().remove(activeAnswerContent);
            activeAnswerContent--;
        }
        react();
    }

    public void addAnswerText() {
        buttonPressed = answerAddTextButton;
        updateModel();
        flashcardManager.getCard(activeDeck, activeCard).addAnswerContentText("Texte");
        react();
    }

    public void addAnswerMedia() {
        buttonPressed = answerAddMediaButton;
        updateModel();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(VboxAnswer.getScene().getWindow());
        if (file == null) {
            return;
        }
        Path path = Paths.get(file.getAbsolutePath());
        flashcardManager.getCard(activeDeck, activeCard).addAnswerContentMultimedia(path.toString(),
                getFileType(path.toString()));
        react();
    }

    public void validationClick() {
        buttonPressed = validationButton;
        react();
    }

    public void validate() throws IOException {
        updateModel();
        react();
        switchToHomeCreation();
    }

    public void updateModel() {
        if (!name.getText().isEmpty()) {
            flashcardManager.getDeck(activeDeck).setName(name.getText());
            activeDeck = flashcardManager.sortByName(flashcardManager.getDeck(activeDeck));

        } else {
            flashcardManager.getDeck(activeDeck).setName("Deck sans nom");
        }
        if (!description.getText().isEmpty()) {
            flashcardManager.getDeck(activeDeck).setDescription(description.getText());
        } else {
            flashcardManager.getDeck(activeDeck).setDescription("Deck sans description");
        }

        for (int index = 0; index < VboxQuestion.getChildren().size(); index++) {
            HBox HboxQchild = (HBox) VboxQuestion.getChildren().get(index);
            Node child = HboxQchild.getChildren().get(0);
            if (child instanceof TextField) {
                flashcardManager.getCard(activeDeck, activeCard).setQuestionContent(index,
                        ((TextField) child).getText());
            }
            if (child instanceof Label) {
                flashcardManager.getCard(activeDeck, activeCard).setQuestionContent(index,
                        ((Label) child).getText());
                flashcardManager.getCard(activeDeck, activeCard).setQuestionContentType(index,
                        getFileType(((Label) child).getText()));
            }
        }

        for (int index = 0; index < VboxAnswer.getChildren().size(); index++) {
            HBox HboxAchild = (HBox) VboxAnswer.getChildren().get(index);
            Node child = HboxAchild.getChildren().get(0);
            if (child instanceof TextField) {
                flashcardManager.getCard(activeDeck, activeCard).setAnswerContent(index,
                        ((TextField) child).getText());
            }
            if (child instanceof Label) {
                flashcardManager.getCard(activeDeck, activeCard).setAnswerContent(index,
                        ((Label) child).getText());
                flashcardManager.getCard(activeDeck, activeCard).setAnswerContentType(index,
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

        return "TEXT";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (flashcardManager.getDeck(activeDeck).getCards().isEmpty()) {
            flashcardManager.getDeck(activeDeck).add(new Card());
        }
        flashcardManager.addObserver(this);

        name.setText(flashcardManager.getDeck(activeDeck).getName());
        description.setText(flashcardManager.getDeck(activeDeck).getDescription());
        VboxQuestion.getChildren().remove(0);
        updateViewQuestions();
        VboxAnswer.getChildren().remove(0);
        updateViewAnswers();
        listCard.getChildren().clear();
        // ButtonBar addDelBar = new ButtonBar();
        // addDelBar.getButtons().addAll(addCardButton, delCardButton);
        // listCard.getChildren().add(addDelBar);
        // Iterate through all decks in the deck manager
        for (int k = 0; k < flashcardManager.getDeck(activeDeck).getCards().size(); k++) {
            // If there is still space on the current row, add the deck button to it
            Button cardj = new Button("carte n°" + k);
            cardj.setId(Integer.toString(k));
            cardj.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;");
            // Set the action for when this button is pressed
            int index = k;
            cardj.setOnAction(event -> {
                updateModel();
                activeCard = index;
                buttonPressed = cardj;
                cardj.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;-fx-background-color: lightblue");
                react();
            });
            listCard.getChildren().add(cardj);
            VBox.setMargin(cardj, new Insets(10, 0, 0, 10));
        }
    }

    @Override
    public void react() {
        VboxQuestion.getChildren().clear();
        updateViewQuestions();
        VboxAnswer.getChildren().clear();
        updateViewAnswers();

        if (buttonPressed != null) {
            buttonPressed.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;-fx-background-color: lightblue");

        }
        listCard.getChildren().clear();
        // ButtonBar addDelBar = new ButtonBar();
        // addDelBar.getButtons().addAll(addCardButton, delCardButton);
        // listCard.getChildren().add(addDelBar);
        // Iterate through all decks in the deck manager
        for (int k = 0; k < flashcardManager.getDeck(activeDeck).getCards().size(); k++) {
            // If there is still space on the current row, add the deck button to it
            Button cardj = new Button("carte n°" + k);
            // Button cardj = new Button(flashcardManager.getCard(activeDeck,
            // k).getQuestionContent(0).getData());
            cardj.setId(Integer.toString(k));
            cardj.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;");

            // cardj.prefWidthProperty().bind(listCard.prefWidthProperty());
            if (cardj.getId().equals(Integer.toString(activeCard))) {
                cardj.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;-fx-background-color: lightblue");
            }
            // Set the action for when this button is pressed
            int index = k;
            cardj.setOnAction(event -> {
                updateModel();
                activeCard = index;
                buttonPressed = cardj;
                cardj.setStyle("-fx-pref-width: 130px; -fx-pref-height: 100px;-fx-background-color: lightblue");
                react();
            });
            listCard.getChildren().add(cardj);
            VBox.setMargin(cardj, new Insets(10, 10, 0, 10));
        }

    }

    protected void updateViewQuestions() {
        for (int i = 0; i < flashcardManager.getCard(activeDeck, activeCard).getQuestion().size(); i++) {
            Button delQuestionButton = new Button("-");
            int index = i;
            delQuestionButton.setOnAction(event -> {
                delContentQuestionButton = delQuestionButton;
                delContentQuestionButton.setId(Integer.toString(index));
                delQuestionContent();
            });
            if (i == 0) {
                delQuestionButton.setVisible(false);
            }
            if (flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("TEXT")) {

                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new HBox(
                                new TextField(flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i)
                                        .getData()),
                                delQuestionButton));

            } else if (flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType()
                    .equals("IMAGE")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new HBox(
                                new Label(flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i)
                                        .getData()),
                                delQuestionButton));

            } else if (flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType()
                    .equals("SON")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new HBox(
                                new Label(flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i)
                                        .getData()),
                                delQuestionButton));

            } else if (flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType()
                    .equals("VIDEO")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new HBox(
                                new Label(flashcardManager.getCard(activeDeck, activeCard).getQuestionContent(i)
                                        .getData()),
                                delQuestionButton));

            }

        }
    }

    protected void updateViewAnswers() {
        for (int j = 0; j < flashcardManager.getCard(activeDeck, activeCard).getAnswer().size(); j++) {
            Button delAnswerButton = new Button("-");
            int index = j;
            delAnswerButton.setOnAction(event -> {
                delContentAnswerButton = delAnswerButton;
                delContentAnswerButton.setId(Integer.toString(index));
                delAnswerContent();
            });
            if (j == 0) {
                delAnswerButton.setVisible(false);
            }

            if (flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType().equals("TEXT")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new HBox(
                                new TextField(
                                        flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getData()),
                                delAnswerButton));

            } else if (flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("IMAGE")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new HBox(
                                new Label(
                                        flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getData()),
                                delAnswerButton));

            } else if (flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("SON")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new HBox(
                                new Label(
                                        flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getData()),
                                delAnswerButton));
            } else if (flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("VIDEO")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size() - 1,
                        new HBox(
                                new Label(
                                        flashcardManager.getCard(activeDeck, activeCard).getAnswerContent(j).getData()),
                                delAnswerButton));
            }
        }
    }
}
