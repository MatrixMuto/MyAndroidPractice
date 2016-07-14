package to.mu.akg;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.lang.ref.WeakReference;

/**
 * Created by muto on 16-7-10.
 */
public class CherryHandler extends Handler {
    private static final String TAG = "CherryHandler";
    private static final int MSG_SURFACE_AVAILABLE = 0;
    private static final int MSG_SHUTDOWN = 10;
    private static final int MSG_FRAME_AVAILABLE = 1;

    private final WeakReference<CherryThread> weakCherryThread;

    /**
     * Call from render thread.
     */
    public CherryHandler(CherryThread ct) {
        Log.v(TAG,"CherryHandler ctor");
        weakCherryThread = new WeakReference<CherryThread>(ct);
    }

    // Call from UI thread
    public void sendSurfaceAvailable(SurfaceHolder holder, boolean newSurface) {
        sendMessage(obtainMessage(MSG_SURFACE_AVAILABLE,
                newSurface ? 1 : 0, 0, holder));
    }

    public void sendSurfaceChanged(int format, int width, int height) {

    }

    public void sendSurfaceDestroyed() {

    }

    /**
     * Sends the "frame available" message.
     * <p/>
     * Call from UI thread.
     */
    public void sendFrameAvailable() {
        sendMessage(obtainMessage(MSG_FRAME_AVAILABLE));
    }

    public void sendShutdown() {
        sendMessage(obtainMessage(MSG_SHUTDOWN));
    }

    @Override // runs on CherryThread
    public void handleMessage(Message msg) {
        int what = msg.what;

        CherryThread cherryThread = weakCherryThread.get();
        if (cherryThread == null)
        {
            return;
        }
        switch (what) {
            case MSG_SURFACE_AVAILABLE:
                cherryThread.surfaceAvaliable((SurfaceHolder)msg.obj);
                break;
            case MSG_SHUTDOWN:
                cherryThread.shutdown();
                break;
            case MSG_FRAME_AVAILABLE:
                cherryThread.frameAvailable();
                break;
            default:
                throw new RuntimeException("unknown message " + what);
        }
    }


}
