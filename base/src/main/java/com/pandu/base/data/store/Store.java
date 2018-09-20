package com.pandu.base.data.store;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Interface for any type of store.
 *
 * @implNote Don't implement this directly,
 * use {@link MemoryStore} or {@link DiskStore} so it is more
 * descriptive.
 */
public interface Store<K, V> {

    @NonNull
    Maybe<V> getSingular(@NonNull final K key);

    @NonNull
    Maybe<List<V>> getAll();

    void putSingular(@NonNull final V value);

    void putAll(@NonNull final List<V> valueList);

    void clear();

    /**
     * More descriptive interface for memory based stores.
     */
    interface MemoryStore<K, V> extends Store<K, V> {
    }

    /**
     * More descriptive interface for disk based stores.
     */
    interface DiskStore<K, V> extends Store<K, V> {
    }
}
