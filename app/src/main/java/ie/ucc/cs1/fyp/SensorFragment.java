package ie.ucc.cs1.fyp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    @InjectView(R.id.gv_device_selector)
    GridView gvDeviceSelector;
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
        displayAvailableDevices();
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
    }

    private void displayAvailableDevices(){
        Utils.methodDebug(LOGTAG);
        ArrayList<String> deviceNames = new ArrayList<String>();
        CurrentSensorValues currentSensorValues = SensorValueManager.getInstance().getCurrentSensorValues();
        if(currentSensorValues != null){
            ArrayList<PeripheralSensorValues> peripheralSensorValues = currentSensorValues.getPeripheral_sensor_values();

            if(peripheralSensorValues != null && !peripheralSensorValues.isEmpty()){
                gvDeviceSelector.setVisibility(View.VISIBLE);
                //Firstly, add the Raspberry Pi
                deviceNames.add(Session.getInstance(getActivity()).getConfig().getSystemDetailsManager().getName());

                int count = 1;
                for(PeripheralSensorValues values : peripheralSensorValues){
                    deviceNames.add("Peripheral " + count++);
                }
                gvDeviceSelector.setAdapter( new DeviceSelectAdaptor(getActivity(), deviceNames));
                gvDeviceSelector.setOnItemClickListener(selectDeviceClickListener);
            }else{
                gvDeviceSelector.setVisibility(View.GONE);
            }
        }else{
            gvDeviceSelector.setVisibility(View.GONE);
        }
    }

    private AdapterView.OnItemClickListener selectDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Utils.methodDebug(LOGTAG);
            CharSequence text = ((TextView)view.findViewById(R.id.tv_device_selector_name)).getText();
            //This should be dynamic
            if(Session.getInstance(getActivity()).getConfig().getSystemDetailsManager().getName().equals(text)){
                currentlySelectedDeviceID = 0;
            }else{
                currentlySelectedDeviceID = SensorValueManager.getInstance().getCurrentSensorValues().getPeripheral_sensor_values().get(0).getDevice_id();
            }
        }
    };

    private class DeviceSelectAdaptor extends BaseAdapter {
        private Context context;
        private final ArrayList<String> textViewValues;

        public DeviceSelectAdaptor(Context context, ArrayList<String> textViewValues) {
            this.context = context;
            this.textViewValues = textViewValues;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Utils.methodDebug(LOGTAG);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view;

            if (convertView == null) {
                view = new View(context);
                // get layout from mobile.xml
                view = inflater.inflate(R.layout.list_item_device_selector, null);
                TextView textView = (TextView) view
                        .findViewById(R.id.tv_device_selector_name);
                textView.setText(textViewValues.get(position));
            } else {
                view = (View) convertView;
            }

            return view;
        }

        @Override
        public int getCount() {
            return textViewValues.size();
        }

        @Override
        public Object getItem(int position) {
            return textViewValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }
}
