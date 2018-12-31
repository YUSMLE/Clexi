package com.clexi.hio.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clexi.hio.app.Consts;
import com.clexi.hio.model.object.Account;

/**
 * Created by Yousef on 10/15/2016.
 */

public class Broadcaster
{

    public static final String TAG = Broadcaster.class.getSimpleName();

    /**
     * OTHER BROADCASTS
     */
    public static void broadcast(Context context, String action)
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

    public static void broadcast(Context context, String action, Account account)
    {
        try
        {
            final Intent intent = new Intent(action);
            intent.putExtra(Consts.ACCOUNT, account);
            context.sendBroadcast(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error with Broadcast " + action, e);
        }
    }
}
