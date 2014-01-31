package ru.mw.moneyutils;

import java.util.HashMap;
import java.util.Map;

class BidirectionalMap<K extends Object, V extends Object> {

    private final Map<K, V> forward = new HashMap<K, V>();

    private final Map<V, K> backward = new HashMap<V, K>();

    public synchronized void put(K key, V value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    public synchronized V getForward(K key) {
        return forward.get(key);
    }

    public synchronized K getBackward(V value) {
        return backward.get(value);
    }
}