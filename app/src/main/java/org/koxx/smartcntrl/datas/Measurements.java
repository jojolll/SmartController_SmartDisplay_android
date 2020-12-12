package org.koxx.smartcntrl.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

public class Measurements implements Serializable {
    public SpeedUnit unit;
    public int speedValue;
    public float voltage;
    public float current;
    public Integer power;
    public float temperature;
    public float humidity;
    public float distanceTrip, distanceOdo;
    public Integer batteryLevel;
    public Integer batteryAutonomy;


    public Measurements(byte[] byteArray) {
        BluetoothBytesParser parser = new BluetoothBytesParser(byteArray);

        speedValue = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
        voltage = (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT16) / 10.0);
        current = (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_SINT16) / 10.0);
        power = parser.getIntValue(BluetoothBytesParser.FORMAT_SINT16);
        temperature =  (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_SINT16) / 10.0);
        humidity =  (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT16) / 10.0);
        distanceTrip =  (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT16) / 10000.0);
        distanceOdo =  (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT16));
        batteryLevel = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
        batteryAutonomy = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
    }

    @Override
    public String toString() {

        return String.format(Locale.ENGLISH,"%d km/h / %2.1f V / %2.1f A / %d W / %2.1f deg / %2.1f HR / %2.1f kms / %2.1f kms / %d batl",
                speedValue, voltage, current, power, temperature, humidity, distanceTrip, distanceOdo, batteryLevel);
    }
}
