package flashcards.controller;

import java.net.URL;
import java.util.ResourceBundle;

import flashcards.model.DeckManager;
import javafx.fxml.Initializable;

public class GameLearningController implements Observer,Initializable {

    private DeckManager flashcardManager;
    private int activeDeck;

    // TODO Réfléchir à l'implémentation des médias par rapport au timer

    public GameLearningController(DeckManager flashcardManager, int activeDeck) {
        this.flashcardManager = flashcardManager;
        this.activeDeck = activeDeck;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub

    }
}
