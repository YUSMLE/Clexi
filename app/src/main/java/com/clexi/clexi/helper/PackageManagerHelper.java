package com.clexi.clexi.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.clexi.clexi.model.object.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yousef on 5/30/2017.
 */

public class PackageManagerHelper
{
    public static final String TAG = PackageManagerHelper.class.getSimpleName();

    public static ApplicationInfo getApplicationInfo(Context context, String packageName)
    {
        final PackageManager pm = context.getPackageManager();

        ApplicationInfo ai;

        try
        {
            ai = pm.getApplicationInfo(packageName, 0);
        }
        catch (final PackageManager.NameNotFoundException e)
        {
            ai = null;
        }

        return ai;
    }

    public static List<ApplicationInfo> getAllInstalledAppInfo(Context context)
    {
        final PackageManager pm = context.getPackageManager();

        // get a list of installed apps
        List<ApplicationInfo> ais = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        return ais;
    }

    public static List<String> getAllInstalledPackageName(Context context)
    {
        List<ApplicationInfo> ais = getAllInstalledAppInfo(context);

        List<String> packageNames = new ArrayList<>();

        for (ApplicationInfo packageInfo : ais)
        {
            packageNames.add(packageInfo.packageName);
        }

        return packageNames;
    }

    public static String getAppName(Context context, String packageName)
    {
        final PackageManager pm = context.getPackageManager();

        ApplicationInfo ai;

        try
        {
            ai = pm.getApplicationInfo(packageName, 0);
        }
        catch (final PackageManager.NameNotFoundException e)
        {
            ai = null;
        }

        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");

        return applicationName;
    }

    public static AppInfo getAppInfo(Context context, String packageName)
    {
        final PackageManager pm = context.getPackageManager();

        PackageInfo pi;

        try
        {
            pi = pm.getPackageInfo(packageName, 0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            pi = null;

            return null;
        }

        AppInfo appInfo = new AppInfo();

        appInfo.appName = pi.applicationInfo.loadLabel(pm).toString();
        appInfo.packageName = pi.packageName;
        appInfo.versionName = pi.versionName;
        appInfo.versionCode = pi.versionCode;
        appInfo.icon = pi.applicationInfo.loadIcon(pm);

        return appInfo;
    }

    public static boolean isSystemApp(ApplicationInfo info)
    {
        return (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
