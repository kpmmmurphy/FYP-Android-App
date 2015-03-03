package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import ie.ucc.cs1.fyp.Model.CurrentSensorValues;
import ie.ucc.cs1.fyp.Model.PeripheralSensorValues;
import ie.ucc.cs1.fyp.Model.Session;
import ie.ucc.cs1.fyp.Network.API;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public class SensorFragment extends Fragment {

    private static String LOGTAG = "__SensorFragment";

    @InjectView(R.id.ll_device_selector)
    LinearLayout llDeviceSelector;
    @InjectView(R.id.gv_sensor)
    GridView mGridView;
    @InjectView(R.id.tv_last_updated)
    TextView tvLastUpdated;
    private GridTileAdapter gridTileAdapter;
    private long currentlySelectedDeviceID = 0;

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
        MyApplication.scheduleTask(new Runnable() {
            @Override
            public void run() {
                if (!Session.getInstance(getActivity()).isConnectedToPi()) {
                    //Networking Stuff
                    API.getInstance(getActivity()).requestSensorValues(successListener, errorListener);
                } else {
                    //Direct Stuff
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshValues();
                        }
                    });
                }
            }
        }, 0, 1, LOGTAG);
    }


    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
        MyApplication.unscheduleTask(LOGTAG);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        if(currentlySelectedDeviceID == 0){
            gridTileAdapter.setSensorOutputs(SensorValueManager.getInstance().getCurrentSensorOutputsList());
        }else{
            gridTileAdapter.setSensorOutputs(SensorValueManager.getInstance().getCurrentPeripheralSensorOutputsList(currentlySelectedDeviceID));
        }
        gridTileAdapter.notifyDataSetChanged();
        if (SensorValueManager.getInstance().getCurrentSensorValues() != null) {
            String dataAndTimeText = String.format( "%s : %s ", getString(R.string.sensor_last_updated), SensorValueManager.getInstance().getCurrentSensorValues().getData_and_time());
            if (!tvLastUpdated.getText().equals(dataAndTimeText)) {
                YoYo.with(Techniques.FadeOut).duration(500).playOn(tvLastUpdated);
                tvLastUpdated.setText(dataAndTimeText);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(tvLastUpdated);
            }
        }
        displayAvailableDevices();
    }

    private void displayAvailableDevices(){
        Utils.methodDebug(LOGTAG);
        CurrentSensorValues currentSensorValues = SensorValueManager.getInstance().getCurrentSensorValues();
        if(currentSensorValues != null){
            ArrayList<PeripheralSensorValues> peripheralSensorValues = currentSensorValues.getPeripheral_sensor_values();

            if(peripheralSensorValues != null && !peripheralSensorValues.isEmpty()){
                //Firstly, add the Raspberry Pi
                TextView textView = new TextView(getActivity());
                textView.setText(Session.getInstance(getActivity()).getConfig().getSystemDetailsManager().getName());
                textView.setOnClickListener(selectDeviceClickListener);
                llDeviceSelector.addView(textView);

                int count = 1;
                for(PeripheralSensorValues values : peripheralSensorValues){
                    //Device id should be dynamic
                    textView = new TextView(getActivity());
                    textView.setText("Peripheral " + count++);
                    textView.setOnClickListener(selectDeviceClickListener);
                    llDeviceSelector.addView(textView);
                }
            }
        }


    }

    private View.OnClickListener selectDeviceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Utils.methodDebug(LOGTAG);
            CharSequence text = ((TextView)view).getText();
            //This should be dynamic
            if(Session.getInstance(getActivity()).getConfig().getSystemDetailsManager().getName().equals(text)){
                currentlySelectedDeviceID = 0;
            }else{
                currentlySelectedDeviceID = SensorValueManager.getInstance().getCurrentSensorValues().getPeripheral_sensor_values().get(0).getDevice_id();
            }
        }
    };
}
