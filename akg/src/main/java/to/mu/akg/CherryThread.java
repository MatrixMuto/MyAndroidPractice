package to.mu.akg;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by muto on 16-7-8.
 */
public class CherryThread extends Thread {

    public CherryThread(Handler mainHandler) {

    }

    @Override
    public void run() {
        Looper.prepare();

        Looper.loop();
    }
}
