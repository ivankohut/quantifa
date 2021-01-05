package sk.ivankohut.quantifa.utils;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KeyOnlyEntry<K, V> implements Map.Entry<K, V> {

    private final K key;

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }
}
