package com.clexi.clexi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clexi.clexi.R;
import com.clexi.clexi.app.Consts;
import com.clexi.clexi.helper.PackageManagerHelper;
import com.clexi.clexi.helper.ScreenHelper;
import com.clexi.clexi.helper.StringHelper;
import com.clexi.clexi.helper.ViewHelper;
import com.clexi.clexi.model.access.DbManager;
import com.clexi.clexi.model.object.Account;
import com.clexi.clexi.model.object.AppInfo;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAccountActivity extends BaseActivity
{

    public static final String TAG = AddAccountActivity.class.getSimpleName();

    public static final String ACTIVITY_TYPE_ADD           = "ACTIVITY_TYPE_ADD";
    public static final String ACTIVITY_TYPE_EDIT          = "ACTIVITY_TYPE_EDIT";
    public static final String ACTIVITY_TYPE_ADD_FOR_LOGIN = "ACTIVITY_TYPE_ADD_FOR_LOGIN";

    /****************************************************
     * VARIABLES
     ***************************************************/

    // What are we going to do?
    // - Add a new item from base,
    // - Edit already existing item or
    // - Add a new item with pre-defined values and then go to login?
    public String mActivityType;

    public Account mAccount;

    // Foreground App Information
    private String  mActiveApp;
    private boolean mIsBrowser;
    private String  mCurrentUrl;

    private String mUsername;
    private String mPassword;

    private String mChoosenApp;

    /****************************************************
     * VIEWS
     ***************************************************/

    @BindView(R.id.inputLayoutTitle)    TextInputLayout inputLayoutTitle;
    @BindView(R.id.inputLayoutApp)      TextInputLayout inputLayoutApp;
    @BindView(R.id.inputLayoutUrl)      TextInputLayout inputLayoutUrl;
    @BindView(R.id.inputLayoutUsername) TextInputLayout inputLayoutUsername;
    @BindView(R.id.inputLayoutPassword) TextInputLayout inputLayoutPassword;
    @BindView(R.id.inputLayoutNote)     TextInputLayout inputLayoutNote;

    @BindView(R.id.title)    EditText          title;
    @BindView(R.id.app)      AppCompatEditText app;
    @BindView(R.id.url)      EditText          url;
    @BindView(R.id.username) EditText          username;
    @BindView(R.id.password) TextInputEditText password;
    @BindView(R.id.note)     EditText          note;

    @BindView(R.id.appChooser)        ImageButton appChooser;
    @BindView(R.id.passwordGenerator) ImageButton passwordGenerator;

    @BindView(R.id.actionSave) ImageView actionSave;

    @BindView(R.id.moreOptionsBtn) Button       moreOptionsBtn;
    @BindView(R.id.moreOptions)    LinearLayout moreOptions;

    /****************************************************
     * ACTIVITY OVERRIDES
     ***************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Intent Data
        pullIntentData();

        // Bind Views
        bindViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_account, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        return true;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Consts.REQUEST_GENERATE_PASSWORD)
        {
            // Request for Generate Password operation
            if (resultCode == RESULT_OK)
            {
                String generatedPassword = data.getStringExtra(Consts.GENERATED_PASSWORD);

                if (generatedPassword != null)
                {
                    password.setText(generatedPassword);
                }
                else
                {
                    // Generating password is failed!
                }
            }
        }
        else if (requestCode == Consts.REQUEST_CHOOSE_APPLICATION)
        {
            // Request for Choose Application operation
            if (resultCode == RESULT_OK)
            {
                mChoosenApp = data.getStringExtra(Consts.PACKAGE_NAME);

                AppInfo appInfo = new AppInfo();
                appInfo.appName = data.getStringExtra(Consts.APP_NAME);
                appInfo.packageName = data.getStringExtra(Consts.PACKAGE_NAME);
                appInfo.versionName = data.getStringExtra(Consts.VERSION_NAME);
                appInfo.versionCode = data.getIntExtra(Consts.VERSION_CODE, 0);

                if (appInfo != null)
                {
                    if (appInfo.appName != null)
                    {
                        app.setText(appInfo.appName);
                        Drawable appIcon = PackageManagerHelper.getAppInfo(getApplicationContext(), appInfo.packageName).icon;
                        Drawable appIconSmall = new BitmapDrawable(
                                getResources(),
                                Bitmap.createScaledBitmap(
                                        ((BitmapDrawable) appIcon).getBitmap(),
                                        ScreenHelper.convertToPixels(getApplicationContext(), 24),
                                        ScreenHelper.convertToPixels(getApplicationContext(), 24),
                                        true
                                )
                        );
                        app.setCompoundDrawablesWithIntrinsicBounds(null, null, appIconSmall, null);
                        app.setCompoundDrawablePadding(8);
                    }
                    else
                    {
                        // No application assigned before!
                    }
                }
            }
        }
        else
        {
            // Nothing
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /****************************************************
     * OTHER OVERRIDES
     ***************************************************/

    // Nothing

    /****************************************************
     * BIND VIEWS
     ***************************************************/

    private void bindViews()
    {
        // Bind views with ButterKnife
        ButterKnife.bind(this);

        // Data Binding
        if (mActivityType.equals(ACTIVITY_TYPE_ADD))
        {
            Log.d(TAG, "ACTIVITY_TYPE_ADD");

            bindDataAsAdd();

            // Defaults
            moreOptionsBtn.setTag(0);
            setMoreOptionsLayoutVisible(true);
        }
        else if (mActivityType.equals(ACTIVITY_TYPE_EDIT))
        {
            Log.d(TAG, "ACTIVITY_TYPE_EDIT");

            bindDataAsEdit();

            // Defaults
            moreOptionsBtn.setTag(0);
        }
        else if (mActivityType.equals(ACTIVITY_TYPE_ADD_FOR_LOGIN))
        {
            Log.d(TAG, "ACTIVITY_TYPE_ADD_FOR_LOGIN");

            bindDataAsAddForLogin();

            // Defaults
            moreOptionsBtn.setTag(0);
            username.requestFocus();
        }

        // Other Defaults
        app.setKeyListener(null);

        actionSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!checkValidation())
                {
                    // Some of values are not valid.
                    // Error messages will be handled by InputLayout.
                    return;
                }

                if (mActivityType.equals(ACTIVITY_TYPE_ADD))
                {
                    // Create the new item
                    mAccount = new Account(
                            title.getText().toString(),
                            mChoosenApp,
                            url.getText().toString(),
                            username.getText().toString(),
                            password.getText().toString(),
                            note.getText().toString()
                    );

                    DbManager.createAccount(mAccount);

                    // Finish activity if saving item (create/update) is done.
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                else if (mActivityType.equals(ACTIVITY_TYPE_EDIT))
                {
                    // Update the item
                    mAccount.setTitle(title.getText().toString());
                    mAccount.setAppId(mChoosenApp);
                    mAccount.setUrl(url.getText().toString());
                    mAccount.setUsername(username.getText().toString());
                    mAccount.setPassword(password.getText().toString());
                    mAccount.setNote(note.getText().toString());

                    DbManager.updateAccount(mAccount);

                    // Finish activity if saving item (create/update) is done.
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                else if (mActivityType.equals(ACTIVITY_TYPE_ADD_FOR_LOGIN))
                {
                    // Create the new item
                    mAccount = new Account(
                            title.getText().toString(),
                            mChoosenApp,
                            url.getText().toString(),
                            username.getText().toString(),
                            password.getText().toString(),
                            note.getText().toString()
                    );

                    DbManager.createAccount(mAccount);

                    // Cache it for login
                    // todo later...

                    // Finish activity if saving item (create/update) is done.
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });

        app.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AddAccountActivity.this, AppChooserActivity.class);
                startActivityForResult(intent, 4231);
            }
        });

        appChooser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AddAccountActivity.this, AppChooserActivity.class);
                startActivityForResult(intent, 4231);
            }
        });

        passwordGenerator.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(AddAccountActivity.this, PasswordGeneratorActivity.class);
                startActivityForResult(i, 1234);
            }
        });

        moreOptionsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ((int) moreOptionsBtn.getTag() == 0)
                {
                    // More Options Layout is closed, so open it
                    setMoreOptionsLayoutVisible(true);
                }
                else
                {
                    // More Options Layout is opened, so close it
                    setMoreOptionsLayoutVisible(false);
                }
            }
        });
    }

    private void bindDataAsAdd()
    {
        // Finally, we look at the 'mChoosenApp' to set as 'appId' for Account,
        // so init it at the first
        mChoosenApp = null;
    }

    private void bindDataAsEdit()
    {
        // Finally, we look at the 'mChoosenApp' to set as 'appId' for Account,
        // so init it at the first
        mChoosenApp = mAccount.getAppId();

        title.setText(mAccount.getTitle());
        url.setText(mAccount.getUrl());
        username.setText(mAccount.getUsername());
        password.setText(mAccount.getPassword());
        note.setText(mAccount.getNote());

        AppInfo appInfo = PackageManagerHelper.getAppInfo(getApplicationContext(), mAccount.getAppId());
        if (appInfo != null)
        {
            if (appInfo.appName != null)
            {
                app.setText(appInfo.appName);
                Drawable appIcon = PackageManagerHelper.getAppInfo(getApplicationContext(), appInfo.packageName).icon;
                Drawable appIconSmall = new BitmapDrawable(
                        getResources(),
                        Bitmap.createScaledBitmap(
                                ((BitmapDrawable) appIcon).getBitmap(),
                                ScreenHelper.convertToPixels(getApplicationContext(), 24),
                                ScreenHelper.convertToPixels(getApplicationContext(), 24),
                                true
                        )
                );
                app.setCompoundDrawablesWithIntrinsicBounds(null, null, appIconSmall, null);
                app.setCompoundDrawablePadding(8);
            }
            else
            {
                // No application assigned before!
            }
        }

        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void bindDataAsAddForLogin()
    {
        // Finally, we look at the 'mChoosenApp' to set as 'appId' for Account,
        // so init it at the first
        mChoosenApp = mActiveApp;

        if (mIsBrowser)
        {
            try
            {
                String middleLevelDomain            = StringHelper.getMiddleLevelDomain(mCurrentUrl);
                String capitalizedMiddleLevelDomain = middleLevelDomain.substring(0, 1).toUpperCase() + middleLevelDomain.substring(1);
                title.setText(capitalizedMiddleLevelDomain);
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
                title.setText(null);
                title.requestFocus();
            }
            url.setText(mCurrentUrl);
        }
        else
        {
            AppInfo appInfo = PackageManagerHelper.getAppInfo(getApplicationContext(), mActiveApp);
            if (appInfo != null)
            {
                title.setText(appInfo.appName);

                if (appInfo.appName != null)
                {
                    app.setText(appInfo.appName);
                    Drawable appIcon = PackageManagerHelper.getAppInfo(getApplicationContext(), appInfo.packageName).icon;
                    Drawable appIconSmall = new BitmapDrawable(
                            getResources(),
                            Bitmap.createScaledBitmap(
                                    ((BitmapDrawable) appIcon).getBitmap(),
                                    ScreenHelper.convertToPixels(getApplicationContext(), 24),
                                    ScreenHelper.convertToPixels(getApplicationContext(), 24),
                                    true
                            )
                    );
                    app.setCompoundDrawablesWithIntrinsicBounds(null, null, appIconSmall, null);
                    app.setCompoundDrawablePadding(8);
                }
                else
                {
                    // No application assigned before!
                }
            }
        }
    }

    private void setMoreOptionsLayoutVisible(boolean visible)
    {
        if (visible)
        {
            // More Options Layout is closed, so open it
            moreOptions.setVisibility(View.VISIBLE);

            moreOptionsBtn.setText(getString(R.string.less_options));
            moreOptionsBtn.setTag(1);
        }
        else
        {
            // More Options Layout is opened, so close it
            moreOptions.setVisibility(View.GONE);

            moreOptionsBtn.setText(getString(R.string.more_options));
            moreOptionsBtn.setTag(0);
        }
    }

    private boolean checkValidation()
    {
        // Reset all previous errors
        inputLayoutTitle.setErrorEnabled(false);
        inputLayoutApp.setErrorEnabled(false);
        inputLayoutUrl.setErrorEnabled(false);
        inputLayoutUsername.setErrorEnabled(false);
        inputLayoutPassword.setErrorEnabled(false);
        inputLayoutNote.setErrorEnabled(false);

        /** Title */
        if (title.getText().toString() == null || title.getText().toString().length() == 0)
        {
            inputLayoutTitle.setError("Title should not be empty.");
            ViewHelper.requestFocus(this, title);

            return false;
        }

        /** Password */
        if (password.getText().toString() == null || password.getText().toString().length() == 0)
        {
            inputLayoutPassword.setError("Password should not be empty.");
            ViewHelper.requestFocus(this, password);

            return false;
        }

        /** App or URL */
        if (app.getText().toString() == null || app.getText().toString().length() == 0)
        {
            if (url.getText().toString() == null || url.getText().toString().length() == 0)
            {
                inputLayoutUrl.setError("Both App and URL should not be empty.");
                ViewHelper.requestFocus(this, url);

                setMoreOptionsLayoutVisible(true);

                return false;
            }
        }

        // todo later...

        return true;
    }

    /****************************************************
     * FUNCTIONALITY
     ***************************************************/

    private void pullIntentData()
    {
        Intent intent = getIntent();

        mActivityType = intent.getStringExtra(Consts.ACTIVITY_TYPE);

        Log.d(TAG, "ACTIVITY_TYPE: " + mActivityType);

        if (mActivityType == null)
        {
            // Unexpected situation
            Log.e(TAG, "ActivityType is undefined!");

            System.exit(0);
        }

        if (mActivityType.equals(ACTIVITY_TYPE_EDIT))
        {
            // So, I expect to have an account id
            long accountId = intent.getLongExtra(Consts.ACCOUNT_ID, 0);

            if (accountId != 0)
            {
                mAccount = DbManager.findAccountById(accountId);
            }
        }
        else if (mActivityType.equals(ACTIVITY_TYPE_ADD_FOR_LOGIN))
        {
            // Pull foreground app information
            mActiveApp = intent.getStringExtra(Consts.ACTIVE_APP);
            mIsBrowser = intent.getBooleanExtra(Consts.IS_BROWSER, false);
            mCurrentUrl = intent.getStringExtra(Consts.CURRENT_URL);

            if (mIsBrowser)
            {
                // App ID is for Google Chrome, OS Browser or other browsers,
                // so we wont set them.
                mActiveApp = null;
            }
        }

        /** Execute the code that should be executed if the activity was launched from history */
        if (isLaunchedFromHistory(intent))
        {
            // This is a consumed intent
            startActivity(new Intent(AddAccountActivity.this, MainActivity.class));

            // Finish the current activity / clear the activity stack
            finish();
        }
    }

    /**
     * Execute the code that should be executed if the activity was launched from history
     */
    private boolean isLaunchedFromHistory(Intent intent)
    {
        boolean launchedFromHistory = intent != null ?
                (intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0 : false;

        // log activity launched state
        Log.d(TAG, "isTaskRoot: " + isTaskRoot());
        Log.d(TAG, "launchedFromHistory: " + launchedFromHistory);

        return launchedFromHistory;
    }

    /****************************************************
     * INNER CLASSES
     ***************************************************/

    // Nothing

}
