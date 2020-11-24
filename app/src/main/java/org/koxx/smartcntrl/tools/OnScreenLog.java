package org.koxx.smartcntrl.tools;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by ariel on 07/07/2016.
 */
public class OnScreenLog {
    private static int timeoutTime = 1000;
    private static TextView tvLog;
    private static int maxChars = 50000;
    private static int cntClicks = 0;
    private static boolean visibility = false;
    private static Activity activity;
    private int maxClicks = 5;

    ArrayList<String> logs2 = new ArrayList<>();

    String logText = "";

    public OnScreenLog() {
    }

    public OnScreenLog(Activity activity, int ViewID) {
        OnScreenLog.activity = activity;
        tvLog = new TextView(activity.getApplicationContext());
        tvLog.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        tvLog.setTextColor(Color.BLACK);
        tvLog.setBackgroundColor(Color.WHITE);
        tvLog.setTextSize(7);

        View v = null;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        try {
            linearLayout = (LinearLayout) activity.findViewById(ViewID);
        } catch (ClassCastException e) {
            linearLayout = null;
        }
        ;

        try {
            relativeLayout = (RelativeLayout) activity.findViewById(ViewID);
        } catch (ClassCastException e) {
            relativeLayout = null;
        }
        ;
        if (linearLayout != null) {
            linearLayout.addView(tvLog);
            v = linearLayout;
        } else if (relativeLayout != null) {
            relativeLayout.addView(tvLog);
            v = relativeLayout;
        }

        if (v != null) {
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            cntClicks++;
                            timerHandler.removeCallbacks(rTimeout);
                            timerHandler.postDelayed(rTimeout, timeoutTime);

                            if (cntClicks > maxClicks - 1) {
                                setLogVisible(!visibility);
                                timerHandler.removeCallbacks(rTimeout);
                                cntClicks = 0;
                            }
                            break;

                    }
                    return false;
                }
            });
        }

    }

    public void log(String text) {
        String logText = text;
        maintainLog(logText);
    }

    public void log(int text) {
        String logText = String.valueOf(text);
        maintainLog(logText);
    }

    public void log(int[] text) {
        StringBuilder builder = new StringBuilder();
        for (int i : text) {
            builder.append(i);
            builder.append("-");
        }
        String logText = builder.toString();
        maintainLog(logText);
    }

    public void log(byte[] text) {
        StringBuilder builder = new StringBuilder();
        for (int i : text) {
            builder.append(i);
            builder.append("-");
        }
        String logText = builder.toString();
        maintainLog(logText);
    }

    private void maintainLog(String newText) {

        logText = newText + "\n" + logText;
        logText = logText.substring(0, (logText.length() > maxChars) ? maxChars : logText.length());
        tvLog.setText(logText);

    }

    public void clearLog() {
        logText = "";
        tvLog.setText(logText);
    }

    public void setLogVisible(boolean visibility) {
        if (visibility) tvLog.setVisibility(View.VISIBLE);
        else tvLog.setVisibility(View.INVISIBLE);
        OnScreenLog.visibility = visibility;
    }

    Handler timerHandler = new Handler();
    Runnable rTimeout = new Runnable() {

        @Override
        public void run() {
            cntClicks = 0;
        }
    };
}
