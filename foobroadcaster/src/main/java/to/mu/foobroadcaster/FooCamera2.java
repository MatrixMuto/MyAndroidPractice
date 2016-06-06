package to.mu.foobroadcaster;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

/**
 * Created by wq1950 on 16-6-6.
 */
public class FooCamera2 {

    public FooCamera2()
    {

    }

    boolean fooFunc1(Context context)
    {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (manager == null) {
//            ErrorDialog.buildErrorDialog("This device doesn't support Camera2 API.").
//                    show(getFragmentManager(), "dialog");
            return false;
        }

        try {
            for (String cameraId : manager.getCameraIdList())
            {

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return false;
    }
}
