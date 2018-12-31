package com.clexi.hio.bluetoothle;

/**
 * Created by yousef on 9/26/2016.
 */

public enum BleState
{
    /**
     * BLUETOOTH_ON
     */
    SEARCHING,
    SCANNING,
    CONNECTED,
    STANDBY, // Empty Device List

    /**
     * UNSUPPORTED_BLUETOOTH,
     * UNSUPPORTED_BLE,
     * BLUETOOTH_OFF
     */
    SLEEP
}
