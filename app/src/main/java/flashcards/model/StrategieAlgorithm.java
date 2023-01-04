package flashcards.model;

import java.util.ArrayList;

public interface StrategieAlgorithm {

    public void computeSequence(int length, Deck deck, ArrayList<Card> sequenCards);
}
