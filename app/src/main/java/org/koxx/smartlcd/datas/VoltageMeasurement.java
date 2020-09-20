package org.koxx.smartlcd.datas;

import androidx.annotation.NonNull;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

public class VoltageMeasurement implements Serializable {

    public double voltage;

    public VoltageMeasurement(byte[] value) {
        BluetoothBytesParser parser = new BluetoothBytesParser(value);

        // Parse the flags
        voltage = (parser.getIntValue(BluetoothBytesParser.FORMAT_SINT32) / 1000.0);

    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%2.2f", voltage);
    }
}
