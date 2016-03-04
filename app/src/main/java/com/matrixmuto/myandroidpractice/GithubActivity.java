package com.matrixmuto.myandroidpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GithubActivity extends AppCompatActivity {

    @OnClick(R.id.button2) void OnButton2Click()
    {

    }

    @OnClick(R.id.button3) void OnButton3Click()
    {

    }

    @OnClick(R.id.button4) void OnButton4Click()
    {

    }

    @OnClick(R.id.button5) void OnButton5Click()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        ButterKnife.bind(this);
    }
}
