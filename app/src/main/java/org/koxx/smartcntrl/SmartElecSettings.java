package org.koxx.smartcntrl;

import android.content.Context;

import com.hotmail.or_dvir.easysettings.pojos.CheckBoxSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.hotmail.or_dvir.easysettings.pojos.HeaderSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SeekBarSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject;
import com.hotmail.or_dvir.easysettings_dialogs.pojos.EditTextSettingsObject;
import com.hotmail.or_dvir.easysettings_dialogs.pojos.ListSettingsObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SmartElecSettings {

    public static final String Smartphone_display = "Smartphone display";
    public static final String Speed_adjustment = "Speed adjustment";
    public static final String Display_current_pane = "Display current pane";
    public static final String Display_power_pane = "Display power pane";
    public static final String Display_brake_mode_pane = "Display brake mode pane";
    public static final String Display_bluetooth_lock_mode_pane = "Display bluetooth lock mode pane";


    public static final String Escooter_Specs = "E-scooter caracteristics";
    public static final String Wheel_size = "Wheel size";
    public static final String Motor_pole_number = "Motor number of magnets pairs";
    public static final String Battery_min_voltage = "Battery min voltage";
    public static final String Battery_max_voltage = "Battery max voltage";
    public static final String Battery_max_distance = "Battery distance - max distance with full battery";

    public static final String Escooter_LCD_display = "E-scooter LCD display";
    public static final String LCD_Speed_adjustement = "LCD Speed adjustment";
    public static final String Use_brake_icon_as_speed_limit_display = "Use brake icon as speed limit display";

    public static final String Escooter_Accessories = "E-scooter Accessories";
    public static final String Button_1_short_press_action = "Button 1 short press action";
    public static final String Button_1_long_press_action = "Button 1 long press action";
    public static final String Button_2_short_press_action = "Button 2 short press action";
    public static final String Button_2_long_press_action = "Button 2 long press action";
    public static final String Button_long_press_duration = "Button long press duration";

    public static final String SmartController = "SmartController";
    public static final String Bluetooth_lock_mode = "Bluetooth lock mode";
    public static final String Bluetooth_pin_code = "Bluetooth pin code";
    public static final String Beacon_Mac_Address = "Beacon Mac Address";
    public static final String Beacon_range = "Beacon range";
    public static final String Mode_1_Power_limitation = "Mode 1 Power limitation";
    public static final String Mode_2_Power_limitation = "Mode 2 Power limitation";
    public static final String Mode_3_Power_limitation = "Mode 3 Power limitation";
    public static final String Mode_Z_Power_limitation = "Mode Z Power limitation -";
    public static final String Mode_Z_Eco_mode = "Mode Z Eco mode";
    public static final String Mode_Z_Acceleration = "Mode Z Acceleration";

    public static final String Electric_brake = "Electric brake";
    public static final String Electric_brake_progressive_mode = "Electric brake progressive mode";
    public static final String Electric_brake_type = "Electric brake type";
    public static final String Electric_brake_min_value = "Electric brake min value";
    public static final String Electric_brake_max_value = "Electric brake max value";
    public static final String Electric_brake_time_between_mode_shift = "Electric brake time between mode shift";
    public static final String Electric_brake_disabled_on_high_voltage = "Electric brake disabled on high battery voltage";
    public static final String Electric_brake_disabled_percent_limit = "Electric brake disabled percent limit";

    public static final String Limiters = "Limiters";
    public static final String Current_loop_mode = "Current loop mode";
    public static final String Current_loop_max_current = "Current loop max current";
    public static final String Speed_loop_mode = "Speed loop mode";
    public static final String Speed_limiter_at_startup = "Speed limiter at startup";
    public static final String Speed_limiter_max_speed = "Speed limiter max speed (km/h)";

    public static final String Battery_saving = "Battery saving";
    public static final String Battery_saving_medium_voltage = "Automatic medium battery saving at battery percent";
    public static final String Battery_saving_strong_voltage = "Automatic strong battery saving at battery percent";

    public static final String OTA_Wifi = "OTA update";
    public static final String Wifi_ssid = "Wifi network name (SSID)";
    public static final String Wifi_pwd = "Wifi password";

    public static final String LIST_Bluetooth_lock_mode_1 = "None";
    public static final String LIST_Bluetooth_lock_mode_2 = "Smartphone connected";
    public static final String LIST_Bluetooth_lock_mode_3 = "Smartphone connected or beacon visible";
    public static final String LIST_Bluetooth_lock_mode_4 = "Beacon visible";

    private static final String LIST_Button_press_action_0 = "None";
    private static final String LIST_Button_press_action_1 = "Mode Z ON/OFF";
    private static final String LIST_Button_press_action_2 = "Anti-theft manual lock ON";
    private static final String LIST_Button_press_action_3 = "Nitro boost continuous";
    private static final String LIST_Button_press_action_6 = "Nitro boost ON/OFF";
    private static final String LIST_Button_press_action_4 = "Startup speed limitation ON/OFF";
    private static final String LIST_Button_press_action_5 = "Aux ON/OFF";

    private static final String LIST_Electric_brake_type_0 = "None";
    private static final String LIST_Electric_brake_type_1 = "Digital";
    private static final String LIST_Electric_brake_type_2 = "Analog";

    public static ArrayList<SettingsObject> initialize() {

        ArrayList<String> LIST_Bluetooth_lock_mode = new ArrayList<>();
        LIST_Bluetooth_lock_mode.add(LIST_Bluetooth_lock_mode_1);
        LIST_Bluetooth_lock_mode.add(LIST_Bluetooth_lock_mode_2);
        LIST_Bluetooth_lock_mode.add(LIST_Bluetooth_lock_mode_3);
        LIST_Bluetooth_lock_mode.add(LIST_Bluetooth_lock_mode_4);

        ArrayList<String> LIST_Button_short_press_action = new ArrayList<>();
        LIST_Button_short_press_action.add(LIST_Button_press_action_0);
        LIST_Button_short_press_action.add(LIST_Button_press_action_1);
        LIST_Button_short_press_action.add(LIST_Button_press_action_3);
        LIST_Button_short_press_action.add(LIST_Button_press_action_4);
        LIST_Button_short_press_action.add(LIST_Button_press_action_5);
        LIST_Button_short_press_action.add(LIST_Button_press_action_6);

        ArrayList<String> LIST_Button_long_press_action = new ArrayList<>();
        LIST_Button_long_press_action.add(LIST_Button_press_action_0);
        LIST_Button_long_press_action.add(LIST_Button_press_action_1);
        LIST_Button_long_press_action.add(LIST_Button_press_action_2);
        LIST_Button_long_press_action.add(LIST_Button_press_action_3);
        LIST_Button_long_press_action.add(LIST_Button_press_action_4);
        LIST_Button_long_press_action.add(LIST_Button_press_action_5);

        ArrayList<String> LIST_Electric_brake_type = new ArrayList<>();
        LIST_Electric_brake_type.add(LIST_Electric_brake_type_0);
        LIST_Electric_brake_type.add(LIST_Electric_brake_type_1);
        LIST_Electric_brake_type.add(LIST_Electric_brake_type_2);

        ArrayList<SettingsObject> settings = EasySettings.createSettingsArray(
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.Escooter_Specs)
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Wheel_size, SmartElecSettings.Wheel_size, "8", "save")
                        .setDialogTitle("In inches")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Motor_pole_number, SmartElecSettings.Motor_pole_number, "15", "save")
                        .setDialogTitle("Number of poles pairs")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Battery_min_voltage, SmartElecSettings.Battery_min_voltage, "42", "save")
                        .setDialogTitle("In volts")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Battery_max_voltage, SmartElecSettings.Battery_max_voltage, "54.8", "save")
                        .setDialogTitle("In volts")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Battery_max_distance, SmartElecSettings.Battery_max_distance, "40", "save")
                        .setDialogTitle("In kilometers")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.Smartphone_display)
                        .build(),

                new EditTextSettingsObject.Builder(SmartElecSettings.Speed_adjustment, SmartElecSettings.Speed_adjustment, "0", "save")
                        .setDialogTitle("Display adjusted speed (-10 = displayed speed decreased by 10%)")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.Escooter_LCD_display)
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.LCD_Speed_adjustement, SmartElecSettings.LCD_Speed_adjustement, "0", "save")
                        .setDialogTitle("Display adjusted speed (-10 = displayed speed decreased by 10%)")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.Escooter_Accessories)
                        .build(),
                new ListSettingsObject.Builder(Button_1_short_press_action, Button_1_short_press_action, LIST_Button_press_action_1, LIST_Button_short_press_action, "save")
                        .setUseValueAsSummary()
                        .setNegativeBtnText("cancel")
                        .build(),
                new ListSettingsObject.Builder(Button_1_long_press_action, Button_1_long_press_action, LIST_Button_press_action_2, LIST_Button_long_press_action, "save")
                        .setUseValueAsSummary()
                        .setNegativeBtnText("cancel")
                        .build(),
                new ListSettingsObject.Builder(Button_2_short_press_action, Button_2_short_press_action, LIST_Button_press_action_3, LIST_Button_short_press_action, "save")
                        .setUseValueAsSummary()
                        .setNegativeBtnText("cancel")
                        .build(),
                new ListSettingsObject.Builder(Button_2_long_press_action, Button_2_long_press_action, LIST_Button_press_action_4, LIST_Button_long_press_action, "save")
                        .setUseValueAsSummary()
                        .setNegativeBtnText("cancel")
                        .build(),
                new SeekBarSettingsObject.Builder(Button_long_press_duration, Button_long_press_duration, 5, 2, 30)
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.SmartController)
                        .build(),
                new ListSettingsObject.Builder(Bluetooth_lock_mode, Bluetooth_lock_mode, LIST_Bluetooth_lock_mode_1, LIST_Bluetooth_lock_mode, "save")
                        .setUseValueAsSummary()
                        .setNegativeBtnText("cancel")
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Bluetooth_pin_code, SmartElecSettings.Bluetooth_pin_code, "147258", "save")
                        .setDialogTitle("PIN code (6 digits max)")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Beacon_Mac_Address, SmartElecSettings.Beacon_Mac_Address, "aa:bb:cc:dd:ee:ff", "save")
                        .setDialogTitle("MAC address")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .build(),
                new SeekBarSettingsObject.Builder(Beacon_range, Beacon_range, -80, -100, -30)
                        .setUseValueAsSummary()
                        .build(),
//
                new SeekBarSettingsObject.Builder(Mode_Z_Power_limitation, Mode_Z_Power_limitation, 100, 0, 100)
                        .setUseValueAsSummary()
                        .build(),
                new CheckBoxSettingsObject.Builder(Mode_Z_Eco_mode, Mode_Z_Eco_mode, false)
                        .setOffText("off")
                        .setOnText("on")
                        .build(),
                new SeekBarSettingsObject.Builder(Mode_Z_Acceleration, Mode_Z_Acceleration, 100, 0, 100)
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),

// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.Battery_saving)
                        .build(),
                new SeekBarSettingsObject.Builder(Battery_saving_medium_voltage, Battery_saving_medium_voltage, 40, 0, 100)
                        .setUseValueAsSummary()
                        .build(),
                new SeekBarSettingsObject.Builder(Battery_saving_strong_voltage, Battery_saving_strong_voltage, 20, 0, 100)
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.Electric_brake)
                        .build(),
                new CheckBoxSettingsObject.Builder(Electric_brake_progressive_mode, Electric_brake_progressive_mode, false)
                        .setOffText("off")
                        .setOnText("on")
                        .build(),
                new ListSettingsObject.Builder(Electric_brake_type, Electric_brake_type, LIST_Electric_brake_type_0, LIST_Electric_brake_type, "save")
                        .setUseValueAsSummary()
                        .setNegativeBtnText("cancel")
                        .build(),
                new SeekBarSettingsObject.Builder(Electric_brake_min_value, Electric_brake_min_value, 0, 0, 5)
                        .setUseValueAsSummary()
                        .build(),
                new SeekBarSettingsObject.Builder(Electric_brake_max_value, Electric_brake_max_value, 5, 0, 5)
                        .setUseValueAsSummary()
                        .build(),
                new SeekBarSettingsObject.Builder(Electric_brake_time_between_mode_shift, Electric_brake_time_between_mode_shift, 500, 100, 2000)
                        .setUseValueAsSummary()
                        .build(),
                new CheckBoxSettingsObject.Builder(Electric_brake_disabled_on_high_voltage, Electric_brake_disabled_on_high_voltage, false)
                        .setOffText("off")
                        .setOnText("on")
                        .build(),
                new SeekBarSettingsObject.Builder(Electric_brake_disabled_percent_limit, Electric_brake_disabled_percent_limit, 90, 80, 95)
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),
//
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.Limiters)
                        .build(),
                new CheckBoxSettingsObject.Builder(Current_loop_mode, Current_loop_mode, false)
                        .setOffText("off")
                        .setOnText("on")
                        .build(),
                new SeekBarSettingsObject.Builder(Current_loop_max_current, Current_loop_max_current, 22, 1, 99)
                        .setUseValueAsSummary()
                        .build(),
                new CheckBoxSettingsObject.Builder(Speed_loop_mode, Speed_loop_mode, false)
                        .setOffText("off")
                        .setOnText("on")
                        .build(),
                new CheckBoxSettingsObject.Builder(Speed_limiter_at_startup, Speed_limiter_at_startup, false)
                        .setOffText("off")
                        .setOnText("on")
                        .build(),
                new SeekBarSettingsObject.Builder(Speed_limiter_max_speed, Speed_limiter_max_speed, 25, 20, 33)
                        .setUseValueAsSummary()
                        .addDivider()
                        .build(),
//
// ----------------------
                new HeaderSettingsObject.Builder(SmartElecSettings.OTA_Wifi)
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Wifi_ssid, SmartElecSettings.Wifi_ssid, "", "save")
                        .setDialogTitle("Wifi SSID")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .setUseValueAsSummary()
                        .build(),
                new EditTextSettingsObject.Builder(SmartElecSettings.Wifi_pwd, SmartElecSettings.Wifi_pwd, "", "save")
                        .setDialogTitle("Wifi password")
                        .setUseValueAsPrefillText()
                        .setNegativeBtnText("cancel")
                        .build()

        );
        return settings;
    }

    static private int listToValueButton(Context ctx, String value) {
        int intValue = 0;

        String valueStr = EasySettings.retrieveSettingsSharedPrefs(ctx).getString(value, "");

        if (valueStr.equals(LIST_Button_press_action_0))
            intValue = 0;
        else if (valueStr.equals(LIST_Button_press_action_1))
            intValue = 1;
        else if (valueStr.equals(LIST_Button_press_action_2))
            intValue = 2;
        else if (valueStr.equals(LIST_Button_press_action_3))
            intValue = 3;
        else if (valueStr.equals(LIST_Button_press_action_4))
            intValue = 4;
        else if (valueStr.equals(LIST_Button_press_action_5))
            intValue = 5;
        else if (valueStr.equals(LIST_Button_press_action_6))
            intValue = 6;
        return intValue;
    }


    public static int listToValueBtLockMode(Context ctx, String value) {
        int intValue = 0;

        String valueStr = EasySettings.retrieveSettingsSharedPrefs(ctx).getString(value, "");

        if (valueStr.equals(LIST_Bluetooth_lock_mode_1))
            intValue = 0;
        else if (valueStr.equals(LIST_Bluetooth_lock_mode_2))
            intValue = 1;
        else if (valueStr.equals(LIST_Bluetooth_lock_mode_3))
            intValue = 2;
        else if (valueStr.equals(LIST_Bluetooth_lock_mode_4))
            intValue = 3;

        return intValue;
    }

    public static int listToValueBrakeMode(Context ctx, String value) {
        int intValue = 0;

        String valueStr = EasySettings.retrieveSettingsSharedPrefs(ctx).getString(value, "");

        if (valueStr.equals(LIST_Electric_brake_type_0))
            intValue = 0;
        else if (valueStr.equals(LIST_Electric_brake_type_1))
            intValue = 1;
        else if (valueStr.equals(LIST_Electric_brake_type_2))
            intValue = 2;

        return intValue;
    }

    static public byte[] settings1ToByteArray(Context ctx) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {

            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Beacon_range, 0));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Mode_Z_Power_limitation, 0));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getBoolean(Mode_Z_Eco_mode, false) ? 1 : 0);
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Mode_Z_Acceleration, 0));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getBoolean(Electric_brake_progressive_mode, false) ? 1 : 0);
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Electric_brake_min_value, 0));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Electric_brake_max_value, 5));
            int value = EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Electric_brake_time_between_mode_shift, 500);
            dos.writeByte((byte) ((value >> 0) & 0xff));
            dos.writeByte((byte) ((value >> 8) & 0xff));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getBoolean(Electric_brake_disabled_on_high_voltage, false) ? 1 : 0);
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Electric_brake_disabled_percent_limit, 0));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getBoolean(Current_loop_mode, false) ? 1 : 0);
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Current_loop_max_current, 0));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getBoolean(Speed_loop_mode, false) ? 1 : 0);
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getBoolean(Speed_limiter_at_startup, false) ? 1 : 0);
            dos.writeByte((int) (Float.parseFloat(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Wheel_size, "").replace(",", ".")) * 10));
            dos.writeByte(Integer.parseInt(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Motor_pole_number, "")));
            dos.writeByte(listToValueBtLockMode(ctx, Bluetooth_lock_mode));
            dos.writeByte(Integer.parseInt(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(LCD_Speed_adjustement, "0")));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Speed_limiter_max_speed, 25));
            // remain 0

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();

    }

    static public byte[] settings2ToByteArray(Context ctx) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeByte(listToValueBrakeMode(ctx, Electric_brake_type));
            String beaconAddress = EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Beacon_Mac_Address, "AA:BB:CC:DD:EE:FF").substring(0, 17).toLowerCase();
            dos.writeByte((int) (Float.parseFloat(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Battery_max_distance, "").replace(",", ".")) * 10)); // dummy
            dos.writeByte((byte) 0); // dummy
            dos.writeBytes(beaconAddress); // size 17
            // remain 2

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();

    }

    static public byte[] settings3ToByteArray(Context ctx) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeByte(listToValueButton(ctx, Button_1_short_press_action));
            dos.writeByte(listToValueButton(ctx, Button_1_long_press_action));
            dos.writeByte(listToValueButton(ctx, Button_2_short_press_action));
            dos.writeByte(listToValueButton(ctx, Button_2_long_press_action));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Button_long_press_duration, 5));

            int value = (int) (Float.parseFloat(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Battery_min_voltage, "").replace(",", ".")) * 10);
            dos.writeByte((byte) ((value >> 0) & 0xff));
            dos.writeByte((byte) ((value >> 8) & 0xff));

            value = (int) (Float.parseFloat(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Battery_max_voltage, "").replace(",", ".")) * 10);
            dos.writeByte((byte) ((value >> 0) & 0xff));
            dos.writeByte((byte) ((value >> 8) & 0xff));

            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Battery_saving_medium_voltage, 0));
            dos.writeByte(EasySettings.retrieveSettingsSharedPrefs(ctx).getInt(Battery_saving_strong_voltage, 0));
            int pinCode = 147258;
            try {
                pinCode = Integer.parseInt(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Bluetooth_pin_code, "147258"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dos.writeByte((byte) ((pinCode >> 0) & 0xff));
            dos.writeByte((byte) ((pinCode >> 8) & 0xff));
            dos.writeByte((byte) ((pinCode >> 16) & 0xff));
            dos.writeByte((byte) ((pinCode >> 24) & 0xff));

            value = (int) (Float.parseFloat(EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Battery_max_distance, "").replace(",", ".")) * 10);
            dos.writeByte((byte) ((value >> 0) & 0xff));
            dos.writeByte((byte) ((value >> 8) & 0xff));

            // remain 3

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }


    static public byte[] settings4ToByteArray(Context ctx) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {

            String wifiSsid = EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Wifi_ssid, "");
            if (wifiSsid.length() > 20)
                wifiSsid = wifiSsid.substring(0, 19);
            dos.writeBytes(wifiSsid); // size 20
            // remain 0

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();

    }

    static public byte[] settings5ToByteArray(Context ctx) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            String wifiPwd = EasySettings.retrieveSettingsSharedPrefs(ctx).getString(Wifi_pwd, "");
            if (wifiPwd.length() > 20)
                wifiPwd = wifiPwd.substring(0, 19);
            dos.writeBytes(wifiPwd); // size 20
            // remain 0

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();

    }
}