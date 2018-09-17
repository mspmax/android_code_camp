package com.pandu.base.common.providers;

import javax.inject.Inject;

/**
 * Inject this instead of using System.currentTimeMillis()
 */
public class TimestampProvider {

    @Inject
    TimestampProvider() {
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
