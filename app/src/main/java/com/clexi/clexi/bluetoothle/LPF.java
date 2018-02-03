package com.clexi.clexi.bluetoothle;

import android.util.Log;

/**
 * Created by Yousef on 12/13/2016.
 */

public class LPF
{

    public static final String TAG = LPF.class.getSimpleName();

    /****************************************************
     * Singleton Eager
     */
    private static final LPF INSTANCE = new LPF();

    public static LPF getInstance()
    {
        return INSTANCE;
    }

    /*****************************************************/

    public static final double beta = 0.8;

    private int previousNormalizedValue = 0;

    public int normalize(int currentValue)
    {
        if (previousNormalizedValue == 0)
        {
            previousNormalizedValue = currentValue;
        }

        int normalizedValue = normalize(currentValue, previousNormalizedValue);

        previousNormalizedValue = normalizedValue;

        // Log it
        Log.d(TAG, "LPF: currentValue: " + currentValue + ",\tnormalizedValue: " + normalizedValue);

        return normalizedValue;
    }

    private int normalize(int currentValue, int previousNormalizedValue)
    {
        return (int) ((previousNormalizedValue * beta) + ((1 - beta) * currentValue));
    }
}
