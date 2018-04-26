package braille.kofefe.app.modules_.starting_.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import braille.kofefe.app.modules_.starting_.fragment.FragmentFirstScreen;
import braille.kofefe.app.modules_.starting_.fragment.FragmentSecondScreen;
import braille.kofefe.app.modules_.starting_.fragment.FragmentThirdScreen;

/**
 * Created by Snow-Dell-05 on 11/6/2017.
 */

public class AdapterViewPagerStarting extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public AdapterViewPagerStarting(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentFirstScreen();
            case 1:
                return new FragmentSecondScreen();
            case 2:
                return new FragmentThirdScreen();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

