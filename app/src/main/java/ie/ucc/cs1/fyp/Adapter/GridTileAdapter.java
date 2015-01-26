package ie.ucc.cs1.fyp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.SensorOutput;
import ie.ucc.cs1.fyp.R;

/**
 * Created by kpmmmurphy on 01/11/14.
 */
public class GridTileAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SensorOutput> sensorOutputs;

    public GridTileAdapter(Context context, ArrayList<SensorOutput> sensorOutputs){
        mContext = context;
        this.sensorOutputs = sensorOutputs;
    }

    @Override
    public int getCount() {
        return sensorOutputs.size();
    }

    @Override
    public SensorOutput getItem(int i) {
        return sensorOutputs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    int fade = 10;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            // inflate the layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.name.setText(String.valueOf(getItem(i).getName()).replace("_", " "));
        holder.outputLevel.setText(String.valueOf(getItem(i).getValue()));
        holder.measurement.setText(String.valueOf(getItem(i).getMeasurement()));
        return view;
    }

    public class Holder{
        @InjectView(R.id.tv_sensor_output_level)
        TextView outputLevel;
        @InjectView(R.id.tv_sensor_measurement)
        TextView measurement;
        @InjectView(R.id.tv_sensor_name)
        TextView name;
        @InjectView(R.id.tv_sensor_max_value)
        TextView maxValue;
        @InjectView(R.id.tv_sensor_max_value)
        TextView minValue;


        public Holder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void setSensorOutputs(ArrayList<SensorOutput> sensorOutputs) {
        this.sensorOutputs = sensorOutputs;
    }
}
