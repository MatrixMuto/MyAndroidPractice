package com.muto.knife_stone.presentation.presenter;

import android.util.Log;

import com.alorma.github.sdk.bean.dto.response.Notification;
import com.alorma.github.sdk.services.notifications.GetNotificationsClient;
import com.alorma.gitskarios.core.client.TokenProvider;
import com.muto.knife_stone.presentation.view.NotificationsView;

import java.util.Collection;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by muto on 16-3-17.
 */
public class NotificationsPresenter {

    private NotificationsView mNotificationListView;

    public NotificationsPresenter()
    {

    }

    public void initialize(){
        loadNotifications();
    }

    public void loadNotifications() {
        getNotifications();
    }

    public void setView(NotificationsView view)
    {
        mNotificationListView = view;
    }

    private void showNotificationInView(Collection<Notification> notiCollection) {
//        final Collection<UserModel> userModelsCollection =
//                this.userModelDataMapper.transform(usersCollection);
        this.mNotificationListView.renderUserList(notiCollection);
    }

    private void getNotifications() {
        GetNotificationsClient nfClient = new GetNotificationsClient(TokenProvider.getInstance().getToken());
        nfClient.setAll(true);
        nfClient.observable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<List<Notification>>() {

                @Override
                public void onCompleted() {
//                    adapter.mData = reps;
//                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<Notification> notifications) {
                    showNotificationInView(notifications);
                    for(Notification n : notifications)
                    {
                        String test = n.reason
                                + " " + n.subject.title
                                + " " + n.subject.type
                                + " " + n.repository.name;

                        Log.d("FooActivity",n.reason);
                        Log.d("FooActivity",n.repository.name);
                        Log.d("FooActivity",n.subject.title);
                        Log.d("FooActivity",n.reason);
                        Log.d("FooActivity",n.reason);
                        Log.d("FooActivity",n.reason);
                    }
                }
            });
    }
}
