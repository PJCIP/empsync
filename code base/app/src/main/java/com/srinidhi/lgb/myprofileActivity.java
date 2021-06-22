package com.srinidhi.lgb;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.rey.material.widget.TabPageIndicator;
import com.srinidhi.lgb.utility.NetworkChangeListener;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class myprofileActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        ScrollerViewPager viewPager=findViewById(R.id.scroll_view_pager);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        SpringIndicator indicator=findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);


    }
    protected void onStart()
    {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }
    protected void onStop()
    {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
