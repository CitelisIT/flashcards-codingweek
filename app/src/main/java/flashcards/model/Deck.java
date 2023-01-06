package flashcards.model;

import java.util.ArrayList;

public class Deck extends Observable {

    private String name;
    private String description;
    private ArrayList<Card> cards;

    public Deck() {
        this.name = "Nouvelle pile";
        this.description = "Décrivez votre pile";
        this.cards = new ArrayList<Card>();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public int getSize() {
        return this.cards.size();
    }

    public Card getCard(int i) {
        return this.cards.get(i);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void add(Card card) {
        this.cards.add(card);
    }

    public void remove(int i) {
        this.cards.remove(i);
    }

    public long getScore() throws ArithmeticException {
        int denom = 0;
        int num = 0;
        try {
            for (Card card : this.cards) {
                denom += card.getApperenceCount();
                num += card.getRightCount();
            }
            long score = num / denom;
            return score;
        } catch (ArithmeticException e) {
            return 0;
        }
    }

    public int getRightAnswers() {
        int num = 0;
        for (Card card : this.cards) {
            num += card.getRightCount();
        }
        return num;
    }

    public int getWrongAnswers() {
        int num = 0;
        for (Card card : this.cards) {
            num += card.getApperenceCount() - card.getRightCount();
        }
        return num;
    }

    public Card getBestCard() {
        Card bestCard = this.cards.get(0);
        for (Card card : this.cards) {
            if (card.getScore() > bestCard.getScore()) {
                bestCard = card;
            }
        }
        return bestCard;
    }

    public Card getWorstCard() {
        Card worstCard = this.cards.get(0);
        for (Card card : this.cards) {
            if (card.getScore() < worstCard.getScore()) {
                worstCard = card;
            }
        }
        return worstCard;
    }
}
