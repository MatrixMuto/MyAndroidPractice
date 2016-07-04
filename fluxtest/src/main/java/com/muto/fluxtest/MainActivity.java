package com.muto.fluxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rvListFoo)
    RecyclerView rvListFoo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        FooLayoutManager layoutManager = new FooLayoutManager();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        FirstAdapter adapter = new FirstAdapter();


        rvListFoo.setLayoutManager(layoutManager);
        rvListFoo.setAdapter(adapter);
    }
}
