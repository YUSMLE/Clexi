package com.clexi.clexi.bluetoothle.queue;

import com.clexi.clexi.bluetoothle.BleManager;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by yousef on 8/10/2016.
 */

public class GattOperationQueue
{

    public static final String TAG = GattOperationQueue.class.getSimpleName();

    /**
     * The queue of pending transmissions
     */
    private Queue<GattOperation> mQueue = new LinkedList<GattOperation>();

    private boolean mIsQueueProcessing = false;

    public GattOperationQueue()
    {
        // Nothing
    }

    /**
     * Add a transaction item to transaction queue
     * @param gattOperation
     */
    public void addToQueue(GattOperation gattOperation)
    {
        mQueue.add(gattOperation);

        // If there is no other transmission processing, go do this one!
        if (!mIsQueueProcessing)
        {
            processQueue();
        }
    }

    /**
     * Call when a transaction has been completed.
     * Will process next transaction if queued.
     */
    public void processQueue()
    {
        if (mQueue.size() <= 0)
        {
            mIsQueueProcessing = false;

            return;
        }

        mIsQueueProcessing = true;
        GattOperation gattOperation = mQueue.remove();

        switch (gattOperation.type)
        {
            case ReadCharacteristic:
                BleManager.getInstance().readCharacteristic(gattOperation.characteristic);
                break;

            case WriteCharacteristic:
                BleManager.getInstance().writeCharacteristic(gattOperation.characteristic, gattOperation.dataToWrite);
                break;

            case SubscribeCharacteristicNotification:
                BleManager.getInstance().setCharacteristicNotification(gattOperation.characteristic, gattOperation.enabled);
        }
    }

    public void resetQueue()
    {
        mQueue.clear();
        mIsQueueProcessing = false;
    }
}
