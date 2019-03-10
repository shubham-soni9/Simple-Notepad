package com.notepad.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.notepad.appdata.Constant;
import com.notepad.fragment.CalenderFragment;
import com.notepad.fragment.FavoriteFragment;
import com.notepad.fragment.HomeFragment;
import com.notepad.fragment.ReminderFragment;
import com.notepad.fragment.TagFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == Constant.HomeNavigation.REMINDER.position) {
            return ReminderFragment.newInstance();
        } else if (position == Constant.HomeNavigation.TAG.position) {
            return TagFragment.newInstance();
        } else if (position == Constant.HomeNavigation.HOME.position) {
            return HomeFragment.newInstance();
        } else if (position == Constant.HomeNavigation.FAVORITE.position) {
            return FavoriteFragment.newInstance();
        } else if (position == Constant.HomeNavigation.CALENDER.position) {
            return CalenderFragment.newInstance();
        }
        return HomeFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 5;
    }

}
