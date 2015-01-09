package ie.ucc.cs1.fyp;

import android.app.ActionBar;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Adapter.TabsAdaptor;
import ie.ucc.cs1.fyp.Socket.Session;
import ie.ucc.cs1.fyp.Socket.SocketManager;
import ie.ucc.cs1.fyp.WifiDirect.WifiDirectBroadcastReceiver;


public class MainActivity extends FragmentActivity {

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static String LOGTAG = "__MainActivity";

    @InjectView(R.id.view_pager)
    protected ViewPager mViewPager;
    protected TabsAdaptor mTabsAdapter;

    //Wifi Direct Objects
    protected WifiP2pManager              mManager;
    protected WifiP2pManager.Channel      mChannel;
    protected WifiDirectBroadcastReceiver mWifiDirectReceiver;
    protected IntentFilter                mIntentFilter;

    //SocketManager Objects
    private SocketManager socketManager;
    private Session session;

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

        session = Session.getInstance(getApplicationContext());
        socketManager = new SocketManager(getApplicationContext());
        socketManager.startConnectionToPi(session);

        //Setup Wifi Direct
        //mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        //mChannel = mManager.initialize(this, getMainLooper(), null);

        //mIntentFilter = WifiDirector.createP2PIntentFilter();

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mWifiDirectReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
        //registerReceiver(mWifiDirectReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mWifiDirectReceiver);
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
                //WifiDirector.detectPeers(mManager, mChannel);
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

}
