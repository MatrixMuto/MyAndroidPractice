package com.example.fooone;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by muto on 16-3-22.
 */
public class MyGLSurfaceView  extends GLSurfaceView {

    MyRender render;

    public MyGLSurfaceView(Context context) {
//        super(context);
        this(context,null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setEGLContextClientVersion(2);
        render = new MyRender();
        setRenderer(render);
        Log.i("@@@", "setrenderer");
    }

    @Override
    public void onPause() {
        //这里的前后顺序也是有点意味。
        render.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        //这里的前后顺序也是有点意味。
        super.onResume();
        render.onResume();
    }
}
