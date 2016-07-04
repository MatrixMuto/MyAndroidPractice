package com.muto.knife_stone.presentation.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by muto on 16-3-15.
 */
public class InterceptTest extends ViewGroup {
    private static final String TAG = InterceptTest.class.getSimpleName();
    public InterceptTest(Context context) {
        super(context);
    }

    public InterceptTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onIntercept:" + ev.getActionMasked());
        return true;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouch:" + event.getActionMasked());
        return true;
    }
}
