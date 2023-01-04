package flashcards.controller;

import java.io.IOException;

import flashcards.App;

public class homeLearningController implements Observer {

    public homeLearningController() {
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation");
    }
}
