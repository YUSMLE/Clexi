package com.clexi.hio.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by yousef on 10/1/2016.
 */

public class Utils
{

    public static void vibratePhone(Context context)
    {
        final long vibrationLong = 30; // ms

        // Vibrate phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrationLong);
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }

        return false;
    }
}
