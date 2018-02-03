package com.clexi.clexi.bluetoothle;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by yousef on 9/7/2016.
 */

public class BleUtils
{

    public static final String TAG = BleUtils.class.getSimpleName();

    public static boolean isBluetothHardwareAvailable(Context context)
    {
        // Check general bluetooth hardware
        final BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (manager == null)
        {
            Log.d(TAG, "Unable to initialize BluetoothManager.");

            return false;
        }

        // Get adapter from manager
        final BluetoothAdapter adapter = manager.getAdapter();

        if (adapter == null)
        {
            Log.d(TAG, "Unable to get BluetoothAdapter.");

            return false;
        }

        return true;
    }

    public static boolean isBluetothLEFeatureAvailable(Context context)
    {
        // Check if Bluetooth LE is also available
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static boolean isBluetothEnabled(Context context)
    {
        final BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (manager == null)
        {
            Log.d(TAG, "Unable to initialize BluetoothManager.");

            return false;
        }

        final BluetoothAdapter adapter = manager.getAdapter();

        if (adapter == null)
        {
            Log.d(TAG, "Unable to get BluetoothAdapter.");

            return false;
        }

        return adapter.isEnabled();
    }

    public static void enableBluetoth(Activity activity, int requestCode)
    {
        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBTIntent, requestCode);
    }

    public static boolean refreshDeviceCache(BluetoothGatt gatt)
    {
        try
        {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh");

            if (localMethod != null)
            {
                return ((Boolean) localMethod.invoke(localBluetoothGatt)).booleanValue();
            }
        }
        catch (Exception localException)
        {
            Log.e(TAG, "Error while Refreshing Gatt Device Cache", localException);
        }

        return false;
    }
}
