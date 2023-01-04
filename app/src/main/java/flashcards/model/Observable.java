package flashcards.model;

import java.util.ArrayList;

import flashcards.controller.Observer;

public class Observable {
    private ArrayList<Observer> observers;

    public Observable(){ this.observers = new ArrayList<Observer>(); }

    public void addObserver(Observer obs) { this.observers.add(obs); }
    public void triggerObserver() {
        for (Observer observer : this.observers) {
            observer.react();
        }
    }
}

