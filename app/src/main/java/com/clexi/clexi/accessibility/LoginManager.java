package com.clexi.clexi.accessibility;

import android.util.Log;

import com.clexi.clexi.model.access.DbManager;
import com.clexi.clexi.model.object.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yousef on 1/20/2018.
 */

public class LoginManager
{

    public static final String TAG = LoginManager.class.getSimpleName();

    public LoginManager()
    {
        // Nothing
    }

    public List<Account> getMatchingLoginsWith(String activeApp, boolean isBrowser, String currentUrl, String username)
    {
        // todo later...

        Log.d(TAG, "Information to filter logins:");
        Log.d(TAG, "activeApp:  " + activeApp);
        Log.d(TAG, "isBrowser:  " + isBrowser);
        Log.d(TAG, "currentUrl: " + currentUrl);
        Log.d(TAG, "username:   " + username);

        List<Account> matchingLogins = DbManager.findAccounts(activeApp, isBrowser, currentUrl, username);

        if (matchingLogins != null)
        {
            Log.d(TAG, "Count of matching logins: " + matchingLogins.size());
        }
        else
        {
            Log.d(TAG, "Count of matching logins: " + 0);
        }

        return matchingLogins;
    }

    public String retrievePasswordOf(Account account)
    {
        // todo later...

        return null;
    }
}
