package ie.ucc.cs1.fyp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.CurrentSensorValuesFromServer;
import ie.ucc.cs1.fyp.Model.SensorValuesHolder;
import ie.ucc.cs1.fyp.Network.API;

public class GraphFragment extends Fragment {

    private static final String LOGTAG = GraphFragment.class.getSimpleName();

    @InjectView(R.id.graphing_line_chart_current_hour)
    LineChart lcCurrentHour;
    @InjectView(R.id.graphing_line_chart_agg_hour)
    LineChart lcAggHour;
    @InjectView(R.id.graphing_line_chart_agg_day)
    LineChart lcAggDay;

    public GraphFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        API.getInstance(getActivity()).requestCurrentHourSensorValues(currentHourSuccessListener, sensorValuesErrorListener);
    }

    @Override
    public void onDetach() {
    }

    private Response.Listener<SensorValuesHolder> currentHourSuccessListener = new Response.Listener<SensorValuesHolder>() {
        @Override
        public void onResponse(SensorValuesHolder response) {
            Utils.methodDebug(LOGTAG);
            if(response.getSensor_values_list() == null || response.getSensor_values_list().isEmpty()){

            }else{
                Log.e(LOGTAG, String.valueOf(response.getSensor_values_list().size()));
                ArrayList<Entry> currentHourData = new ArrayList<Entry>();
                ArrayList<String> xVals = new ArrayList<String>();
                int count = 0;
                for(CurrentSensorValuesFromServer values : response.getSensor_values_list()){
                    Entry entry = new Entry(Float.valueOf(values.getTemperature()), ++count);
                    currentHourData.add(entry);
                    xVals.add(getHourFromDateAndTime(values.getDate_and_time()));
                }

                LineDataSet set1 = new LineDataSet(currentHourData, "Temp");
                ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
                lineDataSets.add(set1);




                LineData lineData = new LineData(xVals, lineDataSets);
                lcCurrentHour.setData(lineData);
            }
        }
    };



    private Response.Listener<SensorValuesHolder> sensorValuesAggPerHourSuccessListener = new Response.Listener<SensorValuesHolder>() {
        @Override
        public void onResponse(SensorValuesHolder response) {
            Utils.methodDebug(LOGTAG);
            if(response.getSensor_values_list() == null || response.getSensor_values_list().isEmpty()){

            }else{
                Log.e(LOGTAG, String.valueOf(response.getSensor_values_list().size()));
                ArrayList<Entry> currentHourData = new ArrayList<Entry>();
                ArrayList<String> xVals = new ArrayList<String>();
                int count = 0;
                for(CurrentSensorValuesFromServer values : response.getSensor_values_list()){
                    Entry entry = new Entry(Float.valueOf(values.getTemperature()), ++count);
                    currentHourData.add(entry);
                    xVals.add(getHourFromDateAndTime(values.getDate_and_time()));
                }

                LineDataSet set1 = new LineDataSet(currentHourData, "Temp");
                ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
                lineDataSets.add(set1);




                LineData lineData = new LineData(xVals, lineDataSets);
                lcAggHour.setData(lineData);
            }
        }
    };

    private Response.ErrorListener sensorValuesErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
        }
    };

    public void onRadioButtonClickedAggHour(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_agg_hour_temp:
                if (checked)
                    Log.e(LOGTAG, "TEMP")
                    break;
            case R.id.radio_agg_hour_flam:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_agg_hour_co:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    private String getHourFromDateAndTime(String date_and_time){
        return date_and_time.split(" ")[1];
    }

}
