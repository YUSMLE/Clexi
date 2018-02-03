package com.clexi.clexi.bluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.clexi.clexi.app.App;

/**
 * Created by Yousef on 8/23/2017.
 */

public class LeScanCallback implements BluetoothAdapter.LeScanCallback
{

    public static final String TAG = LeScanCallback.class.getSimpleName();

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        Log.i(TAG, "onLeScan, Address: " + device.toString() + ", RSSI: " + rssi);

        // Check the Bluetooth Name
        if (!Consts.BLUETOOTH_NAME.equals(device.getName()))
        {
            // It's not my business.
            return;
        }

        // Check RSSI Power for SEARCHING State
        if (rssi < Consts.MINIMUM_RSSI_POWER_FOR_SEARCH)
        {
            // Found item is so far.
            return;
        }

        // Broadcast it...
        Broadcaster.broadcastGattCallback(
                App.getAppContext(),
                Consts.ACTION_GATT_SCAN_CALLBACK,
                device.getAddress());
    }
}
