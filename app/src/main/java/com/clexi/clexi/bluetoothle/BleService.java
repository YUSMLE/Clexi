package com.clexi.clexi.bluetoothle;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by Yousef on 8/23/2017.
 */

public class BleService extends Service
{

    public static final String TAG = BleService.class.getSimpleName();

    private static boolean debug = false;

    /****************************************************
     * VARIABLES
     ***************************************************/

    /*Instance approach*/
    public static BleService mInstance;

    /*Binder aproach*/
    private final IBinder mBinder = new BleService.LocalBinder();

    /*BLUETOTH LE CONNECTION REQUIREMENTS*/
    private BluetoothManager mManager;
    private BluetoothAdapter mAdapter;
    private BluetoothGatt    mGatt;
    private GattCallback     mGattCallback;

    /*BLUETOTH LE SCAN*/
    private Scanner mScanner;

    /*BLUETOTH LE CONNECTION STATES*/
    private BleState mBleState;
    private String   mConnectedAddress;

    /*Listening for bluetooth state changes*/
    private BluetoothStateChangesReceiver mBluetoothStateReceiver;
    private IntentFilter                  mBluetoothStateFilter;

    /****************************************************
     * SERVICE OVERRIDES
     ***************************************************/

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        BleService.mInstance = this;

        // Register BluetoothStateChangeReceiver
        mBluetoothStateReceiver = new BluetoothStateChangesReceiver();
        mBluetoothStateFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mBluetoothStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mBluetoothStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        mBluetoothStateFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mBluetoothStateReceiver, mBluetoothStateFilter);

        /** In the name of 'GOD' */
        start();

        // Return START_STICKY for start again service in background when application killed by OS
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.
        // In this particular example, close() is invoked
        // when the UI is disconnected from the Service.
        close();

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent)
    {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        try
        {
            // Disconnect and clear everything
            // todo later...
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // todo later...
        }

        super.onTaskRemoved(rootIntent);
    }

    /****************************************************
     * GATT
     ***************************************************/

    private boolean initAdapter()
    {
        try
        {
            if (mManager == null)
            {
                mManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

                if (mManager == null)
                {
                    if (debug)
                    {
                        Log.d(TAG, "Unable to initialize BluetoothManager.");
                    }

                    return false;
                }
            }

            mAdapter = mManager.getAdapter();

            if (mAdapter == null)
            {
                if (debug)
                {
                    Log.d(TAG, "Unable to get BluetoothAdapter.");
                }

                return false;
            }

            return true;
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while Initialize Bluetooth LE.", e);
            }

            return false;
        }
    }

    /**
     * Connects to the GATT server hosted on the BLE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully.
     * The connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)} callback.
     */
    public boolean connect(String address)
    {
        try
        {
            if (mAdapter == null || address == null)
            {
                if (debug)
                {
                    Log.d(TAG, "BluetoothAdapter not initialized or unspecified address.");
                }

                return false;
            }

            // Previously connected device? Try to reconnect.
            if (mConnectedAddress != null && mConnectedAddress.equals(address) && mGatt != null)
            {
                if (debug)
                {
                    Log.d(TAG, "Trying to use an existing BluetoothGatt for connection.");
                }

                if (mGatt.connect())
                {
                    return true;
                }
            }

            BluetoothDevice device = mAdapter.getRemoteDevice(address);

            if (device == null)
            {
                if (debug)
                {
                    Log.d(TAG, "Device not found. Unable to connect.");
                }

                return false;
            }

            // Init BluetoothGattCallback here
            mGattCallback = new GattCallback();

            // We want to directly connect to the device, so we are setting the autoConnect parameter to false.
            if (Build.VERSION.SDK_INT < 23)
            {
                mGatt = device.connectGatt(this, false, mGattCallback);
            }
            else
            {
                mGatt = device.connectGatt(this, true, mGattCallback, BluetoothDevice.TRANSPORT_LE);
                mGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
            }

            BleUtils.refreshDeviceCache(mGatt);

            return true;
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while BluetoothGatt connecting", e);
            }

            return false;
        }
    }

    /**
     * Disconnects an existing connection or cancel a pending connection.
     *
     * The disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)} callback.
     */
    public void disconnect()
    {
        try
        {
            if (mAdapter == null || mGatt == null)
            {
                if (debug)
                {
                    Log.d(TAG, "BluetoothAdapter not initialized.");
                }

                return;
            }

            mGatt.disconnect();
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while BluetoothGatt disconnecting", e);
            }
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are released properly.
     */
    public void close()
    {
        try
        {
            if (mGatt == null)
            {
                return;
            }

            mGatt.close();
            mGatt = null;
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while BluetoothGatt closing", e);
            }
        }
    }

    /****************************************************
     * TRANSMIT
     ***************************************************/

    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        try
        {
            if (mAdapter == null || mGatt == null)
            {
                if (debug)
                {
                    Log.d(TAG, "BluetoothAdapter not initialized.");
                }

                return;
            }

            mGatt.readCharacteristic(characteristic);
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while reading characteristic", e);
            }
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] dataToWrite)
    {
        try
        {
            if (mAdapter == null || mGatt == null)
            {
                if (debug)
                {
                    Log.d(TAG, "BluetoothAdapter not initialized.");
                }

                return;
            }

            characteristic.setValue(dataToWrite);
            mGatt.writeCharacteristic(characteristic);
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while writing characteristic", e);
            }
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        try
        {
            if (mAdapter == null || mGatt == null)
            {
                if (debug)
                {
                    Log.d(TAG, "BluetoothAdapter not initialized.");
                }

                return;
            }

            mGatt.setCharacteristicNotification(characteristic, enabled);

            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(Consts.UUID_DESCRIPTOR);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mGatt.writeDescriptor(descriptor);
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while setting characteristic notification", e);
            }
        }
    }

    public void getCharacteristicDescriptor(BluetoothGattDescriptor descriptor)
    {
        try
        {
            if (mAdapter == null || mGatt == null)
            {
                if (debug)
                {
                    Log.d(TAG, "BluetoothAdapter not initialized.");
                }

                return;
            }

            mGatt.readDescriptor(descriptor);
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while Getting Characteristic Descriptor", e);
            }
        }
    }

    public List<BluetoothGattService> getSupportedGattServices()
    {
        try
        {
            if (mAdapter == null || mGatt == null)
            {
                if (debug)
                {
                    Log.d(TAG, "BluetoothAdapter not initialized.");
                }

                return null;
            }

            return mGatt.getServices();
        }
        catch (Exception e)
        {
            if (debug)
            {
                Log.e(TAG, "Error while Getting SupportedGatt Services", e);
            }

            return null;
        }
    }

    /****************************************************
     * STATES
     ***************************************************/

    private void start()
    {
        if (debug)
        {
            Log.d(TAG, "start()");
        }

        // Is bluetooth supported?
        if (BleUtils.isBluetothHardwareAvailable(this))
        {
            // Is bluetooth le supported?
            if (BleUtils.isBluetothLEFeatureAvailable(this))
            {
                // Is bluetooth on or off?
                if (BleUtils.isBluetothEnabled(this))
                {
                    if (initAdapter())
                    {
                        // If previous state == SLEEP, setBleState() will reject the call,
                        // so work around it...
                        mBleState = BleState.STANDBY;
                        setBleState(BleState.SCANNING);
                    }
                    else
                    {
                        sleep();
                    }
                }
                else
                {
                    sleep();
                }
            }
            else
            {
                sleep();
            }
        }
        else
        {
            sleep();
        }
    }

    private void sleep()
    {
        if (debug)
        {
            Log.d(TAG, "sleep()");
        }

        // Release resources
        mManager = null;
        mAdapter = null;
        mGatt = null;
        mGattCallback = null;
    }

    private void manageBleState()
    {
        if (debug)
        {
            Log.d(TAG, "manageBleState()");
        }

        if (mBleState.equals(BleState.SEARCHING))
        {
            stopScanning();
            scan();
        }
        else if (mBleState.equals(BleState.SCANNING))
        {
            stopScanning();
            scan();
        }
        else if (mBleState.equals(BleState.CONNECTED))
        {
            stopScanning();

            // Set connected address
            mConnectedAddress = mGatt.getDevice().getAddress();
        }
        else if (mBleState.equals(BleState.STANDBY))
        {
            stopScanning();
        }
        else
        {
            if (debug)
            {
                Log.d(TAG, "Unexpected situtation!");
            }
        }
    }

    private void scan()
    {
        if (debug)
        {
            Log.d(TAG, "scan()");
        }

        if (mBleState.equals(BleState.SEARCHING))
        {
            mScanner = Scanner.getInstance().setScanner(mAdapter);
            mScanner.start();
        }
        else if (mBleState.equals(BleState.SCANNING))
        {
            // todo later...

            mScanner = Scanner.getInstance().setScanner(mAdapter);
            mScanner.start();
        }
        else
        {
            setBleState(BleState.STANDBY);
        }
    }

    private void stopScanning()
    {
        if (debug)
        {
            Log.d(TAG, "stopScanning()");
        }

        if (mScanner != null)
        {
            mScanner.stop();
        }
    }

    /****************************************************
     * RSSI
     ***************************************************/

    public void readRssiPeriodically()
    {
        if (debug)
        {
            Log.d(TAG, "readRssiPeriodically()");
        }

        // todo later...
    }

    public void stopReadingRssi()
    {
        if (debug)
        {
            Log.d(TAG, "stopReadingRssi()");
        }

        // todo later...
    }

    /****************************************************
     * GETTERS & SETTERS
     ***************************************************/

    public BleState getBleState()
    {
        return mBleState;
    }

    public void setBleState(BleState bleState)
    {
        if (mBleState.equals(BleState.SLEEP))
        {
            return; // Bluetooth is off, so impossible to change state!
        }

        if (mBleState.equals(bleState))
        {
            return; // We are allready in same state!
        }

        mBleState = bleState;

        manageBleState();
    }

    public String getConnectedAddress()
    {
        // todo later...

        return mConnectedAddress;
    }

    public void setConnectedAddress(String connectedAddress)
    {
        mConnectedAddress = connectedAddress;
    }

    public BluetoothGatt getGatt()
    {
        return mGatt;
    }

    public void setGatt(BluetoothGatt gatt)
    {
        mGatt = gatt;
    }

    /****************************************************
     * MANAGE SERVICE
     ***************************************************/

    public static void startService(Context context)
    {
        Intent intent = new Intent(context, BleService.class);
        context.startService(intent);
    }

    public static void destroyService(Context context)
    {
        Intent intent = new Intent(context, BleService.class);
        context.stopService(intent);
    }

    private void destroyService()
    {
        this.stopSelf();
    }

    /****************************************************
     * INNER CLASSES
     ***************************************************/

    public class LocalBinder extends Binder
    {
        /**
         * Class used for the client Binder.
         * Because we know this service always runs in the same process as its clients,
         * we don't need to deal with IPC.
         */
        BleService getService()
        {
            return BleService.this;
        }
    }

    class BluetoothStateChangesReceiver extends BroadcastReceiver
    {

        public final String TAG = BleService.class.getSimpleName() +
                "." +
                BluetoothStateChangesReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
            {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state)
                {
                    case BluetoothAdapter.STATE_OFF:
                        if (debug)
                        {
                            Log.d(TAG, "BluetoothAdapter.STATE_OFF");
                        }
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if (debug)
                        {
                            Log.d(TAG, "BluetoothAdapter.STATE_TURNING_OFF");
                        }
                        // Destroy everything
                        sleep();
                        break;

                    case BluetoothAdapter.STATE_ON:
                        if (debug)
                        {
                            Log.d(TAG, "BluetoothAdapter.STATE_ON");
                        }
                        // Start everything from beginig
                        start();
                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:
                        if (debug)
                        {
                            Log.d(TAG, "BluetoothAdapter.STATE_TURNING_ON");
                        }
                        break;
                }
            }
            else
            {
                /**
                 * We cannot catch ACTION_SCREEN_OFF and ACTION_SCREEN_ON intents through XML (?!).
                 * However, we could use a Service that registers a BroadcastReceiver member in its onStartCommand()
                 * and unregisters it in its onDestroy().
                 * This would require the service to be running in the background, constantly or as long as you need it to,
                 * so be sure to explore alternative routes.
                 *
                 * No trouble with ACTION_USER_PRESENT intent!
                 */
                if (action.equals(Intent.ACTION_SCREEN_OFF))
                {
                    if (debug)
                    {
                        Log.d(TAG, "ACTION_SCREEN_OFF");
                    }
                }
                else if (action.equals(Intent.ACTION_SCREEN_ON))
                {
                    if (debug)
                    {
                        Log.d(TAG, "ACTION_SCREEN_ON");
                    }

                    // Begin the scanning if we're not connected
                    // todo later...
                }
                else if (action.equals(Intent.ACTION_USER_PRESENT))
                {
                    if (debug)
                    {
                        Log.d(TAG, "ACTION_USER_PRESENT");
                    }
                }
            }
        }
    }

}
