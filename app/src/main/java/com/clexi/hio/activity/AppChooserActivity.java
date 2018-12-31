package com.clexi.hio.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.clexi.hio.R;
import com.clexi.hio.app.Consts;
import com.clexi.hio.helper.PackageManagerHelper;
import com.clexi.hio.model.object.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppChooserActivity extends BaseActivity
{

    public static final String TAG = AppChooserActivity.class.getSimpleName();

    /****************************************************
     * VARIABLES
     ***************************************************/

    private List<AppInfo> apps;

    private AppsAdapter adapter;

    /****************************************************
     * VIEWS
     ***************************************************/

    @BindView(R.id.appList) RecyclerView appList;

    /****************************************************
     * ACTIVITY OVERRIDES
     ***************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_chooser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set ActionBar
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
        getMenuInflater().inflate(R.menu.menu_app_chooser, menu);

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

        // List of installed third part apps on this device
        List<ApplicationInfo> ais = PackageManagerHelper.getAllInstalledAppInfo(getApplicationContext());
        apps = new ArrayList<>();
        for (ApplicationInfo ai : ais)
        {
            Log.d(TAG, ai.packageName);

            if (!PackageManagerHelper.isSystemApp(ai))
            {
                Log.d(TAG, ai.packageName + " is'nt a system app!");

                apps.add(PackageManagerHelper.getAppInfo(getApplicationContext(), ai.packageName));
            }
        }

        AppsAdapter.Callback callback = new AppsAdapter.Callback()
        {
            @Override
            public void onSelect(AppInfo item)
            {
                Intent rIntent = new Intent();
                rIntent.putExtra(Consts.APP_NAME, item.appName);
                rIntent.putExtra(Consts.PACKAGE_NAME, item.packageName);
                rIntent.putExtra(Consts.VERSION_NAME, item.versionName);
                rIntent.putExtra(Consts.VERSION_CODE, item.versionCode);
                setResult(RESULT_OK, rIntent);
                finish();
            }
        };

        adapter = new AppsAdapter(getApplicationContext(), apps, callback);
        appList.setAdapter(adapter);

        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(AppChooserActivity.this, LinearLayoutManager.VERTICAL, false);
        appList.setLayoutManager(verticalLayoutManagaer);
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
