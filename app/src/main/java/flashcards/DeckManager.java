package flashcards;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class DeckManager {

    private String name;
    private ArrayList<Deck> deckManager;

    public DeckManager(String name) {
        this.name = name;
        this.deckManager = new ArrayList<Deck>();
    }

    public void addPile(Deck pile) {
        deckManager.add(pile);
    }

    public void exportdeckManager() throws IOException {
        String filename = "app/src/main/resources/" + this.name + ".json";
        Path path = Paths.get(filename);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this.deckManager, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DeckManager importdeckManager(String filename)
            throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        Gson gson = new Gson();
        DeckManager newDeck = gson.fromJson(new FileReader(filename), DeckManager.class);
        return newDeck;
    }

    public void save() {
        String filename = "app/src/main/resources/decks.json";
        Path path = Paths.get(filename);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonFilename = "app/src/main/resources/" + this.name + ".json";
            gson.toJson(jsonFilename, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Deck getPile(int index) {
        return deckManager.get(index);
    }

    public void removePile(int index) {
        deckManager.remove(index);
    }
}
