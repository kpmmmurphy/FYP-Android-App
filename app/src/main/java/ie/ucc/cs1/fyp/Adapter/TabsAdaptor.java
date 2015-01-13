package ie.ucc.cs1.fyp.Adapter;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 02/11/14.
 */
public class TabsAdaptor extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener{

    private final String LOGTAG = "__TabsAdapter";

    private final Context mContext;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    private final ConcurrentHashMap<Integer, TabInfo> mTabs = new ConcurrentHashMap<Integer, TabInfo>();

    public TabsAdaptor(Context mContext, FragmentManager fm, ActionBar mActionBar, ViewPager mViewPager) {
        super(fm);
        this.mContext = mContext;
        this.mActionBar = mActionBar;
        this.mViewPager = mViewPager;

        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        Utils.methodDebug(LOGTAG);

        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        mTabs.put(mTabs.size(), info);
        mActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    public void updateTab(ActionBar.Tab tab, Class<?> clss, Bundle args){
        Utils.methodDebug(LOGTAG);
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        mTabs.put(mTabs.size(), info);
        notifyDataSetChanged();
    }

    //Generic - Holds anything it needs to
    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

    //----FragmentPagerAdapter methods
    @Override
    public Fragment getItem(int position) {
        Utils.methodDebug(LOGTAG);
        TabInfo info = mTabs.get(position);
        Log.e(LOGTAG, info.clss.getName());
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    //----ViewPager.OnPageChangeListener methods
    @Override
    public void onPageScrolled(int i, float v, int i2) {
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onPageSelected(int position) {
        Utils.methodDebug(LOGTAG);
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        Utils.methodDebug(LOGTAG);
    }

    //----ActionBar.TabListener methods
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Utils.methodDebug(LOGTAG);
        Object tag = tab.getTag();
        for (int i=0; i< mTabs.size(); i++) {
            if (mTabs.get(i).equals(tag)) {
                mViewPager.setCurrentItem(i);
            }
        }
        //Fragment.instantiate(mContext, SensorFragment.class.getName());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Utils.methodDebug(LOGTAG);
    }

    //----Custom Methods
    public void clearTabInfo(){
        mTabs.clear();
    }


}
