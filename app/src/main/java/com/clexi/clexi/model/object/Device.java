package com.clexi.clexi.model.object;

import com.orm.SugarRecord;

/**
 * Created by Yousef on 2/6/2018.
 */

public class Device extends SugarRecord
{
    private String name;
    private String address;

    public Device()
    {
        // Nothing!
    }

    public Device(String name, String address)
    {
        this.name = name;
        this.address = address;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
