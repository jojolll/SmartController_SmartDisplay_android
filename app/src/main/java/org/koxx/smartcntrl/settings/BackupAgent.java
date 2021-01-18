package org.koxx.smartcntrl.settings;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.SharedPreferences;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.hotmail.or_dvir.easysettings.pojos.EasySettings;

import java.io.IOException;

public class BackupAgent extends BackupAgentHelper {

    // The name of the SharedPreferences file
    static final String PREFS = EasySettings.SHARED_PREFS_KEY;

    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = EasySettings.SHARED_PREFS_KEY;

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("backup", true);
        editor.commit();
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
        Log.i("BackupAgent", "OnBackUp");
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        super.onRestore(data, appVersionCode, newState);
        Log.i("BackupAgent", "onRestore");
    }

    @Override
    public void onRestoreFinished() {
        super.onRestoreFinished();
        Log.i("BackupAgent", "onRestoreFinished");
    }
}