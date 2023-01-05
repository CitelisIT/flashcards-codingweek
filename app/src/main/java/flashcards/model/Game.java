package flashcards.model;

import java.util.ArrayList;

public class Game {
    private Deck deck;
    private StrategieAlgorithm algoComputeSequence;
    private int nbCards;
    private int currentCardIndex = 0;
    private int numberGoodAnswer = 0;
    private ArrayList<Card> sequenceCards = new ArrayList<Card>();

    public Game(int nbCards, String chosenAlgo, Deck deck) {
        this.nbCards = nbCards;
        this.deck = deck;
        switch (chosenAlgo) {
            case "Combler ses lacunes":
                this.algoComputeSequence = new GapPriorityMode();
                break;
            case "Valider ses acquis":
                this.algoComputeSequence = new KnownPriorityMode();
                break;

            default:
                this.algoComputeSequence = new RandomMode();
                break;
        }
        this.algoComputeSequence.computeSequence(this.nbCards, this.deck, this.sequenceCards);
    }

    public Deck getDeck() {
        return deck;
    }

    public void nextCard() {
        if (currentCardIndex < nbCards) {
            currentCardIndex++;
        }
    }

    public void incrGoodAnswer() {
        this.numberGoodAnswer++;
    }

    public Card getCurrentCard() {
        return sequenceCards.get(currentCardIndex);
    }

    public int getNbCards() {
        return nbCards;
    }

    public int getCurrentCardIndex() {
        return currentCardIndex;
    }

    public ArrayList<Card> getSequenceCards() {
        return sequenceCards;
    }

    public int getNbGoodAnswer() {
        return this.numberGoodAnswer;
    }

    public boolean endOfGame() {
        return this.getCurrentCardIndex() == this.getSequenceCards().size();
    }
}
