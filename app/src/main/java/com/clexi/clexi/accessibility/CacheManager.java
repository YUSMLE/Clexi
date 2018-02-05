package com.clexi.clexi.accessibility;

import com.clexi.clexi.model.access.DbManager;
import com.clexi.clexi.model.object.Account;

/**
 * Created by Yousef on 1/16/2018.
 */

public class CacheManager
{

    public static final String TAG = CacheManager.class.getSimpleName();

    // The period that cached values will be valid (in second)
    private static final long VALID_PERIOD = 13;

    private static long mAccountId = 0;

    // The time that values cached (in second)
    public static long mCacheTimestamp;

    public static void cacheLogin(long accountId)
    {
        CacheManager.mAccountId = accountId;

        CacheManager.mCacheTimestamp = System.currentTimeMillis() / 1000;
    }

    public static Account retrieveCache()
    {
        if (CacheManager.isDeprecated())
        {
            CacheManager.mAccountId = 0;
        }

        Account account = DbManager.findAccountById(CacheManager.mAccountId);

        // Clear cache
        CacheManager.mAccountId = 0;

        return account;
    }

    private static boolean isDeprecated()
    {
        if (CacheManager.mAccountId != 0)
        {
            if ((System.currentTimeMillis() / 1000) - CacheManager.mCacheTimestamp <= CacheManager.VALID_PERIOD)
            {
                // Cached login is still valid
                return false;
            }
        }

        return true;
    }

    public static void clearCache()
    {
        CacheManager.mAccountId = 0;
    }
}
