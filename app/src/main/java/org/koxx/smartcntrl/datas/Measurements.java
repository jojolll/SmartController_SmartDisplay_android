package org.koxx.smartcntrl.datas;

import org.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.util.Locale;

import static org.welie.blessed.BluetoothBytesParser.FORMAT_SINT32;

public class Measurements implements Serializable {
    public SpeedUnit unit;
    public int speedValue;
    public float voltage;
    public float current;
    public Integer power;
    public float temperature;
    public float humidity;
    public float distance;


    public Measurements(byte[] byteArray) {
        BluetoothBytesParser parser = new BluetoothBytesParser(byteArray);

        speedValue = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
        voltage = (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT16) / 10.0);
        current = (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_SINT16) / 10.0);
        power = parser.getIntValue(BluetoothBytesParser.FORMAT_SINT16);
        temperature =  (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_SINT16) / 10.0);
        humidity =  (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT16) / 10.0);
        distance =  (float) (parser.getIntValue(BluetoothBytesParser.FORMAT_UINT16) / 10.0);
    }

    @Override
    public String toString() {

        return String.format(Locale.ENGLISH,"%d km/h / %2.1f V / %2.1f A / %d W / %2.1f deg / %2.1f HR / %2.1f kms",
                speedValue, voltage, current, power, temperature, humidity, distance);
    }
}
