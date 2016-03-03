package com.muto.knife_stone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.alorma.github.sdk.services.repos.GithubReposClient;
import com.alorma.github.sdk.services.repos.UserReposClient;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScrollingActivity extends AppCompatActivity {

    @Bind(R.id.nest_textview)
    TextView mTextView;

    @Bind(R.id.btn_login)
    Button mLoginBtn;

    @OnClick(R.id.btn_login)
    public void onLogin(View view)
    {
        String text="";
//        {
//            GitHubClient client = ServiceGenerator.createService(GitHubClient.class);
//
//            // Fetch and print a list of the contributors to this library.
//            Call<List<Contributor>> call =
//                    client.contributors("fs_opensource", "android-boilerplate");
//            try {
//                List<Contributor> contributors = call.execute().body();
//
//                for (Contributor contributor : contributors) {
//                    text += contributor.login + " (" + contributor.contributions + ")";
//                }
//            } catch (IOException e) {
//                // handle errors
//            }
//        }
//
//        {
//            LoginService loginService =
//                    ServiceGenerator.createService(LoginService.class, "user", "secretpassword");
//            try {
//                Call<User> call = loginService.basicLogin();
//                User user = call.execute().body();
//            }
//            catch (IOException e) {
//
//            }
//
//        }
//        {
//            UserService userService =
//                    ServiceGenerator.createService(UserService.class, "auth-token");
//            try {
//                Call<User> call = userService.me();
//                User user = call.execute().body();
//            }
//            catch (IOException e) {
//
//            }
//        }

//        GithubReposClient client = new UserReposClient(getActivity(), username);
//        client.observable()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe();

        return ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
