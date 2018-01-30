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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof CachedLogin))
        {
            return false;
        }

        CachedLogin that = (CachedLogin) o;

        if (isBrowser != that.isBrowser)
        {
            return false;
        }
        if (activeApp != null ? !activeApp.equals(that.activeApp) : that.activeApp != null)
        {
            return false;
        }
        if (currentUrl != null ? !currentUrl.equals(that.currentUrl) : that.currentUrl != null)
        {
            return false;
        }

        return selectedUsername != null ? selectedUsername.equals(that.selectedUsername) : that.selectedUsername == null;
    }

    @Override
    public int hashCode()
    {
        int result = activeApp != null ? activeApp.hashCode() : 0;
        result = 31 * result + (isBrowser ? 1 : 0);
        result = 31 * result + (currentUrl != null ? currentUrl.hashCode() : 0);
        result = 31 * result + (selectedUsername != null ? selectedUsername.hashCode() : 0);

        return result;
    }
}
