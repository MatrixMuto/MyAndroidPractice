package to.mu.akg;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MangoActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, SurfaceTexture.OnFrameAvailableListener {

    int scrennWidth,screenHeight;
    SurfaceTexture surfaceTexture;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mango);

        TextureView textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        scrennWidth = width;
        screenHeight = height;
        this.surfaceTexture = surface;

        camera = Camera.open();
        try {
            camera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

        try {
            MediaCodec.createEncoderByType("video/avc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        camera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.d("test","" + surfaceTexture.getTimestamp());
        surfaceTexture.updateTexImage();
    }
}
