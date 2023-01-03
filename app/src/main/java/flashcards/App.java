package flashcards;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        // if (fxml.equals("primary")) {
        // fxmlLoader.setControllerFactory(iC -> new CarnetController(carnet));

        // } else {
        // fxmlLoader.setControllerFactory(iC -> new ListController(carnet));
        // }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}