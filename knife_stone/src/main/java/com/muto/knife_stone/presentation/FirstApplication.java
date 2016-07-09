package com.muto.knife_stone.presentation;

import android.app.Application;

//import me.drakeet.library.CrashWoodpecker;

/**
 * Created by muto on 16-3-15.
 */
public class FirstApplication extends Application{
    public static boolean DEBUG = true;
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashWoodpecker.fly().to(this);
    }
}
