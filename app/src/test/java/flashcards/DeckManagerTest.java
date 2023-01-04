package flashcards;

import org.junit.jupiter.api.Test;

import flashcards.model.DeckManager;
import flashcards.model.Deck;

import static org.junit.jupiter.api.Assertions.*;

public class DeckManagerTest {

    @Test
    void TestDeckManager() {
        DeckManager deckManager = new DeckManager();

        Deck deck = new Deck();
        deck.setName("Deck 1");
        deck.setDescription("Deck 1 description");
        deckManager.addDeck(deck);

        Deck deck2 = new Deck();
        deck2.setName("Deck 2");
        deck2.setDescription("Deck 2 description");
        deckManager.addDeck(deck2);

        assertEquals(deck, deckManager.getDeck(0));
        assertEquals(deck2, deckManager.getDeck(1));

        deckManager.removeDeck(0);
        assertEquals(deck2, deckManager.getDeck(0));

    }

}
