package com.muto.knife_stone.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.muto.knife_stone.presentation.view.activity.FooActivity;
import com.muto.knife_stone.presentation.view.activity.PrtTestActivity;
import com.muto.knife_stone.presentation.view.activity.TouchActivity;

import java.io.IOException;
import java.util.List;
import java.util.logging.StreamHandler;

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

/**
 * Created by muto on 16-3-16.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class FooFragment extends Fragment {
    public static final String TAG = FooFragment.class.getSimpleName();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @Bind(R.id.button)
    Button btnOne;

    @OnClick(R.id.button)
    public void onBtnOneClick(View view)
    {
        GithubReposClient client = new UserReposClient("MatrixMuto",null);
        client.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Pair<List<Repo>, Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Pair<List<Repo>, Integer> pair) {
                        List<Repo> list = pair.first;
                        for (Repo repo : list) {
                            Log.d(TAG, repo.full_name);
                        }

                    }
                });
    }

    @Bind(R.id.button2)
    Button btnTwo;

    @OnClick(R.id.button2)
    public void StartFoo()
    {
//            Intent i = new Intent("com.muto.knife_stone.ui.activity.FooActivity");
        Intent i = new Intent();
        i.setClass(getContext(),FooActivity.class);
        startActivity(i);
    }

    @Bind(R.id.button3)
    Button btnThree;

    @OnClick(R.id.button3)
    public void StartPtr()
    {
//            Intent i = new Intent("com.muto.knife_stone.ui.activity.FooActivity");
        Intent i = new Intent();
        i.setClass(getContext(),PrtTestActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.button4)
    public void StartTouch()
    {
//            Intent i = new Intent("com.muto.knife_stone.ui.activity.FooActivity");
        Intent i = new Intent();
        i.setClass(getContext(),TouchActivity.class);
        startActivity(i);
    }

    @Bind(R.id.textView)
    TextView textView;

    @OnClick(R.id.button4)
    public void onBtnFourClick(View view)
    {
        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
//            Request request = builder.url("https://api.github.com/users/octocat/orgs").build();

//            RequestBody body = RequestBody.create(JSON,"{\n" +
//                    "  \"scopes\": [\n" +
//                    "    \"public_repo\"\n" +
//                    "  ],\n" +
//                    "  \"note\": \"admin script\"\n" +
//                    "}");
        Request request = builder.url("https://api.github.com").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(body);
                    }
                });
            }
        });
    }
    public FooFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FooFragment newInstance(int sectionNumber) {
        FooFragment fragment = new FooFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
        ButterKnife.bind(this,rootView);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
