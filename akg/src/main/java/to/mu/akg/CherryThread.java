package to.mu.akg;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;

import com.github.faucamp.simplertmp.DefaultRtmpClient;

import java.io.File;
import java.io.IOException;

import to.mu.akg.graphics.CircularEncoder;
import to.mu.akg.graphics.Drawable2d;
import to.mu.akg.graphics.EglBase;
import to.mu.akg.graphics.FullFrameRect;
import to.mu.akg.graphics.GlUtil;
import to.mu.akg.graphics.ScaledDrawable2d;
import to.mu.akg.graphics.Sprite2d;
import to.mu.akg.graphics.Texture2dProgram;

/**
 * Created by muto on 16-7-8.
 */
public class CherryThread extends Thread implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "CherryThread";
    private static final int REQ_CAMERA_HEIGHT = 1280;
    private static final int REQ_CAMERA_WIDTH = 720;
    private static final int REQ_CAMERA_FPS = 30;

    private static final int VIDEO_WIDTH = 640;
    private static final int VIDEO_HEIGHT = 480;

    private volatile CherryHandler cherryHandler;
    // Used to wait for the thread to start.
    private final Object startLock = new Object();
    private boolean ready = false;
    private Camera camera;
    private EGLSurface windowSurface;
    private Texture2dProgram mTexProgram;
    private SurfaceTexture mCameraTexture;
    private final ScaledDrawable2d mRectDrawable =
            new ScaledDrawable2d(Drawable2d.Prefab.RECTANGLE);
    private final Sprite2d mRect = new Sprite2d(mRectDrawable);
    private float[] mDisplayProjectionMatrix = new float[16];
    private float mPosX;
    private float mPosY;
    private CircularEncoder mCircEncoder;
    private EglBase eglBaseEncoder;
    private int mTextureId;
    EglBase eglBase14;
    private FullFrameRect mFullFrameBlit;

    public CherryThread(Handler mangoHandler) {
        super(TAG);
    }

    @Override
    public void run() {
        Looper.prepare();

        cherryHandler = new CherryHandler(this);
        synchronized (startLock) {
            ready = true;
            startLock.notify();    // signal waitUntilReady()
        }

        // Prepare EGL and open the camera before we start handling messages.
//        mEglCore = new EglCore(null, 0);
        openCamera(REQ_CAMERA_WIDTH, REQ_CAMERA_HEIGHT, REQ_CAMERA_FPS);

        Looper.loop();

        releaseCamera();
        synchronized (startLock) {
            ready = false;
        }
    }

    /**
     * Waits until the cherry thread is ready to receive messages.
     * <p>
     * Call from the UI thread.
     */
    public void waitUntilReady() {
        synchronized (startLock) {
            while (!ready) {
                try {
                    startLock.wait();
                } catch (InterruptedException ie) { /* not expected */ }
            }
        }
    }

    /**
     * Shuts everything down.
     */
    protected void shutdown() {
        Log.d(TAG, "shutdown");
        Looper.myLooper().quit();
    }

    /**
     * Returns the render thread's Handler.  This may be called from any thread.
     */
    public CherryHandler getHandler() {
        Log.v(TAG,"CherryThread getHandler");
        return cherryHandler;
    }

    public void saveFile(File outputFile) {
        if (mCircEncoder != null) {
            mCircEncoder.saveVideo(outputFile);
        }
    }


    /**
     * 在SurfaceView的SurfaceCreate回调中,把Surface传到这个线程环境中.
     *
     * @param holder
     */
    public void surfaceAvaliable(SurfaceHolder holder) {
        holder.getSurface();
        eglBase14 = EglBase.create(null, EglBase.CONFIG_RECORDABLE);
        eglBase14.createSurface(holder.getSurface());
        eglBase14.makeCurrent();

//        mFullFrameBlit = new FullFrameRect(
//                new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));

        mTexProgram = new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT);
        mFullFrameBlit = new FullFrameRect(mTexProgram);
        mTextureId = mTexProgram.createTextureObject();
        mCameraTexture = new SurfaceTexture(mTextureId);
        mRect.setTexture(mTextureId);
        mCameraTexture.setOnFrameAvailableListener(this);

        int width = eglBase14.surfaceWidth();
        int height = eglBase14.surfaceHeight();
//        Log.d(TAG, "finishSurfaceSetup size=" + width + "x" + height +
//                " camera=" + mCameraPreviewWidth + "x" + mCameraPreviewHeight);

        // Use full window.
        GLES20.glViewport(0, 0, width, height);

        // Simple orthographic projection, with (0,0) in lower-left corner.
        Matrix.orthoM(mDisplayProjectionMatrix, 0, 0, width, 0, height, -1, 1);

        // Default position is center of screen.
        mPosX = width / 2.0f;
        mPosY = height / 2.0f;
//
        updateGeometry();

        try {
            camera.setPreviewTexture(mCameraTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();


        try {
            mCircEncoder = new CircularEncoder(VIDEO_WIDTH, VIDEO_HEIGHT, 6000000,
                    30, 7, new CircularEncoder.Callback() {
                @Override
                public void fileSaveComplete(int status) {

                }

                @Override
                public void bufferStatus(long totalTimeMsec) {

                }
            });
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        eglBaseEncoder = EglBase.create(eglBase14.getEglBaseContext(), EglBase.CONFIG_RECORDABLE);
        eglBaseEncoder.createSurface(mCircEncoder.getInputSurface());
//        eglBaseEncoder.makeCurrent();


    }

    /**
     * Updates the geometry of mRect, based on the size of the window and the current
     * values set by the UI.
     */
    private void updateGeometry() {
        int width = eglBase14.surfaceWidth();
        int height = eglBase14.surfaceHeight();

        int smallDim = Math.min(width, height);
        // Max scale is a bit larger than the screen, so we can show over-size.
        float scaled = smallDim * (100.0f / 100.0f) * 1.25f;
        float cameraAspect = (float) width / height;
        int newWidth = Math.round(scaled * cameraAspect);
        int newHeight = Math.round(scaled);

        float zoomFactor = 1.0f - (1.0f / 100.0f);
        int rotAngle = Math.round(360 * (1.0f / 100.0f));

        mRect.setScale(newWidth, newHeight);
        mRect.setPosition(mPosX, mPosY);
        mRect.setRotation(rotAngle);
        mRectDrawable.setScale(zoomFactor);

//        mMainHandler.sendRectSize(newWidth, newHeight);
//        mMainHandler.sendZoomArea(Math.round(mCameraPreviewWidth * zoomFactor),
//                Math.round(mCameraPreviewHeight * zoomFactor));
//        mMainHandler.sendRotateDeg(rotAngle);
    }


    private void openCamera(int width, int heigth, int fps) {
        camera = Camera.open(1);
    }
    private void releaseCamera() {
        camera.release();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        cherryHandler.sendFrameAvailable();
    }
    /**
     * Handles incoming frame of data from the camera.
     */
    protected void frameAvailable() {
        eglBase14.makeCurrent();
        mCameraTexture.updateTexImage();
        draw();
    }

    /**
     * Draws the scene and submits the buffer.
     */
    private void draw() {
        eglBase14.makeCurrent();
        GlUtil.checkGlError("draw start");

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mRect.draw(mTexProgram, mDisplayProjectionMatrix);
        eglBase14.swapBuffers();


//        if (!mFileSaveInProgress) {
        eglBaseEncoder.makeCurrent();
        GLES20.glViewport(0, 0, VIDEO_WIDTH, VIDEO_HEIGHT);
        mRect.draw(mTexProgram, mDisplayProjectionMatrix);
//        mRect.dr(mTextureId, mDisplayProjectionMatrix);
        mCircEncoder.frameAvailableSoon();
//            eglBaseEncoder.setPresentationTime(mCameraTexture.getTimestamp());
        eglBaseEncoder.swapBuffers();
//        }
        GlUtil.checkGlError("draw done");
    }


}
