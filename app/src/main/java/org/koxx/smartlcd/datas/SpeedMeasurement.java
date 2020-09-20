package org.koxx.smartlcd.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

public class SpeedMeasurement implements Serializable {
    public SpeedUnit unit;
    public int speedValue;

    public SpeedMeasurement(byte[] byteArray) {
        BluetoothBytesParser parser = new BluetoothBytesParser(byteArray);

        speedValue = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);

    }

    @Override
    public String toString() {

        return String.format(Locale.ENGLISH,"%d %s", speedValue, "km/h");
    }
}
