package com.pandu.base.common.preconditions;

import android.os.Looper;

import java.util.Objects;

import javax.inject.Inject;

public class AndroidPreconditions {

    @Inject
    AndroidPreconditions() {
    }

    /**
     * Asserts that the current thread is a worker thread.
     */
    public void assertWorkerThread() {
        if (isMainThread()) {
            throw new IllegalStateException(
                    "This task must be run on a worker thread and not on the Main thread");
        }
    }

    /**
     * Asserts that the current thread is the Main Thread.
     */
    public void assertUiThread() {
        if (!isMainThread()) {
            throw new IllegalStateException(
                    "This task must be run on the Main thread and not on a worker thread.");
        }
    }

    /**
     * Returns whether the current thread is the Android main thread
     */
    private boolean isMainThread() {
        return Objects.equals(Looper.getMainLooper().getThread(), Thread.currentThread());
    }

}
