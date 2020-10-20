package com.toperc.aatools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.toperc.lib_aatools.AAToolsMgr;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.open_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AAToolsMgr.open(MainActivity.this);
            }
        });

        findViewById(R.id.test_crash_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1 / 0;
            }
        });

    }
}