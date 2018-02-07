package com.clexi.clexi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.clexi.clexi.R;
import com.clexi.clexi.app.Consts;
import com.clexi.clexi.dialog.ConfirmDialog;
import com.clexi.clexi.helper.PackageManagerHelper;
import com.clexi.clexi.model.access.DbManager;
import com.clexi.clexi.model.object.Account;
import com.clexi.clexi.model.object.AppInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountDetailsActivity extends BaseActivity
{

    public static final String TAG = AccountDetailsActivity.class.getSimpleName();

    /****************************************************
     * VARIABLES
     ***************************************************/

    private long    mAccountId;
    private Account mAccount;

    /****************************************************
     * VIEWS
     ***************************************************/

    /**
     * collapsingToolbarLayout
     */
    @BindView(R.id.appIcon)          ImageView appIcon;
    @BindView(R.id.titleValue)       TextView  titleValue;
    @BindView(R.id.usernamePreValue) TextView  usernamePreValue;
    @BindView(R.id.delete)           TextView  delete;
    @BindView(R.id.edit)             TextView  edit;

    @BindView(R.id.appLabel)      TextView appLabel;
    @BindView(R.id.appValue)      TextView appValue;
    @BindView(R.id.urlLabel)      TextView urlLabel;
    @BindView(R.id.urlValue)      TextView urlValue;
    @BindView(R.id.usernameLabel) TextView usernameLabel;
    @BindView(R.id.usernameValue) TextView usernameValue;
    @BindView(R.id.passwordLabel) TextView passwordLabel;
    @BindView(R.id.passwordValue) TextView passwordValue;
    @BindView(R.id.noteLabel)     TextView noteLabel;
    @BindView(R.id.noteValue)     TextView noteValue;

    @BindView(R.id.passwordToggle) ImageButton passwordToggle;

    /****************************************************
     * ACTIVITY OVERRIDES
     ***************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Show CollapsingToolbarLayout Title ONLY when collapsed, Method #1:
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        //collapsingToolbar.setTitle("Title");
        //collapsingToolbar.setExpandedTitleColor(Color.parseColor("#00000000"));
        //collapsingToolbar.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));

        // Show CollapsingToolbarLayout Title ONLY when collapsed, Method #2:
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if (scrollRange == -1)
                {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0)
                {
                    collapsingToolbar.setTitle(mAccount.getTitle());
                    isShow = true;
                }
                else if (isShow)
                {
                    collapsingToolbar.setTitle(" "); // carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        // Intent Data
        pullIntentData();

        // Bind Views
        bindViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_details, menu);

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
        else if (id == android.R.id.home)
        {
            // App icon in action bar clicked; go home or finish this activity
            /*Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/

            finish();

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

        AppInfo appInfo = PackageManagerHelper.getAppInfo(getApplicationContext(), mAccount.getAppId());

        Drawable appIcon;
        if (appInfo != null)
        {
            appIcon = PackageManagerHelper.getAppInfo(getApplicationContext(), appInfo.packageName).icon;
        }
        else
        {
            // Using TextDrawable library for generating custom icon

            ColorGenerator colorGenerator;
            TextDrawable   textDrawable;

            colorGenerator = ColorGenerator.MATERIAL;
            textDrawable = TextDrawable.builder()
                    .beginConfig().toUpperCase().endConfig()
                    .buildRound(mAccount.getTitle().substring(0, 2), colorGenerator.getColor(mAccount.getTitle()));

            appIcon = textDrawable;
        }

        /** CollapsingToolbarLayout */
        this.appIcon.setImageDrawable(appIcon);
        titleValue.setText(mAccount.getTitle());
        usernamePreValue.setText(mAccount.getUsername());

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToEditAccount(mAccount.getId());

                // Finish the current activity
                // We have to set result here...
                // todo later...
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new ConfirmDialog().setListener(new ConfirmDialog.DialogListener()
                {
                    @Override
                    public void onPositiveButtonClicked()
                    {
                        // OK, delete the Account
                        DbManager.deleteAccount(mAccount);

                        // Finish activity if saving item (create/update) is done.
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onNegativeButtonClicked()
                    {
                        // Nothing
                    }
                })
                        .setMessage(String.format(getString(R.string.confirm_delete_account), mAccount.getTitle()))
                        .show(getFragmentManager(), "");
            }
        });

        /** Other Values */
        if (appInfo != null)
        {
            appValue.setText(appInfo.appName);
        }
        else
        {
            appValue.setText("Browser");
        }
        urlValue.setText(mAccount.getUrl());
        usernameValue.setText(mAccount.getUsername());
        passwordValue.setText("******");
        noteValue.setText(mAccount.getNote());

        // Toogle password field value visibility
        passwordToggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (true)
                {
                    Log.d(TAG, "passwordValue is a Password field!");
                }
                else
                {
                    Log.d(TAG, "passwordValue isn't a Password field!");
                }

                //passwordValue.setTransformationMethod(new PasswordTransformationMethod());
                //passwordValue.setInputType(InputType.TYPE_CLASS_TEXT) | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                /** Try a cooler behavior */
                // First, get and show the password value
                passwordValue.setText(mAccount.getPassword());
                passwordToggle.setImageResource(R.drawable.ic_eye);
                passwordToggle.setEnabled(false);

                // Then, hide/clean again the password value after 5sec
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            passwordValue.setText("******");
                            passwordToggle.setImageResource(R.drawable.ic_eye_strike);
                            passwordToggle.setEnabled(true);
                        }
                        catch (Exception e)
                        {
                            // Maybe activity is finished or paused!

                            e.printStackTrace();
                        }
                    }
                }, 5000);
            }
        });
    }

    private void goToEditAccount(long accountId)
    {
        Intent intent = new Intent(AccountDetailsActivity.this, AddAccountActivity.class);
        intent.putExtra(Consts.ACTIVITY_TYPE, AddAccountActivity.ACTIVITY_TYPE_EDIT);
        intent.putExtra(Consts.ACCOUNT_ID, accountId);
        // With this below code, the second activity return result immediatelly!
        // So, I commented it for now.
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
    }

    /****************************************************
     * FUNCTIONALITY
     ***************************************************/

    private void pullIntentData()
    {
        mAccountId = getIntent().getLongExtra(Consts.ACCOUNT_ID, 0);

        // Get the dataset
        if (mAccountId != 0)
        {
            mAccount = DbManager.findAccountById(mAccountId);
        }
    }

    /****************************************************
     * INNER CLASSES
     ***************************************************/

    // Nothing

}
