package com.clexi.clexi.accessibility;

import com.clexi.clexi.model.object.Account;

import java.util.ArrayList;

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

    public ArrayList<Account> getMatchingLoginssWith(String activeApp, boolean isBrowser, String currentUrl, String username)
    {
        // todo later...

        // TEST
        // Mock Account
        ArrayList<Account> accounts = new ArrayList<Account>();
        Account account1 = new Account(
                "Facebook",
                "com.facebook",
                "facebook.com",
                "facebook_user",
                "facebook_pass",
                "facebook_note"
        );
        Account account2 = new Account(
                "Google",
                "com.google",
                "google.com",
                "google_user",
                "google_pass",
                "google_note"
        );
        Account account3 = new Account(
                "Microsoft",
                "com.microsoft",
                "microsoft.com",
                "microsoft_user",
                "microsoft_pass",
                "microsoft_note"
        );
        Account account4 = new Account(
                "Gharanjik",
                "com.gmail",
                "gmail.com",
                "jgharanjik",
                "jgharanjik_pass",
                "jgharanjik_note"
        );
        Account account5 = new Account(
                "Alopeyk",
                "com.gmail",
                "gmail.com",
                "09168793240",
                "alopeyk_pass",
                "alopeyk_note"
        );
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        accounts.add(account4);
        accounts.add(account5);

        return accounts;
    }

    public String retrievePasswordOf(Account account)
    {
        // todo later...

        return null;
    }
}
