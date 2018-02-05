package com.clexi.clexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.clexi.clexi.R;
import com.clexi.clexi.app.Consts;
import com.clexi.clexi.dialog.ConfirmDialog;
import com.clexi.clexi.dialog.FlyingFOB;
import com.clexi.clexi.model.access.DbManager;
import com.clexi.clexi.model.object.Account;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
{

    public static final String TAG = MainActivity.class.getSimpleName();

    /****************************************************
     * VARIABLES
     ***************************************************/

    private List<Account>   mAccounts;
    private AccountsAdapter mAccountsAdapter;

    /****************************************************
     * VIEWS
     ***************************************************/

    @BindView(R.id.fab)         FloatingActionButton mFab;
    @BindView(R.id.accountList) RecyclerView         mAccountList;

    /****************************************************
     * ACTIVITY OVERRIDES
     ***************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);

        // Add some dummy data :)
        DbManager.addMockData();

        // Intent Data
        pullIntentData();

        // Bind Views
        bindViews();

        // Test with simulator
        startService(new Intent(MainActivity.this, FlyingFOB.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
            // Let's go to manage our BLE connections
            goToConnectionSettings();

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
        Log.d(TAG, "onActivityResult");

        if (requestCode == Consts.REQUEST_ADD_EDIT_DELETE_ACCOUNT)
        {
            // Update list
            if (mAccountsAdapter != null)
            {
                mAccounts = DbManager.listAllAccounts();

                // Update list with dataset to show
                // todo later...

                mAccountsAdapter.updateList(mAccounts);
                mAccountsAdapter.notifyDataSetChanged();
            }
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

        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Add new empty account
                goToAddAccount();
            }
        });

        // Data Binding

        /* List of Accounts */
        mAccounts = DbManager.listAllAccounts();

        AccountsAdapter.Callback callback = new AccountsAdapter.Callback()
        {
            @Override
            public void onSelect(Account item)
            {
                createBottomSheetMenuDialog(item);
            }
        };

        mAccountsAdapter = new AccountsAdapter(MainActivity.this, mAccounts, callback);
        mAccountList.setAdapter(mAccountsAdapter);

        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mAccountList.setLayoutManager(verticalLayoutManagaer);
    }

    private void goToAddAccount()
    {
        Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
        intent.putExtra(Consts.ACTIVITY_TYPE, AddAccountActivity.ACTIVITY_TYPE_ADD);
        // With this below code, the second activity return result immediatelly!
        // So, I commented it for now.
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivityForResult(intent, Consts.REQUEST_ADD_EDIT_DELETE_ACCOUNT);
    }

    private void goToEditAccount(long accountId)
    {
        Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
        intent.putExtra(Consts.ACTIVITY_TYPE, AddAccountActivity.ACTIVITY_TYPE_EDIT);
        intent.putExtra(Consts.ACCOUNT_ID, accountId);
        // With this below code, the second activity return result immediatelly!
        // So, I commented it for now.
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivityForResult(intent, Consts.REQUEST_ADD_EDIT_DELETE_ACCOUNT);
    }

    private void goToConnectionSettings()
    {
        Intent intent = new Intent(MainActivity.this, ConnectionSettingsActivity.class);
        startActivity(intent);
    }

    private void createBottomSheetMenuDialog(final Account account)
    {
        BottomSheetMenuDialog dialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_account)
                .setItemClickListener(new BottomSheetItemClickListener()
                {
                    @Override
                    public void onBottomSheetItemClick(final MenuItem item)
                    {
                        if (item.getItemId() == R.id.menu_details)
                        {
                            // Go to AccountDetailsActivity
                            // todo later...
                        }
                        else if (item.getItemId() == R.id.menu_edit)
                        {
                            // Go to AddAccountActivity for edit
                            goToEditAccount(account.getId());
                        }
                        else if (item.getItemId() == R.id.menu_delete)
                        {
                            new ConfirmDialog().setListener(new ConfirmDialog.DialogListener()
                            {
                                @Override
                                public void onPositiveButtonClicked()
                                {
                                    // OK, delete the Account
                                    DbManager.deleteAccount(account);
                                    mAccounts.remove(account);

                                    // Update list with dataset to show
                                    // todo later...

                                    mAccountsAdapter.updateList(mAccounts);
                                    mAccountsAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onNegativeButtonClicked()
                                {
                                    // Nothing
                                }
                            })
                                    .setMessage(String.format(getString(R.string.confirm_delete_account), account.getTitle()))
                                    .show(getFragmentManager(), "");
                        }
                    }
                })
                .createDialog();

        dialog.show();
    }

    /****************************************************
     * FUNCTIONALITY
     ***************************************************/

    private void pullIntentData()
    {
        // Nothing
    }

    /****************************************************
     * INNER CLASSES
     ***************************************************/

    // Nothing

}
