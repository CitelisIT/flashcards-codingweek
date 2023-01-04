package flashcards.controller;

import java.io.IOException;

import flashcards.App;

public class homeCreationController implements Observer {

    public homeCreationController() {
    }

    public void switchToHomeLearning() throws IOException {
        App.setRoot("homeLearning");
    }

    public void switchToEditCreation() throws IOException {
        App.setRoot("editCreation");
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub
        
    }
}
