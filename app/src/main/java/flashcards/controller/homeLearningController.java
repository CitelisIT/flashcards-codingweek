package flashcards.controller;

import java.io.IOException;

import flashcards.App;

public class homeLearningController {

    public homeLearningController() {
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation");
    }
}
