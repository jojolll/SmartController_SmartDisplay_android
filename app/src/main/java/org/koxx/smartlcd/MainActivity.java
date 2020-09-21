package org.koxx.smartlcd;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject;
import com.welie.blessedexample.R;

import org.koxx.smartlcd.datas.BrakeStatusMeasurement;
import org.koxx.smartlcd.datas.BtlockMeasurement;
import org.koxx.smartlcd.datas.ModeMeasurement;
import org.welie.blessed.BluetoothCentral;
import org.welie.blessed.BluetoothPeripheral;

import org.koxx.smartlcd.chrono.ChronometerTimeOn;
import org.koxx.smartlcd.chrono.ChronometerTimeRun;
import org.koxx.smartlcd.datas.AmpereMeasurement;
import org.koxx.smartlcd.datas.PowerMeasurement;
import org.koxx.smartlcd.datas.SpeedMeasurement;
import org.koxx.smartlcd.datas.VoltageMeasurement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import timber.log.Timber;

// TODO : GPS speed
// TODO : missing settings
// TODO : screen fraction
// TODO : brake (to fix)
// TODO : add beacon visibility
// TODO : change icons

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int ACCESS_LOCATION_REQUEST = 2;
    private static final int REQUEST_CODE_ACTIVITY_SETTINGS = 1001;

    public static final String EXTRA_SETTINGS_LIST = "EXTRA_SETTINGS_LIST";
    public static final String SETTINGS_KEY_RINGTONE = "SETTINGS_KEY_RINGTONE";

    private TextView tvSpeed, tvVoltage, tvCurrent, tvSpeedMax, tvCurrentMax, tvPower, tvPowerMax, tvTemperature, tvTemperatureMax, tvBtLock, tvHumidity, tvSpeedLimiter, tvEco, tvAccel;
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

    // SmartLcd values
    int mLcdMode = 0;
    boolean mBatteryOverLoad = false;
    int mBrakeStatus = 0;
    float mLastSpeed = 0;
    float mMaxSpeed = 0;
    float mLastPower = 0;
    float mLastCurrent = 0;
    float mLastTemp = 0;
    int mSpeedLimiter = 0;
    int mEco = 0;
    int mAccel = 0;

    int mBleLockForce = 0;

    int mLastBtStatus = BluetoothHandler.CONNECT_STATUS_DISCONNECTED;

    // chronometer
    Thread threadTimeOn, threadTimeRun;
    TextView tvTimeOn, tvTimeRun;
    LinearLayout viewTime;
    ChronometerTimeOn chronoTimeOn;
    ChronometerTimeRun chronoTimeRun;


    ArrayList<SettingsObject> mySettingsList;

    private MenuItem item;
    private Menu menu;
    private AnimationSet animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvSpeed = (TextView) findViewById(R.id.SpeedValue);
        tvVoltage = (TextView) findViewById(R.id.VoltageValue);
        tvCurrent = (TextView) findViewById(R.id.AmpereValue);
        tvPower = (TextView) findViewById(R.id.PowerValue);
        tvTemperature = (TextView) findViewById(R.id.TemperatureValue);
        tvSpeedMax = (TextView) findViewById(R.id.SpeedMaxValue);
        tvCurrentMax = (TextView) findViewById(R.id.AmpereMaxValue);
        tvPowerMax = (TextView) findViewById(R.id.PowerMaxValue);
        tvTemperatureMax = (TextView) findViewById(R.id.TemperatureMaxValue);
        tvBtLock = (TextView) findViewById(R.id.BtValue);
        tvHumidity = (TextView) findViewById(R.id.HumidityValue);
        tvSpeedLimiter = (TextView) findViewById(R.id.SpdLimitValue);
        tvEco = (TextView) findViewById(R.id.EcoValue);
        tvAccel = (TextView) findViewById(R.id.AccelValue);


        registerReceiver(connectStatusReceiver, new IntentFilter(BluetoothHandler.CONNECT_STATUS));

        registerReceiver(locationServiceStateReceiver, new IntentFilter(LocationManager.MODE_CHANGED_ACTION));

        registerReceiver(modeDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_MODE));
        registerReceiver(speedDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_SPEED));
        registerReceiver(brakeStatusDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_BRAKE_STATUS));
        registerReceiver(voltageDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_VOLTAGE));
        registerReceiver(ampereDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_AMPERE));
        registerReceiver(powerDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_POWER));
        registerReceiver(btLockDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_BTLOCK));
        registerReceiver(temperatureDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_TEMPERATURE));
        registerReceiver(humidityDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_HUMIDITY));
        registerReceiver(speedLimiterDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_SPEED_LIMITER));
        registerReceiver(ecoDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_ECO));
        registerReceiver(accelDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_ACCEL));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //todo note that there might be some more methods available for the below builders.
        //todo please check docs/original code for all available options
        mySettingsList = Settings.initialize();

        EasySettings.initializeSettings(this, mySettingsList);

        // set indicators
        setModeIndicator(1);
        setBatteryIndicator(40, 60, 60);
        setBrakeIndicator(EasySettings.retrieveSettingsSharedPrefs(this).getInt(Settings.Electric_brake_min_value, 0),
                -1,
                EasySettings.retrieveSettingsSharedPrefs(this).getInt(Settings.Electric_brake_max_value, 0),
                mBatteryOverLoad);

        // chronometer
        if (ChronometerTimeOn.chrono == null) {
            ChronometerTimeOn.setChrono(new ChronometerTimeOn());
            chronoTimeOn = ChronometerTimeOn.getChrono();
        } else {
            chronoTimeOn = ChronometerTimeOn.getChrono();
        }
        if (ChronometerTimeRun.chrono == null) {
            ChronometerTimeRun.setChrono(new ChronometerTimeRun());
            chronoTimeRun = ChronometerTimeRun.getChrono();
        } else {
            chronoTimeRun = ChronometerTimeRun.getChrono();
        }
        tvTimeOn = (TextView) findViewById(R.id.TimeOnValue);
        tvTimeOn.setText(ChronometerTimeOn.formatToSeconds(chronoTimeOn.global));
        tvTimeRun = (TextView) findViewById(R.id.TimeRunValue);
        tvTimeRun.setText(ChronometerTimeOn.formatToSeconds(chronoTimeRun.global));
        viewTime = (LinearLayout) findViewById(R.id.Time);
        chronoTimeOn.Start();
        viewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoTimeOn.Reset();
                chronoTimeOn.Start();
                chronoTimeRun.Reset();
                tvTimeOn.setText(ChronometerTimeOn.formatToSeconds(chronoTimeOn.global));
                tvTimeRun.setText(ChronometerTimeOn.formatToSeconds(chronoTimeRun.global));
            }
        });

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.);
//        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    protected void onStart() {
        super.onStart();
        tvTimeOn.setText(ChronometerTimeOn.formatToSeconds(chronoTimeOn.global));
        threadTimeOn = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(10);  //1000ms = 1 se
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                chronoTimeOn.CountUp();
                                tvTimeOn.setText(ChronometerTimeOn.formatToSeconds(chronoTimeOn.global));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        threadTimeOn.start();


        tvTimeRun.setText(ChronometerTimeOn.formatToSeconds(chronoTimeRun.global));
        threadTimeRun = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(10);  //1000ms = 1 se
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                chronoTimeRun.CountUp();
                                tvTimeRun.setText(ChronometerTimeOn.formatToSeconds(chronoTimeRun.global));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        threadTimeRun.start();
    }


    public void setBrakeIndicator(int min, int value, int max, boolean isBatteryTooLoaded) {
        for (int i = 0; i <= 5; i++) {
            String viewId = "BrakeValue" + i;
            int resID = getResources().getIdentifier(viewId, "id", getPackageName());

            if (!isBatteryTooLoaded) {
                if (i == value) {
                    ((TextView) findViewById(resID)).setTextColor(getResources().getColor(R.color.colorText));
                    ((TextView) findViewById(resID)).setVisibility(View.VISIBLE);
                } else if (i < min) {
                    ((TextView) findViewById(resID)).setVisibility(View.GONE);
                } else if (i > max) {
                    ((TextView) findViewById(resID)).setVisibility(View.GONE);
                } else {
                    ((TextView) findViewById(resID)).setTextColor(getResources().getColor(R.color.colorTextDisabled));
                    ((TextView) findViewById(resID)).setVisibility(View.VISIBLE);
                }
            } else {
                if (i == 0) {
                    ((TextView) findViewById(resID)).setTextColor(getResources().getColor(R.color.colorText));
                    ((TextView) findViewById(resID)).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) findViewById(resID)).setVisibility(View.GONE);
                }
            }
        }
    }


    public void setBatteryIndicator(float min, float value, float max) {

        int bat = (int) (1 / ((max - min) / (value - min)) * 100);

        for (int i = 0; i < 10; i++) {
            String viewId = "VoltageValue" + (i + 1);
            int resID = getResources().getIdentifier(viewId, "id", getPackageName());
            if (bat / 10 > i)
                ((TextView) findViewById(resID)).setBackgroundColor(getResources().getColor(R.color.colorText));
            else
                ((TextView) findViewById(resID)).setBackgroundColor(getResources().getColor(R.color.colorTextDisabled));
        }
        if (bat < 0)
            ((TextView) findViewById(R.id.BatteryValue)).setText("0%");
        else
            ((TextView) findViewById(R.id.BatteryValue)).setText(bat + "%");
    }

    public void setModeIndicator(int value) {
        for (int i = 1; i <= 4; i++) {
            String viewId = "ModeValue" + (i);
            int resID = getResources().getIdentifier(viewId, "id", getPackageName());
            if (value == i)
                ((TextView) findViewById(resID)).setTextColor(getResources().getColor(R.color.colorText));
            else
                ((TextView) findViewById(resID)).setTextColor(getResources().getColor(R.color.colorTextDisabled));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ACTIVITY_SETTINGS &&
                resultCode == RESULT_OK) {
            BluetoothPeripheral peripheral = BluetoothHandler.getInstance(this).getConnectedPeripheral();
            if (peripheral != null) {
                BluetoothHandler.getInstance(this).sendSettings();
            }

            mySettingsList = (ArrayList<SettingsObject>) data.getSerializableExtra(SettingsActivity.INTENT_EXTRA_RESULT);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(EXTRA_SETTINGS_LIST, mySettingsList);
                startActivityForResult(intent, REQUEST_CODE_ACTIVITY_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickSpeed(View v) {
        Log.d(TAG, "onClickSpeed");

        mMaxSpeed = 0;
        tvSpeedMax.setText(String.format(Locale.ENGLISH, "..."));

    }

    public void onClickSpeedLmt(View v) {
        Log.d(TAG, "onClickSpeedLmt");

        if (mSpeedLimiter == 1)
            mSpeedLimiter = 0;
        else
            mSpeedLimiter = 1;

        BluetoothHandler.getInstance(this).sendSpeedLimiterValue((byte) mSpeedLimiter);

        Log.d(TAG, "mSpeedLimiter sent : " + mSpeedLimiter);
    }

    public void onClickEco(View v) {

        mEco--;
        if (mEco < 1)
            mEco = 3;

        BluetoothHandler.getInstance(this).sendEcoValue((byte) mEco);

        Log.d(TAG, "onClickEco");
    }

    public void onClickAccel(View v) {

        mAccel--;
        if (mAccel < 0)
            mAccel = 5;

        BluetoothHandler.getInstance(this).sendAccelValue((byte) mAccel);

        Log.d(TAG, "onClickAccele");
    }

    public void onClickPower(View v) {
        Log.d(TAG, "onClickPower");

        mLastPower = 0;
        tvPowerMax.setText(String.format(Locale.ENGLISH, "..."));
    }

    public void onClickCurrent(View v) {
        Log.d(TAG, "onClickCurrent");

        BluetoothHandler.getInstance(this).sendCurrentCalibValue((byte) 0x01);

        mLastCurrent = 0;
        tvCurrentMax.setText(String.format(Locale.ENGLISH, "..."));
    }

    public void onClickBtLock(View v) {
        Log.d(TAG, "onClickBtLock");

        if (mBleLockForce == 1)
        BluetoothHandler.getInstance(this).sendBleLockForceValue((byte) 0x00);
        else
            BluetoothHandler.getInstance(this).sendBleLockForceValue((byte) 0x01);

    }

    public void onClickTemperature(View v) {
        tvTemperatureMax.setText(String.format(Locale.ENGLISH, "..."));
        mLastTemp = 0;
    }

    public void onClickMode(View v) {
        Log.d(TAG, "onClickMode");
        mLcdMode++;
        if (mLcdMode > 4)
            mLcdMode = 1;

        BluetoothHandler.getInstance(this).sendModeValue((byte) mLcdMode);

        setModeIndicator(mLcdMode);

        Log.d(TAG, "lcdMode sent : " + mLcdMode);

    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        this.menu = menu;

        item = menu.findItem(R.id.bt_connect);
        if (item.getActionView() != null)
            item.getActionView().clearAnimation();
        item.setActionView(getBtAnimation(mLastBtStatus));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) return;

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            // Check if Location services are on because they are required to make scanning work
            if (checkLocationServices()) {
                // Check if the app has the right permissions
                if (hasPermissions()) {
                    initBluetoothHandler();
                }
            }
        }
    }

    private void initBluetoothHandler() {
        BluetoothHandler.getInstance(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationServiceStateReceiver);
        unregisterReceiver(modeDataReceiver);
        unregisterReceiver(speedDataReceiver);
        unregisterReceiver(brakeStatusDataReceiver);
        unregisterReceiver(voltageDataReceiver);
        unregisterReceiver(ampereDataReceiver);
        unregisterReceiver(powerDataReceiver);
        unregisterReceiver(btLockDataReceiver);
        unregisterReceiver(temperatureDataReceiver);
        unregisterReceiver(humidityDataReceiver);

    }

    private final BroadcastReceiver locationServiceStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(LocationManager.MODE_CHANGED_ACTION)) {
                boolean isEnabled = areLocationServicesEnabled();
                Timber.i("Location service state changed to: %s", isEnabled ? "on" : "off");
                checkLocationServices();
            }
        }
    };

    ImageView getBtAnimation(int value) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = null;

        if (value == BluetoothHandler.CONNECT_STATUS_OK) {
            iv = (ImageView) inflater.inflate(R.layout.blue_only, null);

        } else if (value == BluetoothHandler.CONNECT_STATUS_FAILED) {
            iv = (ImageView) inflater.inflate(R.layout.red_only, null);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            iv.clearAnimation();
            iv.startAnimation(alphaAnimation);

        } else if (value == BluetoothHandler.CONNECT_STATUS_DISCONNECTED) {
            iv = (ImageView) inflater.inflate(R.layout.grey_only, null);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            iv.clearAnimation();
            iv.startAnimation(alphaAnimation);
        }

        return iv;
    }

    private final BroadcastReceiver connectStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Integer value = (Integer) intent.getSerializableExtra(BluetoothHandler.CONNECT_STATUS_EXTRA);

            mLastBtStatus = value;

            Timber.i("==========> new BT status : %d", value);

            item = menu.findItem(R.id.bt_connect);
            item.getActionView().clearAnimation();
            item.setActionView(getBtAnimation(value));

            //item.setEnabled(menusEnabled); // any text will be automatically disabled
            //item.setIcon(resIcon);
        }
    };
    private final BroadcastReceiver modeDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            ModeMeasurement measurement = (ModeMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_MODE_EXTRA);
            if (measurement == null) return;

            setModeIndicator(measurement.modeValue);

            mLcdMode = measurement.modeValue;
        }
    };

    private final BroadcastReceiver speedDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            SpeedMeasurement measurement = (SpeedMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_SPEED_EXTRA);
            if (measurement == null) return;

            tvSpeed.setText(String.format(Locale.ENGLISH, "%d km/h", measurement.speedValue));

            // start / stop 'time run'
            if ((measurement.speedValue > 0) && (mLastSpeed < 5)) {
                if (chronoTimeRun.isStarted) {
                    chronoTimeRun.Continue();
                } else {
                    chronoTimeRun.Start();
                }
            } else if ((measurement.speedValue == 0) && (mLastSpeed > 0)) {
                chronoTimeRun.Stop();
            }

            if (measurement.speedValue >= mMaxSpeed) {
                tvSpeedMax.setText(String.format(Locale.ENGLISH, "%d km/h", measurement.speedValue));
                mMaxSpeed = measurement.speedValue;
            }

            mLastSpeed = measurement.speedValue;


        }
    };

    private final BroadcastReceiver brakeStatusDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            BrakeStatusMeasurement measurement = (BrakeStatusMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_BRAKE_STATUS_EXTRA);
            if (measurement == null) return;

            mBrakeStatus = measurement.brakeValue;

            setBrakeIndicator(EasySettings.retrieveSettingsSharedPrefs(context).getInt(Settings.Electric_brake_min_value, 0),
                    mBrakeStatus,
                    EasySettings.retrieveSettingsSharedPrefs(context).getInt(Settings.Electric_brake_max_value, 0),
                    mBatteryOverLoad);

        }
    };

    private final BroadcastReceiver voltageDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            VoltageMeasurement measurement = (VoltageMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_VOLTAGE_EXTRA);
            if (measurement == null) return;

            tvVoltage.setText(String.format(Locale.ENGLISH, "%2.1f V", measurement.voltage));

            // update battery indicator
            setBatteryIndicator(
                    Float.parseFloat(EasySettings.retrieveSettingsSharedPrefs(context).getString(Settings.Battery_min_voltage, "0")),
                    (float) (measurement.voltage),
                    Float.parseFloat(EasySettings.retrieveSettingsSharedPrefs(context).getString(Settings.Battery_max_voltage, "0"))
            );

            // check is battery is overloaded
            boolean overloadCheck = EasySettings.retrieveSettingsSharedPrefs(context).getBoolean(Settings.Electric_brake_disabled_condition, true);
            int maxVoltage = EasySettings.retrieveSettingsSharedPrefs(context).getInt(Settings.Electric_brake_disabled_voltage_limit, 0);
            if ((measurement.voltage > maxVoltage) && (overloadCheck))
                mBatteryOverLoad = true;
            else
                mBatteryOverLoad = false;

            // update brake indicator
            setBrakeIndicator(EasySettings.retrieveSettingsSharedPrefs(context).getInt(Settings.Electric_brake_min_value, 0),
                    mBrakeStatus,
                    EasySettings.retrieveSettingsSharedPrefs(context).getInt(Settings.Electric_brake_max_value, 0),
                    mBatteryOverLoad);
        }
    };

    private final BroadcastReceiver ampereDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            AmpereMeasurement measurement = (AmpereMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_AMPERE_EXTRA);
            if (measurement == null) return;

            tvCurrent.setText(String.format(Locale.ENGLISH, "%2.1f A", measurement.current));

            if (measurement.current >= mLastCurrent) {
                tvCurrentMax.setText(String.format(Locale.ENGLISH, "%2.1f A", measurement.current));
                mLastCurrent = measurement.current;
            }
        }
    };

    private final BroadcastReceiver powerDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            PowerMeasurement measurement = (PowerMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_POWER_EXTRA);
            if (measurement == null) return;


            tvPower.setText(String.format(Locale.ENGLISH, "%d W", measurement.power));

            if (measurement.power >= mLastPower) {
                tvPowerMax.setText(String.format(Locale.ENGLISH, "%d W", measurement.power));
                mLastPower = measurement.power;
            }
        }
    };

    private final BroadcastReceiver btLockDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.i("btLockDataReceiver");
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            BtlockMeasurement measurement = (BtlockMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_BTLOCK_EXTRA);
            if (measurement == null) return;

            int bleLockStatus = measurement.bleLockStatus;
            int bleLockBeaconVisibleValue = measurement.btLockBeaconVisibleValue;
            int btLockBeaconRssiValue = measurement.btLockBeaconRssiValue;
            int bleLockForcedValue = measurement.bleLockForcedValue;

            mBleLockForce  = bleLockForcedValue;

            int btLockMode = Settings.listToValueBtLockMode(context, EasySettings.retrieveSettingsSharedPrefs(context).getString(Settings.Bluetooth_lock_mode, ""));

            // LOCKED
            if (bleLockStatus == 1) {

                if (bleLockForcedValue == 1) {
                    tvBtLock.setText(String.format(Locale.ENGLISH, "%s", "ON (FORCED)"));
                } else {
                    if (bleLockBeaconVisibleValue == 0) {
                        tvBtLock.setText(String.format(Locale.ENGLISH, "%s", "ON (beacon lost)"));
                    } else {
                        tvBtLock.setText(String.format(Locale.ENGLISH, "%s", "ON (smart. lost)"));
                    }
                }
            }
            // UNLOCKED
            else {
                if (bleLockBeaconVisibleValue == 1)
                    if ((bleLockStatus == 3) || (bleLockStatus == 4)) {
                        tvBtLock.setText(String.format(Locale.ENGLISH, "%s", "OFF (beacon visible"));
                    } else if ((bleLockStatus == 2)) {
                        tvBtLock.setText(String.format(Locale.ENGLISH, "%s", "OFF (smart. connected)"));
                    } else {
                        tvBtLock.setText(String.format(Locale.ENGLISH, "%s", "OFF"));
                    }
                else {
                    tvBtLock.setText(String.format(Locale.ENGLISH, "%s", "OFF"));
                }
            }

        }
    };

    private final BroadcastReceiver temperatureDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            Integer value = (Integer) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_TEMPERATURE_EXTRA);
            if (value == null) return;

            int temp = value / 1000;

            tvTemperature.setText(String.format(Locale.ENGLISH, "%d °", temp));

            if (temp >= mLastTemp) {
                tvTemperatureMax.setText(String.format(Locale.ENGLISH, "%d °", temp));
                mLastTemp = temp;
            }
        }
    };
    private final BroadcastReceiver humidityDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            Integer value = (Integer) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_HUMIDITY_EXTRA);
            if (value == null) return;

            int temp = value / 1000;

            tvHumidity.setText(String.format(Locale.ENGLISH, "%d %%", temp));
        }
    };
    private final BroadcastReceiver speedLimiterDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            Integer value = (Integer) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_SPEED_LIMITER_EXTRA);
            if (value == null) return;

            mSpeedLimiter = value;

            if (value == 0)
                tvSpeedLimiter.setText(String.format(Locale.ENGLISH, "OFF"));
            else
                tvSpeedLimiter.setText(String.format(Locale.ENGLISH, "ON"));
        }
    };
    private final BroadcastReceiver ecoDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            Integer value = (Integer) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_ECO_EXTRA);
            if (value == null) return;

            mEco = value;

            String txt = "";
            if (mEco == 1)
                txt = "MAX";
            else if (mEco == 2)
                txt = "MEDIUM";
            else if (mEco == 3)
                txt = "NONE";

            tvEco.setText(txt);
        }
    };
    private final BroadcastReceiver accelDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothPeripheral peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL));
            Integer value = (Integer) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_ACCEL_EXTRA);
            if (value == null) return;

            mAccel = value;

            String txt = "";
            if (mAccel == 0)
                txt = "MAX";
            else if (mAccel == 1)
                txt = "VERY FAST";
            else if (mAccel == 2)
                txt = "FAST";
            else if (mAccel == 3)
                txt = "MEDIUM";
            else if (mAccel == 4)
                txt = "SLOW";
            else if (mAccel == 5)
                txt = "VERY SLOW";

            tvAccel.setText(txt);
        }
    };


    private BluetoothPeripheral getPeripheral(String peripheralAddress) {
        BluetoothCentral central = BluetoothHandler.getInstance(getApplicationContext()).central;
        return central.getPeripheral(peripheralAddress);
    }

    private boolean hasPermissions() {
        int targetSdkVersion = getApplicationInfo().targetSdkVersion;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && targetSdkVersion >= Build.VERSION_CODES.Q) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST);
                return false;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_LOCATION_REQUEST);
                return false;
            }
        }
        return true;
    }

    private boolean areLocationServicesEnabled() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            Timber.e("could not get location manager");
            return false;
        }

        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isGpsEnabled || isNetworkEnabled;
    }

    private boolean checkLocationServices() {
        if (!areLocationServicesEnabled()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Location services are not enabled")
                    .setMessage("Scanning for Bluetooth peripherals requires locations services to be enabled.") // Want to enable?
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_LOCATION_REQUEST:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        initBluetoothHandler();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
