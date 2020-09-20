package org.koxx.smartlcd.datas;

import androidx.annotation.NonNull;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

import static org.welie.blessed.BluetoothBytesParser.FORMAT_SINT32;

public class PowerMeasurement implements Serializable {

    public Integer power;

    public PowerMeasurement(byte[] value) {
        BluetoothBytesParser parser = new BluetoothBytesParser(value);

        // Parse the flags
        power = parser.getIntValue(FORMAT_SINT32);

    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%d", power);
    }
}
