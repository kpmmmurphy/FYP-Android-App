package ie.ucc.cs1.fyp.WifiDirect;

import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;

import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 11/11/14.
 */
public class WifiDirector  {

    public static final String LOGTAG = "__WifiDirector";

    public static IntentFilter createP2PIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        return intentFilter;
    }

    public static void detectPeers(WifiP2pManager manager, WifiP2pManager.Channel channel) {

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Utils.methodDebug(LOGTAG);
            }

            @Override
            public void onFailure(int reasonCode) {
                Utils.methodDebug(LOGTAG);
            }
        });
    }
}
