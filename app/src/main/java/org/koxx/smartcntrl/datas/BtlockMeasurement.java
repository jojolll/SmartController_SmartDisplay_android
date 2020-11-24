package org.koxx.smartcntrl.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

public class BtlockMeasurement implements Serializable {

    public int btLockBeaconRssiValue;
    public int btLockBeaconVisibleValue;
    public int bleLockStatus;
    public int bleLockForcedValue;

    public BtlockMeasurement(byte[] value) {
        BluetoothBytesParser parser = new BluetoothBytesParser(value);

        // Get values
        byte[] values = parser.getValue();
        bleLockStatus = values[0];
        btLockBeaconVisibleValue = values[1];
        btLockBeaconRssiValue = values[2];
        bleLockForcedValue = values[3];

    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "bleLockStatus %d / btLockBeaconVisibleValue %d / btLockBeaconRssiValue %d / bleLockForcedValue %d",
                bleLockStatus, btLockBeaconVisibleValue, btLockBeaconRssiValue, bleLockForcedValue);
    }
}
