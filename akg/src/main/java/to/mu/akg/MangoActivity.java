package to.mu.akg;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 *
 */
public class MangoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "MangoActivity";
    private MangoHandler mangoHandler;
    private CherryThread cherryThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mango);

        mangoHandler = new MangoHandler(this);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        if (surfaceView != null)
            surfaceView.getHolder().addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cherryThread = new CherryThread(mangoHandler);
        cherryThread.start();
        cherryThread.waitUntilReady();
    }

    @Override
    protected void onPause() {
        super.onPause();

        CherryHandler cherryHandler = cherryThread.getHandler();
        cherryHandler.sendShutdown();
        try {
            cherryThread.join();
        } catch (InterruptedException ie) {
            // not expected
            throw new RuntimeException("join was interrupted", ie);
        }
        cherryThread = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (cherryThread != null) {
            CherryHandler handler = cherryThread.getHandler();
            handler.sendSurfaceAvailable(holder, true);
        } else {
            Log.d(TAG, "Ignoring surfaceCreated");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged fmt=" + format + " size=" + width + "x" + height +
                " holder=" + holder);

        if (cherryThread != null) {
            CherryHandler handler = cherryThread.getHandler();
            handler.sendSurfaceChanged(format, width, height);
        } else {
            Log.d(TAG, "Ignoring surfaceChanged");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (cherryThread != null) {
            CherryHandler handler = cherryThread.getHandler();
            handler.sendSurfaceDestroyed();
        } else {
            Log.d(TAG, "Ignoring surfaceDestroyed");
        }
    }

    private static class MangoHandler extends Handler {

        public MangoHandler(MangoActivity activity) {
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
