package com.clexi.clexi.bluetoothle;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.clexi.clexi.app.App;
import com.clexi.clexi.bluetoothle.object.ApduCommand;
import com.clexi.clexi.bluetoothle.object.BlePacket;
import com.clexi.clexi.bluetoothle.object.InitBlePacket;
import com.clexi.clexi.bluetoothle.object.SeqBlePacket;
import com.clexi.clexi.bluetoothle.queue.GattOperation;
import com.clexi.clexi.bluetoothle.queue.GattOperationQueue;
import com.clexi.clexi.bluetoothle.queue.GattOperationType;

import java.util.ArrayList;

/**
 * Created by Yousef on 8/23/2017.
 */

public class BleManager
{

    public static final String TAG = BleManager.class.getSimpleName();

    private BleService mBleService;

    private ArrayList<BlePacket> mPackets = new ArrayList<BlePacket>();

    private GattOperationQueue mQueue;

    // Listening for bluetooth state changes
    private BroadcastReceiver mReceiver;
    private IntentFilter      mFilter;

    /****************************************************
     * Queue
     ***************************************************/

    public void readCharacteristicQueue(BluetoothGattCharacteristic characteristic)
    {
        // Add to queue because shitty Android GATT stuff is only synchronous
        GattOperation mQueueItem = new GattOperation();
        mQueueItem.type = GattOperationType.ReadCharacteristic;
        mQueueItem.characteristic = characteristic;
        mQueue.addToQueue(mQueueItem);
    }

    public void writeCharacteristicQueue(byte[] dataToWrite)
    {
        BluetoothGattCharacteristic characteristic = null;

        try
        {
            BluetoothGattService service = mBleService.getGatt().getService(Consts.UUID_SERVICE);
            characteristic = service.getCharacteristic(Consts.UUID_CHARACTERISTIC);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Gatt is null, or unnable to get service from it.", e);

            // Ignore add operation to queue
            return;
        }

        // Add to queue because shitty Android GATT stuff is only synchronous
        GattOperation mQueueItem = new GattOperation();
        mQueueItem.type = GattOperationType.WriteCharacteristic;
        mQueueItem.characteristic = characteristic;
        mQueueItem.dataToWrite = dataToWrite;
        mQueue.addToQueue(mQueueItem);
    }

    public void setCharacteristicNotificationQueue(boolean enabled)
    {
        BluetoothGattCharacteristic characteristic = null;

        try
        {
            BluetoothGattService service = mBleService.getGatt().getService(Consts.UUID_SERVICE);
            characteristic = service.getCharacteristic(Consts.UUID_NOTIFICATION);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Gatt is null, or unnable to get service from it.", e);

            // Ignore add operation to queue
            return;
        }

        // Add to queue because shitty Android GATT stuff is only synchronous
        GattOperation mQueueItem = new GattOperation();
        mQueueItem.type = GattOperationType.SubscribeCharacteristicNotification;
        mQueueItem.characteristic = characteristic;
        mQueueItem.enabled = enabled;
        mQueue.addToQueue(mQueueItem);
    }

    public void processQueue()
    {
        mQueue.processQueue();
    }

    /****************************************************
     * GATT
     ***************************************************/

    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        mBleService.readCharacteristic(characteristic);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] dataToWrite)
    {
        mBleService.writeCharacteristic(characteristic, dataToWrite);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        mBleService.setCharacteristicNotification(characteristic, enabled);
    }

    public void connect(String address)
    {
        if (mBleService.getBleState().equals(BleState.CONNECTED))
        {
            Log.w(TAG, "We are already connected!");

            return;
        }

        mBleService.connect(address);
    }

    public void connect(BluetoothDevice device)
    {
        if (mBleService.getBleState().equals(BleState.CONNECTED))
        {
            Log.w(TAG, "We are already connected!");

            return;
        }

        mBleService.connect(device);
    }

    public void disconnect()
    {
        if (!mBleService.getBleState().equals(BleState.CONNECTED))
        {
            Log.w(TAG, "We are not connected!");

            return;
        }

        mBleService.disconnect();
    }

    public void close()
    {
        mBleService.close();
    }

    public void disconnectRequest(boolean cmd)
    {
        // todo later...
    }

    public void lockRequest()
    {
        // todo later...
    }

    public void lockOnDisconnectRequest(boolean cmd, byte osType)
    {
        // todo later...
    }

    public void firmwareVersionRequest()
    {
        if (!getBleState().equals(BleState.CONNECTED))
        {
            Log.w(TAG, "We are not connected!");

            return;
        }

        ApduCommand command = new ApduCommand();
        command.setCla((byte) 0);
        command.setIns((byte) 0);
        command.setP1((byte) 0);
        command.setP2((byte) 0);
        command.setLc((byte) 0);
        command.setData(new byte[0]);

        // Send APDU Packet
        transmitApduPacket(command);
    }

    public boolean readRemoteRssi()
    {
        if (!mBleService.getBleState().equals(BleState.CONNECTED))
        {
            Log.w(TAG, "We are not connected!");

            return false;
        }

        if (mBleService.getGatt() == null)
        {
            Log.w(TAG, "BluetoothGatt is null, perhaps we are not connected!");

            return false;
        }

        return mBleService.getGatt().readRemoteRssi();
    }

    /****************************************************
     * GATT Callbacks
     ***************************************************/

    public void onConnectionStateChange(int status, int newState)
    {
        switch (newState)
        {
            case BluetoothProfile.STATE_CONNECTED:
                BleManager.getInstance().setBleState(BleState.CONNECTED);
                break;

            case BluetoothProfile.STATE_DISCONNECTED:
                // Clear connected address
                setConnectedAddress(null);
                BleManager.getInstance().setBleState(BleState.SCANNING);
                break;

            default:
                // Nothing
        }

        // todo later...
    }

    public void onServicesDiscovered()
    {
        // Reset Queue
        mQueue.resetQueue();

        // Set Characteristic Notification
        setCharacteristicNotificationQueue(true); // enable equals to true by default

        // Get Firmware Version
        firmwareVersionRequest();
    }

    public void onCharacteristicChanged(byte[] data)
    {
        // Do something with changed data...
        processReceivedData(data);
    }

    public void onCharacteristicRead(byte[] data)
    {
        // Do something with readed data...
        // Ready for next transmission
        processQueue();
    }

    public void onCharacteristicWrite()
    {
        // Ready for next transmission
        processQueue();
    }

    public void onDescriptorWrite()
    {
        // Ready for next transmission
        processQueue();
    }

    public void onReadRemoteRssi(int rssi)
    {
        // todo later...
    }

    /****************************************************
     * Transmit data
     ***************************************************/

    private void transmitApduPacket(ApduCommand packet)
    {
        transmitApduData(packet.pushTo());
    }

    private void transmitApduData(byte[] data)
    {
        // Making BLE Packets
        int                  totalPackets = BlePacket.calcTotalPacket(data.length);
        ArrayList<BlePacket> blePackets   = new ArrayList<>(totalPackets);

        // Init BLE Packet
        InitBlePacket initBlePacket = new InitBlePacket();
        initBlePacket.setCmd((byte) 0);
        initBlePacket.setHlen((byte) 0);
        initBlePacket.setLlen((byte) 0);
        initBlePacket.setData(new byte[Consts.DATA_LENGHT_PER_BLE_INIT_PACKET]);
        System.arraycopy(data, 0,
                         initBlePacket.getData(), 0,
                         data.length >= Consts.DATA_LENGHT_PER_BLE_INIT_PACKET ? Consts.DATA_LENGHT_PER_BLE_INIT_PACKET : data.length
        );

        blePackets.add(initBlePacket);

        // Other Sequence BLE Packets
        for (int i = 1; i < totalPackets; i++)
        {
            SeqBlePacket seqBlePacket = new SeqBlePacket();
            seqBlePacket.setSeq((byte) i);
            seqBlePacket.setData(new byte[Consts.DATA_LENGHT_PER_BLE_SEQ_PACKET]);

            int srcPos = i * 19 - 2;
            int length = (srcPos + 19) <= data.length ? 19 : data.length - srcPos;

            System.arraycopy(data, srcPos,
                             seqBlePacket.getData(), 0,
                             length
            );

            blePackets.add(seqBlePacket);
        }

        // Send BLE Packets
        for (BlePacket packet : blePackets)
        {
            transmitBlePacket(packet);
        }
    }

    private void transmitBlePacket(BlePacket packet)
    {
        transmitBlePacketData(packet.pushTo());
    }

    private void transmitBlePacketData(byte[] data)
    {
        writeCharacteristicQueue(data);
    }

    /****************************************************
     * Process data
     ***************************************************/

    private void processReceivedData(byte[] data)
    {
        // todo later...

        if (mPackets.isEmpty())
        {
            InitBlePacket packet = new InitBlePacket();
            packet.pullFrom(data);
            mPackets.add(packet);
        }
        else
        {
            SeqBlePacket packet = new SeqBlePacket();
            packet.pullFrom(data);
            mPackets.add(packet);
        }

        InitBlePacket initPackect  = (InitBlePacket) mPackets.get(0);
        int           totalLen     = initPackect.calcTotalLen(initPackect.getHlen(), initPackect.getLlen());
        int           totalPackets = BlePacket.calcTotalPacket(totalLen);

        if (mPackets.size() == totalPackets)
        {
            Log.d(TAG, "An APDU Packet received.");

            if (initPackect.getCmd() == Consts.BLE_PACKET_CMD_EVENT)
            {
                // This is an Event
                Log.d(TAG, "An event received.");

                byte[]      totalData = mergeData();
                ApduCommand command   = new ApduCommand();
                command.pullFrom(totalData);

                if (command.getIns() == (byte) 0x03)
                {
                    // Standard Click
                    Log.d(TAG, "Standard Click");

                    Broadcaster.broadcastKey(App.getAppContext(), Consts.KEY_A);
                }
                else if (command.getIns() == (byte) 0x04)
                {
                    // Long Click
                    Log.d(TAG, "Long Click");

                    Broadcaster.broadcastKey(App.getAppContext(), Consts.KEY_B);
                }
            }

            // Clear received BLE Packets
            mPackets.clear();
        }
    }

    private byte[] mergeData()
    {
        InitBlePacket initPackect = (InitBlePacket) mPackets.get(0);
        int           totalLen    = initPackect.calcTotalLen(initPackect.getHlen(), initPackect.getLlen());

        byte[] totalData = new byte[totalLen];

        System.arraycopy(initPackect.getData(), 0,
                         totalData, 0,
                         totalLen >= Consts.DATA_LENGHT_PER_BLE_INIT_PACKET ? Consts.DATA_LENGHT_PER_BLE_INIT_PACKET : totalLen
        );

        for (int i = 1; i < mPackets.size(); i++)
        {
            System.arraycopy(((SeqBlePacket) mPackets.get(i)).getData(), 0,
                             totalData, (i - 1) * Consts.DATA_LENGHT_PER_BLE_SEQ_PACKET,
                             Consts.DATA_LENGHT_PER_BLE_SEQ_PACKET
            );
        }

        return totalData;
    }

    /****************************************************
     * Getters & Setters
     ***************************************************/

    public BleState getBleState()
    {
        if (mBleService == null)
        {
            return BleState.SLEEP;
        }

        return mBleService.getBleState();
    }

    public void setBleState(BleState bleState)
    {
        mBleService.setBleState(bleState);
    }

    public String getConnectedAddress()
    {
        if (mBleService == null)
        {
            return null;
        }

        return mBleService.getConnectedAddress();
    }

    public void setConnectedAddress(String connectedAddress)
    {
        if (mBleService == null)
        {
            return;
        }

        mBleService.setConnectedAddress(connectedAddress);
    }

    /****************************************************
     * Lazy and Thread-safe Singleton Pattern
     ***************************************************/

    private BleManager()
    {
        mBleService = BleService.mInstance;

        mQueue = new GattOperationQueue();

        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                // Broadcasted action
                String action = intent.getAction();

                // Bonding state
                int state = intent.getIntExtra(
                        BluetoothDevice.EXTRA_BOND_STATE,
                        BluetoothDevice.ERROR
                );

                // Broadcasted bluetooth device
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                /**
                 * Check gatt actions
                 */
                if (action.equals(Consts.ACTION_GATT_CONNECTION_STATE_CHANGE))
                {
                    int status   = intent.getIntExtra(Consts.GATT_STATUS, 0);
                    int newState = intent.getIntExtra(Consts.GATT_NEW_STATE, 0);
                    onConnectionStateChange(status, newState);
                }
                else if (action.equals(Consts.ACTION_GATT_SERVICES_DISCOVERED))
                {
                    onServicesDiscovered();
                }
                else if (action.equals(Consts.ACTION_GATT_CHARACTERISTIC_CHANGED))
                {
                    byte[] data = intent.getByteArrayExtra(Consts.GATT_DATA);
                    onCharacteristicChanged(data);
                }
                else if (action.equals(Consts.ACTION_GATT_CHARACTERISTIC_READ))
                {
                    byte[] data = intent.getByteArrayExtra(Consts.GATT_DATA);
                    onCharacteristicRead(data);
                }
                else if (action.equals(Consts.ACTION_GATT_CHARACTERISTIC_WRITE))
                {
                    onCharacteristicWrite();
                }
                else if (action.equals(Consts.ACTION_GATT_DESCRIPTOR_READ))
                {
                    // Nothing
                }
                else if (action.equals(Consts.ACTION_GATT_DESCRIPTOR_WRITE))
                {
                    onDescriptorWrite();
                }

                /**
                 * Check bonding state
                 */
                else if (state == BluetoothDevice.BOND_BONDED)
                {
                    Log.d(TAG, "Device " + device + " paired.");
                }
                else if (state == BluetoothDevice.BOND_BONDING)
                {
                    Log.d(TAG, "Device " + device + " pairing is in process...");
                }
                else if (state == BluetoothDevice.BOND_NONE)
                {
                    Log.d(TAG, "Device " + device + " is unpaired.");
                }
                /*else
                {
                    Log.d(TAG, "Device " + device + " is in undefined state!");
                }*/

                /**
                 * Check connecting state
                 */
                else if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // A device is found
                    Log.d(TAG, "Device " + device + " is found.");
                }
                else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))
                {
                    // A device is connected
                    Log.d(TAG, "Device " + device + " is connected.");
                }
                else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))
                {
                    // A device is disconnected
                    Log.d(TAG, "Device " + device + " is disconnected.");
                }
            }
        };

        mFilter = new IntentFilter();

        mFilter.addAction(Consts.ACTION_GATT_CONNECTION_STATE_CHANGE);
        mFilter.addAction(Consts.ACTION_GATT_SERVICES_DISCOVERED);
        mFilter.addAction(Consts.ACTION_GATT_CHARACTERISTIC_CHANGED);
        mFilter.addAction(Consts.ACTION_GATT_CHARACTERISTIC_READ);
        mFilter.addAction(Consts.ACTION_GATT_CHARACTERISTIC_WRITE);
        mFilter.addAction(Consts.ACTION_GATT_DESCRIPTOR_READ);
        mFilter.addAction(Consts.ACTION_GATT_DESCRIPTOR_WRITE);
        mFilter.addAction(Consts.ACTION_GATT_READ_REMOTE_RSSI);

        App.getAppContext().registerReceiver(mReceiver, mFilter);
    }

    private static class SingletonHolder
    {
        private static final BleManager INSTANCE = new BleManager();
    }

    public static BleManager getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

}
