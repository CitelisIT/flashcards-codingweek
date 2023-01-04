package flashcards.controller;

import java.io.IOException;

import flashcards.App;

public class HomeLearningController implements Observer {

    public HomeLearningController() {
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation");
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub

    }
}
