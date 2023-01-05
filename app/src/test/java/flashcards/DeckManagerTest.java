package flashcards;

import org.junit.jupiter.api.Test;

import flashcards.model.DeckManager;
import flashcards.model.Card;
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

        Card card = new Card();
        card.incrApperenceCount();
        card.incrApperenceCount();
        card.incrRightCount();
        deck.add(card); // Score = 1/2
        Card card2 = new Card();
        card2.incrApperenceCount();
        card2.incrApperenceCount();
        card2.incrApperenceCount();
        card2.incrRightCount();
        card2.incrRightCount();
        deck.add(card2); // Score = 2/3
        Card card3 = new Card();
        card3.incrApperenceCount();
        card3.incrApperenceCount();
        card3.incrRightCount();
        deck2.add(card3); // Score = 1/2
        Card card4 = new Card();
        card4.incrApperenceCount();
        card4.incrApperenceCount();
        card4.incrApperenceCount();
        card4.incrRightCount();
        card4.incrRightCount();
        card4.incrRightCount();
        deck2.add(card4); // Score = 3/3
        assertEquals(7, deckManager.getRightAnswers());
        assertEquals(3, deckManager.getWrongAnswers());
        assertEquals(card4, deckManager.getBestCard());
        assertEquals(deck, deckManager.getBestDeck());
        deckManager.removeDeck(0);
        assertEquals(deck2, deckManager.getDeck(0));

    }

}
