package flashcards;

import java.util.ArrayList;

public class Deck {

    private String name;
    private String description;

    public Deck() {
        this.name = "Nouvelle pile";
        this.description = "Décrivez votre pile";
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

    public void add() {

    }

}
