package com.muto.knife_stone.presentation.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by muto on 16-3-15.
 */
public class TestRelativeLayout extends RelativeLayout {
    private static final String TAG = TestRelativeLayout.class.getSimpleName();

    public TestRelativeLayout(Context context) {
        super(context);
    }

    public TestRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onIntercept:" + ev.getActionMasked());
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouch:" + event.getActionMasked());
        return true;
    }
}
