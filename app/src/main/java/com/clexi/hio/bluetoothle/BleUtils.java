package com.clexi.hio.bluetoothle;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.clexi.hio.helper.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        // Check if BluetoothLE is also available
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
            Method        localMethod        = localBluetoothGatt.getClass().getMethod("refresh");

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

    public static List<BluetoothDevice> getPairedDevices()
    {
        // Get paired devices
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // Or get it from BluetoothManager instance
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // Cast Set* to List*
        List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
        devices.addAll(pairedDevices);

        return devices;
    }

    public static List<BluetoothDevice> getConnectedDevices(Context context)
    {
        // Get connected devices
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        List<BluetoothDevice> connectedDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);

        return connectedDevices;
    }

    public static boolean isConnectedDevice(Context context, BluetoothDevice bluetoothDevice)
    {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        int connectionState = bluetoothManager.getConnectionState(bluetoothDevice, BluetoothProfile.GATT);

        return connectionState == BluetoothProfile.STATE_CONNECTED;
    }

    public static boolean discoverDevices()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // Or get it from BluetoothManager instance

        if (bluetoothAdapter.isDiscovering())
        {
            bluetoothAdapter.cancelDiscovery();
        }

        return bluetoothAdapter.startDiscovery();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createBond(BluetoothGatt gatt)
    {
        try
        {
            gatt.getDevice().createBond();
        }
        catch (Exception e)
        {
            e.printStackTrace();

            // Unable to get device from this BluetoothGatt instance;
            // Maybe gatt is null,
            // or can't get device from it.

            // todo later...
        }
    }

    public static void startBleService(Context context)
    {
        // Start BleService, if it's not
        if (!Utils.isMyServiceRunning(context, BleService.class))
        {
            BleService.startService(context);
        }
    }
}
