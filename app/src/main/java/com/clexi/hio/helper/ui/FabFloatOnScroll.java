package com.clexi.hio.helper.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Created by Valdio Veliu on 1/13/2016.
 */

public class FabFloatOnScroll extends FloatingActionButton.Behavior
{

    public FabFloatOnScroll(Context context, AttributeSet attrs)
    {
        super();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        // child -> Floating Action Button
        if (dyConsumed > 0)
        {
            CoordinatorLayout.LayoutParams layoutParams     = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int                            fab_bottomMargin = layoutParams.bottomMargin;

            child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator(new LinearInterpolator()).start();
        }
        else if (dyConsumed < 0)
        {
            child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes)
    {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

}