package sdd.aisle4android;

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
abstract class Event<T> {
    List<T> listeners = new ArrayList<T>();
    void attach(T listener) {
        listeners.add(listener);
    }
    void dettach(T listener) {
        listeners.remove(listener);
    }
}