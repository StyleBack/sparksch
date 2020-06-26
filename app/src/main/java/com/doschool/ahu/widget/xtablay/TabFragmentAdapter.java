package com.doschool.ahu.widget.xtablay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by X on 2019/1/10.
 */
public class TabFragmentAdapter<T extends Fragment> extends FragmentStatePagerAdapter {


    private List<T> list;

    public TabFragmentAdapter(FragmentManager fm, List<T> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public T getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
