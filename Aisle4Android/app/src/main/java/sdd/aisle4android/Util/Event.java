package sdd.aisle4android.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 3/25/2017.
 */

/**
 * An event instance serves as an observable in the observer pattern. Observers can attach listeners
 * and be notified when fire is called.
 * @param <T>
 */
public abstract class Event<T> {
    protected List<T> listeners = new ArrayList<T>();
    public void attach(T listener) {
        listeners.add(listener);
    }
    public void dettach(T listener) {
        listeners.remove(listener);
    }
}