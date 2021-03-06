package ie.ucc.cs1.fyp;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.BuildConfig;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Adapter.TabsAdaptor;
import ie.ucc.cs1.fyp.Model.Session;
import ie.ucc.cs1.fyp.PushNotifcation.PNManager;
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
    private PiConfigUpdatedReciever piConfigUpdatedReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //Register for Push Notifcations
        PNManager.getInstance(this).registerForPN();

        //This will setup the server socket
        SocketManager.getInstance(getApplicationContext());

        mViewPager.setId(R.id.view_pager);
        mViewPager.setAdapter(mTabsAdapter);

        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        mTabsAdapter = new TabsAdaptor(this, getSupportFragmentManager(), bar, mViewPager);
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section1)), SensorFragment.class, null);
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section2)), CameraFragment.class, null);
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section4)), GraphFragment.class, null);
        mTabsAdapter.addTab(getActionBar().newTab().setText(getString(R.string.title_section3)), ControlFragment.class, null);

        connectedToPiReceiver   = new ConnectedToPiReceiver();
        piConfigUpdatedReciever = new PiConfigUpdatedReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(connectedToPiReceiver, new IntentFilter(Constants.INTENT_CONNECTED_TO_PI));
        LocalBroadcastManager.getInstance(this).registerReceiver(piConfigUpdatedReciever, new IntentFilter(Constants.INTENT_PI_CONFIG_UPDATED));


        bar.setSelectedNavigationItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check if we're starting from Pending Intent(From a Push Notification)
        if(getIntent().getBooleanExtra(Constants.PN_FROM_PENDING_INTENT, false)){
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("fyp", Context.MODE_PRIVATE);
            Utils.createDialog(this, prefs.getString(Constants.SENSOR_NAME, ""), prefs.getString(Constants.SENSOR_VALUE, ""));
            getIntent().putExtra(Constants.PN_FROM_PENDING_INTENT, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connectedToPiReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(piConfigUpdatedReciever);
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
                SocketManager.getInstance(this).startConnectionToPi(Session.getInstance(this));
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
                SocketManager.getInstance(MainActivity.this).startPiDirectThread();
                Toast.makeText(getApplicationContext(), getString(R.string.connected_to_pi_success), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.connected_to_pi_failed), Toast.LENGTH_SHORT).show();
            }
            updateFragments();
        }
    }

    public class PiConfigUpdatedReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BuildConfig.DEBUG){
                Log.d(LOGTAG, "Broadcast received");
            }
            Toast.makeText(MainActivity.this, getString(R.string.config_upload_success), Toast.LENGTH_LONG).show();
        }
    }

    private void hideFragments(){
        mViewPager.removeAllViews();
    }
    private void updateFragments(){
        mViewPager.setAdapter(null);
        mTabsAdapter.clearTabInfo();
        getActionBar().removeAllTabs();
        mViewPager.setAdapter(mTabsAdapter);
        mTabsAdapter.updateTab(getActionBar().newTab().setText(getString(R.string.title_section1)), SensorFragment.class, null);
        mTabsAdapter.updateTab(getActionBar().newTab().setText(getString(R.string.title_section2)), CameraFragment.class, null);
        mTabsAdapter.updateTab(getActionBar().newTab().setText(getString(R.string.title_section4)), GraphFragment.class, null);
        mTabsAdapter.updateTab(getActionBar().newTab().setText(getString(R.string.title_section3)), ControlFragment.class, null);
    }



}
