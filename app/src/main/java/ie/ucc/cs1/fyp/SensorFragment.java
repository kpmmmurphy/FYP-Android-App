package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Adapter.GridTileAdapter;
import ie.ucc.cs1.fyp.Model.SensorOutput;
import ie.ucc.cs1.fyp.Network.API;
import ie.ucc.cs1.fyp.Socket.Session;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public class SensorFragment extends Fragment {

    private static String LOGTAG = "__SensorFragment";
    private Thread sensorValueReadThread;

    @InjectView(R.id.gv_sensor)
    GridView mGridView;
    @InjectView(R.id.tv_last_updated)
    TextView tvLastUpdated;

    private GridTileAdapter gridTileAdapter;

    private ArrayList<SensorOutput> sensorOutputs;

    public SensorFragment() {
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.methodDebug(LOGTAG);

        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.methodDebug(LOGTAG);
        gridTileAdapter = new GridTileAdapter(getActivity().getApplicationContext(), Utils.randomSensorOutput());
        mGridView.setAdapter(gridTileAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.methodDebug(LOGTAG);
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            sensorValueReadThread = new Thread(){
                @Override
                public void run() {
                    while (!this.isInterrupted()){
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                gridTileAdapter.setSensorOutputs(SensorManager.getInstance().getCurrentSensorOutputsList());
                                gridTileAdapter.notifyDataSetChanged();
                                tvLastUpdated.setText(SensorManager.getInstance().getCurrentSensorValues().getTime_stamp());
                            }
                        });
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            sensorValueReadThread.start();
        }else{
            //Networking Stuff
            API.getInstance(getActivity()).requestSensorValues(null);
            gridTileAdapter.setSensorOutputs(SensorManager.getInstance().getCurrentSensorOutputsList());
            gridTileAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
    }
}
