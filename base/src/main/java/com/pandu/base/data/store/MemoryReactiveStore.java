package com.pandu.base.data.store;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import polanski.option.Option;
import polanski.option.function.Func1;

import static polanski.option.Option.none;
import static polanski.option.Option.ofObj;

/**
 * This reactive store has only a memory cache as form of storage.
 */
public class MemoryReactiveStore<K, V> implements ReactiveStore<K, V> {

    @NonNull
    private final Store.MemoryStore<K, V> cache;

    @NonNull
    private final Func1<V, K> extractKeyFromModel;

    @NonNull
    private final Subject<Option<List<V>>> allSubject;

    @NonNull
    private final Map<K, Subject<Option<V>>> subjectMap = new HashMap<>();

    public MemoryReactiveStore(@NonNull final Func1<V, K> extractKeyFromModel,
                               @NonNull final Store.MemoryStore<K, V> cache) {
        this.allSubject = PublishSubject.<Option<List<V>>>create().toSerialized();
        this.cache = cache;
        this.extractKeyFromModel = extractKeyFromModel;
    }

    @Override
    public Observable<Option<V>> getSingular(@NonNull K key) {
        return Observable.defer(() -> getOrCreateSubjectForKey(key).startWith(getValue(key)))
                .observeOn(Schedulers.computation());
    }

    @Override
    public Observable<Option<List<V>>> getAll() {
        return Observable.defer(() -> allSubject.startWith(getAllValues()))
                .observeOn(Schedulers.computation());
    }

    @Override
    public void storeSingular(@NonNull V model) {
        final K key = extractKeyFromModel.call(model);
        cache.putSingular(model);
        getOrCreateSubjectForKey(key).onNext(ofObj(model));
        // One item has been added/updated, notify to all as well
        final Option<List<V>> allValues = cache.getAll().map(Option::ofObj).blockingGet(none());
        allSubject.onNext(allValues);
    }

    @Override
    public void storeAll(@NonNull List<V> modelList) {
        cache.putAll(modelList);
        allSubject.onNext(ofObj(modelList));
        // Publish in all the existing single item streams.
        // This could be improved publishing only in the items that changed. Maybe use DiffUtils?
        publishInEachKey();
    }

    @Override
    public void replaceAll(@NonNull List<V> modelList) {
        cache.clear();
        storeAll(modelList);
    }

    @NonNull
    private Option<V> getValue(@NonNull final K key) {
        return cache.getSingular(key).map(Option::ofObj).blockingGet(none());
    }

    @NonNull
    private Option<List<V>> getAllValues() {
        return cache.getAll().map(Option::ofObj).blockingGet(none());
    }

    @NonNull
    private Subject<Option<V>> getOrCreateSubjectForKey(@NonNull final K key) {
        synchronized (subjectMap) {
            return ofObj(subjectMap.get(key)).orDefault(() -> createAndStoreNewSubjectForKey(key));
        }
    }

    @NonNull
    private Subject<Option<V>> createAndStoreNewSubjectForKey(@NonNull final K key) {
        final Subject<Option<V>> processor = PublishSubject.<Option<V>>create().toSerialized();
        synchronized (subjectMap) {
            subjectMap.put(key, processor);
        }
        return processor;
    }

    /**
     * Publishes the cached data in each independent stream only if it exists already.
     */
    private void publishInEachKey() {
        final Set<K> keySet;
        synchronized (subjectMap) {
            keySet = new HashSet<>(subjectMap.keySet());
        }
        for (K key : keySet) {
            final Option<V> value = cache.getSingular(key).map(Option::ofObj).blockingGet(none());
            publishInKey(key, value);
        }
    }

    /**
     * Publishes the cached value if there is an already existing stream for the passed key.
     * The case where there isn't a stream for the passed key
     * means that the data for this key is not being consumed and therefore there is no need
     * to publish.
     */
    private void publishInKey(@NonNull final K key, @NonNull final Option<V> model) {
        final Subject<Option<V>> processor;
        synchronized (subjectMap) {
            processor = subjectMap.get(key);
        }
        ofObj(processor).ifSome(it -> it.onNext(model));
    }
}
