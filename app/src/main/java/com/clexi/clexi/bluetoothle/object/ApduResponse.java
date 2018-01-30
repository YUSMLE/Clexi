package com.clexi.clexi.bluetoothle.object;

/**
 * Created by Yousef on 9/6/2017.
 */

public class ApduResponse
{

    public static final String TAG = ApduResponse.class.getSimpleName();

    private byte[] data;
    private byte cla;
    private byte sw1;
    private byte sw2;

    public ApduResponse()
    {
        // Nothing
    }

    public ApduResponse(byte[] data, byte cla, byte sw1, byte sw2)
    {
        this.data = data;
        this.cla = cla;
        this.sw1 = sw1;
        this.sw2 = sw2;
    }

    public void pullFrom(byte[] apduData)
    {
        data = new byte[apduData.length - 3];
        cla = apduData[apduData.length - 3];
        sw1 = apduData[apduData.length - 2];
        sw2 = apduData[apduData.length - 1];

        System.arraycopy(apduData, 0, data, 0, apduData.length - 3);
    }

    public byte[] pushTo()
    {
        byte[] apduData = new byte[data.length + 3];

        apduData[apduData.length - 3] = cla;
        apduData[apduData.length - 2] = sw1;
        apduData[apduData.length - 1] = sw2;

        System.arraycopy(data, 0, apduData, 0, data.length);

        return apduData;
    }

    /****************************************************
     * Getters & Setters
     ***************************************************/

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    public byte getCla()
    {
        return cla;
    }

    public void setCla(byte cla)
    {
        this.cla = cla;
    }

    public byte getSw1()
    {
        return sw1;
    }

    public void setSw1(byte sw1)
    {
        this.sw1 = sw1;
    }

    public byte getSw2()
    {
        return sw2;
    }

    public void setSw2(byte sw2)
    {
        this.sw2 = sw2;
    }
}
