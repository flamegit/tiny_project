package com.flame.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 2016/11/3.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

   // private String[] mTitles;
    private Fragment[] mFragments;

    public FragmentAdapter(FragmentManager fm,  Fragment[] fragments) {
        super(fm);
        //mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mTitles[position];
//    }

    @Override
    public int getCount() {
        //if (mTitles == null) return 0;
        return mFragments.length;
    }
}
