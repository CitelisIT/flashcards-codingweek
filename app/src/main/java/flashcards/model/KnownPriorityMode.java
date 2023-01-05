package flashcards.model;

import java.util.ArrayList;
import java.util.Random;

public class KnownPriorityMode implements StrategieAlgorithm {

    ArrayList<Card> tirage = new ArrayList<Card>();
    Random random = new Random();

    public void computeSequence(int length, Deck deck, ArrayList<Card> sequenCards) {
        for (Card card : deck.getCards()) {
            for (int i = 0; i < (card.getScore()) * 10 + 1; i++) {
                tirage.add(card);
            }
        }
        for (int i = 0; i < length; i++) {
            sequenCards.add(tirage.get(random.nextInt(tirage.size())));
        }
    }
}