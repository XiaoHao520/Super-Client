package com.superschool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
   private FragmentManager fm;

    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
        this.fm = fm;
    }



    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
