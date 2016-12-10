package com.sam_chordas.android.stockhawk.ui;

/**
 * Created by T510 Owner on 11/26/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String mSymbol;
    Intent intent;
    Bundle args = new Bundle();
    public PagerAdapter(FragmentManager fm, int NumOfTabs, String symbol) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        mSymbol = symbol;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                args.putString("symbol", mSymbol);
                tab1.setArguments(args);
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                args.putString("symbol", mSymbol);
                tab2.setArguments(args);
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                args.putString("symbol", mSymbol);
                tab3.setArguments(args);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
