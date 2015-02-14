package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Adapter.GridTileAdapter;
import ie.ucc.cs1.fyp.Model.APIResponse;
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
        gridTileAdapter = new GridTileAdapter(getActivity(), Utils.randomSensorOutput());
        mGridView.setAdapter(gridTileAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.methodDebug(LOGTAG);

        sensorValueReadThread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {

                    if (!Session.getInstance(getActivity()).isConnectedToPi()) {
                        //Networking Stuff
                        API.getInstance(getActivity()).requestSensorValues(successListener, errorListener);
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshValues();
                            }
                        });
                    }

                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        sensorValueReadThread.start();
    }


    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
        sensorValueReadThread.interrupt();
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorValueReadThread = null;
    }

    //--API Listeners
    private Response.Listener<APIResponse> successListener = new Response.Listener<APIResponse>() {
        @Override
        public void onResponse(APIResponse response) {
            Utils.methodDebug(LOGTAG);
            SensorValueManager.getInstance().setCurrentSensorValuesFromServer(response.sensor_values);
            refreshValues();
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
            if (BuildConfig.DEBUG) {
                error.printStackTrace();
            }
        }
    };

    private void refreshValues() {
        gridTileAdapter.setSensorOutputs(SensorValueManager.getInstance().getCurrentSensorOutputsList());
        gridTileAdapter.notifyDataSetChanged();
        if (SensorValueManager.getInstance().getCurrentSensorValues() != null){
            YoYo.with(Techniques.FadeOut).duration(500).playOn(tvLastUpdated);
            tvLastUpdated.setText(getString(R.string.sensor_last_updated) + " " + SensorValueManager.getInstance().getCurrentSensorValues().getData_and_time());
            YoYo.with(Techniques.FadeIn).duration(500).playOn(tvLastUpdated);
        }
    }
}
