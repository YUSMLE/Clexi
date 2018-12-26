package com.clexi.clexi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.clexi.clexi.R;
import com.clexi.clexi.accessibility.CacheManager;
import com.clexi.clexi.app.Consts;
import com.clexi.clexi.dialog.ConfirmDialog;
import com.clexi.clexi.model.access.DbManager;
import com.clexi.clexi.model.object.Account;

import java.util.ArrayList;
import java.util.List;

import br.com.mauker.materialsearchview.MaterialSearchView;
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

    // Foreground App Information
    private String  mActiveApp;
    private boolean mIsBrowser;
    private String  mCurrentUrl;

    /****************************************************
     * VIEWS
     ***************************************************/

    // MaterialSearchView; init
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
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        getMenuInflater().inflate(R.menu.menu_search_for_login, menu);

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
        else if (id == R.id.action_search)
        {
            // MaterialSearchView; open the search view on the menu item click.
            mSearchView.openSearch();

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

        // MaterialSearchView; manage suggestions
        mSearchView.activityResumed();
        String[] arr = getResources().getStringArray(R.array.query_suggestions);
        mSearchView.addSuggestions(arr);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // MaterialSearchView; manage suggestions
        mSearchView.clearSuggestions();
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
        // MaterialSearchView; to close the search view using the back button
        if (mSearchView.isOpen())
        {
            // Close the search on the back button press.
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
        // MaterialSearchView; search via voice
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

        // MaterialSearchView; If you want to submit the query from the selected suggestion
        mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Do something when the suggestion list is clicked.
                String suggestion = mSearchView.getSuggestionAtPosition(position);

                mSearchView.setQuery(suggestion, true);
            }
        });
        // MaterialSearchView; handle either QueryTextChange or QueryTextSubmit events inside the MaterialSearchView
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                // Do some magic
                return false;
            }
        });
        // MaterialSearchView; handle the open or close events of the MaterialSearchView
        mSearchView.setSearchViewListener(new MaterialSearchView.SearchViewListener()
        {
            @Override
            public void onSearchViewOpened()
            {
                // Do something once the view is open.
            }

            @Override
            public void onSearchViewClosed()
            {
                // Do something once the view is closed.
            }
        });
        // MaterialSearchView; handle click event on voice icon
        mSearchView.setOnVoiceClickedListener(new MaterialSearchView.OnVoiceClickedListener()
        {
            @Override
            public void onVoiceClicked()
            {
                Log.d(TAG, "Try to voice search...");
            }
        });
        // MaterialSearchView; provide search suggestions
        mSearchView.addSuggestions(getResources().getStringArray(R.array.query_suggestions));
        // MaterialSearchView; customize view
        mSearchView.setTintAlpha(200);
        mSearchView.adjustTintAlpha(0.8f);

        /* List of Accounts */

        mAccounts = DbManager.listAllAccounts();

        AccountsAdapter.Callback callback = new AccountsAdapter.Callback()
        {
            @Override
            public void onSelect(final Account item)
            {
                // Cache it for login
                // todo later...

                new ConfirmDialog().setListener(new ConfirmDialog.DialogListener()
                {
                    @Override
                    public void onPositiveButtonClicked()
                    {
                        // Update account
                        if (mIsBrowser)
                        {
                            // Update url of account
                            item.setUrl(mCurrentUrl);
                        }
                        else
                        {
                            // Update appId of account
                            item.setAppId(mActiveApp);
                        }

                        // Update database
                        DbManager.updateAccount(item);

                        // TEST
                        CacheManager.cacheLogin(item.getId());

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
                        .setMessage(String.format(getString(R.string.confirm_assign_account), item.getTitle()))
                        .show(getFragmentManager(), "");
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
        Intent intent = getIntent();

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

        /** Execute the code that should be executed if the activity was launched from history */
        if (isLaunchedFromHistory(intent))
        {
            // This is a consumed intent
            startActivity(new Intent(SearchForLoginActivity.this, MainActivity.class));

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
