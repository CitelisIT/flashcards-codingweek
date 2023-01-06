package flashcards;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import flashcards.controller.EditCreationController;
import flashcards.controller.GameLearningController;
import flashcards.controller.HomeCreationController;
import flashcards.controller.HomeLearningController;
import flashcards.model.Deck;
import flashcards.model.FlashcardManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FlashcardManager flashcardManager = new FlashcardManager();
        Gson gson = new Gson();
        Type DeclList = new TypeToken<ArrayList<Deck>>() {}.getType();

        try {
            ArrayList<Deck> newDeck = gson.fromJson(new FileReader("decks.json"), DeclList);
            flashcardManager.setDeckManager(newDeck);
        } catch (FileNotFoundException e) {}
        scene = new Scene(loadFXML("homeCreation", flashcardManager, 0), 900, 650);
        stage.setScene(scene);
        stage.setTitle("Application flashcard");
        stage.show();
    }

    public static void setRoot(String fxml, FlashcardManager flashcardManager, int activeDeck) throws IOException {
        scene.setRoot(loadFXML(fxml, flashcardManager, activeDeck));
    }

    private static Parent loadFXML(String fxml, FlashcardManager flashcardManager, int activeDeck) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml + ".fxml"));
        if (fxml.equals("homeCreation")) {
            fxmlLoader.setControllerFactory(controllerClass -> new HomeCreationController(flashcardManager));
        } else if (fxml.equals("editCreation")) {
            fxmlLoader.setControllerFactory(controllerClass -> new EditCreationController(flashcardManager, activeDeck));
        } else if (fxml.equals("homeLearning")) {
            fxmlLoader.setControllerFactory(controllerClass -> new HomeLearningController(flashcardManager));
        } else {
            fxmlLoader.setControllerFactory(controllerClass -> new GameLearningController(flashcardManager));
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}