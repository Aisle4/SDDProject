package sdd.aisle4android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 3/25/2017.
 */

public abstract class Event<T> {
    public List<T> listeners = new ArrayList<T>();
    public void attach(T listener) {
        listeners.add(listener);
    }
    public void dettach(T listener) {
        listeners.remove(listener);
    }
}