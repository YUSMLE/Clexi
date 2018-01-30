package com.clexi.clexi.model.access;


import android.content.Context;

import com.clexi.clexi.model.object.Account;
import com.orm.SugarContext;

import java.util.List;

/**
 * Created by yousef on 8/23/2016.
 */

public class DbManager
{

    public static final String TAG = DbManager.class.getSimpleName();

    /****************************************************
     * MANAGE DATABASE INSTANT
     ***************************************************/

    public static void startSugar(Context context)
    {
        SugarContext.init(context);
    }

    public static void stopSugar()
    {
        SugarContext.terminate();
    }

    /* Mock Data Helper */
    public static void addMockData()
    {
        if (DbManager.listAllAccounts() != null && DbManager.listAllAccounts().size() > 0)
        {
            return;
        }

        for (int i = 0; i < 4; i++)
        {
            DbManager.createAccount(new Account("Saderat", "ir.saderat", "bsi.ir", "sun123", "spw123", "This is an important credential."));
            DbManager.createAccount(new Account("Samen", "ir.samen", "samen.ir", "sun123", "spw123", "This is an important credential."));
            DbManager.createAccount(new Account("Gmail", "com.google.mail", "gmail.com", "gun123", "gpw123", "This is an important credential."));
            DbManager.createAccount(new Account("Facebook", "com.facebook", "facebook.com", "fun123", "fpw123", "This is an important credential."));
            DbManager.createAccount(new Account("Amazon", "com.amazon", "amazon.com", "aun123", "apw123", "This is an important credential."));
        }
    }

    /****************************************************
     * MANAGE ACCOUNTS
     ***************************************************/

    public static void createAccount(Account item)
    {
        item.save();
    }

    // Get the item id
    public static Account findAccountById(long id)
    {
        Account item = Account.findById(Account.class, id);

        return item;
    }

    // Get the items by app, isBrowser & url
    public static List<Account> findAccounts(String app, boolean isBrowser, String url)
    {
        List<Account> items = null;

        if (isBrowser)
        {
            items = Account.find(Account.class, "URL = ?", url);
        }
        else
        {
            items = Account.find(Account.class, "APP_ID = ?", app);
        }

        return items;
    }

    // Get the items by app, isBrowser, url & username
    public static List<Account> findAccounts(String app, boolean isBrowser, String url, String username)
    {
        if (username == null)
        {
            return findAccounts(app, isBrowser, url);
        }

        List<Account> items = null;

        if (isBrowser)
        {
            items = Account.find(Account.class, "URL = ? AND USERNAME = ?", url, username);
        }
        else
        {
            items = Account.find(Account.class, "APP_ID = ? AND USERNAME = ?", app, username);
        }

        return items;
    }

    public static List<Account> listAllAccounts()
    {
        List<Account> items = Account.listAll(Account.class);

        return items;
    }

    public static void updateAccount(Account item)
    {
        // modify the values before send record to here!
        // updates the previous entry with new values.

        item.save();
    }

    public static void deleteAccount(Account item)
    {
        item.delete();
    }

    public static void deleteAllAccounts()
    {
        Account.deleteAll(Account.class);
    }

    /****************************************************
     * MANAGE HISTORY
     ***************************************************/

    // Nothing

    /****************************************************
     * MANAGE DEVICES
     ***************************************************/

    // Nothing

    /****************************************************
     * MANAGE SETTINGS
     ***************************************************/

    // Nothing

}
