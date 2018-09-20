package com.pandu.base.presentation.rcv;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.pandu.base.common.preconditions.AndroidPreconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    @NonNull
    private final List<DisplayableItem> modelItems = new ArrayList<>();

    @NonNull
    private final ItemComparator comparator;

    @NonNull
    private final Map<Integer, ViewHolderFactory> factoryMap;

    @NonNull
    private final Map<Integer, ViewHolderBinder> binderMap;

    @NonNull
    private final AndroidPreconditions androidPreconditions;

    public RecyclerViewAdapter(@NonNull ItemComparator comparator,
                               @NonNull Map<Integer, ViewHolderFactory> factoryMap,
                               @NonNull Map<Integer, ViewHolderBinder> binderMap,
                               @NonNull AndroidPreconditions androidPreconditions) {
        this.comparator = comparator;
        this.factoryMap = factoryMap;
        this.binderMap = binderMap;
        this.androidPreconditions = androidPreconditions;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, int viewType) {
        return factoryMap.get(viewType).createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final @NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final DisplayableItem item = modelItems.get(position);
        binderMap.get(item.type()).bind(viewHolder, item);
    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return modelItems.get(position).type();
    }

    /**
     * Updates modelItems currently stored in adapter with the new modelItems.
     *
     * @param items collection to update the previous values
     */
    @MainThread
    public void update(@NonNull final List<DisplayableItem> items) {
        androidPreconditions.assertUiThread();

        if (modelItems.isEmpty()) {
            updateAllItems(items);
        } else {
            updateDiffItemsOnly(items);
        }
    }

    /**
     * This is only used for the first update of the adapter, when it is still empty.
     */
    private void updateAllItems(@NonNull final List<DisplayableItem> items) {
        Single.just(items)
                .doOnSuccess(this::updateItemsInModel)
                .subscribe(__ -> notifyDataSetChanged());
    }

    /**
     * Do not use for first update of the adapter. The method {@link DiffUtil.DiffResult#dispatchUpdatesTo(RecyclerView.Adapter)}
     * is significantly slower than {@link RecyclerViewAdapter#notifyDataSetChanged()}
     * when it comes to update all the items in the adapter.
     */
    private void updateDiffItemsOnly(@NonNull final List<DisplayableItem> items) {
        // IMPROVEMENT: The diff calculation should happen in the background
        Single.fromCallable(() -> calculateDiff(items))
                .doOnSuccess(__ -> updateItemsInModel(items))
                .subscribe(this::updateAdapterWithDiffResult);
    }

    private DiffUtil.DiffResult calculateDiff(@NonNull final List<DisplayableItem> newItems) {
        return DiffUtil.calculateDiff(new DiffUtilCallback(modelItems, newItems, comparator));
    }

    private void updateItemsInModel(@NonNull final List<DisplayableItem> items) {
        modelItems.clear();
        modelItems.addAll(items);
    }

    private void updateAdapterWithDiffResult(@NonNull final DiffUtil.DiffResult result) {
        result.dispatchUpdatesTo(this);
    }
}
