package com.clexi.clexi.bluetoothle;

import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.clexi.clexi.app.App;

import java.util.List;

/**
 * Created by Yousef on 8/23/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScanCallback extends android.bluetooth.le.ScanCallback
{

    public static final String TAG = ScanCallback.class.getSimpleName();

    @Override
    public void onScanResult(int callbackType, ScanResult result)
    {
        Log.d(TAG, "onScanResult, Address: " + result.getDevice().toString() + ", RSSI: " + result.getRssi());

        // Check the Bluetooth Name
        if (!Consts.BLUETOOTH_NAME.equals(result.getDevice().getName()))
        {
            // It's not my business.
            return;
        }

        // Check RSSI Power for SEARCHING State
        if (result.getRssi() < Consts.MINIMUM_RSSI_POWER_FOR_SEARCH)
        {
            // Found item is so far.
            return;
        }

        // Broadcast it...
        Broadcaster.broadcastGattCallback(
                App.getAppContext(),
                Consts.ACTION_GATT_SCAN_CALLBACK,
                result.getDevice().getAddress());
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results)
    {
        for (ScanResult sr : results)
        {
            Log.d(TAG, "onBatchScanResults: " + sr.toString());
        }
    }

    @Override
    public void onScanFailed(int errorCode)
    {
        Log.d(TAG, "onScanFailed: " + errorCode);
    }
}
