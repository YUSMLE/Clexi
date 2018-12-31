package com.clexi.clexi.crypto;

import java.math.BigInteger;

/**
 * Created by Yusmle on 06/17/2016.
 */

public class Converter
{

    public static byte[] decodeFromHexadecimal(String str)
    {
        try
        {
            byte[] byteArray = new BigInteger("10" + str, 16).toByteArray();

            // Copy all the REAL bytes, not the "first"
            byte[] readyBytes = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, readyBytes, 0, readyBytes.length);

            return readyBytes;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return new byte[]{};
        }
    }

    public static String encodeToHexadecimal(byte[] data)
    {
        try
        {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < data.length; i++)
            {
                stringBuilder.append(String.format("%02X", data[i]));
            }

            return stringBuilder.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return "";
        }
    }

    public static String encodeToHexadecimal(byte data)
    {
        try
        {
            return String.format("%02X", data);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return "";
        }
    }

    public static byte[] decodeFromBase32(String str)
    {
        try
        {
            return Base32String.decode(str);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return new byte[]{};
        }
    }

    public static String encodeToBase32(byte[] data)
    {
        try
        {
            return Base32String.encode(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return "";
        }
    }

    public static byte[] decodeFromBase64(String str)
    {
        // in building...

        return null;
    }

    public static String encodeToBase64(byte[] data)
    {
        // in building...

        return null;
    }
}
