package com.clexi.clexi.accessibility;

import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.clexi.clexi.helper.StringHelper;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Yousef on 1/15/2018.
 */

public class Utils
{

    public static final String TAG = Utils.class.getSimpleName();

    public static boolean isBrowser(String appPackageName)
    {
        return (appPackageName != null &&
                (appPackageName.equals("com.android.chrome") || appPackageName.equals("com.android.browser")));
    }

    public static String getWebViewTitle(AccessibilityNodeInfo source)
    {
        if (source == null)
        {
            return null;
        }

        for (int i = 0; i < source.getChildCount(); i++)
        {
            AccessibilityNodeInfo node = source.getChild(i);

            if (node == null)
            {
                continue;
            }

            if (node.getClassName() != null && node.getClassName().equals("android.webkit.WebView"))
            {
                if (node.getContentDescription() != null)
                {
                    return node.getContentDescription().toString();
                }

                return "";
            }

            if (getWebViewTitle(node) != null)
            {
                return getWebViewTitle(node);
            }

            node.recycle();
        }

        return null;
    }

    public static String getWebViewUrl(AccessibilityNodeInfo source)
    {
        String webViewTitle = null, webViewUrl = null;

        // First, try to get the current Title
        webViewTitle = Utils.getWebViewTitle(source);

        if (webViewTitle != null)
        {
            /**
             * Then, get the current URL
             */
            List<AccessibilityNodeInfo> nodes = source.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar");

            if (nodes.isEmpty())
            {
                nodes = source.findAccessibilityNodeInfosByViewId("com.android.browser:id/url");
            }

            for (AccessibilityNodeInfo node : nodes)
            {
                if (node.getText() != null)
                {
                    try
                    {
                        webViewUrl = StringHelper.getHostName2(node.getText().toString());
                    }
                    catch (MalformedURLException e)
                    {
                        e.printStackTrace();

                        if (e.toString().contains("protocol"))
                        {
                            try
                            {
                                webViewUrl = StringHelper.getHostName2("http://" + node.getText().toString());
                            }
                            catch (MalformedURLException e2)
                            {
                                // Ignore it

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return webViewUrl;
    }

    public static boolean isBrowserUrlBar(AccessibilityNodeInfo source)
    {
        return StringHelper.isValidUrl(source.getText());
    }

    /**
     * Ummm, didn't work :(
     */
    public static boolean isEditTextWithHint(AccessibilityNodeInfo node)
    {
        boolean isHint;
        String  text, hint;

        text = node.getText().toString();

        Log.d(TAG, "Text of node: " + text);

        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "");
        node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);

        Log.d(TAG, "node.refresh() = " + node.refresh());

        hint = node.getText().toString();

        Log.d(TAG, "Hint of node: " + hint);

        if (text != null && hint != null && text.equals(hint))
        {
            // Current value of node is a Hint!
            Log.d(TAG, text + " is a hint.");

            isHint = true;
        }
        else
        {
            Log.d(TAG, text + " is a real value.");

            isHint = false;
        }

        Bundle arguments2 = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments2);

        return isHint;
    }

    /**
     * Temporary solution for recognize hint values
     */
    public static boolean isShowingHintText(AccessibilityNodeInfo node)
    {
        String text;

        text = getTextOfNode(node);
        if (text != null)
        {
            Log.d(TAG, "Text of node: " + text);

            if (text.contains(" "))
            {
                return true;
            }

            if (StringHelper.isProbablyPersian(text))
            {
                return true;
            }

            if (text.equalsIgnoreCase("EMAIL"))
            {
                return true;
            }
        }

        return false;
    }

    public static String getTextOfNode(AccessibilityNodeInfo node)
    {
        CharSequence text, contentDescription;

        text = node.getText();
        if (text != null)
        {
            return text.toString();
        }

        contentDescription = node.getContentDescription();
        if (contentDescription != null)
        {
            return contentDescription.toString();
        }

        return null;
    }
}
