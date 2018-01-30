package com.clexi.clexi.accessibility;

/**
 * Created by Yousef on 1/16/2018.
 */

public class CacheManager
{

    public static final String TAG = CacheManager.class.getSimpleName();

    // The period that cached values will be valid (in second)
    private static final long VALID_PERIOD = 13;

    private static CachedLogin cachedLogin;

    public static void cacheLogin(CachedLogin cachedLogin)
    {
        CacheManager.cachedLogin = cachedLogin;

        CacheManager.cachedLogin.cacheTimestamp = System.currentTimeMillis() / 1000;
    }

    public static CachedLogin retrieveCache()
    {
        if (CacheManager.isDeprecated())
        {
            CacheManager.cachedLogin = null;
        }

        return CacheManager.cachedLogin;
    }

    private static boolean isDeprecated()
    {
        if (CacheManager.cachedLogin != null)
        {
            if ((System.currentTimeMillis() / 1000) - CacheManager.cachedLogin.cacheTimestamp <= CacheManager.VALID_PERIOD)
            {
                // Cached login is still valid
                return false;
            }
        }

        return true;
    }

    public static void clearCache()
    {
        CacheManager.cachedLogin = null;
    }
}
