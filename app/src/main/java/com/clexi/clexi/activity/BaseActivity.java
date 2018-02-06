package com.clexi.clexi.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.clexi.clexi.R;
import com.clexi.clexi.bluetoothle.BleService;
import com.clexi.clexi.helper.Utils;

public class BaseActivity extends AppCompatActivity
{

    public static final String TAG = BaseActivity.class.getSimpleName();

    /****************************************************
     * VARIABLES
     ***************************************************/

    // Nothing

    /****************************************************
     * VIEWS
     ***************************************************/

    // Nothing

    /****************************************************
     * ACTIVITY OVERRIDES
     ***************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
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
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }*/

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

        // Bind to BleService, again
        //bindToBleService();
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

        // Unbind from BleService for prevent of memory leak
        //unbindFromService();
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
        // Nothing
    }

    /****************************************************
     * FUNCTIONALITY
     ***************************************************/

    protected void startBleService()
    {
        // Start BleService, if it's not
        if (!Utils.isMyServiceRunning(getApplicationContext(), BleService.class))
        {
            BleService.startService(getApplicationContext());
        }
    }

    protected void bindToBleService()
    {
        // Bind to BleService
        Intent intent = new Intent(BaseActivity.this, BleService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    protected void unbindFromService()
    {
        // Unbind from BleService
        unbindService(mConnection);
        mBound = false;
    }

    /****************************************************
     * INNER CLASSES
     ***************************************************/

    protected BleService mService;
    protected boolean mBound = false;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BleService.LocalBinder binder = (BleService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            Log.i(TAG, "Connected to BleService");
        }

        @Override
        public void onServiceDisconnected(ComponentName className)
        {
            mBound = false;

            Log.i(TAG, "Disconnected from to BleService");
        }
    };

    /**
     * What do you want to do when BleService be connected?!
     */
    // todo later...

}
