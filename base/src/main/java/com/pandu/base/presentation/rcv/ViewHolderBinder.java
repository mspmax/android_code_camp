package com.pandu.base.presentation.rcv;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Populates a {@link RecyclerView.ViewHolder} with the model details.
 */
public interface ViewHolderBinder {

    /**
     * Populates the passed {@link RecyclerView.ViewHolder} with the details of the passed {@link DisplayableItem}.
     */
    void bind(@NonNull final RecyclerView.ViewHolder viewHolder, @NonNull final DisplayableItem item);
}
