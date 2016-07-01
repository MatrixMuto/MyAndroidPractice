package to.mu.library;

import android.opengl.EGL14;

import javax.microedition.khronos.egl.EGL10;

public class EglBase14 {
    private static final int EGL_RECORDABLE_ANDROID = 0x3142;

    public static final int[] CONFIG_PLAIN = {
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL10.EGL_NONE
    };
    public static final int[] CONFIG_RGBA = {
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL10.EGL_NONE
    };
    public static final int[] CONFIG_PIXEL_BUFFER = {
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
            EGL10.EGL_NONE
    };
    public static final int[] CONFIG_PIXEL_RGBA_BUFFER = {
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
            EGL10.EGL_NONE
    };
    public static final int[] CONFIG_RECORDABLE = {
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL_RECORDABLE_ANDROID, 1,
            EGL10.EGL_NONE
    };
}