package com.clexi.clexi.app;

import android.os.Environment;

/**
 * Created by yousef on 9/6/2016.
 */

public class Consts
{

    // Application Package
    public static final String PACKAGE_NAME_VALUE = "com.clexi.clexi";
    public static final String APP_NAME_VALUE     = "Clexi";

    // Literals
    public static final String SEPARATOR = "/";
    public static final String SPACE     = " ";
    public static final String TAB       = "\t";
    public static final String ENTER     = "\n";

    // Directories
    public static final String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DIR_APP    = DIR_SDCARD + SEPARATOR + APP_NAME_VALUE;

    // Files
    public static final String logFileName = APP_NAME_VALUE.toLowerCase() + "_log.txt";

    // Request Codes
    public static final int REQUEST_ENABLE_BT                     = 1000;
    public static final int REQUEST_ENABLE_ACCESSIBILITY_SETTINGS = 1001;
    public static final int REQUEST_MANAGE_OVERLAY_PERMISSION     = 1002;
    public static final int REQUEST_ADD_EDIT_DELETE_ACCOUNT       = 1003;
    public static final int REQUEST_GENERATE_PASSWORD             = 1004;
    public static final int REQUEST_CHOOSE_APPLICATION            = 1005;

    // Request Permission Codes
    public static final int PERMISSIONS_REQUEST_ALL                    = 2000;
    public static final int PERMISSIONS_REQUEST_BLUETOOTH              = 2001;
    public static final int PERMISSIONS_REQUEST_BLUETOOTH_ADMIN        = 2002;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2003;
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE  = 2004;

    // Actions
    public static final String ACTION_LOGIN_REQUEST    = "ACTION_LOGIN_REQUEST";   /*KEY_A*/
    public static final String ACTION_LOGIN_FIRE       = "ACTION_LOGIN_FIRE";
    public static final String ACTION_MANAGEMENT       = "ACTION_MANAGEMENT";      /*KEY_B*/
    public static final String ACTION_SEARCH_FOR_LOGIN = "ACTION_SEARCH_FOR_LOGIN";
    public static final String ACTION_NEW_FOR_LOGIN    = "ACTION_NEW_FOR_LOGIN";

    // Keys
    public static final String KEY_A = ACTION_LOGIN_REQUEST;
    public static final String KEY_B = ACTION_MANAGEMENT;

    // Request EXTRA PARAMETERS / Keys
    public static final String ACCOUNT            = "ACCOUNT";
    public static final String ACCOUNTS           = "ACCOUNTS";
    public static final String ACTIVE_APP         = "ACTIVE_APP";
    public static final String IS_BROWSER         = "IS_BROWSER";
    public static final String CURRENT_URL        = "CURRENT_URL";
    public static final String SELECTED_USERNAME  = "SELECTED_USERNAME";
    public static final String ACCOUNT_ID         = "ACCOUNT_ID";
    public static final String GENERATED_PASSWORD = "GENERATED_PASSWORD";
    public static final String APP_NAME           = "APP_NAME";
    public static final String PACKAGE_NAME       = "PACKAGE_NAME";
    public static final String VERSION_NAME       = "VERSION_NAME";
    public static final String VERSION_CODE       = "VERSION_CODE";
    public static final String MODE               = "MODE";
}
