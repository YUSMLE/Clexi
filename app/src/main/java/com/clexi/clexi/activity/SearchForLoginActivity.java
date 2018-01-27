package com.clexi.clexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.clexi.clexi.R;
import com.clexi.clexi.model.access.DbManager;
import com.clexi.clexi.model.object.Account;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchForLoginActivity extends BaseActivity
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

    @BindView(R.id.searchView)  MaterialSearchView mSearchView;
    @BindView(R.id.accountList) RecyclerView       mAccountList;

    /****************************************************
     * ACTIVITY OVERRIDES
     ***************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_login);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_for_login_menu, menu);

        // Set MaterialSearchView
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

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
        if (mSearchView.isSearchOpen())
        {
            mSearchView.closeSearch();
        }
        else
        {
            super.onBackPressed();
        }
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
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0)
            {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd))
                {
                    mSearchView.setQuery(searchWrd, false);
                }
            }

            return;
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

        /* Data Binding */

        /* Material Search View */

        // Set the listeners
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //Do some magic
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener()
        {
            @Override
            public void onSearchViewShown()
            {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed()
            {
                //Do some magic
            }
        });

        // Allow/Disable VoiceSearch for MaterialSearchView
        mSearchView.setVoiceSearch(true);

        // Add suggestions to MaterialSearchView
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        // Add custom cursor to MaterialSearchView
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);

        /* List of Accounts */

        mAccounts = DbManager.listAllAccounts();

        AccountsAdapter.Callback callback = new AccountsAdapter.Callback()
        {
            @Override
            public void onSelect(Account item)
            {
                // Nothing
            }
        };

        mAccountsAdapter = new AccountsAdapter(SearchForLoginActivity.this, mAccounts, callback);
        mAccountList.setAdapter(mAccountsAdapter);

        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(SearchForLoginActivity.this, LinearLayoutManager.VERTICAL, false);
        mAccountList.setLayoutManager(verticalLayoutManagaer);
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
