package com.example.fooone;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = ((Button) findViewById(R.id.button));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlankFragment fragment = new BlankFragment();
                FragmentManager m = getSupportFragmentManager();
                FragmentTransaction t = m.beginTransaction();
                t.replace(R.id.container,fragment);
                t.commit();
            }
        });
    }
}
