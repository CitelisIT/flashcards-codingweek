package flashcards;

import org.junit.jupiter.api.Test;

import flashcards.model.Deck;
import flashcards.model.Card;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    void TestDeck() {
        Deck deck = new Deck();
        assertEquals("Nouvelle pile", deck.getName());
        assertEquals("Décrivez votre pile", deck.getDescription());
        deck.setName("Deck 1");
        deck.setDescription("Deck 1 description");
        assertEquals("Deck 1", deck.getName());
        assertEquals("Deck 1 description", deck.getDescription());
        Card card = new Card();
        card.incrApperenceCount();
        card.incrApperenceCount();
        card.incrApperenceCount();
        card.incrRightCount();
        card.incrRightCount();
        deck.add(card);
        assertEquals(2 / 3, deck.getScore());
        Card card2 = new Card();
        card2.incrApperenceCount();
        card2.incrApperenceCount();
        card2.incrRightCount();
        deck.add(card2);
        assertEquals(card2, deck.getCard(1));
        assertEquals(1 / 2, deck.getScore());
        assertEquals(2, deck.getSize());
        assertEquals(card, deck.getBestCard());
        deck.remove(0);
        assertEquals(0, deck.getScore());
        assertEquals(1, deck.getSize());
        deck.remove(0);
        assertEquals(0, deck.getSize());

        Deck deck2 = new Deck();
        deck2.setName("Deck 2");
        deck2.setDescription("Deck 2 description");
        assertEquals(0, deck2.getScore()); // Thanks to the try/catch in Deck.java

    }
}
