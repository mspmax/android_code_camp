package com.pandu.base.data.store;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import polanski.option.Option;

/**
 * Interface for any kind of reactive store.
 */
public interface ReactiveStore<K, V> {

    Observable<Option<V>> getSingular(@NonNull final K key);

    Observable<Option<List<V>>> getAll();

    void storeSingular(@NonNull final V model);

    void storeAll(@NonNull final List<V> modelList);

    void replaceAll(@NonNull final List<V> modelList);
}
