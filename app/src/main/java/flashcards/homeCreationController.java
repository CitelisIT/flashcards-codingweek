package flashcards;

import java.io.IOException;

public class homeCreationController {

    public homeCreationController() {
    }

    public void switchToHomeLearning() throws IOException {
        App.setRoot("homeApprentisage");
    }

    public void switchToEditCreation() throws IOException {
        App.setRoot("editCreation");
    }
}
