package com.clexi.clexi.helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ScreenHelper
{
	
	public static float getScale(Context mContext)
	{
		return mContext.getResources().getDisplayMetrics().density;
	}
	
	public static int convertToDip(Context mContext, float px)
    {
        return (int) ((px - 0.5f) / getScale(mContext));
    }
	
	public static int convertToPixels(Context mContext, int dp)
    {
        return (int) (dp * getScale(mContext) + 0.5f) ;
    }
	
	public static int getDisplayWidthPixels(Context mContext)
	{
		return mContext.getResources().getDisplayMetrics().widthPixels;
	}
	
	public static int getDisplayHeightPixels(Context mContext)
	{
		return mContext.getResources().getDisplayMetrics().heightPixels;
	}
	
	/**
	 * TOOLS
	 */
	public static void hideSoftKeyboard(Context context, View view)
	{
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null && view != null)
		{
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
	public static void toggleSoftKeyboard(Context context, View view)
	{
		view.requestFocus();
	
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null && view != null)
		{
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}
	
	public static void showSoftKeyboard(Context context, View view)
	{
		view.requestFocus();
		
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null && view != null)
		{
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}

}
