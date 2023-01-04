package flashcards;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
import flashcards.model.DeckManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        DeckManager allDeck = new DeckManager("Papa");
        Gson gson = new Gson();
        Type DeclList = new TypeToken<ArrayList<Deck>>() {
        }.getType();

        ArrayList<Deck> newDeck = gson.fromJson(new FileReader("decks.json"), DeclList);
        allDeck.setDeckManager(newDeck);
        scene = new Scene(loadFXML("homeCreation", allDeck), 900, 650);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml, DeckManager allDeck) throws IOException {
        scene.setRoot(loadFXML(fxml, allDeck));
    }

    private static Parent loadFXML(String fxml, DeckManager allDeck) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml + ".fxml"));
        if (fxml.equals("homeCreation")) {
            fxmlLoader.setControllerFactory(controllerClass -> new HomeCreationController(allDeck));
        } else if (fxml.equals("editCreation")) {
            fxmlLoader.setControllerFactory(controllerClass -> new EditCreationController(allDeck));
        } else if (fxml.equals("homeLearning")) {
            fxmlLoader.setControllerFactory(controllerClass -> new HomeLearningController(allDeck));
        } else {
            fxmlLoader.setControllerFactory(controllerClass -> new GameLearningController(allDeck));
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}