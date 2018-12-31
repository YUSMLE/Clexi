package com.clexi.hio.app;

import android.content.Context;

import com.clexi.hio.bluetoothle.BleUtils;
import com.clexi.hio.model.access.DbManager;
import com.orm.SugarApp;

/**
 * Created by Yousef on 2/27/2017.
 */

public class App extends SugarApp
{

    private static App mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mInstance = this;

        // Init Database
        DbManager.startSugar(getApplicationContext());

        // Set Context for BluetoothLE Framework
        // todo later...

        // Start BleService
        BleUtils.startBleService(mInstance);
    }

    public static Context getAppContext()
    {
        return mInstance.getApplicationContext();
    }
}
