package com.clexi.hio.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.clexi.hio.R;
import com.clexi.hio.bluetoothle.BleUtils;
import com.clexi.hio.model.access.DbManager;
import com.clexi.hio.model.object.Device;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionSettingsActivity extends BaseActivity
{

    public static final String TAG = ConnectionSettingsActivity.class.getSimpleName();

    /****************************************************
     * VARIABLES
     ***************************************************/

    private List<BluetoothDevice> mPairedDevices;
    private List<BluetoothDevice> mFoundDevices;

    private ConnectionSettingsAdapter mConnectionSettingsAdapter;

    /****************************************************
     * VIEWS
     ***************************************************/

    @BindView(R.id.deviceList) RecyclerView mDeviceList;

    /****************************************************
     * ACTIVITY OVERRIDES
     ***************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);
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
        getMenuInflater().inflate(R.menu.menu_connection_settings, menu);

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

        // Data Binding

        // todo later...

        // Get already paired devices
        mPairedDevices = BleUtils.getPairedDevices();
        if (mPairedDevices == null)
        {
            mPairedDevices = new ArrayList<BluetoothDevice>();
        }

        // Init array list that will be used while searching new devices
        mFoundDevices = new ArrayList<BluetoothDevice>();

        /* Init main list */

        ConnectionSettingsAdapter.Callback callback = new ConnectionSettingsAdapter.Callback()
        {
            @Override
            public void onSelect(BluetoothDevice item)
            {
                // Nothing
            }
        };

        mConnectionSettingsAdapter = new ConnectionSettingsAdapter(
                ConnectionSettingsActivity.this,
                mPairedDevices,
                mFoundDevices,
                callback
        );
        Device defaultDevice = DbManager.getDevice();
        mConnectionSettingsAdapter.setDefaultDevice(defaultDevice);

        mDeviceList.setAdapter(mConnectionSettingsAdapter);

        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(
                ConnectionSettingsActivity.this,
                LinearLayoutManager.VERTICAL,
                false
        );
        mDeviceList.setLayoutManager(verticalLayoutManagaer);
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
