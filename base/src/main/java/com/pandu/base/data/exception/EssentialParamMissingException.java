package com.pandu.base.data.exception;

import android.support.annotation.NonNull;

/**
 * Exception thrown when an essential parameter is missing in the backend/network response.
 *
 * @implNote used this during sanity checks before saving in any
 * type of {@link com.pandu.base.data.store.Store}
 */
public class EssentialParamMissingException extends RuntimeException {

    public EssentialParamMissingException(@NonNull final String missingParam, @NonNull final Object rawObject) {
        super("The params: " + missingParam + " are missing in received object: " + rawObject);
    }
}
