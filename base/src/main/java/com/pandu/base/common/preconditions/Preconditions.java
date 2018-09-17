package com.pandu.base.common.preconditions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Checks if the object reference is not null.
     *
     * @param object an object reference
     * @return the non-null reference
     * @throws NullPointerException if {@code object} is null
     */
    @NonNull
    public static <T> T get(@Nullable final T object) {
        if (object == null) {
            throw new NullPointerException("Assertion for a nonnull object failed.");
        }
        return object;
    }

    /**
     * Checks if the object reference is not null.
     *
     * @param object an object reference
     * @return non-null reference
     * @throws NullPointerException if {@code object} is null
     */
    public static <T> T checkNotNull(final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    /**
     * Checks if the object reference is not null.
     *
     * @param object       an object reference
     * @param errorMessage message used if the check fails
     * @return non-null reference
     * @throws NullPointerException if {@code object} is null
     */
    public static <T> T checkNotNull(final T object, @NonNull final String errorMessage) {
        if (object == null) {
            throw new NullPointerException(get(errorMessage));
        }
        return object;
    }
}
