package org.koxx.smartlcd.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

public class BrakeStatusMeasurement implements Serializable {

    public int brakeValue;
    public boolean brakePressed;

    public BrakeStatusMeasurement(byte[] byteArray) {
        BluetoothBytesParser parser = new BluetoothBytesParser(byteArray);

        brakeValue = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
        brakePressed = (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8) == 1);

    }

    @Override
    public String toString() {

        return String.format(Locale.ENGLISH,"%d / %d", brakeValue, brakePressed);
    }
}
