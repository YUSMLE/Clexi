package com.clexi.clexi.dialog;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.clexi.clexi.R;
import com.clexi.clexi.app.App;
import com.clexi.clexi.app.Consts;
import com.clexi.clexi.helper.Broadcaster;
import com.clexi.clexi.helper.ScreenHelper;

/**
 * Created by Yousef on 4/16/2017.
 */

public class FlyingFOB extends Service
{

    public static final String TAG = FlyingFOB.class.getSimpleName();

    private WindowManager mWindowManager;
    private ImageView mFob;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mFob = new ImageView(this);

        mFob.setImageResource(R.mipmap.ic_fob);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                ScreenHelper.convertToPixels(getApplicationContext(), 50),
                ScreenHelper.convertToPixels(getApplicationContext(), 50),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mWindowManager.addView(mFob, params);

        try
        {
            mFob.setOnTouchListener(new View.OnTouchListener()
            {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            // Get current time in nano seconds
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;

                        case MotionEvent.ACTION_UP:
                            if (initialTouchX == event.getRawX() && initialTouchY == event.getRawY())
                            {
                                // User just clicked the view
                                Log.d("FOB", "onClick");
                            }
                            break;

                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            mWindowManager.updateViewLayout(mFob, paramsF);
                            break;
                    }

                    return false;
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mFob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("FOB", "onClick");

                Broadcaster.broadcast(App.getAppContext(), Consts.KEY_A);
            }
        });

        mFob.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Log.d("FOB", "onLongClick");

                Broadcaster.broadcast(App.getAppContext(), Consts.KEY_B);

                return true;
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mFob != null)
        {
            mWindowManager.removeView(mFob);
        }
    }
}
