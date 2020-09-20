package org.koxx.smartlcd.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

public class BtlockMeasurement implements Serializable {

    public int btLockValue;

    public BtlockMeasurement(byte[] value) {
        BluetoothBytesParser parser = new BluetoothBytesParser(value);

        // Get temperature value
        btLockValue = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8) + 1;

    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,"%d", btLockValue);
    }
}
