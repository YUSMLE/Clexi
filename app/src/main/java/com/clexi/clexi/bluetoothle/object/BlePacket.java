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

    public int calcTotalLen(byte hlen, byte llen)
    {
        return (int) (hlen << 8 | llen);
    }

    public static int calcTotalPacket(int totalLen)
    {
        if (totalLen <= 17)
        {
            return 1;
        }

        if ((totalLen - 17) % 19 == 0)
        {
            return (totalLen - 17) / 19 + 1;
        }

        return (totalLen - 17) / 19 + 2;
    }
}
