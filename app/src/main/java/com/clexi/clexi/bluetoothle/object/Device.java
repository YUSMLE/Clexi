package com.clexi.clexi.bluetoothle.object;

import com.orm.SugarRecord;

/**
 * Created by yousef on 9/6/2016.
 */

public class Device extends SugarRecord
{

    private String  name;
    private String  macAddress;

    public Device()
    {
        // Nothing
    }

    public Device(String name, String macAddress)
    {
        this.name = name;
        this.macAddress = macAddress;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }
}
