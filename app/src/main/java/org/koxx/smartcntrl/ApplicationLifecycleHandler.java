package org.koxx.smartcntrl;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifecycleHandler.class.getSimpleName();
    private static boolean isInBackground = false;
    private final Context ctx;

    public ApplicationLifecycleHandler(Context applicationContext) {
        ctx = applicationContext;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d(TAG, "ApplicationLifecycleHandler : onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "ApplicationLifecycleHandler : onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {

        Log.d(TAG, "ApplicationLifecycleHandler : onActivityResumed");
        if (isInBackground) {
            Log.d(TAG, "app went to foreground");
            isInBackground = false;
            if (BluetoothHandler.getInstance(ctx).getConnectedPeripheral() == null)
                BluetoothHandler.getInstance(ctx).central.scanForPeripherals();

        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "ApplicationLifecycleHandler : onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "ApplicationLifecycleHandler : onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d(TAG, "ApplicationLifecycleHandler : onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "ApplicationLifecycleHandler : onActivityDestroyed");
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        Log.d(TAG, "ApplicationLifecycleHandler : onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "ApplicationLifecycleHandler : onLowMemory");
    }

    @Override
    public void onTrimMemory(int i) {
        Log.d(TAG, "ApplicationLifecycleHandler : onTrimMemory");
        if (i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.d(TAG, "app went to background");
            isInBackground = true;
            if (BluetoothHandler.getInstance(ctx).getConnectedPeripheral() == null) {
                BluetoothHandler.getInstance(ctx).central.stopScan();
            }
            else {
              //  BluetoothHandler.getInstance(ctx).central.close();
            }
        }
    }

    public boolean isIsInBackground() {
        return isInBackground;
    }
}