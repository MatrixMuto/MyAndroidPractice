package to.mu.akg;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by muto on 16-7-8.
 */
public class CherryThread extends Thread {
    private static final String TAG = "CherryThread";
    private volatile CherryHandler cherryHandler;
    // Used to wait for the thread to start.
    private final Object mStartLock = new Object();
    private boolean mReady = false;

    public CherryThread(Handler mangoHandler) {
        super("CherryThread");

    }

    @Override
    public void run() {
        Looper.prepare();

        cherryHandler = new CherryHandler(this);
        synchronized (mStartLock) {
            mReady = true;
            mStartLock.notify();    // signal waitUntilReady()
        }

        Looper.loop();
    }

    /**
     * Waits until the render thread is ready to receive messages.
     * <p>
     * Call from the UI thread.
     */
    public void waitUntilReady() {
        synchronized (mStartLock) {
            while (!mReady) {
                try {
                    mStartLock.wait();
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


}
