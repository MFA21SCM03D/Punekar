package com.example.punekar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.punekar.Adapters.IntroScreenViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager intropages;
    private IntroScreenViewPagerAdapter introScreenViewPagerAdapter;
    private Context mContext;
    private List<IntroScreenItems> mScreenItemsList;
    private TabLayout indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_introscreen);

        intropages = findViewById(R.id.intro);
        indicator = findViewById(R.id.indicator);

        mScreenItemsList = new ArrayList<>();
        mScreenItemsList.add(new IntroScreenItems("Necessities", "", R.drawable.splashscreen));
        mScreenItemsList.add(new IntroScreenItems("Roommates", "", R.drawable.roommateswanted));
        mScreenItemsList.add(new IntroScreenItems("Payments", "", R.drawable.payments));
        introScreenViewPagerAdapter = new IntroScreenViewPagerAdapter(mScreenItemsList, mContext);
        intropages.setAdapter(introScreenViewPagerAdapter);

        indicator.setupWithViewPager(intropages,true);

    }
}
