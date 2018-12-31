package com.clexi.clexi.test;

import com.clexi.clexi.bluetoothle.object.ApduCommand;

/**
 * Created by Yousef on 8/5/2018.
 */

public class TestCommands
{

    public static final String TAG = TestCommands.class.getSimpleName();

    public static ApduCommand requestBatteryLevel()
    {
        ApduCommand command = new ApduCommand();

        command.setCla((byte) 0x00);
        command.setIns((byte) 0x20);
        command.setP1((byte) 0x00);
        command.setP2((byte) 0x00);
        command.setLc1((byte) 0x00);
        command.setLc2((byte) 0x00);
        command.setLc3((byte) 0x00);
        command.setData(new byte[0]);

        return command;
    }
}
