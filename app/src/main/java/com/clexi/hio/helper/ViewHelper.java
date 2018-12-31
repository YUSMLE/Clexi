package com.clexi.hio.helper;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by yousef on 9/28/2016.
 */

public class ViewHelper
{

    public static void requestFocus(Activity activity, View view)
    {
        if (view.requestFocus())
        {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
