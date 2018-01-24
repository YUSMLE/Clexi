package com.clexi.clexi.accessibility;

/**
 * Created by Yousef on 1/23/2018.
 */

public class CachedLogin
{

    // Last Foreground App Information
    public String  activeApp;
    public boolean isBrowser;
    public String  currentUrl;

    // Selected username
    public String selectedUsername;

    // Matched password
    public String matchedPassword;

    // The time that values cached (in second)
    public long cacheTimestamp;

    public CachedLogin(String activeApp, boolean isBrowser, String currentUrl, String selectedUsername)
    {
        this.activeApp = activeApp;
        this.isBrowser = isBrowser;
        this.currentUrl = currentUrl;
        this.selectedUsername = selectedUsername;
    }
}
