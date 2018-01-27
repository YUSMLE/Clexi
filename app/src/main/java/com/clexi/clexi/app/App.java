package com.clexi.clexi.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Yousef on 2/27/2017.
 */

public class App extends Application
{

    private static App mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mInstance = this;

        // Init Database
        // todo later...

        // Set Context for BluetoothLE Framework
        // todo later...
    }

    public static Context getAppContext()
    {
        return mInstance.getApplicationContext();
    }
}
