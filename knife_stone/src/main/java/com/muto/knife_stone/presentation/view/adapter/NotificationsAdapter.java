package com.muto.knife_stone.presentation.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alorma.github.sdk.bean.dto.response.Notification;
import com.muto.knife_stone.R;
import com.muto.knife_stone.presentation.FirstApplication;
import com.muto.knife_stone.presentation.model.NotificationModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by muto on 16-3-17.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    public static final String TAG = NotificationsAdapter.class.getSimpleName();
    private boolean DEBUG = FirstApplication.DEBUG;

    List<Notification> mNotificationList;

    public NotificationsAdapter()
    {
        mNotificationList = Collections.emptyList();
    }

    public void setNotificationCollection(Collection<Notification> notiCollection) {
        mNotificationList = (List<Notification>) notiCollection;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onUserItemClicked(Notification userModel);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(DEBUG)
            Log.d(TAG,"onCreateViewHolder " + viewType);
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NotificationsAdapter.ViewHolder holder, int position) {
        if(DEBUG)
            Log.d(TAG,"onBindViewHolder " + position);
        final Notification data = mNotificationList.get(position);
        holder.text1.setText(data.repository.full_name);
        holder.text2.setText(data.subject.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NotificationsAdapter.this.onItemClickListener != null){
                    onItemClickListener.onUserItemClicked(data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mNotificationList != null) ? mNotificationList.size() : 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @Bind(R.id.textView3)
        TextView text1;

        @Bind(R.id.imageView)
        ImageView imageView;

        @Bind(R.id.textView4)
        TextView text2;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

}
