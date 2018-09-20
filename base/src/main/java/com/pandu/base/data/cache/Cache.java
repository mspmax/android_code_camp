package com.pandu.base.data.cache;

import android.support.annotation.NonNull;

import com.pandu.base.common.providers.TimestampProvider;
import com.pandu.base.common.utils.ListUtils;
import com.pandu.base.data.store.Store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import polanski.option.Option;

import static polanski.option.Option.none;
import static polanski.option.Option.ofObj;

/**
 * Generic memory cache with timeout for the entries.
 */
public class Cache<K, V> implements Store.MemoryStore<K, V> {

    @NonNull
    private final TimestampProvider timestampProvider;

    @NonNull
    private final Function<V, K> extractKeyFromModel;

    @NonNull
    private final Option<Long> itemLifespanMs;

    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();

    public Cache(@NonNull final Function<V, K> extractKeyFromModel,
                 @NonNull final TimestampProvider timestampProvider) {
        this(extractKeyFromModel, timestampProvider, none());
    }

    public Cache(@NonNull final Function<V, K> extractKeyFromModel,
                 @NonNull final TimestampProvider timestampProvider,
                 final long timeoutMs) {
        this(extractKeyFromModel, timestampProvider, ofObj(timeoutMs));
    }

    private Cache(@NonNull final Function<V, K> extractKeyFromModel,
                  @NonNull final TimestampProvider timestampProvider,
                  @NonNull final Option<Long> timeoutMs) {
        this.timestampProvider = timestampProvider;
        this.itemLifespanMs = timeoutMs;
        this.extractKeyFromModel = extractKeyFromModel;
    }

    @Override
    public void putSingular(@NonNull V value) {
        Single.fromCallable(() -> extractKeyFromModel.apply(value))
                .subscribeOn(Schedulers.computation())
                .subscribe(key -> cache.put(key, createCacheEntry(value)));
    }

    @Override
    public void putAll(@NonNull List<V> values) {
        Observable.fromIterable(values)
                .toMap(extractKeyFromModel, this::createCacheEntry)
                .subscribeOn(Schedulers.computation())
                .subscribe(cache::putAll);
    }

    @Override
    @NonNull
    public Maybe<V> getSingular(@NonNull final K key) {
        return Maybe.fromCallable(() -> cache.containsKey(key))
                .filter(isPresent -> isPresent)
                .map(__ -> cache.get(key))
                .filter(this::notExpired)
                .map(CacheEntry::cachedObject)
                .subscribeOn(Schedulers.computation());
    }

    @Override
    @NonNull
    public Maybe<List<V>> getAll() {
        return Observable.fromIterable(cache.values())
                .filter(this::notExpired)
                .map(CacheEntry::cachedObject)
                .toList()
                .filter(ListUtils::isNotEmpty)
                .subscribeOn(Schedulers.computation());
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @NonNull
    private CacheEntry<V> createCacheEntry(@NonNull final V value) {
        return CacheEntry.<V>builder().cachedObject(value)
                .creationTimestamp(timestampProvider.currentTimeMillis())
                .build();
    }

    private boolean notExpired(@NonNull final CacheEntry<V> cacheEntry) {
        return itemLifespanMs.match(lifespanMs -> cacheEntry.creationTimestamp()
                                                  + lifespanMs > timestampProvider.currentTimeMillis(),
                // When lifespan was not set the items in the cache never expire
                () -> true);
    }
}
