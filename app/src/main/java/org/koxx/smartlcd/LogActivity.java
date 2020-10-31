package org.koxx.smartlcd;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import org.koxx.smartlcd.tools.OnScreenLog;

public class LogActivity extends AppCompatActivity {

    private OnScreenLog log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothHandler.getInstance(getApplicationContext()).clearLogs();
            }
        });

        /*
        ScrollView svLogs = (ScrollView) findViewById(R.id.svLogs);
        svLogs.fullScroll(View.FOCUS_DOWN);
        TextView tv = findViewById(R.id.logs);
        */


        log = new OnScreenLog(this, R.id.content_1);

        BluetoothHandler.getInstance(this).setLogActivity(this);
    }

    @Override
    protected void onDestroy() {
        BluetoothHandler.getInstance(this).setLogActivity(null);
        log.clearLog();
        super.onDestroy();

    }

    public OnScreenLog getOnscreenLog() {
        return log;
    }

}