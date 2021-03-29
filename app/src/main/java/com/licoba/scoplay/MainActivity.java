package com.licoba.scoplay;

import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.licoba.scoplay.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        changeMode(0);
        ViewPager viewPager = findViewById(R.id.view_pager);
        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e(TAG,"onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected；" + position);
                changeMode(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.e(TAG,"onPageScrollStateChanged");

            }
        });
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "需要配合蓝牙耳机使用 🎧", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    public void changeMode(int position) {
        Log.e(TAG, "切换模式");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (position == 0) {
                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    audioManager.setBluetoothScoOn(true);
                    audioManager.startBluetoothSco();
                } else if (position == 1) {
                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.stopBluetoothSco();
                    audioManager.setBluetoothScoOn(false);
                }
            }
        }).start();

    }
}