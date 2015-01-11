package ie.ucc.cs1.fyp;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Adapter.TabsAdaptor;
import ie.ucc.cs1.fyp.Socket.Session;
import ie.ucc.cs1.fyp.Socket.SocketManager;


public class MainActivity extends FragmentActivity {

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static String LOGTAG = "__MainActivity";

    @InjectView(R.id.view_pager)
    protected ViewPager mViewPager;
    protected TabsAdaptor mTabsAdapter;

    private ConnectedToPiReceiver connectedToPiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mViewPager.setId(R.id.view_pager);
        mViewPager.setAdapter(mTabsAdapter);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        mTabsAdapter = new TabsAdaptor(this, getSupportFragmentManager(), getActionBar(), mViewPager);
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section1)), SensorFragment.class, null);
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section2)), CameraFragment.class, null);
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section3)), ControlFragment.class, null);

        connectedToPiReceiver = new ConnectedToPiReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(connectedToPiReceiver, new IntentFilter(Constants.INTENT_CONNECTED_TO_PI));

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connectedToPiReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_bar_menu_wifi_direct:
                hideFragments();
                SocketManager.getInstance(getApplicationContext()).startConnectionToPi(Session.getInstance(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public class ConnectedToPiReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BuildConfig.DEBUG){
                Log.d(LOGTAG, "Broadcast received");
            }
            boolean isConnected = intent.getBooleanExtra(Constants.SERVICE_PAIRED, false);
            if(isConnected){
                SocketManager.getInstance(getApplicationContext()).startPiDirectThread();
                Toast.makeText(getApplicationContext(), getString(R.string.connected_to_pi_success), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.connected_to_pi_failed), Toast.LENGTH_SHORT).show();
            }
            updateFragments();
        }
    }

    private void hideFragments(){
        mViewPager.removeAllViews();
    }
    private void updateFragments(){
        mViewPager.setAdapter(null);
        mViewPager.setAdapter(mTabsAdapter);
        mTabsAdapter.clearTabInfo();
        mTabsAdapter.updateTab(getActionBar().newTab().setText(getString(R.string.title_section1)), SensorFragment.class, null);
        mTabsAdapter.updateTab(getActionBar().newTab().setText(getString(R.string.title_section2)), CameraFragment.class, null);
        mTabsAdapter.updateTab(getActionBar().newTab().setText(getString(R.string.title_section3)), ControlFragment.class, null);
    }

}
