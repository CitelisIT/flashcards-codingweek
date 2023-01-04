package flashcards.model;

import java.util.ArrayList;
import java.util.Random;

public class RandomMode implements StrategieAlgorithm {
    Random random = new Random();

    public void computeSequence(int length, Deck deck, ArrayList<Card> sequenCards) {
        for (int i = 0; i < length; i++) {
            sequenCards.add(deck.getCard(random.nextInt(deck.getCards().size())));
        }
    }
}
