package to.mu.mediaprojectiontest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import java.io.File;

import to.mu.library.EglBase14;
import to.mu.library.VideoEncoderCore;

public class MainActivity extends AppCompatActivity {

    MyHandler handler = new MyHandler();
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private TextureView textureView;
    private int mScreenDensity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = (TextureView) findViewById(R.id.texture_view);
        if (savedInstanceState != null) {

        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
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

    // Return an EGLDisplay, or die trying.
    private static EGLDisplay getEglDisplay() {
        EGLDisplay eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("Unable to get EGL14 display");
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("Unable to initialize EGL14");
        }
        return eglDisplay;
    }

    // Return an EGLConfig, or die trying.
    private static EGLConfig getEglConfig(EGLDisplay eglDisplay, int[] configAttributes) {
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig(
                eglDisplay, configAttributes, 0, configs, 0, configs.length, numConfigs, 0)) {
            throw new RuntimeException("Unable to find any matching EGL config");
        }
        return configs[0];
    }

    // Return an EGLConfig, or die trying.
    private static EGLContext createEglContext(EGLDisplay eglDisplay, EGLConfig eglConfig) {
//        if (sharedContext != null && sharedContext.egl14Context == EGL14.EGL_NO_CONTEXT) {
//            throw new RuntimeException("Invalid sharedContext");
//        }
        int[] contextAttributes = {EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE};
//        EGLContext rootContext =
//                sharedContext == null ? EGL14.EGL_NO_CONTEXT : sharedContext.egl14Context;
        EGLContext eglContext =
                EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, contextAttributes, 0);
        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("Failed to create EGL context");
        }
        return eglContext;
    }


    class MyHandler extends Handler {
        private int resultCode;
        private Intent resultData;
        private MediaProjection media_projection;
        private VirtualDisplay virtual_display;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    media_projection_manager = (MediaProjectionManager)
                            getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                    startActivityForResult(
                            media_projection_manager.createScreenCaptureIntent(),
                            REQUEST_MEDIA_PROJECTION);
                    break;
                case 101:
                    media_projection = media_projection_manager.getMediaProjection(resultCode, resultData);
                    int width = 1280, height = 720;

                    final EGLDisplay eglDisplay = getEglDisplay();
                    EGLConfig eglConfig = getEglConfig(eglDisplay, EglBase14.CONFIG_PIXEL_RGBA_BUFFER);
                    final EGLContext eglContext = createEglContext(eglDisplay, eglConfig);

                    int[] surfaceAttribs = {EGL14.EGL_WIDTH, width, EGL14.EGL_HEIGHT, height, EGL14.EGL_NONE};
                    final EGLSurface eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig, surfaceAttribs, 0);
                    if (eglSurface == EGL14.EGL_NO_SURFACE) {
                        throw new RuntimeException(
                                "Failed to create pixel buffer surface with size: " + width + "x" + height);
                    }

                    if (!EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                        throw new RuntimeException("eglMakeCurrent failed");
                    }

//                    EGL14.eglSwapInterval(eglDisplay, 50);

                    int target = GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
                    final int textureArray[] = new int[1];
                    GLES20.glGenTextures(1, textureArray, 0);
                    final int textureId = textureArray[0];
                    GLES20.glBindTexture(target, textureId);
                    GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                    GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                    GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                    GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);


                    int oesTextureId = textureId;
                    SurfaceTexture surfaceTexture = new SurfaceTexture(oesTextureId);
                    surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                        @Override
                        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                            Log.d("test","onFrameAvailable");
                            if (!EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                                throw new RuntimeException("eglMakeCurrent failed");
                            }
                            surfaceTexture.updateTexImage();

//                            eglSurface.setPresentationTime(surfaceTexture.getTimestamp());
                            EGL14.eglSwapBuffers(eglDisplay, eglSurface);
//                            GLES20.glReadPixels();
                        }
                    });
//                    textureView.setSurfaceTexture(surfaceTexture);

                    Surface surface = new Surface(surfaceTexture);
//                    File file = new File(getExternalFilesDir())
//                    VideoEncoderCore vec = new VideoEncoderCore(width,height,300000,)
                    virtual_display = media_projection.createVirtualDisplay(
                        "name", width, height, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                            surface,null,null);

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
