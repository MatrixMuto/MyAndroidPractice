package to.mu.akg;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by muto on 16-7-8.
 */
public class CherryThread extends Thread {
    private static final String TAG = "CherryThread";
    private static final int REQ_CAMERA_HEIGHT = 1280;
    private static final int REQ_CAMERA_WIDTH = 720;
    private static final int REQ_CAMERA_FPS = 30;

    private volatile CherryHandler cherryHandler;
    // Used to wait for the thread to start.
    private final Object startLock = new Object();
    private boolean ready = false;
    private Camera camera;

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

    public void surfaceAvaliable(SurfaceHolder holder) {
        holder.getSurface();
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private void openCamera(int width, int heigth, int fps) {
        camera = Camera.open(1);
    }
    private void releaseCamera() {
        camera.release();
    }

    @Override   // SurfaceTexture.OnFrameAvailableListener; runs on arbitrary thread
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mHandler.sendFrameAvailable();
    }

    /**
     * Handles incoming frame of data from the camera.
     */
    private void frameAvailable() {
        mCameraTexture.updateTexImage();
        draw();
    }

    /**
     * Draws the scene and submits the buffer.
     */
    private void draw() {
        GlUtil.checkGlError("draw start");

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mRect.draw(mTexProgram, mDisplayProjectionMatrix);
        mWindowSurface.swapBuffers();

        GlUtil.checkGlError("draw done");
    }
}
