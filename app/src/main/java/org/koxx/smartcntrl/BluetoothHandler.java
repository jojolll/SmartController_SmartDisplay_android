package org.koxx.smartcntrl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import org.koxx.smartcntrl.datas.CalibType;
import org.koxx.smartcntrl.tools.BytesTools;
import org.welie.blessed.BluetoothBytesParser;
import org.welie.blessed.BluetoothCentral;
import org.welie.blessed.BluetoothCentralCallback;
import org.welie.blessed.BluetoothPeripheral;
import org.welie.blessed.BluetoothPeripheralCallback;
import org.koxx.smartcntrl.datas.BrakeStatusMeasurement;
import org.koxx.smartcntrl.datas.BtlockMeasurement;
import org.koxx.smartcntrl.datas.ModeMeasurement;
import org.koxx.smartcntrl.datas.Measurements;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import timber.log.Timber;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_HIGH;
import static android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;
import static java.lang.Math.abs;
import static org.welie.blessed.BluetoothPeripheral.BOND_BONDED;

class BluetoothHandler {

    public static final String PREFS_NAME = "BLE_PREFS";
    public static final String PREFS_PREFERRED_BLE = "PREFERRED_BLE";

    public static final int CONNECT_STATUS_OK = 0;
    public static final int CONNECT_STATUS_FAILED = 1;
    public static final int CONNECT_STATUS_DISCONNECTED = 2;

    // Intent constants
    public static final String CONNECT_STATUS = "connect_status";
    public static final String CONNECT_STATUS_EXTRA = "connect_status.extra";
    public static final String CONNECT_STATUS_EXTRA_NAME = "connect_status.extra.name";
    public static final String MEASUREMENT_EXTRA_PERIPHERAL = "measurement.peripheral";
    public static final String MEASUREMENT_MODE = "measurement.mode";
    public static final String MEASUREMENT_MODE_EXTRA = "measurement.mode.extra";
    public static final String MEASUREMENT_SPEED = "measurement.speed";
    public static final String MEASUREMENT_SPEED_EXTRA = "measurement.speed.extra";
    public static final String MEASUREMENT_BRAKE_STATUS = "measurement.brake_status";
    public static final String MEASUREMENT_BRAKE_STATUS_EXTRA = "measurement.brake_status.extra";
    public static final String MEASUREMENT_BTLOCK = "measurement.btlock";
    public static final String MEASUREMENT_BTLOCK_EXTRA = "measurement.btlock.extra";
    public static final String MEASUREMENT_SPEED_LIMITER = "measurement.speed_limiter";
    public static final String MEASUREMENT_SPEED_LIMITER_EXTRA = "measurement.speed_limiter.extra";
    public static final String MEASUREMENT_ECO = "measurement.eco";
    public static final String MEASUREMENT_ECO_EXTRA = "measurement.eco.extra";
    public static final String MEASUREMENT_ACCEL = "measurement.accel";
    public static final String MEASUREMENT_ACCEL_EXTRA = "measurement.accel.extra";
    public static final String MEASUREMENT_CURRENT_CALIB = "measurement.current_calib";
    public static final String MEASUREMENT_CURRENT_CALIB_EXTRA = "measurement.current_calib.extra";
    public static final String MEASUREMENT_AUX = "measurement.aux";
    public static final String MEASUREMENT_AUX_EXTRA = "measurement.aux.extra";


    // UUIDs all datas
    private static final UUID SMARTCNTRL_MAIN_SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    private static final UUID MEASUREMENTS_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a0");
    private static final UUID MODE_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a1");
    private static final UUID BRAKE_STATUS_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a2");
//    private static final UUID xxxxxxxxx = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a3");
//    private static final UUID AMPERE_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a4");
//    private static final UUID POWER_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a5");
    private static final UUID BTLOCK_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a6");
    private static final UUID SETTINGS4_WIFI_SSID_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a7");
    private static final UUID SETTINGS5_WIFI_PWD_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");
    private static final UUID SETTINGS1_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a9");
    private static final UUID SPEED_LIMITER_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26aa");
    private static final UUID ECO_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26ab");
    private static final UUID ACCEL_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26ac");
    private static final UUID CALIB_ORDER_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26ad");
    private static final UUID SWITCH_TO_OTA_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26ae");
    private static final UUID LOGS_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26af");
    private static final UUID FAST_UPDATE_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b0");
    private static final UUID SETTINGS2_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b1");
    private static final UUID SETTINGS3_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b2");
    private static final UUID AUX_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b3");
    private static final UUID SPEED_PID_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b4");
    private static final UUID DISTANCE_RST_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b5");


    // Local variables
    public BluetoothCentral central;
    private static BluetoothHandler instance = null;
    private Context context;
    private Handler handler = new Handler();

    private LogActivity logsView;
    private String logText = "";

    ArrayList<String> ignoreList = new ArrayList<>();

    private GraphActivity graphView = null;

    // Callback for peripherals
    private final BluetoothPeripheralCallback peripheralCallback = new BluetoothPeripheralCallback() {
        @Override
        public void onServicesDiscovered(@NotNull BluetoothPeripheral peripheral) {
            Timber.i("discovered services");

            // Request a higher MTU, iOS always asks for 185
            peripheral.requestMtu(128);

            // Request a new connection priority
            peripheral.requestConnectionPriority(CONNECTION_PRIORITY_HIGH);

            if (peripheral.getService(SMARTCNTRL_MAIN_SERVICE_UUID) != null) {
                BluetoothGattCharacteristic speedCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, MEASUREMENTS_CHARACTERISTIC_UUID);
                if (speedCharacteristic != null) {
                    peripheral.setNotify(speedCharacteristic, true);
                }
                BluetoothGattCharacteristic modeCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, MODE_CHARACTERISTIC_UUID);
                if (modeCharacteristic != null) {
                    peripheral.setNotify(modeCharacteristic, true);
                    peripheral.readCharacteristic(modeCharacteristic);
                }
                BluetoothGattCharacteristic brakeStatusCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, BRAKE_STATUS_CHARACTERISTIC_UUID);
                if (brakeStatusCharacteristic != null) {
                    peripheral.setNotify(brakeStatusCharacteristic, true);
                    peripheral.readCharacteristic(brakeStatusCharacteristic);
                }
                BluetoothGattCharacteristic btlockCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, BTLOCK_CHARACTERISTIC_UUID);
                if (btlockCharacteristic != null) {
                    peripheral.setNotify(btlockCharacteristic, true);
                    peripheral.readCharacteristic(btlockCharacteristic);
                }
                BluetoothGattCharacteristic speedLimiterCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SPEED_LIMITER_CHARACTERISTIC_UUID);
                if (speedLimiterCharacteristic != null) {
                    peripheral.setNotify(speedLimiterCharacteristic, true);
                    peripheral.readCharacteristic(speedLimiterCharacteristic);
                }
                BluetoothGattCharacteristic ecoCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, ECO_CHARACTERISTIC_UUID);
                if (ecoCharacteristic != null) {
                    peripheral.setNotify(ecoCharacteristic, true);
                    peripheral.readCharacteristic(ecoCharacteristic);
                }
                BluetoothGattCharacteristic accelCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, ACCEL_CHARACTERISTIC_UUID);
                if (accelCharacteristic != null) {
                    peripheral.setNotify(accelCharacteristic, true);
                    peripheral.readCharacteristic(accelCharacteristic);
                }
                BluetoothGattCharacteristic logCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, LOGS_CHARACTERISTIC_UUID);
                if (logCharacteristic != null) {
                    peripheral.setNotify(logCharacteristic, true);
                }
                BluetoothGattCharacteristic auxCharacteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, AUX_CHARACTERISTIC_UUID);
                if (auxCharacteristic != null) {
                    peripheral.setNotify(auxCharacteristic, true);
                    peripheral.readCharacteristic(auxCharacteristic);
                }

                sendSettings();
            }

        }

        @Override
        public void onNotificationStateUpdate(@NotNull BluetoothPeripheral peripheral, @NotNull BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothPeripheral.GATT_SUCCESS) {
                if (peripheral.isNotifying(characteristic)) {
                    Timber.i("SUCCESS: Notify set to 'on' for %s", characteristic.getUuid());
                } else {
                    Timber.i("SUCCESS: Notify set to 'off' for %s", characteristic.getUuid());
                }
            } else {
                Timber.e("ERROR: Changing notification state failed for %s", characteristic.getUuid());
            }
        }

        @Override
        public void onCharacteristicWrite(@NotNull BluetoothPeripheral peripheral, @NotNull byte[] value, @NotNull BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothPeripheral.GATT_SUCCESS) {
                Timber.i("SUCCESS: Writing <%s> to <%s>", BluetoothBytesParser.bytes2String(value), characteristic.getUuid().toString());
            } else {
                Timber.i("ERROR: Failed writing <%s> to <%s>", BluetoothBytesParser.bytes2String(value), characteristic.getUuid().toString());
            }
        }

        @Override
        public void onCharacteristicUpdate(@NotNull BluetoothPeripheral peripheral, @NotNull byte[] value, @NotNull BluetoothGattCharacteristic characteristic, int status) {
            if (status != BluetoothPeripheral.GATT_SUCCESS) return;
            UUID characteristicUUID = characteristic.getUuid();
            BluetoothBytesParser parser = new BluetoothBytesParser(value);
            Timber.i("update <%s>", characteristicUUID);

            if (characteristicUUID.equals(MODE_CHARACTERISTIC_UUID)) {
                ModeMeasurement measurement = new ModeMeasurement(value);
                Intent intent = new Intent(MEASUREMENT_MODE);
                intent.putExtra(MEASUREMENT_MODE_EXTRA, measurement);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);
                //Timber.d("mode : %s", measurement);
            } else if (characteristicUUID.equals(MEASUREMENTS_CHARACTERISTIC_UUID)) {
                Measurements measurement = new Measurements(value);
                Intent intent = new Intent(MEASUREMENT_SPEED);
                intent.putExtra(MEASUREMENT_SPEED_EXTRA, measurement);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);

                if (graphView != null) {
                    graphView.addSpeedData((float) (measurement.speedValue));
                    graphView.addCurrentData((float) (measurement.current));
                }

                Timber.d("measurement : %s", measurement.toString());

            } else if (characteristicUUID.equals(BRAKE_STATUS_CHARACTERISTIC_UUID)) {
                BrakeStatusMeasurement measurement = new BrakeStatusMeasurement(value);
                Intent intent = new Intent(MEASUREMENT_BRAKE_STATUS);
                intent.putExtra(MEASUREMENT_BRAKE_STATUS_EXTRA, measurement);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);
                //Timber.d("brake : %s", measurement);

            } else if (characteristicUUID.equals(BTLOCK_CHARACTERISTIC_UUID)) {
                Timber.i("received BTLOCK_CHARACTERISTIC_UUID");
                BtlockMeasurement measurement = new BtlockMeasurement(value);
                Intent intent = new Intent(MEASUREMENT_BTLOCK);
                intent.putExtra(MEASUREMENT_BTLOCK_EXTRA, measurement);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);
            } else if (characteristicUUID.equals(SPEED_LIMITER_CHARACTERISTIC_UUID)) {
                Integer speedLimiter = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
                Intent intent = new Intent(MEASUREMENT_SPEED_LIMITER);
                intent.putExtra(MEASUREMENT_SPEED_LIMITER_EXTRA, speedLimiter);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);
            } else if (characteristicUUID.equals(ECO_CHARACTERISTIC_UUID)) {
                Integer eco = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
                Intent intent = new Intent(MEASUREMENT_ECO);
                intent.putExtra(MEASUREMENT_ECO_EXTRA, eco);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);
            } else if (characteristicUUID.equals(ACCEL_CHARACTERISTIC_UUID)) {
                Integer accel = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
                Intent intent = new Intent(MEASUREMENT_ACCEL);
                intent.putExtra(MEASUREMENT_ACCEL_EXTRA, accel);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);
            } else if (characteristicUUID.equals(AUX_CHARACTERISTIC_UUID)) {
                Integer aux = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
                Intent intent = new Intent(MEASUREMENT_AUX);
                intent.putExtra(MEASUREMENT_AUX_EXTRA, aux);
                intent.putExtra(MEASUREMENT_EXTRA_PERIPHERAL, peripheral.getAddress());
                context.sendBroadcast(intent);
            } else if (characteristicUUID.equals(LOGS_CHARACTERISTIC_UUID)) {
                String log = parser.getStringValue();
                Timber.i("ESP : %s", log);

                addLog(log);

            }
        }

        @Override
        public void onMtuChanged(@NotNull BluetoothPeripheral peripheral, int mtu, int status) {
            Timber.i("new MTU set: %d / status: %d", mtu, status);
        }
    };


    // Callback for central
    private final BluetoothCentralCallback bluetoothCentralCallback = new BluetoothCentralCallback() {

        @Override
        public void onConnectedPeripheral(@NotNull BluetoothPeripheral peripheral) {
            Timber.i("connected to '%s'", peripheral.getName());

            addLog(">>> connected");

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREFS_PREFERRED_BLE, peripheral.getAddress());
            editor.commit();

            Intent intent = new Intent(CONNECT_STATUS);
            intent.putExtra(CONNECT_STATUS_EXTRA, CONNECT_STATUS_OK);
            intent.putExtra(CONNECT_STATUS_EXTRA_NAME, peripheral.getName());
            context.sendBroadcast(intent);
        }

        @Override
        public void onConnectionFailed(@NotNull final BluetoothPeripheral peripheral, final int status) {
            Timber.e("connection '%s' failed with status %d", peripheral.getName(), status);

            addLog(">>> connection failed");

            Intent intent = new Intent(CONNECT_STATUS);
            intent.putExtra(CONNECT_STATUS_EXTRA, CONNECT_STATUS_FAILED);
            context.sendBroadcast(intent);

            // Reconnect to this device when it becomes available again
            if ((peripheral.getBondState() == BOND_BONDED) && peripheral.getName() != null && peripheral.getName().startsWith("SmartCntrl")) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        central.autoConnectPeripheral(peripheral, peripheralCallback);
                    }
                }, 5000);
            } else {
                ignoreList.add(peripheral.getAddress());
                central.scanForPeripherals();
            }
        }

        @Override
        public void onDisconnectedPeripheral(@NotNull final BluetoothPeripheral peripheral, final int status) {
            Timber.i("disconnected '%s' with status %d", peripheral.getName(), status);

            addLog(">>> disconnected");

            Intent intent = new Intent(CONNECT_STATUS);
            intent.putExtra(CONNECT_STATUS_EXTRA, CONNECT_STATUS_DISCONNECTED);
            context.sendBroadcast(intent);

            // Reconnect to this device when it becomes available again
            if ((peripheral.getBondState() == BOND_BONDED) && peripheral.getName() != null && peripheral.getName().startsWith("SmartCntrl")) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        central.autoConnectPeripheral(peripheral, peripheralCallback);
                    }
                }, 5000);
            } else {
                ignoreList.add(peripheral.getAddress());
                central.scanForPeripherals();
            }
        }

        @Override
        public void onDiscoveredPeripheral(@NotNull BluetoothPeripheral peripheral, @NotNull ScanResult scanResult) {
            Timber.i("Found peripheral '%s' / %s", peripheral.getName(), peripheral.getAddress());
            Timber.i("=> Peripheral bond state = %d", peripheral.getBondState());

            if (peripheral.getBondState() == BOND_BONDED) {
                if ((peripheral.getBondState() == BOND_BONDED) && peripheral.getName() != null && peripheral.getName().startsWith("SmartCntrl")) {
                    Timber.i(" ==> connect to '%s' / %s", peripheral.getName(), peripheral.getAddress());
                    central.stopScan();
                    central.connectPeripheral(peripheral, peripheralCallback);
                }
            } else {

                Timber.i("  ==> found a peripheral ... but not bonded");

                if (getPreferedPeripheral() == null || getPreferedPeripheral().equals("")) {
                    if (!ignoreList.contains(peripheral.getAddress())) {
                        if (peripheral.getName() != null && peripheral.getName().startsWith("SmartCntrl")) {
                            Timber.i(" ==> connect to '%s'", peripheral.getAddress());
                            central.stopScan();
                            central.connectPeripheral(peripheral, peripheralCallback);
                        } else {
                            Timber.i(" ==> not prefered peripheral / name not matching");
                        }
                    }
                }
            }
        }

        @Override
        public void onBluetoothAdapterStateChanged(int state) {
            Timber.i("bluetooth adapter changed state to %d", state);
            if (state == BluetoothAdapter.STATE_ON) {
                // Bluetooth is on now, start scanning again
                // Scan for peripherals with a certain service UUIDs
                central.startPairingPopupHack();

                // central.scanForPeripheralsWithServices(new UUID[]{BLP_SERVICE_UUID});
                //central.scanForPeripheralsWithNames(new String[]{blePrefAddress});
                central.scanForPeripherals();
                //central.autoConnectPeripheral(blePrefAddress);
                // central.scanForPeripherals();
            }
        }
    };

    public String getPreferedPeripheral() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        String blePrefAddress = sharedPreferences.getString(PREFS_PREFERRED_BLE, "");
        return blePrefAddress;
    }

    public static synchronized BluetoothHandler getInstance(Context context) {
        if (instance == null) {
            instance = new BluetoothHandler(context.getApplicationContext());
        }
        return instance;
    }

    private BluetoothHandler(Context context) {
        this.context = context;

        // Plant a tree
        //Timber.plant(new Timber.DebugTree());

        // Create BluetoothCentral
        central = new BluetoothCentral(context, bluetoothCentralCallback, new Handler());

        // Scan for peripherals with a certain service UUIDs
        central.startPairingPopupHack();
//        central.scanForPeripheralsWithServices(new UUID[]{BLP_SERVICE_UUID, HTS_SERVICE_UUID, HRS_SERVICE_UUID});

        /*
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d("paired devices:"," BLE Name:"+device.getName());
                try {
                    if(device.getName().contains("koko")){
                        Method m = device.getClass()
                                .getMethod("removeBond", (Class[]) null);
                        m.invoke(device, (Object[]) null);
                    }
                } catch (Exception e) {
                    Log.e("fail", e.getMessage());
                }
            }
        }
        */

        String blePrefAddress = getPreferedPeripheral();

        Timber.i(" ==> scan for %s", blePrefAddress);

        // central.scanForPeripheralsWithServices(new UUID[]{BLP_SERVICE_UUID});
        /*
        if (blePrefAddress.contains(":"))
            central.scanForPeripheralsWithAddresses(new String[]{blePrefAddress});
        else
            central.scanForPeripheralsWithNames(new String[]{blePrefAddress});

         */
        central.scanForPeripherals();
    }


    public void sendSettings() {
        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;

        byte[] dataSettings;
        BluetoothGattCharacteristic characteristic;

        try {
            characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SETTINGS1_CHARACTERISTIC_UUID);
            dataSettings = Settings.settings1ToByteArray(context);
            peripheral.writeCharacteristic(characteristic, dataSettings, WRITE_TYPE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SETTINGS2_CHARACTERISTIC_UUID);
            dataSettings = Settings.settings2ToByteArray(context);
            peripheral.writeCharacteristic(characteristic, dataSettings, WRITE_TYPE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SETTINGS3_CHARACTERISTIC_UUID);
            dataSettings = Settings.settings3ToByteArray(context);
            peripheral.writeCharacteristic(characteristic, dataSettings, WRITE_TYPE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SETTINGS4_WIFI_SSID_CHARACTERISTIC_UUID);
            dataSettings = Settings.settings4ToByteArray(context);
            peripheral.writeCharacteristic(characteristic, dataSettings, WRITE_TYPE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SETTINGS5_WIFI_PWD_CHARACTERISTIC_UUID);
            dataSettings = Settings.settings5ToByteArray(context);
            peripheral.writeCharacteristic(characteristic, dataSettings, WRITE_TYPE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendModeValue(byte value) {
        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, MODE_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{value}, WRITE_TYPE_DEFAULT);
    }

    public void sendBrakeManualValue(byte value) {
        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, BRAKE_STATUS_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{value}, WRITE_TYPE_DEFAULT);
    }

    public void sendSpeedLimiterValue(byte value) {
        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SPEED_LIMITER_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{value}, WRITE_TYPE_DEFAULT);
    }

    public void readSpeedLimiterValue(byte value) {
        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SPEED_LIMITER_CHARACTERISTIC_UUID);
        peripheral.readCharacteristic(characteristic);
    }

    public void sendEcoValue(byte value) {
        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, ECO_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{value}, WRITE_TYPE_DEFAULT);
    }

    public void sendAccelValue(byte value) {
        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, ACCEL_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{value}, WRITE_TYPE_DEFAULT);
    }

    public void sendBleLockForceValue(byte value) {
        Timber.i("sendBleLockForceValue");

        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, BTLOCK_CHARACTERISTIC_UUID);
        byte[] values = new byte[4];
        values[3] = value;
        peripheral.writeCharacteristic(characteristic, values, WRITE_TYPE_DEFAULT);
    }

    public void sendSwitchOtaValue() {
        Timber.i("sendSwitchOtaValue");

        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SWITCH_TO_OTA_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{0x01}, WRITE_TYPE_DEFAULT);
    }

    public void sendAuxValue(byte value) {
        Timber.i("sendAuxValue");

        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, AUX_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{value}, WRITE_TYPE_DEFAULT);
    }

    public void sendDstReset() {
        Timber.i("sendDstReset");

        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, DISTANCE_RST_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{0}, WRITE_TYPE_DEFAULT);
    }

    public void sendFastUpdateValue(byte value) {
        Timber.i("sendFastUpdateValue");

        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, FAST_UPDATE_CHARACTERISTIC_UUID);
        peripheral.writeCharacteristic(characteristic, new byte[]{value}, WRITE_TYPE_DEFAULT);
    }

    public void sendCalibOrder(CalibType order, int value) {
        Timber.i("sendCalibOrder");

        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, CALIB_ORDER_CHARACTERISTIC_UUID);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {

            dos.writeByte((byte) order.getNumVal());
            dos.write(BytesTools.intToByteArrayInverted(value));

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        peripheral.writeCharacteristic(characteristic, bos.toByteArray(), WRITE_TYPE_DEFAULT);
    }

    public void sendSpeedPidValue(Integer kp, Integer ki, Integer kd) {
        Timber.i("sendSpeedPidValue");

        BluetoothPeripheral peripheral = getConnectedPeripheral();
        if (peripheral == null) return;
        BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(SMARTCNTRL_MAIN_SERVICE_UUID, SPEED_PID_CHARACTERISTIC_UUID);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {

            dos.writeByte((byte) ((kp >> 0) & 0xff));
            dos.writeByte((byte) ((kp >> 8) & 0xff));
            dos.writeByte((byte) ((kp >> 16) & 0xff));
            dos.writeByte((byte) ((kp >> 24) & 0xff));

            dos.writeByte((byte) ((ki >> 0) & 0xff));
            dos.writeByte((byte) ((ki >> 8) & 0xff));
            dos.writeByte((byte) ((ki >> 16) & 0xff));
            dos.writeByte((byte) ((ki >> 24) & 0xff));

            dos.writeByte((byte) ((kd >> 0) & 0xff));
            dos.writeByte((byte) ((kd >> 8) & 0xff));
            dos.writeByte((byte) ((kd >> 16) & 0xff));
            dos.writeByte((byte) ((kd >> 24) & 0xff));

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        peripheral.writeCharacteristic(characteristic, bos.toByteArray(), WRITE_TYPE_DEFAULT);
    }

    public BluetoothPeripheral getConnectedPeripheral() {
        if (central.getConnectedPeripherals().size() > 0)
            return central.getConnectedPeripherals().get(0);
        else
            return null;
    }

    public void setLogActivity(LogActivity view) {
        logsView = view;
    }

    public void clearLogs() {
        if (logsView != null) {
            logsView.getOnscreenLog().clearLog();
        }
    }

    private void addLog(String log) {
        if (logsView != null) {
            String date = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
            logText = date + " : " + log;
            logsView.getOnscreenLog().log(logText);
        }
    }

    public void setGraph(GraphActivity graphActivity) {
        graphView = graphActivity;
    }

    public void resetBlePreferredPeripheral() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_PREFERRED_BLE, "");
        editor.commit();
    }

}
