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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Card getCard(int i) {
        return cards.get(i);
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(int i) {
        cards.remove(i);
    }

    public long getScore() throws ArithmeticException {
        int denom = 0;
        int num = 0;
        try {
            for (Card card : cards) {
                denom += card.getApperenceCount();
                num += card.getRightCount();
            }
            long score = num / denom;
            return score;
        } catch (ArithmeticException e) {
            return 0;
        }
    }
}
