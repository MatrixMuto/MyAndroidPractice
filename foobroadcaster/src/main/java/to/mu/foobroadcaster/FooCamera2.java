package to.mu.foobroadcaster;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.view.TextureView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Semaphore;

/**
 * Created by wq1950 on 16-6-6.
 */
public class FooCamera2 {

    private final Object mCameraStateLock = new Object();
    private CameraCharacteristics mCharacteristics;
    private String mCameraId;
    private RefCountedAutoCloseable<ImageReader> mJpegImageReader;
    private RefCountedAutoCloseable<ImageReader> mRawImageReader;
    private Handler mBackgroundHandler;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private CaptureRequest.Builder mPreviewRequestBuilder;

    public FooCamera2() {

    }

    boolean fooFunc1(Context context) {
        mBackgroundHandler = new Handler();
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (manager == null) {
            return false;
        }

        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                int[] caps = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
//                if (!Utils.contains(caps, CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW)) {
//                    continue;
//                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                // For still image captures, we use the largest available size.
                Size largestJpeg = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());

//                Size largestRaw = Collections.max(
//                        Arrays.asList(map.getOutputSizes(ImageFormat.RAW_SENSOR)),
//                        new CompareSizesByArea());

                synchronized (mCameraStateLock) {
                    // Set up ImageReaders for JPEG and RAW outputs.  Place these in a reference
                    // counted wrapper to ensure they are only closed when all background tasks
                    // using them are finished.
                    if (mJpegImageReader == null || mJpegImageReader.getAndRetain() == null) {
                        mJpegImageReader = new RefCountedAutoCloseable<>(
                                ImageReader.newInstance(largestJpeg.getWidth(),
                                        largestJpeg.getHeight(), ImageFormat.JPEG, /*maxImages*/5));
                    }
                    mJpegImageReader.get().setOnImageAvailableListener(
                            mOnJpegImageAvailableListener, mBackgroundHandler);

//                    if (mRawImageReader == null || mRawImageReader.getAndRetain() == null) {
//                        mRawImageReader = new RefCountedAutoCloseable<>(
//                                ImageReader.newInstance(largestRaw.getWidth(),
//                                        largestRaw.getHeight(), ImageFormat.RAW_SENSOR, /*maxImages*/ 5));
//                    }
//                    mRawImageReader.get().setOnImageAvailableListener(
//                            mOnRawImageAvailableListener, mBackgroundHandler);

                    mCharacteristics = characteristics;
                    mCameraId = cameraId;
                }
                return true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    private final ImageReader.OnImageAvailableListener mOnJpegImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
//            dequeueAndSaveImage(mJpegResultQueue, mJpegImageReader);
        }

    };

    private final ImageReader.OnImageAvailableListener mOnRawImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
//            dequeueAndSaveImage(mRawResultQueue, mRawImageReader);
        }

    };

    private CameraDevice mCameraDevice;

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private TextureView mTextureView;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
//            Activity activity = getActivity();
//            if (null != activity) {
//                activity.finish();
//            }
        }

    };

    private void createCameraPreviewSession() {
        try {

            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public boolean open(final @NonNull Activity activity, final @NonNull Context context) {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (manager == null) {
            return false;
        }

        try {
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}


class Utils {
    /**
     * Return true if the given array contains the given integer.
     *
     * @param modes array to check.
     * @param mode  integer to get for.
     * @return true if the array contains the given integer, otherwise false.
     */
    public static boolean contains(int[] modes, int mode) {
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == mode) {
                return true;
            }
        }
        return false;
    }
}

class CompareSizesByArea implements Comparator<Size> {

    @Override
    public int compare(Size lhs, Size rhs) {
        // We cast here to ensure the multiplications won't overflow
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }

}

class RefCountedAutoCloseable<T extends AutoCloseable> implements AutoCloseable {
    private T mObject;
    private long mRefCount = 0;

    public RefCountedAutoCloseable(T object) {
        if (object == null) throw new NullPointerException();
        mObject = object;
    }

    public synchronized T getAndRetain() {
        if (mRefCount < 0) {
            return null;
        }
        mRefCount++;
        return mObject;
    }

    public synchronized T get() {
        return mObject;
    }

    @Override
    public synchronized void close() {
        if (mRefCount >= 0) {
            mRefCount--;
            if (mRefCount < 0) {
                try {
                    mObject.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    mObject = null;
                }
            }
        }
    }
}