package ru.santaev.clipboardtranslator.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainActivityViewPagerAdapter extends FragmentPagerAdapter{

    MainActivityViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return TranslateFragment.Companion.newInstance();
            case 1:
                return HistoryFragment.newInstance();
            default:
                throw new IllegalArgumentException("Not found item for position " + position);
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
