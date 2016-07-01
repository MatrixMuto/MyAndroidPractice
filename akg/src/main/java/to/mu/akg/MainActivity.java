package to.mu.akg;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.btn_start);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == startButton) {
            Intent intent = new Intent(this,MangoActivity.class)
                    .setData(Uri.parse("rtmp://172.17.196.3:1935/live/test"));
            startActivity(intent);
        }
    }
}
