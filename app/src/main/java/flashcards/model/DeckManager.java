package flashcards.model;

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

import flashcards.controller.Observer;

public class DeckManager extends Observable {

    private String name;
    private ArrayList<Deck> deckManager;
    private transient ArrayList<Observer> obs = new ArrayList<Observer>();

    public DeckManager(String name) {
        this.name = name;
        this.deckManager = new ArrayList<Deck>();
    }

    public void addDeck(Deck Deck) {
        deckManager.add(Deck);
    }

    public void exportdeckManager(Deck deck, Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(deck, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Deck importdeckManager(String filename)
            throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        Gson gson = new Gson();
        Deck newDeck = gson.fromJson(new FileReader(filename), Deck.class);
        return newDeck;
    }

    public void save() {
        String filename = "decks.json";
        Path path = Paths.get(filename);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(deckManager, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Deck getDeck(int index) {
        return deckManager.get(index);
    }

    public void removeDeck(int index) {
        deckManager.remove(index);
    }

    public int getDeckManagerSize() {
        return deckManager.size();
    }

}
