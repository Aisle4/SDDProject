package sdd.aisle4android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 3/25/2017.
 */

public class Event {
    private List<IListener> listeners = new ArrayList<>();

    public void attach(IListener listener) {
        listeners.add(listener);
    }
    public void dettach(IListener listener) {
        listeners.remove(listener);
    }
    public void fire() {
        for (IListener listener : listeners) {
            listener.onEvent(this);
        }
    }


    public interface IListener {
        public void onEvent(Event e);
    }
}