package flashcards.controller;

import java.io.IOException;

import flashcards.App;

public class EditCreationController implements Observer {

    public EditCreationController() {
    }

    public void switchToHomeCreation() throws IOException {
        App.setRoot("homeCreation");
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub
        
    }
}
