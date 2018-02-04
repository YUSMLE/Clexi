package com.clexi.clexi.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.clexi.clexi.R;

import java.util.List;

public class ConnectionSettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TEST
        /* List of Devices */
        RecyclerView list = (RecyclerView) findViewById(R.id.deviceList);

        List<BluetoothDevice> devices = null;

        ConnectionSettingsAdapter.Callback callback = new ConnectionSettingsAdapter.Callback()
        {
            @Override
            public void onSelect(BluetoothDevice item)
            {
                // Nothing
            }
        };

        ConnectionSettingsAdapter adapter = new ConnectionSettingsAdapter(ConnectionSettingsActivity.this, devices, callback);
        list.setAdapter(adapter);

        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(ConnectionSettingsActivity.this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(verticalLayoutManagaer);
    }

}
