package to.mu.mediaprojectiontest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

public class MainActivity extends AppCompatActivity {

    MyHandler handler = new MyHandler();
    private static final int REQUEST_MEDIA_PROJECTION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {

        }
        handler.sendEmptyMessageDelayed(100, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handler.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    MediaProjectionManager media_projection_manager;

    class MyHandler extends Handler {
        private int resultCode;
        private Intent resultData;
        private MediaProjection media_projection;
        private VirtualDisplay virtual_display;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    media_projection_manager = (MediaProjectionManager)
                            getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                    startActivityForResult(
                            media_projection_manager.createScreenCaptureIntent(),
                            REQUEST_MEDIA_PROJECTION);


                    break;
                case 101:
                    media_projection = media_projection_manager.getMediaProjection(resultCode,resultData);
                    EGL14.eglCreateContext()
                    GLES20.gl
                    SurfaceTexture st = new SurfaceTexture();
                    Surface surface = new Surface();
                    virtual_display = media_projection.createVirtualDisplay(
                        "name", 640, height, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                            null,null,null);
                default:
                    break;
            }
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_MEDIA_PROJECTION) {
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }

                this.resultCode = resultCode;
                this.resultData = data;
                handler.sendEmptyMessage(101);
            }
        }
    }

}
