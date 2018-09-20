package com.pandu.base.presentation.providers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.util.Pair;

import com.pandu.base.common.utils.StringUtils;
import com.pandu.base.injection.qualifiers.ForApplication;

import javax.inject.Inject;

/**
 * Provides access to string resources to classes that have not access
 * to Context (publishers, mappers, etc.)
 */
public class StringProvider {

    @NonNull
    private final Context context;

    @NonNull
    private final StringUtils stringUtils;

    @Inject
    StringProvider(@NonNull @ForApplication final Context context,
                   @NonNull final StringUtils stringUtils) {
        this.context = context;
        this.stringUtils = stringUtils;
    }

    @NonNull
    public String getString(@StringRes final int resId) {
        return context.getString(resId);
    }

    @NonNull
    public String getString(@StringRes final int resId, @NonNull final Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    /**
     * Use to replace the placeholders for strings that use the format "text {{placeholder}} text".
     *
     * @param stringResId   string resource id
     * @param substitutions substitutions
     * @return string
     */
    @SuppressWarnings("unchecked")
    public String getStringAndApplySubstitutions(@StringRes final int stringResId,
                                                 @NonNull final Pair<String, String>... substitutions) {
        return stringUtils.applySubstitutionsToString(context.getString(stringResId), substitutions);
    }
}
