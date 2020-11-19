package org.koxx.smartcntrl.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class ModeMeasurement implements Serializable {

    public Integer userID;
    public int modeValue;
    public Date timestamp;

    public ModeMeasurement(byte[] value) {
        BluetoothBytesParser parser = new BluetoothBytesParser(value);

        // Get temperature value
        modeValue = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);

    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,"%d", modeValue);
    }
}
