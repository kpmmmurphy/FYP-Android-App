package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Adapter.GridTileAdapter;
import ie.ucc.cs1.fyp.Model.SensorOutput;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public class SensorFragment extends Fragment {

    private static String LOGTAG = "SENSOR_FRAGMENT";

    @InjectView(R.id.gv_sensor)
    GridView mGridView;
    private GridTileAdapter gridTileAdapter;

    private ArrayList<SensorOutput> sensorOutputs;

    public SensorFragment() {
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.methodDebug(LOGTAG);
        mGridView.setAdapter(new GridTileAdapter(getActivity().getApplicationContext(), Utils.randomSensorOutput()));
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

    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.methodDebug(LOGTAG);

    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
    }
}
