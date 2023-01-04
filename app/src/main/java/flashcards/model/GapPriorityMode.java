package flashcards.model;

import java.util.ArrayList;

public class GapPriorityMode implements StrategieAlgorithm {

    public void computeSequence(int length, Deck deck, ArrayList<Card> sequenCards) {
        for (int i = 0; i < length; i++) {
            sequenCards.add(deck.getCard(i % deck.getCards().size()));
        }
    }
}
