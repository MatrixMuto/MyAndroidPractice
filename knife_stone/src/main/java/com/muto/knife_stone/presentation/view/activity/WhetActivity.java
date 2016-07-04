package com.muto.knife_stone.presentation.view.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.repos.GithubReposClient;
import com.alorma.github.sdk.services.repos.UserReposClient;
import com.alorma.gitskarios.core.Pair;
import com.muto.knife_stone.R;
import com.muto.knife_stone.presentation.view.adapter.FooAdapter;
import com.muto.knife_stone.presentation.view.fragment.FooFragment;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WhetActivity extends AppCompatActivity {

    private static final String TAG = WhetActivity.class.getSimpleName();

    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.container)
    ViewPager mViewPager;

    private FooAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whet);
        ButterKnife.bind(this);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_menu_camera).setText(R.string.camera));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_menu_camera).setText(R.string.camera));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_menu_camera).setText(R.string.camera));

        mSectionsPagerAdapter = new FooAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //TODO: Deep in TabLayout and ViewPager
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }
}
