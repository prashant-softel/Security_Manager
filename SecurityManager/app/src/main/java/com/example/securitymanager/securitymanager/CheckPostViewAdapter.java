package com.example.securitymanager.securitymanager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

public class CheckPostViewAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    public CheckPostViewAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               upcomming upcommingFregment = new upcomming();
               return upcommingFregment;
            case 1:
                ongoing ongoingFragment = new ongoing();
                return ongoingFragment;
            case 2:
              //  MovieFragment movieFragment = new MovieFragment();
               // return movieFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}