package com.clexi.clexi.dialog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.clexi.clexi.R;
import com.clexi.clexi.activity.AddAccountActivity;
import com.clexi.clexi.activity.MainActivity;
import com.clexi.clexi.activity.SearchForLoginActivity;
import com.clexi.clexi.app.Consts;
import com.clexi.clexi.helper.Broadcaster;
import com.clexi.clexi.helper.PackageManagerHelper;
import com.clexi.clexi.helper.ScreenHelper;
import com.clexi.clexi.helper.StringHelper;
import com.clexi.clexi.model.object.Account;
import com.clexi.clexi.model.object.AppInfo;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Yousef on 5/22/2017.
 */

public class AccountsDialog extends Service
{

    public static final String TAG = AccountsDialog.class.getSimpleName();

    private WindowManager mWindowManager;
    private View          mRootView;
    private RecyclerView  mAccountList;
    private TextView      mEmptyMessage;
    private Button        mCloseButton;
    private Button        mSearchButton;
    private Button        mNewButton;

    // Matching Logins
    private List<Account> mAccounts;

    // Foreground App Information
    private String  mActiveApp;
    private boolean mIsBrowser;
    private String  mCurrentUrl;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.w(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.w(TAG, "onStartCommand");

        // pull intent data
        mAccounts = intent.getParcelableArrayListExtra(Consts.ACCOUNTS);
        mActiveApp = intent.getStringExtra(Consts.ACTIVE_APP);
        mIsBrowser = intent.getBooleanExtra(Consts.IS_BROWSER, false);
        mCurrentUrl = intent.getStringExtra(Consts.CURRENT_URL);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // inflate root view
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.dialog_accounts, null);

        // bind views
        mAccountList = (RecyclerView) mRootView.findViewById(R.id.accountList);

        if (mAccounts == null || mAccounts.size() == 0)
        {
            // refer to add new item for this login
            mEmptyMessage = (TextView) mRootView.findViewById(R.id.emptyMessage);
            mEmptyMessage.setVisibility(View.VISIBLE);

            // get the title for this login
            if (mIsBrowser)
            {
                try
                {
                    String middleLevelDomain            = StringHelper.getMiddleLevelDomain(mCurrentUrl);
                    String capitalizedMiddleLevelDomain = middleLevelDomain.substring(0, 1).toUpperCase() + middleLevelDomain.substring(1);
                    mEmptyMessage.setText(String.format(
                            getString(R.string.no_matching_logins),
                            capitalizedMiddleLevelDomain
                    ));
                }
                catch (URISyntaxException e)
                {
                    e.printStackTrace();

                    mEmptyMessage.setText(String.format(
                            getString(R.string.no_matching_logins),
                            "this login"
                    ));
                }
            }
            else
            {
                AppInfo appInfo = PackageManagerHelper.getAppInfo(getApplicationContext(), mActiveApp);

                mEmptyMessage.setText(String.format(
                        getString(R.string.no_matching_logins),
                        appInfo.appName
                ));
            }

            mAccountList.setVisibility(View.GONE);
        }
        else
        {
            // callback
            AccountsDialogAdapter.Callback callback = new AccountsDialogAdapter.Callback()
            {
                @Override
                public void onSelect(Account item)
                {
                    Broadcaster.broadcast(
                            AccountsDialog.this.getApplicationContext(),
                            Consts.ACTION_LOGIN_FIRE,
                            item
                    );

                    destroyService();
                }
            };

            // adapter
            AccountsDialogAdapter adapter = new AccountsDialogAdapter(this, mAccounts, callback);
            mAccountList.setAdapter(adapter);

            // layout manager
            LinearLayoutManager verticalLayoutManagaerA = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mAccountList.setLayoutManager(verticalLayoutManagaerA);
        }

        // close button
        mCloseButton = (Button) mRootView.findViewById(R.id.closeButton);
        mCloseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                destroyService();
            }
        });

        // search button
        mSearchButton = (Button) mRootView.findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), SearchForLoginActivity.class);

                i.putExtra(Consts.ACTIVE_APP, mActiveApp);
                i.putExtra(Consts.IS_BROWSER, mIsBrowser);
                i.putExtra(Consts.CURRENT_URL, mCurrentUrl);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(i);

                destroyService();
            }
        });

        // new button
        mNewButton = (Button) mRootView.findViewById(R.id.newButton);
        mNewButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), AddAccountActivity.class);
                i.putExtra(Consts.ACTIVITY_TYPE, AddAccountActivity.ACTIVITY_TYPE_ADD_FOR_LOGIN);

                i.putExtra(Consts.ACTIVE_APP, mActiveApp);
                i.putExtra(Consts.IS_BROWSER, mIsBrowser);
                i.putExtra(Consts.CURRENT_URL, mCurrentUrl);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(i);

                destroyService();
            }
        });

        // custom button labels
        if (mAccounts == null || mAccounts.size() == 0)
        {
            mCloseButton.setText(getString(R.string.cancel));
            mNewButton.setText(getString(R.string.add));
        }

        // bind dialog
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                ScreenHelper.convertToPixels(getApplicationContext(), 321),
                ScreenHelper.convertToPixels(
                        getApplicationContext(),
                        (mAccounts == null || mAccounts.size() == 0) ? 200 : 300
                ),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0; //ScreenHelper.getDisplayWidthPixels(getApplicationContext());
        params.y = 0;

        mWindowManager.addView(mRootView, params);

        // set dialog floating
        try
        {
            mRootView.setOnTouchListener(new View.OnTouchListener()
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
                            mWindowManager.updateViewLayout(mRootView, paramsF);
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

        mRootView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Nothing
            }
        });

        // return START_STICKY for start again service in background when application killed by OS
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mRootView != null)
        {
            mWindowManager.removeView(mRootView);
        }
    }

    //TODO Manage Service **************************************************

    public static void startMyService(Context context)
    {
        Intent intent = new Intent(context, AccountsDialog.class);

        // Put extras here...

        context.startService(intent);
    }

    public static void destroyMyService(Context context)
    {
        Intent intent = new Intent(context, AccountsDialog.class);
        context.stopService(intent);
    }

    private void destroyService()
    {
        this.stopSelf();
    }
}
