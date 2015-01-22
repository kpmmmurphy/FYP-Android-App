package ie.ucc.cs1.fyp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

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

    @InjectView(R.id.radio_group_current_hour)
    RadioGroup rgCurrentHour;
    @InjectView(R.id.radio_group_agg_hour)
    RadioGroup rgAggHour;
    @InjectView(R.id.radio_group_agg_day)
    RadioGroup rgAggDay;


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

        rgAggHour.setOnCheckedChangeListener(onCheckedChangeListenerAggHour);

        API.getInstance(getActivity()).requestCurrentHourSensorValues(currentHourSuccessListener, sensorValuesErrorListener);
        API.getInstance(getActivity()).requestAggSensorValuesPerHour(sensorValuesAggPerHourSuccessListener, sensorValuesErrorListener);
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
                /*Log.e(LOGTAG, String.valueOf(response.getSensor_values_list().size()));
                ArrayList<Entry> currentHourData = new ArrayList<Entry>();
                ArrayList<String> xVals = new ArrayList<String>();
                int count = 0;
                for(CurrentSensorValuesFromServer values : response.getSensor_values_list()){
                    Entry entry = new Entry(Float.valueOf(values.getAvg_carbon_monoxide()), ++count);
                    currentHourData.add(entry);
                    xVals.add(getHourFromDateAndTime(values.getDate_and_time()));
                }

                LineDataSet set1 = new LineDataSet(currentHourData, "Temp");
                ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
                lineDataSets.add(set1);

                LineData lineData = new LineData(xVals, lineDataSets);
                lcAggHour.setData(lineData);*/
                populateLineChart(lcAggHour, response.getSensor_values_list(), Constants.SENSOR_NAME_MQ2);
            }
        }
    };

    private void populateLineChart(LineChart lineChart,  ArrayList<CurrentSensorValuesFromServer> sensorValues, String sensor){

        if(sensorValues.size() > 0){
            ArrayList<String> xVals  = new ArrayList<String>();
            ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
            ArrayList<Entry> minData = new ArrayList<Entry>();
            ArrayList<Entry> maxData = new ArrayList<Entry>();
            ArrayList<Entry> avgData = new ArrayList<Entry>();

            int count = 0;
            Entry entry1 = null, entry2 = null, entry3 = null;
            for(CurrentSensorValuesFromServer values : sensorValues){
                if(sensor.equals(Constants.SENSOR_NAME_THERMISTOR)){
                    entry1 = new Entry(Float.valueOf(values.getMin_temperature()), count);
                    entry2 = new Entry(Float.valueOf(values.getMax_temperature()), count);
                    entry3 = new Entry(Float.valueOf(values.getAvg_temperature()), count);
                }else if(sensor.equals(Constants.SENSOR_NAME_MQ7)){
                    entry1 = new Entry(Float.valueOf(values.getMin_carbon_monoxide()), count);
                    entry2 = new Entry(Float.valueOf(values.getMax_carbon_monoxide()), count);
                    entry3 = new Entry(Float.valueOf(values.getAvg_carbon_monoxide()), count);
                }else if(sensor.equals(Constants.SENSOR_NAME_MQ2)){
                    entry1 = new Entry(Float.valueOf(values.getMin_flammable_gas()), count);
                    entry2 = new Entry(Float.valueOf(values.getMax_flammable_gas()), count);
                    entry3 = new Entry(Float.valueOf(values.getAvg_flammable_gas()), count);
                }
                ++count;

                minData.add(entry1);
                maxData.add(entry2);
                avgData.add(entry3);
                xVals.add(getHourFromDateAndTime(values.getDate_and_time()));
            }
            LineDataSet set1 = new LineDataSet(minData, "Min " + sensor);
            LineDataSet set2 = new LineDataSet(maxData, "Max" +  sensor);
            LineDataSet set3 = new LineDataSet(avgData, "Avg" +  sensor);

            lineDataSets.add(set1);
            lineDataSets.add(set2);
            lineDataSets.add(set3);

            lineChart.setData(new LineData(xVals, lineDataSets));
        }

    }

    private Response.ErrorListener sensorValuesErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
        }
    };

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListenerAggHour = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch(i) {
                case R.id.radio_agg_hour_temp:
                    Log.e(LOGTAG, "TEMP");
                    break;
                case R.id.radio_agg_hour_flam:
                    Log.e(LOGTAG, "Flam");
                    break;
                case R.id.radio_agg_hour_co:
                    Log.e(LOGTAG, "CO");
                    break;
                case R.id.radio_agg_hour_motion:
                    Log.e(LOGTAG, "Motion");
                    break;
            }
        }
    };

    private String getHourFromDateAndTime(String date_and_time){
        return date_and_time.split(" ")[1];
    }

}
