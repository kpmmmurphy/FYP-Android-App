package ie.ucc.cs1.fyp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.SensorValuesHolder;
import ie.ucc.cs1.fyp.Network.API;

public class GraphFragment extends Fragment {

    private static final String LOGTAG = GraphFragment.class.getSimpleName();

    @InjectView(R.id.graphing_line_chart)
    LineChart lineChart;

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
        API.getInstance(getActivity()).requestCurrentHourSensorValues(currentHourSuccessListener, currentHourErrorListener);
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
                ArrayList<Entry> currentHourData = new ArrayList<Entry>();
                Entry entry1 = new Entry(Float.valueOf(response.getSensor_values_list().get(0).getTemperature()), 1);
                currentHourData.add(entry1);

                LineDataSet set1 = new LineDataSet(currentHourData, "Temp");
                ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
                lineDataSets.add(set1);

                ArrayList<String> xVals = new ArrayList<String>();
                xVals.add("Hello");

                LineData lineData = new LineData(xVals, lineDataSets);
                lineChart.setData(lineData);
            }
        }
    };

    private Response.ErrorListener currentHourErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
        }
    };

}
