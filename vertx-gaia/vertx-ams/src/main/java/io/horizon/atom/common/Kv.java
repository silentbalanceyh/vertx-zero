package io.horizon.atom.common;

import java.util.Map;
import java.util.Objects;

/*
 * [Data Structure]
 * Definition for `key = value` pair here
 * It stored `key = value` and act as Pair here for some spec usage
 */
public final class Kv<K, V> {
    private K key;
    private V value;

    private Kv(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Kv<K, V> create() {
        return new Kv<>(null, null);
    }

    public static <K, V> Kv<K, V> create(final K key,
                                         final V value) {
        return new Kv<>(key, value);
    }

    public K key() {
        return this.key;
    }

    public V value() {
        return this.value;
    }

    public void set(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public void set(final V value) {
        this.value = value;
    }

    public boolean valid() {
        return Objects.nonNull(this.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof final Map.Entry<?, ?> e) {
            return Objects.equals(this.key, e.getKey()) &&
                Objects.equals(this.value, e.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Kv{" +
            "key=" + this.key +
            ", value=" + this.value +
            '}';
    }
}
