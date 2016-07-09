package to.mu.akg;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        List<String> strings = new ArrayList<>();
//        strings.add("test1");
//        SimpleAdapter simpleAdapter = new SimpleAdapter(strings,);
//        ListView listView = (ListView) findViewById(R.id.listView);
//
//        if (listView != null)
//            listView.setAdapter(simpleAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//        MediaCodecIn
//        MedaCodec

        try {
            listCodec();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Intent intent = new Intent(this, MangoActivity.class)
                        .setData(Uri.parse("rtmp://172.17.196.3:1935/live/test"));
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @TargetApi(21)
    private void listCodec() throws IOException {
        String test = "";
        MediaCodecList list = new MediaCodecList(MediaCodecList.ALL_CODECS);
        MediaCodecInfo[] infos = list.getCodecInfos();
        for (MediaCodecInfo info : infos) {
            test += info.getName() + "\n";
        }
        MediaFormat format = new MediaFormat();
//        format.setLong(MediaFormat.KEY_BITRATE_MODE, );
        String encoder = list.findEncoderForFormat(format);
//        MediaCodec codec = MediaCodec.createByCodecName(encoder);
////        codec.configure();
//        codec.start();
//
//        codec.stop();
//        codec.setVideoScalingMode();
        Log.d("test", test);
    }
}
