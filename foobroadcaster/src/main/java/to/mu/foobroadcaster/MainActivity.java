package to.mu.foobroadcaster;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements Handler.Callback {

    private HandlerThread handlerThread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handlerThread = new HandlerThread("foo1");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), this);
        Log.d("tag", stringFromJNI());
        handler.sendMessage(handler.obtainMessage(1));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    public native String stringFromJNI();

    static {
        System.loadLibrary("hello-jni");
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:


                return true;
        }
        return false;
    }
}
