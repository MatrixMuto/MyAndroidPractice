package com.muto.knife_stone.presentation.view.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alorma.github.sdk.bean.dto.response.Notification;
import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.notifications.GetNotificationsClient;
import com.alorma.github.sdk.services.user.UserFollowersClient;
import com.alorma.github.sdk.services.user.UserFollowingClient;
import com.alorma.gitskarios.core.Pair;
import com.alorma.gitskarios.core.client.TokenProvider;
import com.alorma.gitskarios.core.client.TokenProviderInterface;
import com.muto.knife_stone.R;
import com.muto.knife_stone.data.Cheeses;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FooActivity extends AppCompatActivity {

    public static final String TAG = FooActivity.class.getSimpleName();



    @OnClick(R.id.button7)
    public void OnBtn7Click()
    {
        GetNotificationsClient nfClient = new GetNotificationsClient(TokenProvider.getInstance().getToken());
        nfClient.observable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<List<Notification>>() {

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<Notification> notifications) {
                    for(Notification n : notifications)
                    {
                        Log.d("FooActivity",n.reason);
                    }
                }
            });
    }


    @OnClick(R.id.button8)
    public void OnBtn8Click()
    {
        UserFollowersClient ufClient = new UserFollowersClient("MatrixMuto");
        ufClient.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Pair<List<User>,Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(Pair<List<User>, Integer> pair) {
                        List<User> list = pair.first;
                        Log.d(TAG, "follower size:"+list.size());
                        for (User user : list) {
                            Log.d(TAG, pair.second +":"+user.name);
                        }
                    }
                });
        UserFollowingClient ufiClient = new UserFollowingClient("MatrixMuto");
        ufiClient.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Pair<List<User>,Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(Pair<List<User>, Integer> pair) {
                        List<User> list = pair.first;
                        for (User user : list) {
                            Log.d(TAG, pair.second +":"+user.toString());
                        }
                    }
                });
    }

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foo);
        ButterKnife.bind(this);



        handler =  new Handler();
        ListAdapter adapter = new ArrayAdapter<String>(

                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Cheeses.randomList(6));


        lvFooList.setAdapter(adapter);
        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srlContainer.setRefreshing(false);
                    }
                },1000);
            }
        });

    }

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout srlContainer;
    @Bind(R.id.mylist)
    ListView lvFooList;
}
