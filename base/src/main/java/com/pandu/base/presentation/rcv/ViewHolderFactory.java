package com.pandu.base.presentation.rcv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class ViewHolderFactory {

    @NonNull
    protected final Context context;

    protected ViewHolderFactory(@NonNull final Context context) {
        this.context = context;
    }

    /**
     * Creates a {@link RecyclerView.ViewHolder}
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @return the newly created {@link RecyclerView.ViewHolder}
     */
    @NonNull
    public abstract RecyclerView.ViewHolder createViewHolder(@NonNull final ViewGroup parent);
}
