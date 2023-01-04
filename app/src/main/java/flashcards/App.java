package flashcards;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import flashcards.controller.editCreationController;
import flashcards.controller.homeCreationController;
import flashcards.controller.homeLearningController;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("homeCreation"), 900, 650);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println("/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml + ".fxml"));
        if (fxml.equals("homeCreation")) {
            fxmlLoader.setControllerFactory(controllerClass -> new homeCreationController());
        } else if (fxml.equals("editCreation")) {
            fxmlLoader.setControllerFactory(controllerClass -> new editCreationController());
        } else if (fxml.equals("homeLearning")) {
            fxmlLoader.setControllerFactory(controllerClass -> new homeLearningController());
        // } else if (fxml.equals("gameLearning")) {
        } else {
            // fxmlLoader.setControllerFactory(controllerClass -> new gameLearningController());
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}