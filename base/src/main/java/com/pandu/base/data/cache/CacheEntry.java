package com.pandu.base.data.cache;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * Cache entry that contains the object and the creation timestamp.
 */
@AutoValue
abstract class CacheEntry<T> {
    static <T> Builder<T> builder() {
        return new AutoValue_CacheEntry.Builder<>();
    }

    @NonNull
    abstract T cachedObject();

    abstract long creationTimestamp();

    @AutoValue.Builder
    interface Builder<T> {

        Builder<T> cachedObject(T object);

        Builder<T> creationTimestamp(long creationTimestamp);

        CacheEntry<T> build();
    }
}
