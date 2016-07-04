package com.muto.knife_stone.presentation.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.alorma.github.sdk.bean.dto.response.Notification;
import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.notifications.GetNotificationsClient;
import com.alorma.github.sdk.services.repos.GithubReposClient;
import com.alorma.github.sdk.services.repos.UserReposClient;
import com.alorma.gitskarios.core.Pair;
import com.alorma.gitskarios.core.client.TokenProvider;
import com.alorma.gitskarios.core.client.TokenProviderInterface;
import com.muto.knife_stone.R;
import com.muto.knife_stone.presentation.presenter.NotificationsPresenter;
import com.muto.knife_stone.presentation.view.NotificationsView;
import com.muto.knife_stone.presentation.view.adapter.FirstAdapter;
import com.muto.knife_stone.presentation.view.adapter.NotificationsAdapter;
import com.muto.knife_stone.presentation.view.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PrtTestActivity extends AppCompatActivity implements NotificationsView {
    public static final String TAG = PrtTestActivity.class.getSimpleName();

    @Bind(R.id.store_house_ptr_frame)
    PtrFrameLayout ptrFrameLayout;

    @Bind(R.id.ptr_test_recycler_view)
    RecyclerView rcvNotifcation;

    LinearLayoutManager mLayoutManager;
    NotificationsPresenter mNotificationPresenter;
    NotificationsAdapter mNotiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prt_test);
        ButterKnife.bind(this);

        initPtrFrameLayout();

        mLayoutManager = new LinearLayoutManager(this);
        mNotiAdapter = new NotificationsAdapter();
        mNotiAdapter.setOnItemClickListener(new NotificationsAdapter.OnItemClickListener() {
            @Override
            public void onUserItemClicked(Notification userModel) {

            }
        });

        rcvNotifcation.setLayoutManager(mLayoutManager);
        rcvNotifcation.setAdapter(mNotiAdapter);
        rcvNotifcation.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        TokenProvider.setTokenProviderInstance(new TokenProviderInterface() {
            @Override
            public String getToken() {
                return "72064bb8a0c0e9e0fbf34366c51894eb0cd63b46";
            }
        });

        mNotificationPresenter = new NotificationsPresenter();
        mNotificationPresenter.setView(this);
        mNotificationPresenter.initialize();
    }


    private void initPtrFrameLayout() {
        StoreHouseHeader header = new StoreHouseHeader(this);
//        header.setPadding(0, LocalDisplay.dp2px(20), 0, LocalDisplay.dp2px(20));
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        header.setPadding(0, (int)(dm.density * 20.0f +0.5f), 0, (20));
        header.initWithString("Ultra PTR");

        ptrFrameLayout.setDurationToCloseHeader(1000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 50);
            }
        });
    }

    @Override
    public void renderUserList(Collection<Notification> notiCollection) {
        mNotiAdapter.setNotificationCollection(notiCollection);
    }
}
