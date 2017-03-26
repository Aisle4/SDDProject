package sdd.aisle4android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 3/25/2017.
 */

public class Event<T> {
    private List<IListener<T>> listeners = new ArrayList<>();

    public void attach(IListener<T> listener) {
        listeners.add(listener);
    }
    public void dettach(IListener<T> listener) {
        listeners.remove(listener);
    }
    public void fire(T arg) {
        for (IListener listener : listeners) {
            listener.onEvent(this, arg);
        }
    }

    public interface IListener<T> {
        public void onEvent(Event e, T arg);
    }
}
