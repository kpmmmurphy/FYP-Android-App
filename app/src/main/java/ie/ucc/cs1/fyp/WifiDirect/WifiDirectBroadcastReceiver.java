package ie.ucc.cs1.fyp.WifiDirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 11/11/14.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private static String LOGTAG = "__WifiDirectBroadcastReciever";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private Activity mActivity;

    private WifiDirectPeerListener mPeerListListener;

    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       Activity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        mPeerListListener = new WifiDirectPeerListener();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.methodDebug(LOGTAG);
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled, update UI
            } else {
                // Wi-Fi P2P is not enabled, update UI
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, mPeerListListener);
            }

            /*mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Utils.methodDebug(LOGTAG);


                }

                @Override
                public void onFailure(int i) {
                    Utils.methodDebug(LOGTAG);
                }
            });*/

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            if(mManager != null){
                NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                if (networkInfo.isConnected()) {
                    //Connect to other device, now find group owner IP
                    //Pass it our channel listener
                    //TODO - Create a Fragment/Activity that handles all found device details
                    mManager.requestConnectionInfo(mChannel, null);
                } else {
                    // It's a disconnect
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }


}
