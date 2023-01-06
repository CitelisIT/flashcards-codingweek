package flashcards.model;

import java.util.ArrayList;
import java.util.Random;

public class GapPriorityMode implements StrategyAlgorithm {

    ArrayList<Card> draw = new ArrayList<Card>();
    Random random = new Random();

    public void computeSequence(int length, Deck deck, ArrayList<Card> sequenCards) {
        for (Card card : deck.getCards()) {
            for (int i = 0; i < (1 - card.getScore()) * 10 + 1; i++) {
                this.draw.add(card);
            }
        }
        for (int i = 0; i < length; i++) {
            sequenCards.add(this.draw.get(this.random.nextInt(this.draw.size())));
        }
    }
}
