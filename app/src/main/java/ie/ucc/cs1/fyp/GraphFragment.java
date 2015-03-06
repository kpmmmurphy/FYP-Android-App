package ie.ucc.cs1.fyp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.CurrentSensorValuesFromServer;
import ie.ucc.cs1.fyp.Model.Packet;
import ie.ucc.cs1.fyp.Model.SensorValuesHolder;
import ie.ucc.cs1.fyp.Model.Session;
import ie.ucc.cs1.fyp.Network.API;
import ie.ucc.cs1.fyp.Socket.SocketManager;

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

    ArrayList<CurrentSensorValuesFromServer> currentHourValues;
    ArrayList<CurrentSensorValuesFromServer> currentDayValues;
    ArrayList<CurrentSensorValuesFromServer> dayValues;


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

        LineChart[] charts = new LineChart[]{lcCurrentHour, lcAggHour, lcAggDay};
        for(LineChart chart : charts){
            chart.setHighlightEnabled(false);
            XLabels xl = chart.getXLabels();
            YLabels yl = chart.getYLabels();
            xl.setTypeface(Typeface.SANS_SERIF);
            yl.setTypeface(Typeface.SANS_SERIF);
            //TODO Style labels here and description
            chart.setDescription("");
            chart.setNoDataTextDescription("");
        }

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        currentHourValues = new ArrayList<CurrentSensorValuesFromServer>();
        currentDayValues  = new ArrayList<CurrentSensorValuesFromServer>();
        dayValues         = new ArrayList<CurrentSensorValuesFromServer>();

        if(Session.getInstance(getActivity()).isConnectedToPi()){
            SocketManager.getInstance(getActivity()).sendPacketToPi(Utils.toJson(new Packet(Constants.JSON_VALUE_WIFI_DIRECT_GET_GRAPH_DATA , null)));
            if(Session.getInstance(getActivity()).getGraphData().containsKey(Constants.GRAPH_DATA_CUR_HOUR)){
                currentHourValues.addAll(Session.getInstance(getActivity()).getGraphData().get(Constants.GRAPH_DATA_CUR_HOUR));
                onCheckedChangeListenerAggHour.onCheckedChanged(rgCurrentHour,R.id.radio_current_hour_temp);
            }
            if(Session.getInstance(getActivity()).getGraphData().containsKey(Constants.GRAPH_DATA_CUR_DAY_AGG_HOUR)){
                currentDayValues.addAll(Session.getInstance(getActivity()).getGraphData().get(Constants.GRAPH_DATA_CUR_DAY_AGG_HOUR));
                onCheckedChangeListenerAggHour.onCheckedChanged(rgAggHour,R.id.radio_agg_hour_temp);

            }
            if(Session.getInstance(getActivity()).getGraphData().containsKey(Constants.GRAPH_DATA_AGG_DAY)){
                dayValues.addAll(Session.getInstance(getActivity()).getGraphData().get(Constants.GRAPH_DATA_AGG_DAY));
                onCheckedChangeListenerAggHour.onCheckedChanged(rgAggDay,R.id.radio_agg_day_temp);
            }
        }else{
            API.getInstance(getActivity()).requestCurrentHourSensorValues(currentHourSuccessListener, sensorValuesErrorListener);
            API.getInstance(getActivity()).requestAggSensorValuesPerHour(sensorValuesAggPerHourSuccessListener, sensorValuesErrorListener);
            API.getInstance(getActivity()).requestAggSensorValuesPerDay(sensorValuesAggPerDaySuccessListener, sensorValuesErrorListener);
        }

        rgCurrentHour.setOnCheckedChangeListener(onCheckedChangeListenerAggHour);
        rgAggHour.setOnCheckedChangeListener(onCheckedChangeListenerAggHour);
        rgAggDay.setOnCheckedChangeListener(onCheckedChangeListenerAggHour);
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Response.Listener<SensorValuesHolder> currentHourSuccessListener = new Response.Listener<SensorValuesHolder>() {
        @Override
        public void onResponse(SensorValuesHolder response) {
            Utils.methodDebug(LOGTAG);
            if(response.getSensor_values_list() == null || response.getSensor_values_list().isEmpty()){

            }else{
                currentHourValues = response.getSensor_values_list();
                onCheckedChangeListenerAggHour.onCheckedChanged(rgCurrentHour,R.id.radio_current_hour_temp);
            }
        }
    };

    private Response.Listener<SensorValuesHolder> sensorValuesAggPerHourSuccessListener = new Response.Listener<SensorValuesHolder>() {
        @Override
        public void onResponse(SensorValuesHolder response) {
            Utils.methodDebug(LOGTAG);
            if(response.getSensor_values_list() == null || response.getSensor_values_list().isEmpty()){

            }else{
                currentDayValues = response.getSensor_values_list();
                onCheckedChangeListenerAggHour.onCheckedChanged(rgAggHour,R.id.radio_agg_hour_temp);
            }
        }
    };

    private Response.Listener<SensorValuesHolder> sensorValuesAggPerDaySuccessListener = new Response.Listener<SensorValuesHolder>() {
        @Override
        public void onResponse(SensorValuesHolder response) {
            Utils.methodDebug(LOGTAG);
            if(response.getSensor_values_list() == null || response.getSensor_values_list().isEmpty()){

            }else{
                 dayValues = response.getSensor_values_list();
                 onCheckedChangeListenerAggHour.onCheckedChanged(rgAggDay,R.id.radio_agg_day_temp);
            }
        }
    };

    private void populateLineChart(LineChart lineChart,  ArrayList<CurrentSensorValuesFromServer> sensorValues, String sensor, boolean isAgg, int dateTimeType){

        if(sensorValues.size() > 0){
            ArrayList<String> xVals  = new ArrayList<String>();
            ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
            ArrayList<Entry> minData = new ArrayList<Entry>();
            ArrayList<Entry> maxData = new ArrayList<Entry>();
            ArrayList<Entry> avgData = new ArrayList<Entry>();
            ArrayList<Entry> normalData = new ArrayList<Entry>();

            int count = 0;
            Entry entry1 = null, entry2 = null, entry3 = null, entry4 = null;
            for(CurrentSensorValuesFromServer values : sensorValues){
                if(isAgg){
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
                    }else if(sensor.equals(Constants.SENSOR_NAME_MOTION)){
                        entry4 = new Entry(Float.valueOf(values.getMotion()), count);
                    }

                    if(entry1 != null){
                        minData.add(entry1);
                    }
                    if(entry2 != null){
                        maxData.add(entry2);
                    }
                    if(entry3 != null){
                        avgData.add(entry3);
                    }
                    if(entry4 != null){
                        normalData.add(entry4);
                    }
                }else{
                    if(sensor.equals(Constants.SENSOR_NAME_THERMISTOR)){
                        entry1 = new Entry(Float.valueOf(values.getTemperature()), count);
                    }else if(sensor.equals(Constants.SENSOR_NAME_MQ7)){
                        entry1 = new Entry(Float.valueOf(values.getCarbon_monoxide()), count);
                    }else if(sensor.equals(Constants.SENSOR_NAME_MQ2)){
                        entry1 = new Entry(Float.valueOf(values.getFlammable_gas()), count);
                    }else if(sensor.equals(Constants.SENSOR_NAME_MOTION)){
                        entry1 = new Entry(Float.valueOf(values.getMotion()), count);
                    }

                    normalData.add(entry1);

                }

                String xValString = "";
                if(dateTimeType == Constants.GRAPH_DATE_TIME_TYPE_HOUR_WITH_SECOND){
                    xValString = getHourWithSecFromDateAndTime(values.getDate_and_time());
                }else if(dateTimeType == Constants.GRAPH_DATE_TIME_TYPE_HOUR){
                    xValString = getHourFromDateAndTime(values.getDate_and_time());
                }else if(dateTimeType == Constants.GRAPH_DATE_TIME_TYPE_DAY){
                    xValString = getDayFromDateAndTime(values.getDate_and_time());
                }
                xVals.add(xValString);
                ++count;
            }

            String measurement = Constants.SENSOR_MEASUREMENT_PPM;
            if(sensor.equalsIgnoreCase(Constants.SENSOR_NAME_THERMISTOR)){
                measurement = Constants.SENSOR_MEASUREMENT_CELCIUS;
            }else if (sensor.equalsIgnoreCase(Constants.SENSOR_NAME_MOTION)){
                measurement = Constants.SENSOR_MEASUREMENT_PERCENTAGE;
            }

            if(minData.size() > 0) {
                LineDataSet set1 = new LineDataSet(minData, String.format("Min of  %s (%s)", sensor.replace("_", " "), measurement.toUpperCase()));
                set1.setColor(getResources().getColor(R.color.graph_min));
                set1.setCircleColor(getResources().getColor(R.color.graph_min));
                lineDataSets.add(set1);
            }
            if(maxData.size() > 0){
                LineDataSet set2 = new LineDataSet(maxData, String.format("Max  %s (%s)", sensor.replace("_", " "), measurement.toUpperCase()));
                set2.setColor(getResources().getColor(R.color.graph_max));
                set2.setCircleColor(getResources().getColor(R.color.graph_max));
                lineDataSets.add(set2);
            }
            if(avgData.size() > 0){
                LineDataSet set3 = new LineDataSet(avgData, String.format("Avg  %s (%s)", sensor.replace("_", " "), measurement.toUpperCase()));
                lineDataSets.add(set3);
            }

            if(normalData.size() > 0){
                LineDataSet set4 = new LineDataSet(normalData,String.format("Level of  %s (%s)", sensor.replace("_", " "), measurement.toUpperCase()));
                lineDataSets.add(set4);
            }

            lineChart.setData(new LineData(xVals, lineDataSets));
            lineChart.animateXY(1000, 1000);
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
                //CURRENT HOUR
                case R.id.radio_current_hour_temp:
                    populateLineChart(lcCurrentHour, currentHourValues, Constants.SENSOR_NAME_THERMISTOR,false, Constants.GRAPH_DATE_TIME_TYPE_HOUR_WITH_SECOND);
                    break;
                case R.id.radio_current_hour_flam:
                    populateLineChart(lcCurrentHour, currentHourValues, Constants.SENSOR_NAME_MQ2, false, Constants.GRAPH_DATE_TIME_TYPE_HOUR_WITH_SECOND);
                    break;
                case R.id.radio_current_hour_co:
                    populateLineChart(lcCurrentHour, currentHourValues, Constants.SENSOR_NAME_MQ7, false,  Constants.GRAPH_DATE_TIME_TYPE_HOUR_WITH_SECOND);
                    break;
                case R.id.radio_current_hour_motion:
                    populateLineChart(lcCurrentHour, currentHourValues, Constants.SENSOR_NAME_MOTION, false, Constants.GRAPH_DATE_TIME_TYPE_HOUR_WITH_SECOND);
                    break;
                //AGG HOUR
                case R.id.radio_agg_hour_temp:
                    populateLineChart(lcAggHour, currentDayValues, Constants.SENSOR_NAME_THERMISTOR, true, Constants.GRAPH_DATE_TIME_TYPE_HOUR);
                    break;
                case R.id.radio_agg_hour_flam:
                    populateLineChart(lcAggHour, currentDayValues, Constants.SENSOR_NAME_MQ2, true, Constants.GRAPH_DATE_TIME_TYPE_HOUR);
                    break;
                case R.id.radio_agg_hour_co:
                    populateLineChart(lcAggHour, currentDayValues, Constants.SENSOR_NAME_MQ7, true, Constants.GRAPH_DATE_TIME_TYPE_HOUR);
                    break;
                case R.id.radio_agg_hour_motion:
                    populateLineChart(lcAggHour, currentDayValues, Constants.SENSOR_NAME_MOTION,true, Constants.GRAPH_DATE_TIME_TYPE_HOUR);
                    break;
                //AGG DAY
                case R.id.radio_agg_day_temp:
                    populateLineChart(lcAggDay, dayValues, Constants.SENSOR_NAME_THERMISTOR, true, Constants.GRAPH_DATE_TIME_TYPE_DAY);
                    break;
                case R.id.radio_agg_day_flam:
                    populateLineChart(lcAggDay, dayValues, Constants.SENSOR_NAME_MQ2, true, Constants.GRAPH_DATE_TIME_TYPE_DAY);
                    break;
                case R.id.radio_agg_day_co:
                    populateLineChart(lcAggDay, dayValues, Constants.SENSOR_NAME_MQ7, true, Constants.GRAPH_DATE_TIME_TYPE_DAY);
                    break;
                case R.id.radio_agg_day_motion:
                    populateLineChart(lcAggDay, dayValues, Constants.SENSOR_NAME_MOTION,true, Constants.GRAPH_DATE_TIME_TYPE_DAY);
            }
        }
    };

    private String getHourWithSecFromDateAndTime(String date_and_time){
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            date_and_time = date_and_time.replace("T", " ");
        }
        return date_and_time.split(" ")[1];
    }

    private String getHourFromDateAndTime(String date_and_time){
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            date_and_time = date_and_time.replace("T", " ");
        }
        return date_and_time.split(" ")[1].substring(0, 5);
    }

    private String getDayFromDateAndTime(String date_and_time){
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            date_and_time = date_and_time.replace("T", " ");
        }
        return date_and_time.split(" ")[0];
    }

}
