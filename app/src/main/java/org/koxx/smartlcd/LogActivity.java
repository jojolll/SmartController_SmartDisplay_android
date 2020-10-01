package org.koxx.smartlcd;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

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

        ScrollView svLogs = (ScrollView) findViewById(R.id.svLogs);
        svLogs.fullScroll(View.FOCUS_DOWN);

        TextView tv = findViewById(R.id.logs);
        
        BluetoothHandler.getInstance(this).setLogActivity(tv);
    }
}