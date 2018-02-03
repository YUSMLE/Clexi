package com.clexi.clexi.bluetoothle.queue;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by yousef on 8/10/2016.
 * <p>
 * An enqueueable write operation -
 * characteristic notification subscription,
 * characteristic read or
 * characteristic write
 */

public class GattOperation
{
    public GattOperationType           type;
    public BluetoothGattCharacteristic characteristic;
    public byte[]                      dataToWrite;    // Only used for characteristic write
    public boolean                     enabled;        // Only used for characteristic notification subscription
}
