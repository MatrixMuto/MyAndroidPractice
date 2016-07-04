package com.muto.fluxtest;

import android.support.v7.widget.RecyclerView;

/**
 * Created by muto on 16-3-17.
 */
public class FooLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
}
