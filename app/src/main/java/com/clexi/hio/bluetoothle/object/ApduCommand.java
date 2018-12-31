package com.clexi.hio.bluetoothle.object;

/**
 * Created by Yousef on 9/6/2017.
 */

public class ApduCommand
{

    public static final String TAG = ApduCommand.class.getSimpleName();

    private byte   cla;
    private byte   ins;
    private byte   p1;
    private byte   p2;
    private byte   lc1;
    private byte   lc2;
    private byte   lc3;
    private byte[] data;

    // Optional
    //private byte le;

    public ApduCommand()
    {
        // Nothing
    }

    public ApduCommand(byte cla, byte ins, byte p1, byte p2, byte lc1, byte lc2, byte lc3, byte[] data)
    {
        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
        this.lc1 = lc1;
        this.lc2 = lc2;
        this.lc3 = lc3;
        this.data = data;
    }

    public void pullFrom(byte[] apduData)
    {
        cla = apduData[0];
        ins = apduData[1];
        p1 = apduData[2];
        p2 = apduData[3];
        lc1 = apduData[4];
        lc2 = apduData[5];
        lc3 = apduData[6];
        //data = new byte[lc & 0xFF]; //todo later...

        //System.arraycopy(apduData, 5, data, 0, lc & 0xFF); // todo later...
    }

    public byte[] pushTo()
    {
        byte[] apduData = new byte[data.length + 7];

        apduData[0] = cla;
        apduData[1] = ins;
        apduData[2] = p1;
        apduData[3] = p2;
        apduData[4] = lc1;
        apduData[5] = lc2;
        apduData[6] = lc3;

        System.arraycopy(data, 0, apduData, 7, data.length);

        return apduData;
    }

    /****************************************************
     * Getters & Setters
     ***************************************************/

    public byte getCla()
    {
        return cla;
    }

    public void setCla(byte cla)
    {
        this.cla = cla;
    }

    public byte getIns()
    {
        return ins;
    }

    public void setIns(byte ins)
    {
        this.ins = ins;
    }

    public byte getP1()
    {
        return p1;
    }

    public void setP1(byte p1)
    {
        this.p1 = p1;
    }

    public byte getP2()
    {
        return p2;
    }

    public void setP2(byte p2)
    {
        this.p2 = p2;
    }

    public byte getLc1()
    {
        return lc1;
    }

    public void setLc1(byte lc1)
    {
        this.lc1 = lc1;
    }

    public byte getLc2()
    {
        return lc2;
    }

    public void setLc2(byte lc2)
    {
        this.lc2 = lc2;
    }

    public byte getLc3()
    {
        return lc3;
    }

    public void setLc3(byte lc3)
    {
        this.lc3 = lc3;
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }
}
