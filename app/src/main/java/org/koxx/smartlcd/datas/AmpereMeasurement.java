package org.koxx.smartlcd.datas;

import androidx.annotation.NonNull;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

import static org.welie.blessed.BluetoothBytesParser.FORMAT_SINT32;

public class AmpereMeasurement implements Serializable {

    public float current;

    public AmpereMeasurement(byte[] value) {
        BluetoothBytesParser parser = new BluetoothBytesParser(value);

        // Parse the flags
        current = (float) (parser.getIntValue(FORMAT_SINT32) / 1000.0);

    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%2.2f", current);
    }
}
