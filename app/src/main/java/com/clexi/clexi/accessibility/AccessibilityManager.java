package com.clexi.clexi.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.clexi.clexi.activity.MainActivity;
import com.clexi.clexi.app.Consts;
import com.clexi.clexi.dialog.AccountsDialog;
import com.clexi.clexi.helper.Broadcaster;
import com.clexi.clexi.model.object.Account;

import java.util.ArrayList;

/**
 * Created by Yousef on 1/15/2018.
 */

public class AccessibilityManager extends AccessibilityService
{

    public static final String TAG = AccessibilityManager.class.getSimpleName();

    private static final boolean DEBUG = false;

    /****************************************************
     * VARIABLES
     ***************************************************/

    // Foreground App Information
    private String  mActiveApp;
    private boolean mIsBrowser;
    private String  mCurrentUrl;

    // Root of foreground screen
    private AccessibilityNodeInfo mRoot;

    // Accessibility Node Infos
    private ArrayList<AccessibilityNodeInfo> mFields;
    private AccessibilityNodeInfo            mUsernameField;
    private AccessibilityNodeInfo            mPasswordField;

    private String mUsername;
    private String mPassword;

    private LoginManager mLoginManager;

    /****************************************************
     * SERVICE OVERRIDES
     ***************************************************/

    @Override
    protected void onServiceConnected()
    {
        super.onServiceConnected();

        Log.d(TAG, "onServiceConnected");

        // Init app
        init();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOWS_CHANGED)
        {
            Log.d(TAG, "AccessibilityEvent: TYPE_WINDOW_CHANGED");
        }
        else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
        {
            Log.d(TAG, "AccessibilityEvent: TYPE_WINDOW_STATE_CHANGED");

            // Check if this TYPE_WINDOW_STATE_CHANGED event is reffers to our app.
            if (getRootInActiveWindow().getPackageName().equals("com.clexi.clexi"))
            {
                // Give up, it's me :)
                Log.d(TAG, "PackageName of event reffers to our app.");
            }
            else
            {
                // Login, after search or add new account
                checkCachedLogin();
            }
        }
        else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
        {
            Log.d(TAG, "AccessibilityEvent: TYPE_WINDOW_CONTENT_CHANGED");
        }
        else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED)
        {
            Log.d(TAG, "AccessibilityEvent: TYPE_VIEW_FOCUSED");
        }
        else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED)
        {
            Log.d(TAG, "AccessibilityEvent: TYPE_VIEW_CLICKED");
        }
        else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
        {
            Log.d(TAG, "AccessibilityEvent: TYPE_VIEW_TEXT_CHANGED");
        }
        else
        {
            Log.d(TAG, "AccessibilityEvent: UNDEFINED");

            // Nothing
        }
    }

    @Override
    public void onInterrupt()
    {
        Log.d(TAG, "onInterrupt");
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event)
    {
        // Just dispatch hardware keys
        Log.d(TAG, "onKeyEvent");
        Log.d(TAG, "key code: " + event.getKeyCode());

        return super.onKeyEvent(event);
    }

    /****************************************************
     * FUNCTIONALITY
     ***************************************************/

    private void checkForegroundApp(AccessibilityNodeInfo root)
    {
        /**
         * We can use both of AccessibilityEvent and AccessibilityNodeInfo instances
         * for analyze the active window.
         */

        if (root == null)
        {
            // Root node in active window doesn't retrieved
            Log.d(TAG, "Root node in active window is equals to null.");

            return;
        }

        if (root.getPackageName() == null)
        {
            // Probably it's OS application
            Log.d(TAG, "PackageName of event/node is equals to null.");

            return;
        }

        if (root.getPackageName().equals("com.clexi.clexi"))
        {
            // Give up, it's me :)
            Log.d(TAG, "PackageName of event reffers to our app.");

            return;
        }

        if (((String) root.getPackageName()).contains("inputmethod") ||
                ((String) root.getPackageName()).contains("keyboard"))
        {
            // Give up, it's me again :)
            Log.d(TAG, "PackageName of event reffer to a Keyboard.");

            return;
        }

        // Detremine last foreground App and if it's browser
        mActiveApp = (String) getRootInActiveWindow().getPackageName();
        mIsBrowser = Utils.isBrowser(mActiveApp);

        // If it is Google Chrome (or other browsers), then retrieve the URL
        if (mIsBrowser)
        {
            mCurrentUrl = Utils.getWebViewUrl(root);
        }
        else
        {
            mCurrentUrl = null;
        }

        Log.d(TAG, "Active App:  " + mActiveApp);
        Log.d(TAG, "Is Browser:  " + mIsBrowser);
        Log.d(TAG, "Current Url: " + mCurrentUrl);
    }

    private void checkCachedLogin()
    {
        Account cachedLogin = CacheManager.retrieveCache();

        if (cachedLogin == null) // || CacheManager.isDeprecated())
        {
            // There is no cashed Login, or it's deprecated
            Log.d(TAG, "There is no cashed Login, or it's deprecated.");

            return;
        }

        // todo later...

        // Query content of active window
        mRoot = getRootInActiveWindow();

        // Retrieve active app informations
        checkForegroundApp(mRoot);

        if (cachedLogin.getAppId() != null &&
                cachedLogin.getAppId().equals(mActiveApp))
        {
            // We have a cached login. Probably we returned from Add/Edit operation.
            // So, fire that...
            Broadcaster.broadcast(AccessibilityManager.this, Consts.ACTION_LOGIN_FIRE, cachedLogin);

            return;
        }

        if (cachedLogin.getUrl() != null &&
                cachedLogin.getUrl().equals(mCurrentUrl))
        {
            // We have a cached login. Probably we returned from Add/Edit operation.
            // So, fire that...
            Broadcaster.broadcast(AccessibilityManager.this, Consts.ACTION_LOGIN_FIRE, cachedLogin);

            return;
        }

        // There is no cashed Login, or it's deprecated
        Log.d(TAG, "There is no cashed matching Login.");
    }

    // Let's try to find username and password fields
    private void getFields(AccessibilityNodeInfo source)
    {
        if (source == null)
        {
            return;
        }

        for (int i = 0; i < source.getChildCount(); i++)
        {
            AccessibilityNodeInfo node = source.getChild(i);

            if (node == null)
            {
                continue;
            }

            if (node.getChildCount() == 0)
            {
                if (node.getClassName() != null &&
                        node.getClassName().toString().contentEquals("android.widget.EditText") &&
                        node.isVisibleToUser() &&
                        !Utils.isBrowserUrlBar(node))
                {
                    // This is a text field wrapper,
                    // add it to list
                    mFields.add(node);

                    // DEBUG
                    Log.e(TAG, "Find EditText with text: " + Utils.getTextOfNode(node));

                    // Is this a password field although?!
                    if (node.isPassword())
                    {
                        // Yes, this is a password field!
                        mPasswordField = node;

                        try
                        {
                            if (mFields.size() >= 2)
                            {
                                // Most probably, this is a username field!
                                mUsernameField = mFields.get(mFields.size() - 2);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                getFields(node);
            }
        }
    }

    private void getUsernameField(ArrayList<AccessibilityNodeInfo> fields)
    {
        Log.d(TAG, "Try to find username field...");

        if (mUsernameField != null)
        {
            // Username field allready is found

            Log.d(TAG, "Username field allready is found.");

            return;
        }

        if (fields != null && fields.size() > 0)
        {
            mUsernameField = !fields.get(0).isPassword() ? fields.get(0) : null;
        }
    }

    // Let's try to fill the username or password field
    private void setField(AccessibilityNodeInfo source, String valueToInsert)
    {
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, valueToInsert);
        source.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    private int getFocusedField(ArrayList<AccessibilityNodeInfo> fields)
    {
        if (fields == null || fields.size() == 0)
        {
            // There is not any filed
            return -1;
        }

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).isFocused())
            {
                return i;
            }
        }

        // There is not any focused field
        return -1;
    }

    /****************************************************
     * LOGICS
     ***************************************************/

    private BroadcastReceiver mLoginReceiver;
    private IntentFilter      mLoginReceiverFilter;

    public void init()
    {
        mLoginManager = new LoginManager();
        mFields = new ArrayList<AccessibilityNodeInfo>();

        // Init the mLoginReceiver
        mLoginReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent.getAction().equals(Consts.ACTION_LOGIN_REQUEST))
                {
                    Log.d(TAG, "ACTION_LOGIN_REQUEST");

                    // Query content of active window
                    mRoot = getRootInActiveWindow();

                    if (mRoot.getPackageName().equals("com.clexi.clexi"))
                    {
                        // Give up, it's me :)
                        Log.d(TAG, "PackageName of event reffers to our app.");

                        return;
                    }

                    // Retrieve active app informations
                    checkForegroundApp(mRoot);

                    // First, clear already queried fields
                    mFields.clear();
                    mUsernameField = null;
                    mPasswordField = null;
                    mUsername = null;
                    mPassword = null;

                    // Then, query username and password fields
                    getFields(mRoot);

                    if (mUsernameField == null)
                    {
                        getUsernameField(mFields);
                    }

                    // And get theirs values
                    if (mUsernameField != null &&
                            Utils.getTextOfNode(mUsernameField) != null &&
                            !Utils.getTextOfNode(mUsernameField).isEmpty() &&
                            !Utils.isShowingHintText(mUsernameField))
                    {
                        mUsername = Utils.getTextOfNode(mUsernameField);
                    }

                    // If there is not any username field,
                    // or no username be setted,
                    // check the cache if there is any chached username
                    // todo later...

                    // TEST
                    if (mUsername == null)
                    {
                        Account cachedLogin = CacheManager.retrieveCache();
                        if (cachedLogin != null)
                        {
                            mUsername = cachedLogin.getUsername();
                        }
                    }

                    // TEST
                    Log.d(TAG, "Username value: " + mUsername);

                    ArrayList<Account> matchingLogins = (ArrayList) mLoginManager.getMatchingLoginsWith(
                            mActiveApp,
                            mIsBrowser,
                            mCurrentUrl,
                            mUsername
                    );

                    if (matchingLogins == null || matchingLogins.size() == 0)
                    {
                        /** Offer user to search or add a new Account */

                        if (!com.clexi.clexi.helper.Utils.isMyServiceRunning(getApplicationContext(), AccountsDialog.class))
                        {
                            /*Start the Service*/

                            Intent i = new Intent(AccessibilityManager.this, AccountsDialog.class);

                            i.putExtra(Consts.ACTIVE_APP, mActiveApp);
                            i.putExtra(Consts.IS_BROWSER, mIsBrowser);
                            i.putExtra(Consts.CURRENT_URL, mCurrentUrl);

                            startService(i);
                        }
                    }
                    else if (matchingLogins.size() == 1)
                    {
                        /** Try to retrieve password, and then insert values */

                        Broadcaster.broadcast(getApplicationContext(), Consts.ACTION_LOGIN_FIRE, matchingLogins.get(0));
                    }
                    else
                    {
                        /**
                         * Show the menu to user to choose an account,
                         * or search or add a new Account
                         */

                        if (!com.clexi.clexi.helper.Utils.isMyServiceRunning(getApplicationContext(), AccountsDialog.class))
                        {
                            /*Start the Service*/

                            Intent i = new Intent(AccessibilityManager.this, AccountsDialog.class);

                            i.putParcelableArrayListExtra(Consts.ACCOUNTS, matchingLogins);
                            i.putExtra(Consts.ACTIVE_APP, mActiveApp);
                            i.putExtra(Consts.IS_BROWSER, mIsBrowser);
                            i.putExtra(Consts.CURRENT_URL, mCurrentUrl);

                            startService(i);
                        }
                    }
                }
                else if (intent.getAction().equals(Consts.ACTION_LOGIN_FIRE))
                {
                    Log.d(TAG, "ACTION_LOGIN_FIRE");

                    // Query content of active window
                    mRoot = getRootInActiveWindow();

                    // First, clear already queried fields
                    mFields.clear();
                    mUsernameField = null;
                    mPasswordField = null;
                    mUsername = null;
                    mPassword = null;

                    // Then, query username and password fields
                    getFields(mRoot);

                    if (mUsernameField == null)
                    {
                        getUsernameField(mFields);
                    }

                    // Retrieve chosen account from intent
                    Account account = intent.getExtras().getParcelable(Consts.ACCOUNT);

                    if (mUsernameField != null && mPasswordField != null)
                    {
                        Log.d(TAG, "ACTION_LOGIN_FIRE: " + "We have both username and password fields.");

                        // First, insert the username
                        // todo later...

                        // Then, retrieve password
                        // todo later...

                        // When password retrieved, insert it
                        // todo later...

                        // TEST
                        setField(mUsernameField, account.getUsername());
                        setField(mPasswordField, account.getPassword());
                        Log.e(TAG, "Text of password field: " + Utils.getTextOfNode(mPasswordField));
                    }
                    else if (mUsernameField != null)
                    {
                        Log.d(TAG, "ACTION_LOGIN_FIRE: " + "We just have username field.");

                        // First, insert the username
                        // todo later...

                        // Then, cache the username,
                        // because probably this is a two step login form,
                        // and in the next step, we need to retrieve password according this username
                        // todo later...

                        // TEST
                        setField(mUsernameField, account.getUsername());

                        // TEST
                        CacheManager.cacheLogin(account.getId());
                    }
                    else if (mPasswordField != null)
                    {
                        Log.d(TAG, "ACTION_LOGIN_FIRE: " + "We just have password field.");

                        // First, retrieve password
                        // todo later...

                        // When password retrieved, insert it
                        // todo later...

                        // TEST
                        setField(mPasswordField, account.getPassword());
                    }
                    else
                    {
                        Log.d(TAG, "ACTION_LOGIN_FIRE: " + "We have not any login field.");

                        // Nothing
                    }
                }
                else if (intent.getAction().equals(Consts.ACTION_MANAGEMENT))
                {
                    Log.d(TAG, "ACTION_MANAGEMENT");

                    /*Open the main UI to manage the accounts*/

                    // todo later...

                    /*Intent i = new Intent(AccessibilityManager.this, MainActivity.class);
                    // Calling startActivity() from outside of an Activity context
                    // requires the FLAG_ACTIVITY_NEW_TASK flag.
                    // Is this really what you want? :)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/

                    // TEST
                    /**
                     * Show the menu to user to choose an account,
                     * or search or add a new Account
                     */

                    // Query content of active window
                    mRoot = getRootInActiveWindow();

                    if (mRoot.getPackageName().equals("com.clexi.clexi"))
                    {
                        // Give up, it's me :)
                        Log.d(TAG, "PackageName of event reffers to our app.");

                        return;
                    }

                    // Retrieve active app informations
                    checkForegroundApp(mRoot);

                    // First, clear already queried fields
                    mFields.clear();
                    mUsernameField = null;
                    mPasswordField = null;
                    mUsername = null;
                    mPassword = null;

                    // Then, query username and password fields
                    getFields(mRoot);

                    // Queries for username are removed from here...

                    ArrayList<Account> matchingLogins = (ArrayList) mLoginManager.getMatchingLoginsWith(
                            mActiveApp,
                            mIsBrowser,
                            mCurrentUrl,
                            mUsername
                    );

                    /**
                     * Show the menu to user to choose an account,
                     * or search or add a new Account
                     */

                    if (!com.clexi.clexi.helper.Utils.isMyServiceRunning(getApplicationContext(), AccountsDialog.class))
                    {
                            /*Start the Service*/

                        Intent i = new Intent(AccessibilityManager.this, AccountsDialog.class);

                        i.putParcelableArrayListExtra(Consts.ACCOUNTS, matchingLogins);
                        i.putExtra(Consts.ACTIVE_APP, mActiveApp);
                        i.putExtra(Consts.IS_BROWSER, mIsBrowser);
                        i.putExtra(Consts.CURRENT_URL, mCurrentUrl);

                        startService(i);
                    }
                }
                else
                {
                    // Nothing
                }
            }
        };

        mLoginReceiverFilter = new IntentFilter();
        mLoginReceiverFilter.addAction(Consts.ACTION_LOGIN_REQUEST);
        mLoginReceiverFilter.addAction(Consts.ACTION_LOGIN_FIRE);
        mLoginReceiverFilter.addAction(Consts.ACTION_MANAGEMENT);

        registerReceiver(mLoginReceiver, mLoginReceiverFilter);
    }

}
