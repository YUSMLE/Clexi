package com.clexi.hio.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class BitmapHelper
{

    /**
     * Solution to `AdaptiveIconDrawable cannot be cast to android.graphics.drawable.BitmapDrawable`
     * ClassCastException exception in API level 26 (Android 8).
     *
     * @param drawable
     * @return
     */
    @NonNull
    public static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable)
    {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bmp;
    }
}
