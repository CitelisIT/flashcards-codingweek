package flashcards.controller;

import java.io.IOException;

import flashcards.App;
import flashcards.model.DeckManager;

public class HomeLearningController implements Observer {
    private DeckManager allDeck;

    public HomeLearningController(DeckManager allDeck) {
        this.allDeck = allDeck;
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation", allDeck);
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub

    }
}
