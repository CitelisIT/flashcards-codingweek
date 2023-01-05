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
import flashcards.model.DeckManager;
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
    @FXML
    private VBox listCard;
    @FXML
    private Button addCardButton;

    public EditCreationController(DeckManager allDeck, int activeDeck) {
        this.allDeck = allDeck;
        this.activeDeck = activeDeck;
    }

    public void switchToHomeCreation() throws IOException {
        activeCard = 0;
        react();
        App.setRoot("homeCreation", allDeck, 0);
    }

    public void addCard() {
        allDeck.getDeck(activeDeck).add(new Card());
        updateModel();
        activeCard = allDeck.getDeck(activeDeck).getCards().size() - 1;
        react();
    }

    public void addQuestionText() {
        buttonPressed = questionAddTextButton;
        allDeck.getCard(activeDeck, activeCard).addQuestionContentText("Texte");
        react();
    }

    public void addQuestionMedia() {
        buttonPressed = questionAddMediaButton;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(VboxQuestion.getScene().getWindow());
        if (file == null) {
            return;
        }
        Path path = Paths.get(file.getAbsolutePath());
        allDeck.getCard(activeDeck, activeCard).addQuestionContentMultimedia(path.toString(),
                getFileType(path.toString()));
        react();
    }

    public void addAnswerText() {
        buttonPressed = answerAddTextButton;
        allDeck.getCard(activeDeck, activeCard).addAnswerContentText("Texte");
        react();
    }

    public void addAnswerMedia() {
        buttonPressed = answerAddMediaButton;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(VboxAnswer.getScene().getWindow());
        if (file == null) {
            return;
        }
        Path path = Paths.get(file.getAbsolutePath());
        allDeck.getCard(activeDeck, activeCard).addAnswerContentMultimedia(path.toString(),
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
            allDeck.getDeck(activeDeck).setName(name.getText());
        } else {
            allDeck.getDeck(activeDeck).setName("Deck sans nom");
        }
        if (!description.getText().isEmpty()) {
            allDeck.getDeck(activeDeck).setDescription(description.getText());
        } else {
            allDeck.getDeck(activeDeck).setDescription("Deck sans description");
        }

        for (int index = 0; index < VboxQuestion.getChildren().size(); index++) {
            Node child = VboxQuestion.getChildren().get(index);
            if (child instanceof TextField) {
                allDeck.getCard(activeDeck, activeCard).setQuestionContent(index,
                        ((TextField) child).getText());
            }
            if (child instanceof Label) {
                allDeck.getCard(activeDeck, activeCard).setQuestionContent(index,
                        ((Label) child).getText());
                allDeck.getCard(activeDeck, activeCard).setQuestionContentType(index,
                        getFileType(((Label) child).getText()));
            }
        }

        for (int index = 0; index < VboxAnswer.getChildren().size(); index++) {
            Node child = VboxAnswer.getChildren().get(index);
            if (child instanceof TextField) {
                allDeck.getCard(activeDeck, activeCard).setAnswerContent(index,
                        ((TextField) child).getText());
            }
            if (child instanceof Label) {
                allDeck.getCard(activeDeck, activeCard).setAnswerContent(index,
                        ((Label) child).getText());
                allDeck.getCard(activeDeck, activeCard).setAnswerContentType(index,
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
        if (allDeck.getDeck(activeDeck).getCards().isEmpty()) {
            allDeck.getDeck(activeDeck).add(new Card());
        }
        allDeck.addObserver(this);

        name.setText(allDeck.getDeck(activeDeck).getName());
        description.setText(allDeck.getDeck(activeDeck).getDescription());
        VboxQuestion.getChildren().remove(0);
        for (int i = 0; i < allDeck.getCard(activeDeck, activeCard).getQuestion().size(); i++) {
            if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("TEXT")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new TextField(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("IMAGE")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("SON")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("VIDEO")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            }

        }
        VboxAnswer.getChildren().remove(0);
        for (int j = 0; j < allDeck.getCard(activeDeck, activeCard).getAnswer().size(); j++) {
            if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType().equals("TEXT")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new TextField(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("IMAGE")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType().equals("SON")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("VIDEO")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            }
        }
        listCard.getChildren().clear();
        listCard.getChildren().add(addCardButton);
        // Iterate through all decks in the deck manager
        for (int k = 0; k < allDeck.getDeck(activeDeck).getCards().size(); k++) {
            // If there is still space on the current row, add the deck button to it
            Button cardj = new Button("carte n°" + k);
            cardj.setId(Integer.toString(k));
            // Set the action for when this button is pressed
            int index = k;
            cardj.setOnAction(event -> {
                System.out.println(activeCard);
                updateModel();
                activeCard = index;
                cardj.setStyle("-fx-background-color: lightgreen");
                react();
            });
            listCard.getChildren().add(cardj);
        }
    }

    @Override
    public void react() {
        VboxQuestion.getChildren().clear();
        for (int i = 0; i < allDeck.getCard(activeDeck, activeCard).getQuestion().size(); i++) {
            if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("TEXT")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new TextField(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("IMAGE")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("SON")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getDataType().equals("VIDEO")) {
                VboxQuestion.getChildren().add(VboxQuestion.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getQuestionContent(i).getData()));

            }

        }
        VboxAnswer.getChildren().clear();
        for (int j = 0; j < allDeck.getCard(activeDeck, activeCard).getAnswer().size(); j++) {
            if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType().equals("TEXT")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new TextField(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("IMAGE")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType().equals("SON")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size(),
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            } else if (allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getDataType()
                    .equals("VIDEO")) {
                VboxAnswer.getChildren().add(VboxAnswer.getChildren().size() - 1,
                        new Label(allDeck.getCard(activeDeck, activeCard).getAnswerContent(j).getData()));

            }
        }

        if (buttonPressed != null) {
            buttonPressed.setStyle("-fx-background-color: lightgreen");

        }
        listCard.getChildren().clear();
        listCard.getChildren().add(addCardButton);
        // Iterate through all decks in the deck manager
        for (int k = 0; k < allDeck.getDeck(activeDeck).getCards().size(); k++) {
            // If there is still space on the current row, add the deck button to it
            Button cardj = new Button("carte n°" + k);
            cardj.setId(Integer.toString(k));
            // Set the action for when this button is pressed
            int index = k;
            cardj.setOnAction(event -> {
                updateModel();
                activeCard = index;
                cardj.setStyle("-fx-background-color: lightgreen");
                react();
            });
            listCard.getChildren().add(cardj);

        }

    }
}
