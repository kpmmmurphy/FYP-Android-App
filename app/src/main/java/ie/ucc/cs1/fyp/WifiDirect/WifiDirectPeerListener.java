package ie.ucc.cs1.fyp.WifiDirect;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.ucc.cs1.fyp.MyApplication;

/**
 * Created by kpmmmurphy on 28/11/14.
 */
public class WifiDirectPeerListener implements WifiP2pManager.PeerListListener{

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private WifiP2pDevice device;

    //----WifiP2pManager.PeerListListener Method
    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        peers.clear();
        peers.addAll(wifiP2pDeviceList.getDeviceList());
        //TODO - Notify Data set changed
        if (peers.size() == 0) {
            Toast.makeText(MyApplication.getContext(), "Nothing found!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MyApplication.getContext(),"We've found " + peers.size() + " Peers", Toast.LENGTH_LONG).show();
        }
    }
}
