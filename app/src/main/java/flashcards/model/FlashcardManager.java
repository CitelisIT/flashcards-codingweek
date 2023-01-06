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

public class FlashcardManager extends Observable {

    private ArrayList<Deck> deckManager;
    private Game game;

    public FlashcardManager() {
        this.deckManager = new ArrayList<Deck>();
    }

    /**
     * Adds a deck to the deck manager.
     *
     * @param Deck the deck to be added to the deck manager
     */
    public void addDeck(Deck Deck) {
        deckManager.add(Deck);
    }

    /**
     * Sorts the deck manager by name, and return the index of the parameter deck.
     * 
     * @param deck the deck to find the index of
     * @return the index of the parameter deck
     */
    public int sortByName(Deck deck) {
        deckManager.sort((Deck d1, Deck d2) -> d1.getName().compareTo(d2.getName()));
        return deckManager.indexOf(deck);
    }

    /**
     * Exports a deck to a file specified by a path.
     *
     * @param deck the deck to be exported
     * @param path the path specifying the file to which the deck is exported
     * @throws IOException if an error occurs while writing to the file
     */
    public void exportdeckManager(Deck deck, Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(deck, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imports a deck from a file specified by a path.
     *
     * @param filename the path specifying the file from which the deck is imported
     * @return the imported deck
     * @throws JsonSyntaxException   if the JSON file is not properly formatted
     * @throws JsonIOException       if an error occurs while reading the JSON file
     * @throws FileNotFoundException if the file specified by the path is not found
     */
    public Deck importdeckManager(String filename)
            throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        Gson gson = new Gson();
        Deck newDeck = gson.fromJson(new FileReader(filename), Deck.class);
        return newDeck;
    }

    /**
     * Saves the deck manager to a file.
     */
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

    /**
     * Gets a deck at a specified index in the deck manager.
     *
     * @param index the index of the deck to retrieve
     * @return the deck at the specified index
     */
    public Deck getDeck(int index) {
        return deckManager.get(index);
    }

    public ArrayList<Deck> getDeckList() {
        return this.deckManager;
    }

    /**
     * Gets a card at a specified deck in the deck manager.
     *
     * @param index the index of the deck to retrieve
     * @return the deck at the specified index
     */
    public Card getCard(int index, int cardIndex) {
        return deckManager.get(index).getCard(cardIndex);
    }

    /**
     * Gets the best card of any deck in the deck manager.
     * 
     * @return the best card of any deck in the deck manager
     */
    public Card getBestCard() {
        int bestCardIndex = 0;
        float bestCardScore = 0;
        int bestCardLocation = 0;
        for (int i = 0; i < deckManager.size(); i++) {
            for (int j = 0; j < deckManager.get(i).getSize(); j++) {
                if (deckManager.get(i).getCard(j).getScore() > bestCardScore) {
                    bestCardIndex = j;
                    bestCardScore = deckManager.get(i).getCard(j).getScore();
                    bestCardLocation = i;
                }
            }
        }
        return deckManager.get(bestCardLocation).getCard(bestCardIndex);
    }

    /**
     * Gets the worst card of any deck in the deck manager.
     * 
     * @return the worst card of any deck in the deck manager
     */
    public Card getWorstCard() {
        int worstCardIndex = 0;
        float worstCardScore = 1;
        int worstCardLocation = 0;
        for (int i = 0; i < deckManager.size(); i++) {
            for (int j = 0; j < deckManager.get(i).getSize(); j++) {
                if (deckManager.get(i).getCard(j).getScore() < worstCardScore) {
                    worstCardIndex = j;
                    worstCardScore = deckManager.get(i).getCard(j).getScore();
                    worstCardLocation = i;
                }
            }
        }
        return deckManager.get(worstCardLocation).getCard(worstCardIndex);
    }

    /**
     * Gets the best deck in the deck manager.
     * 
     * @return the best deck in the deck manager
     */
    public Deck getBestDeck() {
        int bestDeckIndex = 0;
        float bestDeckScore = 0;
        for (int i = 0; i < deckManager.size(); i++) {
            if (deckManager.get(i).getScore() > bestDeckScore) {
                bestDeckIndex = i;
                bestDeckScore = deckManager.get(i).getScore();
            }
        }
        return deckManager.get(bestDeckIndex);
    }

    /**
     * Gets the worst deck in the deck manager.
     * 
     * @return the worst deck in the deck manager
     */
    public Deck getWorstDeck() {
        int worstDeckIndex = 0;
        float worstDeckScore = 1;
        for (int i = 0; i < deckManager.size(); i++) {
            if (deckManager.get(i).getScore() < worstDeckScore) {
                worstDeckIndex = i;
                worstDeckScore = deckManager.get(i).getScore();
            }
        }
        return deckManager.get(worstDeckIndex);
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets the number of right answers for any card in the deck manager.
     * 
     * @return the number of right answers for any card in the deck manager
     */
    public int getRightAnswers() {
        int rightAnswers = 0;
        for (int i = 0; i < deckManager.size(); i++) {
            rightAnswers += deckManager.get(i).getRightAnswers();
        }
        return rightAnswers;
    }

    /**
     * Gets the number of wrong answers for any card in the deck manager.
     * 
     * @return the number of wrong answers for any card in the deck manager
     */
    public int getWrongAnswers() {
        int wrongAnswers = 0;
        for (int i = 0; i < deckManager.size(); i++) {
            wrongAnswers += deckManager.get(i).getWrongAnswers();
        }
        return wrongAnswers;
    }

    /**
     * Removes a deck at a specified index in the deck manager.
     *
     * @param index the index of the deck to remove
     */
    public void removeDeck(int index) {
        deckManager.remove(index);
    }

    /**
     * Returns the size of the deckManager.
     * 
     * @return the size of the deckManager
     */
    public int getDeckManagerSize() {
        return deckManager.size();
    }

    /**
     * Sets the value of the deckManager to the given ArrayList of Decks.
     * 
     * @param deckManager the ArrayList of Decks to set as the new deckManager
     */
    public void setDeckManager(ArrayList<Deck> deckManager) {
        this.deckManager = deckManager;
    }

    public void updateGoodAnswer() {
        this.game.getCurrentCard().incrRightCount();
        this.game.getCurrentCard().incrApperenceCount();
        this.game.incrGoodAnswer();
        this.game.nextCard();
        this.triggerObserver();
    }

    public void updateBadAnswer() {
        this.game.getCurrentCard().incrApperenceCount();
        this.game.nextCard();
        this.triggerObserver();
    }
}
