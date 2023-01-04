package flashcards.controller;

import java.io.IOException;

import flashcards.App;

public class editCreationController {

    public editCreationController() {
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation");
    }
}
