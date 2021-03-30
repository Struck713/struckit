package uk.nstr.util.tuple;

import lombok.Getter;

@Getter
public class Pair<K, V> {

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }


    private K key;
    private V value;

    protected Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }


}
