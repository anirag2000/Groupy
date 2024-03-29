package com.example.groupy.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.groupy.R;
import com.example.groupy.calling.SinchService;
import com.example.groupy.calling.SinchStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


//Page Viewer



public class Home extends AppCompatActivity  {

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        supportPostponeEnterTransition();
        supportStartPostponedEnterTransition();
        startService(new Intent(this, SinchService.class));
        viewPager= findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));









    }
    public void selectIndex(int newIndex) {
        viewPager.setCurrentItem(newIndex);
    }

    @Override
    public void onBackPressed() {
        int currentPosition = viewPager.getCurrentItem();
        if (currentPosition != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSinchConnected(SinchStatus.SinchConnected sinchConnected){

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSinchDisconnected(SinchStatus.SinchDisconnected sinchDisconnected){

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSinchFailed(SinchStatus.SinchFailed sinchFailed){

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSinchLogging(SinchStatus.SinchLogger sinchLogger){

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



}

 class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

            case 0: return new Main();
            case 1: return new Dms();

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
