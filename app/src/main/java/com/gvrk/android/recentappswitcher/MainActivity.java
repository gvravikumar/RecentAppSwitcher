package com.gvrk.android.recentappswitcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button service;
    ImageView iv;
    TextView tv;
    long hour;

    @Override
    protected void onResume() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        hour = c.get(Calendar.HOUR_OF_DAY);
        if (!Global.isServiceRunning) {
            service.setText("Start RAS");
        } else {
            service.setText("Stop RAS");
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = (Button) findViewById(R.id.servicebtn);
        iv = (ImageView) findViewById(R.id.backgroundIv);
        tv = (TextView) findViewById(R.id.tv);
        tv.setTextColor(Color.WHITE);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 12) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.morningbg);
            iv.setImageBitmap(bitmap);
        }
        if (hour >= 12 && hour < 16) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noonbg);
            iv.setImageBitmap(bitmap);
        }
        if (hour >= 16 && hour < 19) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eveningbg);
            iv.setImageBitmap(bitmap);
        }
        if (hour >= 19 && hour <= 23) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nightbg);
            iv.setImageBitmap(bitmap);
        }
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isServiceRunning) {
                    Global.isServiceRunning = true;
                    startService(new Intent(MainActivity.this, RASService.class));
                    service.setText("Stop RAS");
                } else {
                    service.setText("Start RAS");
                    stopService(new Intent(MainActivity.this, RASService.class));
                    Global.isServiceRunning = false;
                }
            }
        });
    }
}
