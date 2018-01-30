package com.clexi.clexi.bluetoothle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Yousef on 8/27/2017.
 */

public class Broadcaster
{

    public static final String TAG = Broadcaster.class.getSimpleName();

    /**
     * GATT BROADCASTS
     */
    public static void broadcastGattCallback(Context context, String action)
    {
        try
        {
            final Intent intent = new Intent(action);
            context.sendBroadcast(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error with broadcast " + action, e);
        }
    }

    public static void broadcastGattCallback(Context context, String action, byte[] data)
    {
        try
        {
            final Intent intent = new Intent(action);
            intent.putExtra(Consts.GATT_DATA, data);
            context.sendBroadcast(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error with broadcast " + action, e);
        }
    }

    public static void broadcastGattCallback(Context context, String action, int status, int newState)
    {
        try
        {
            final Intent intent = new Intent(action);
            intent.putExtra(Consts.GATT_STATUS, status);
            intent.putExtra(Consts.GATT_NEW_STATE, newState);
            context.sendBroadcast(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error with broadcast " + action, e);
        }
    }

    public static void broadcastGattCallback(Context context, String action, int rssi)
    {
        try
        {
            final Intent intent = new Intent(action);
            intent.putExtra(Consts.GATT_RSSI, rssi);
            context.sendBroadcast(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error with broadcast " + action, e);
        }
    }

    public static void broadcastGattCallback(Context context, String action, String address)
    {
        try
        {
            final Intent intent = new Intent(action);
            intent.putExtra(Consts.GATT_ADDRESS, address);
            context.sendBroadcast(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error with broadcast " + action, e);
        }
    }
}
