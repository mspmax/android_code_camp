package com.pandu.base.common.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {

    private ListUtils() {
    }

    /**
     * Returns a new list containing the second list appended to the
     * first list.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return a new list containing the union of those lists
     */
    @NonNull
    public static <T> List<T> union(@NonNull final List<T> list1, @NonNull final List<T> list2) {
        List<T> unionList = new ArrayList<>();
        unionList.addAll(list1);
        unionList.addAll(list2);
        return unionList;
    }

    public static <T> boolean isNotEmpty(@NonNull final List<T> list) {
        return !list.isEmpty();
    }
}
