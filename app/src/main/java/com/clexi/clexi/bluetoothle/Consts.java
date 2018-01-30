package com.clexi.clexi.bluetoothle;

import java.util.UUID;

/**
 * Created by Yousef on 8/23/2017.
 */

public class Consts
{

    public static final String TAG = Consts.class.getSimpleName();

    /****************************************************
     * GENERAL
     ***************************************************/

    public static final String BLUETOOTH_NAME = "ClexiFOB";

    /****************************************************
     * UUIDs
     ***************************************************/

    public static final UUID UUID_SERVICE        = UUID.fromString("321d1e00-e0f8-7eab-e74e-29b7e0855737");
    public static final UUID UUID_CHARACTERISTIC = UUID.fromString("321d1e01-e0f8-7eab-e74e-29b7e0855737");
    public static final UUID UUID_NOTIFICATION   = UUID.fromString("321d1e02-e0f8-7eab-e74e-29b7e0855737");
    public static final UUID UUID_DESCRIPTOR     = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    /****************************************************
     * ACTIONS
     ***************************************************/

    public static final String ACTION_GATT_CONNECTION_STATE_CHANGE = "ACTION_GATT_CONNECTION_STATE_CHANGE";
    public static final String ACTION_GATT_SERVICES_DISCOVERED     = "ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_GATT_CHARACTERISTIC_CHANGED  = "ACTION_GATT_CHARACTERISTIC_CHANGED";
    public static final String ACTION_GATT_CHARACTERISTIC_READ     = "ACTION_GATT_CHARACTERISTIC_READ";
    public static final String ACTION_GATT_CHARACTERISTIC_WRITE    = "ACTION_GATT_CHARACTERISTIC_WRITE";
    public static final String ACTION_GATT_DESCRIPTOR_READ         = "ACTION_GATT_DESCRIPTOR_READ";
    public static final String ACTION_GATT_DESCRIPTOR_WRITE        = "ACTION_GATT_DESCRIPTOR_WRITE";
    public static final String ACTION_GATT_READ_REMOTE_RSSI        = "ACTION_GATT_READ_REMOTE_RSSI";

    public static final String ACTION_GATT_SCAN_CALLBACK = "ACTION_GATT_SCAN_CALLBACK";

    /****************************************************
     * REQUEST CODES
     ***************************************************/

    // todo later...

    /****************************************************
     * BLUETOOTH PACKET CMD
     ***************************************************/

    public static final byte BLE_PACKET_CMD_EVENT = (byte) 0xC3;

    /****************************************************
     * LENGHTS
     ***************************************************/

    public static final int TOTAL_BLE_PACKET_LENGHT         = 20;
    public static final int DATA_LENGHT_PER_BLE_INIT_PACKET = 17;
    public static final int DATA_LENGHT_PER_BLE_SEQ_PACKET  = 19;

    public static final int MINIMUM_RSSI_POWER_FOR_SEARCH = -50;

    public final static int SCAN_INTERVAL       = 1000;    //ms
    public final static int SCAN_PERIOD         = 10000;   //ms
    public final static int SCAN_REFRESH_PERIOD = 3000;    //ms
    public final static int RSSI_INTERVAL       = 1000;    //ms

    /****************************************************
     * EXTRAS
     ***************************************************/

    public static final String GATT_DATA      = "GATT_DATA";
    public static final String GATT_STATUS    = "GATT_STATUS";
    public static final String GATT_NEW_STATE = "GATT_NEW_STATE";
    public static final String GATT_RSSI      = "GATT_RSSI";

    public static final String GATT_ADDRESS = "GATT_ADDRESS";

}
