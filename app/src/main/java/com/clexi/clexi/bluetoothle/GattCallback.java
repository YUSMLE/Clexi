package com.clexi.clexi.bluetoothle;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clexi.clexi.app.App;
import com.clexi.clexi.crypto.Converter;

import java.util.UUID;

/**
 * Created by Yousef on 8/23/2017.
 */

public class GattCallback extends BluetoothGattCallback
{

    public static final String TAG = GattCallback.class.getSimpleName();

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
    {
        Log.d(TAG, "onConnectionStateChange()");

        Broadcaster.broadcastGattCallback(App.getAppContext(), Consts.ACTION_GATT_CONNECTION_STATE_CHANGE, status, newState);

        switch (newState)
        {
            case BluetoothProfile.STATE_CONNECTED:
                Log.d(TAG, "Connection State: STATE_CONNECTED");
                // Let's discover services
                gatt.discoverServices();
                break;

            case BluetoothProfile.STATE_DISCONNECTED:
                Log.d(TAG, "Connection State: STATE_DISCONNECTED");
                // Try to close connection, if exists
                // todo later...
                break;

            case BluetoothProfile.STATE_CONNECTING:
                Log.d(TAG, "Connection State: STATE_CONNECTING");
                // Nothing
                break;

            case BluetoothProfile.STATE_DISCONNECTING:
                Log.d(TAG, "Connection State: STATE_DISCONNECTING");
                // Nothing
                break;

            default:
                // Nothing
        }
    }

    @Override
    public void onServicesDiscovered(final BluetoothGatt gatt, int status)
    {
        Log.d(TAG, "onServicesDiscovered()");

        if (status == BluetoothGatt.GATT_SUCCESS)
        {
            Broadcaster.broadcastGattCallback(App.getAppContext(), Consts.ACTION_GATT_SERVICES_DISCOVERED);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
    {
        Log.d(TAG, "onCharacteristicChanged()");

        // TEST
        UUID uuid = characteristic.getUuid();
        if (uuid.equals(Consts.UUID_CHARACTERISTIC_CLEXI_RESPONSE))
        {
            Log.d(TAG, "CHARACTERISTIC CLEXI RESPONSE:");
        }
        else if (uuid.equals(Consts.UUID_CHARACTERISTIC_CLEXI_EVENT))
        {
            Log.d(TAG, "CHARACTERISTIC CLEXI EVENT:");
        }
        else
        {
            Log.d(TAG, "UNKNOWN CHARACTERISTIC:");
        }
        Log.d(TAG, "VALUE: " + Converter.encodeToHexadecimal(characteristic.getValue()));

        Broadcaster.broadcastGattCallback(App.getAppContext(), Consts.ACTION_GATT_CHARACTERISTIC_CHANGED, characteristic.getValue());
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
        Log.d(TAG, "onCharacteristicRead()");

        if (status == BluetoothGatt.GATT_SUCCESS)
        {
            Broadcaster.broadcastGattCallback(App.getAppContext(), Consts.ACTION_GATT_CHARACTERISTIC_READ, characteristic.getValue());
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
        Log.d(TAG, "onCharacteristicWrite()");

        Broadcaster.broadcastGattCallback(App.getAppContext(), Consts.ACTION_GATT_CHARACTERISTIC_WRITE);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
    {
        Log.d(TAG, "onDescriptorRead()");

        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
    {
        Log.d(TAG, "onDescriptorWrite()");

        Broadcaster.broadcastGattCallback(App.getAppContext(), Consts.ACTION_GATT_DESCRIPTOR_WRITE);
    }

    @Override
    public void onReadRemoteRssi(final BluetoothGatt gatt, int rssi, int status)
    {
        Log.d(TAG, "onReadRemoteRssi()");

        if (status == BluetoothGatt.GATT_SUCCESS)
        {
            Broadcaster.broadcastGattCallback(App.getAppContext(),
                    Consts.ACTION_GATT_READ_REMOTE_RSSI,
                    LPF.getInstance().normalize(rssi));
        }
    }

    /**
     * BondingReceiver
     */
    private BroadcastReceiver bondingReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
            {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

                switch (state)
                {
                    case BluetoothDevice.BOND_BONDING:
                        // Bonding...
                        break;

                    case BluetoothDevice.BOND_BONDED:
                        // Bonded...
                        break;

                    case BluetoothDevice.BOND_NONE:
                        // Not bonded...
                        break;
                }
            }
        }
    };

    //private IntentFilter bondingReceiverFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
}
