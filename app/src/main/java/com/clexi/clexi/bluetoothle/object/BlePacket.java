package com.clexi.clexi.bluetoothle.object;

/**
 * Created by Yousef on 8/24/2017.
 */

public abstract class BlePacket
{

    public static final String TAG = BlePacket.class.getSimpleName();

    private byte[] data;

    public abstract void pullFrom(byte[] packetData);

    public abstract byte[] pushTo();

    public int calcTotalLen(byte[] length)
    {
        return (int) (length[0] << 16 | length[1] << 8 | length[2]);
    }

    public static int calcTotalPacket(int totalLen)
    {
        if (totalLen <= 16)
        {
            return 1;
        }

        if ((totalLen - 16) % 17 == 0)
        {
            return (totalLen - 16) / 17 + 1;
        }

        return (totalLen - 16) / 17 + 2;
    }
}
