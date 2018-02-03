package com.clexi.clexi.bluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yousef on 8/27/2017.
 */

public class Scanner
{

    public static final String TAG = Scanner.class.getSimpleName();

    private BluetoothAdapter   mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback       mScanCallback;
    private LeScanCallback     mLeScanCallback;
    private ScanSettings       mSettings;
    private List<ScanFilter>   mFilters;

    private boolean isScanning = false;

    public void start()
    {
        if (isScanning)
        {
            Log.d(TAG, "Allready scanning.");

            return;
        }
        else
        {
            isScanning = true;
        }

        try
        {
            if (Build.VERSION.SDK_INT < 21)
            {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
            else
            {
                mBluetoothLeScanner.startScan(mFilters, mSettings, mScanCallback);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while starting scan.", e);

            isScanning = false;
        }

        // Try to refresh scan
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (isScanning)
                {
                    // Refresh scan
                    stop();
                    start();
                }
                else
                {
                    Log.d(TAG, "Can not refresh Scan, it stoped before.");
                }
            }
        }, Consts.SCAN_REFRESH_PERIOD);
    }

    public void stop()
    {
        try
        {
            if (Build.VERSION.SDK_INT < 21)
            {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                isScanning = false;
            }
            else
            {
                mBluetoothLeScanner.stopScan(mScanCallback);
                isScanning = false;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while stopping scan.", e);

            isScanning = false;
        }
    }

    public Scanner setScanner(BluetoothAdapter bluetoothAdapter)
    {
        // Be carefull; first of all, set the BluetoothAdapter
        mBluetoothAdapter = bluetoothAdapter;

        if (Build.VERSION.SDK_INT < 21)
        {
            Log.d(TAG, "Android API Level is < 21, it is: " + Build.VERSION.SDK_INT);

            mLeScanCallback = new LeScanCallback();
        }
        else
        {
            Log.d(TAG, "Android API Level is >= 21, it is: " + Build.VERSION.SDK_INT);

            mScanCallback = new ScanCallback();
        }

        if (Build.VERSION.SDK_INT >= 21)
        {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            mSettings = new ScanSettings
                    .Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            mFilters = new ArrayList<ScanFilter>();
        }

        return this;
    }

    /****************************************************
     * Lazy and Thread-safe Singleton Pattern
     */
    private static class SingletonHolder
    {
        private static final Scanner INSTANCE = new Scanner();
    }

    private Scanner()
    {
        // Nothing
    }

    public static Scanner getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

}
