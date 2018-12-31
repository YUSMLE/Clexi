package com.clexi.hio.model.access;


import android.content.Context;

import com.clexi.hio.model.object.Account;
import com.clexi.hio.model.object.Device;
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

        DbManager.createAccount(new Account(
                "Gmail",
                "com.gmail",
                "gmail.com",
                "behfar01",
                "497cfc124",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Outlook",
                "com.outlook",
                "login.live.com",
                "jbehfar",
                "497cfc124",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Reyhoon",
                "com.reyhoon",
                "reyhoon.com",
                "09168793240",
                "reyhoon",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Reyhoon",
                "com.reyhoon",
                "reyhoon.com",
                "09168793241",
                "reyhoon",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Reyhoon",
                "com.reyhoon",
                "reyhoon.com",
                "09168793242",
                "reyhoon",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Digikala",
                "com.digikala",
                "digikala.com",
                "jgharanjik@gmail.com",
                "digikala123456789",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Facebook - Yousef",
                "com.facebook",
                "m.facebook.com",
                "yusmle@yahoo.com",
                "497cfc124",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Facebook - Mani",
                "com.facebook",
                "m.facebook.com",
                "mani",
                "yusmle123456789",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Facebook - Mana",
                "com.facebook",
                "m.facebook.com",
                "mana",
                "yusmle123456789",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Varzesh3",
                "com.varzesh3",
                "account.varzesh3.com",
                "behfar",
                "behfar123456789",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Varzesh3 - Seyfi",
                "com.varzesh3",
                "account.varzesh3.com",
                "hasan_seyfi",
                "hasan123456789",
                "This is an important credential."
        ));
        DbManager.createAccount(new Account(
                "Bamilo Account",
                "com.bamilo",
                "bamilo.com",
                "faranak",
                "HRM123456789",
                "This is an important credential."
        ));
    }

    /****************************************************
     * MANAGE ACCOUNTS
     ***************************************************/

    public static void createAccount(Account item)
    {
        item.save();
    }

    // Get the item by id
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

    // Get the item
    public static Device getDevice()
    {
        List<Device> items = Device.listAll(Device.class);

        if (items != null && items.size() > 0)
        {
            return items.get(0);
        }

        return null;
    }

    // Set the device
    public static void setDevice(Device item)
    {
        // Delete already existing device
        deleteAllDevices();

        item.save();
    }

    public static void deleteAllDevices()
    {
        Device.deleteAll(Device.class);
    }

    /****************************************************
     * MANAGE SETTINGS
     ***************************************************/

    // Nothing

}
