package com.clexi.hio.bluetoothle.object;

import com.clexi.hio.bluetoothle.Consts;

/**
 * Created by Yousef on 9/6/2017.
 */

public class InitBlePacket extends BlePacket
{

    public static final String TAG = InitBlePacket.class.getSimpleName();

    private byte   type;
    private byte[] length; // 3 bytes
    private byte[] data;

    public InitBlePacket()
    {
        // Nothing
    }

    public InitBlePacket(byte type, byte[] length, byte[] data)
    {
        this.type = type;
        this.length = length;
        this.data = data;
    }

    @Override
    public void pullFrom(byte[] packetData)
    {
        type = packetData[0];
        length = new byte[3];
        System.arraycopy(packetData, 1, length, 0, 3);
        data = new byte[Consts.DATA_LENGHT_PER_BLE_INIT_PACKET];
        System.arraycopy(packetData, 4, data, 0, Consts.DATA_LENGHT_PER_BLE_INIT_PACKET);
    }

    @Override
    public byte[] pushTo()
    {
        byte[] packetData = new byte[Consts.TOTAL_BLE_PACKET_LENGHT];

        packetData[0] = type;
        System.arraycopy(length, 0, packetData, 1, 3);
        System.arraycopy(data, 0, packetData, 4, Consts.DATA_LENGHT_PER_BLE_INIT_PACKET);

        return packetData;
    }

    /****************************************************
     * Getters & Setters
     ***************************************************/

    public byte getType()
    {
        return type;
    }

    public void setType(byte type)
    {
        this.type = type;
    }

    public byte[] getLength()
    {
        return length;
    }

    public void setLength(byte[] length)
    {
        this.length = length;
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
