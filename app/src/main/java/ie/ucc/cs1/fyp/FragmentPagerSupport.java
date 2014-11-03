package ie.ucc.cs1.fyp;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Adapter.TabsAdaptor;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public class FragmentPagerSupport extends FragmentActivity {

    private static String LOGTAG = "__FragmentPagerSupport";

    @InjectView(R.id.view_pager)
    protected ViewPager   mViewPager;
    protected TabsAdaptor mTabsAdapter;

    //----FragmentActivity Methods
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.view_pager);
        mViewPager.setAdapter(mTabsAdapter);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        mTabsAdapter = new TabsAdaptor(this, getSupportFragmentManager(), getActionBar(), mViewPager  );
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section1)), SensorFragment.class, null);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }






}
