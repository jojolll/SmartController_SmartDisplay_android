package org.koxx.smartlcd.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

public class BrakeStatusMeasurement implements Serializable {

    public int brakeValue;

    public BrakeStatusMeasurement(byte[] byteArray) {
        BluetoothBytesParser parser = new BluetoothBytesParser(byteArray);

        brakeValue = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);

    }

    @Override
    public String toString() {

        return String.format(Locale.ENGLISH,"%d", brakeValue);
    }
}
