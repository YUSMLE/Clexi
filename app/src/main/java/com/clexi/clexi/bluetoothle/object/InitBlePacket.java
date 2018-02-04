package com.clexi.clexi.bluetoothle.object;

import com.clexi.clexi.bluetoothle.Consts;

/**
 * Created by Yousef on 9/6/2017.
 */

public class InitBlePacket extends BlePacket
{

    public static final String TAG = InitBlePacket.class.getSimpleName();

    private byte   cmd;
    private byte   hlen;
    private byte   llen;
    private byte[] data;

    public InitBlePacket()
    {
        // Nothing
    }

    public InitBlePacket(byte cmd, byte hlen, byte llen, byte[] data)
    {
        this.cmd = cmd;
        this.hlen = hlen;
        this.llen = llen;
        this.data = data;
    }

    @Override
    public void pullFrom(byte[] packetData)
    {
        cmd = packetData[0];
        hlen = packetData[1];
        llen = packetData[2];
        data = new byte[Consts.DATA_LENGHT_PER_BLE_INIT_PACKET];

        System.arraycopy(packetData, 3, data, 0, Consts.DATA_LENGHT_PER_BLE_INIT_PACKET);
    }

    @Override
    public byte[] pushTo()
    {
        byte[] packetData = new byte[Consts.TOTAL_BLE_PACKET_LENGHT];

        packetData[0] = cmd;
        packetData[1] = hlen;
        packetData[2] = llen;

        System.arraycopy(data, 0, packetData, 3, Consts.DATA_LENGHT_PER_BLE_INIT_PACKET);

        return packetData;
    }

    /****************************************************
     * Getters & Setters
     ***************************************************/

    public byte getCmd()
    {
        return cmd;
    }

    public void setCmd(byte cmd)
    {
        this.cmd = cmd;
    }

    public byte getHlen()
    {
        return hlen;
    }

    public void setHlen(byte hlen)
    {
        this.hlen = hlen;
    }

    public byte getLlen()
    {
        return llen;
    }

    public void setLlen(byte llen)
    {
        this.llen = llen;
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
