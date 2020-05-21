package obsv;

import java.util.ArrayList;

public class Observable {

    private ArrayList<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<>();
    }
    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void notifyObservers() {
        for(Observer o : observers) {
            o.update();
        }
    }
}
