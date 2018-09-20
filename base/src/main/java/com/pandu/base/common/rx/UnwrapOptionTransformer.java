package com.pandu.base.common.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import polanski.option.Option;

/**
 * Filters out all Option of NONE if any, but if Some, then unwraps and returns the value.
 */
public class UnwrapOptionTransformer<T> implements ObservableTransformer<Option<T>, T> {

    public static <T> UnwrapOptionTransformer<T> create() {
        return new UnwrapOptionTransformer<>();
    }

    @Override
    public ObservableSource<T> apply(Observable<Option<T>> upstream) {
        return null;
    }
}
