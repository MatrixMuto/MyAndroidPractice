package com.matrixmuto.myandroidpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DocserverActivity extends AppCompatActivity {

    @Bind(R.id.button) Button mCheckBtn;
    @Bind(R.id.textView3) TextView mTextView;

    @OnClick(R.id.button)
    public void check(View view)
    {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://doc-server")
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //here is in another thread.
                final String resp =response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(resp);
                    }
                });

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docserver);
        ButterKnife.bind(this);
    }
}
