package flashcards.model;

import java.util.ArrayList;

public class Game {
    private Deck deck;
    private StrategyAlgorithm algoComputeSequence;
    private int nbCards;
    private int currentCardIndex = 0;
    private int numberGoodAnswer = 0;
    private int timer;
    private ArrayList<Card> sequenceCards = new ArrayList<Card>();

    public Game(int nbCards, String chosenAlgo, Deck deck, int timer) {
        this.nbCards = nbCards;
        this.deck = deck;
        this.timer = timer;
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
        return this.deck;
    }

    public int getTimer() {
        return this.timer;
    }

    public Card getCurrentCard() {
        return this.sequenceCards.get(currentCardIndex);
    }

    public int getNbCards() {
        return this.nbCards;
    }

    public int getCurrentCardIndex() {
        return this.currentCardIndex;
    }

    public ArrayList<Card> getSequenceCards() {
        return this.sequenceCards;
    }

    public int getNbGoodAnswer() {
        return this.numberGoodAnswer;
    }

    public void nextCard() {
        if (this.currentCardIndex < this.nbCards) {
            this.currentCardIndex++;
        }
    }

    public void incrGoodAnswer() {
        this.numberGoodAnswer++;
    }

    public boolean endOfGame() {
        return this.getCurrentCardIndex() == this.getSequenceCards().size();
    }
}
