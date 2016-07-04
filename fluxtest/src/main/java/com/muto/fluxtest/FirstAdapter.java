package com.muto.fluxtest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by muto on 16-3-15.
 */
public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.ViewHolder> {
    public static final String TAG = FirstAdapter.class.getSimpleName();
    private boolean DEBUG = true;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tvFirstAdapter);
        }
    }

    public ArrayList<String> mData = Cheeses.randomList(10);

    // Provide a suitable constructor (depends on the kind of dataset)
    public FirstAdapter() {

    }

    public interface OnItemClickListener {
        void onUserItemClicked(Object userModel);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FirstAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        if(DEBUG)
            Log.d(TAG,"onCreateViewHolder " + viewType);
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_first_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //TextView v = new TextView(parent.getContext());
        //v.setText("test");
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(DEBUG)
            Log.d(TAG,"onBindViewHolder " + position);
        final String data = mData.get(position);
        holder.mTextView.setText(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (FirstAdapter.this.onItemClickListener != null){
                onItemClickListener.onUserItemClicked(data);
            }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(DEBUG)
            Log.d(TAG,"getItemCount " + mData.size());
        return mData.size();
    }
}
