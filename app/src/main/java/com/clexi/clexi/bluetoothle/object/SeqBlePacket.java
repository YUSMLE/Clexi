package com.clexi.clexi.bluetoothle.object;

import com.clexi.clexi.bluetoothle.Consts;

/**
 * Created by Yousef on 9/6/2017.
 */

public class SeqBlePacket extends BlePacket
{

    public static final String TAG = SeqBlePacket.class.getSimpleName();

    private byte   seq;
    private byte[] data;

    public SeqBlePacket()
    {
        // Nothing
    }

    public SeqBlePacket(byte seq, byte[] data)
    {
        this.seq = seq;
        this.data = data;
    }

    @Override
    public void pullFrom(byte[] packetData)
    {
        seq = packetData[0];
        data = new byte[Consts.DATA_LENGHT_PER_BLE_SEQ_PACKET];

        System.arraycopy(packetData, 1, data, 0, Consts.DATA_LENGHT_PER_BLE_SEQ_PACKET);
    }

    @Override
    public byte[] pushTo()
    {
        byte[] packetData = new byte[Consts.TOTAL_BLE_PACKET_LENGHT];

        packetData[0] = seq;

        System.arraycopy(data, 0, packetData, 1, Consts.DATA_LENGHT_PER_BLE_SEQ_PACKET);

        return packetData;
    }

    /****************************************************
     * Getters & Setters
     ***************************************************/

    public byte getSeq()
    {
        return seq;
    }

    public void setSeq(byte seq)
    {
        this.seq = seq;
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
