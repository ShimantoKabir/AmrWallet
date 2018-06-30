package com.example.maask.amrwallet;

/**
 * Created by Maask on 1/5/2018.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maask on 12/24/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {



    public SectionsPagerAdapter(FragmentManager fm) {

        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                Home home = new Home();
                return home;
            case 1:
                MoneyIn moneyIn = new MoneyIn();
                return moneyIn;
            case 2:
                MoneyOut moneyOut = new MoneyOut();
                return moneyOut;
            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        return 3;
    }


}