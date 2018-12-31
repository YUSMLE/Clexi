package com.clexi.hio.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Yousef on 2/27/2017.
 */

public class Account extends SugarRecord implements Parcelable
{
    private String title;
    private String appId;
    private String url;
    private String username;
    private String password;
    private String note;
    private String lastUsedTimestamp;

    public Account()
    {
        // Nothing!
    }

    public Account(String title, String appId, String url, String username, String password, String note)
    {
        this.title = title;
        this.appId = appId;
        this.url = url;
        this.username = username;
        this.password = password;
        this.note = note;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getLastUsedTimestamp()
    {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(String lastUsedTimestamp)
    {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    /****************************************************
     * Make this class Parcelable
     ***************************************************/

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.title);
        dest.writeString(this.appId);
        dest.writeString(this.url);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.note);
        dest.writeString(this.lastUsedTimestamp);
        dest.writeValue(this.getId());
    }

    protected Account(Parcel in)
    {
        this.title = in.readString();
        this.appId = in.readString();
        this.url = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.note = in.readString();
        this.lastUsedTimestamp = in.readString();
        this.setId((Long) in.readValue(Long.class.getClassLoader()));
    }

    public static final Creator<Account> CREATOR = new Creator<Account>()
    {
        @Override
        public Account createFromParcel(Parcel source)
        {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size)
        {
            return new Account[size];
        }
    };
}
