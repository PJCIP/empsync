package com.srinidhi.lgb;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;




public class Adapter extends FragmentStatePagerAdapter {
    public Adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int i){
        switch (i)
        {
            case 0:

                return new BlankFragment();
            case 1:
                return new BlankFragment3();
            default:
                return null;

        }
    }
    public int getCount(){
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Profile Detail";
            case 1:
                return "Asset Detail";

            default:
                return "";
        }
    }
}
