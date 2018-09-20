package com.pandu.base.presentation;

import android.content.Context;

public abstract class BaseInjectingFragment extends BaseFragment {

    @Override
    public void onAttach(final Context context) {
        onInject();
        super.onAttach(context);
    }

    public abstract void onInject();
}
