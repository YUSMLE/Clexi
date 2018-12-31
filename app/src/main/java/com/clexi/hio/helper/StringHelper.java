package com.clexi.hio.helper;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Yousef on 1/15/2018.
 */

public class StringHelper
{

    public final String TAG = StringHelper.class.getSimpleName();

    /****************************************************
     * String & Bytes
     ***************************************************/

    public static String printByteArray(byte[] mBytes)
    {
        String str = "[";

        for (byte mByte : mBytes)
        {
            str += mByte + ", ";
        }

        str += "]";

        return str;
    }

    /****************************************************
     * String & Arrays - Add & Remove
     ***************************************************/

    public static String[] addToArray(String[] sArray, String add)
    {
        if (sArray == null)
        {
            return null;
        }

        String[] dArrray = new String[sArray.length + 1];

        for (int i = 0; i < sArray.length; i++)
        {
            dArrray[i] = sArray[i];
        }

        dArrray[sArray.length] = add;

        return dArrray;
    }

    public static String[] removeFromArray(String[] sArray, String remove)
    {
        if (sArray == null)
        {
            return null;
        }

        int removableCount = 0, j = 0;

        for (int i = 0; i < sArray.length; i++)
        {
            if (sArray[i].equals(remove))
            {
                removableCount++;
            }
        }

        String[] dArrray = new String[sArray.length - removableCount];

        for (int i = 0; i < sArray.length; i++)
        {
            if (!sArray[i].equals(remove))
            {
                dArrray[j] = sArray[i];
                j++;
            }
        }

        return dArrray;
    }

    /****************************************************
     * String - URL
     ***************************************************/

    public static boolean isValidUrl(CharSequence sequence)
    {
        try
        {
            String text = sequence.toString();

            return isValidUrl(text);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return false;
        }
    }

    public static boolean isValidUrl(String text)
    {
        try
        {
            URL url = new URL(text);

            return true;
        }
        catch (MalformedURLException e)
        {
            //e.printStackTrace();

            return false;
        }
    }

    public static String getHostName(String url) throws URISyntaxException
    {
        URI uri = new URI(url);
        String host = uri.getHost();

        // To provide faultproof result, check if not null then return only hostname, without www.
        if (host == null)
        {
            return url.startsWith("www.") ? url.substring(4) : url;
        }

        return host.startsWith("www.") ? host.substring(4) : host;
    }

    public static String getHostName2(String urlAddress) throws MalformedURLException
    {
        URL url = new URL(urlAddress);
        String host = url.getHost();

        return host.startsWith("www.") ? host.substring(4) : host;
    }

    public static String getMiddleLevelDomain(String url) throws URISyntaxException
    {
        String hostName = getHostName(url);

        String[] splits = hostName.split("\\.");

        if (splits.length <= 0)
        {
            return null;
        }
        else if (splits.length == 1)
        {
            return splits[splits.length - 1];
        }
        else
        {
            return splits[splits.length - 2];
        }
    }

    /****************************************************
     * String - Persian, Urdu or Arabic alphabet
     ***************************************************/

    public static boolean isProbablyPersian(String s)
    {
        for (int i = 0; i < Character.codePointCount(s, 0, s.length()); i++)
        {
            int c = s.codePointAt(i);

            if (c >= 0x0600 && c <= 0x06FF)
            {
                return true;
            }
            if (c >= 0xFB50 && c <= 0xFDFF)
            {
                return true;
            }
            if (c >= 0xFE70 && c <= 0xFEFF)
            {
                return true;
            }
        }

        return false;
    }
}
