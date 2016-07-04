package com.example.fooone;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by muto on 16-3-22.
 */
public class MyRender implements GLSurfaceView.Renderer {

    public MyRender() {
//        mVertices = ByteBuffer.allocateDirect(mVerticesData.length
//                * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mVertices.put(mVerticesData).position(0);
//
//        Matrix.setIdentityM(mSTMatrix, 0);
    }

    public void onPause() {
    }

    public void onResume() {
        //mLastTime = SystemClock.elapsedRealtimeNanos();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("@@@", "onSurfaceCreated");
        //glEnable是打开了OpenGL状态机，管线的一个开关
        //GL_BLEND开关的作用是什么？
        GLES20.glEnable(GLES20.GL_BLEND);
        //glBlendFunc的作用
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        mProgram = createProgram(null, null);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i("@@@", "onSurfaceChanged (w,h)" + width + "," + height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i("@@@", "onDrawFrame");

    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e("@@@", "Could not compile shader " + shaderType + ":");
                Log.e("@@@", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");

        }
        return program;
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        {
            Log.e("@@@",op + ":glError " + error);
            throw new RuntimeException(op + ": glErro" + error);
        }
    }


    private int mProgram;
}
