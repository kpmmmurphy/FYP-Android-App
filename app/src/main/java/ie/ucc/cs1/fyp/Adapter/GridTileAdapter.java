package ie.ucc.cs1.fyp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Model.Sensor;
import ie.ucc.cs1.fyp.Model.SensorOutput;
import ie.ucc.cs1.fyp.Model.Session;
import ie.ucc.cs1.fyp.R;
import ie.ucc.cs1.fyp.Utils;

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

        if(getItem(i).getName().equalsIgnoreCase(Constants.SENSOR_NAME_MOTION)){
            if(getItem(i).getMaxValue() != null)
                holder.maxValue.setText(getItem(i).getMaxValue() + "%");

            Session session = Session.getInstance(mContext);
            //Check if in lockdown mode
            if( session != null &&
                    session.getConfig() != null &&
                    session.getConfig().getAlertManager() != null &&
                    session.getConfig().getAlertManager().isLockdown_on()){
                holder.sensorAlertIcn.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Shake).duration(400).playOn(holder.sensorAlertIcn);
            }else{
                holder.sensorAlertIcn.setVisibility(View.GONE);
            }

        }else{
            if(getItem(i).getMaxValue() != null)
                holder.maxValue.setText("Max " + String.valueOf(getItem(i).getMaxValue()));

            if(getItem(i).getMinValue() != null)
                holder.minValue.setText("Min " + String.valueOf(getItem(i).getMinValue()));
        }

        if(Session.getInstance(mContext) != null
                && Session.getInstance(mContext).getConfig() != null
                && Session.getInstance(mContext).getConfig().getSensors() != null){

            for(Sensor sensor : Session.getInstance(mContext).getConfig().getSensors()){
                if(sensor.getName().equalsIgnoreCase(getItem(i).getName())){
                    if(sensor.getAlert_threshold() <= getItem(i).getValue()){
                        if (!getItem(i).getName().equalsIgnoreCase(Constants.SENSOR_NAME_MOTION)){
                            //Don't show alert icon for motion detection, only when in lockdown mode
                            holder.sensorAlertIcn.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.Shake).duration(400).playOn(holder.sensorAlertIcn);
                        }
                    }else{
                        holder.sensorAlertIcn.setVisibility(View.GONE);
                    }
                }
            }

        }else{
            holder.sensorAlertIcn.setVisibility(View.GONE);
        }

        view.setOnClickListener(onSensorTileClick);
        view.setOnLongClickListener(onSensorTileLongClick);

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
        @InjectView(R.id.tv_sensor_min_value)
        TextView minValue;
        @InjectView(R.id.im_sensor_alert)
        ImageView sensorAlertIcn;

        public Holder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private View.OnClickListener onSensorTileClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView tvTitle = (TextView)(view.findViewById(R.id.tv_sensor_name));
            TextView tvValue = (TextView)(view.findViewById(R.id.tv_sensor_output_level));

            String sensorName  = tvTitle.getText().toString();
            String sensorValue = tvValue.getText().toString();

            Utils.createDialog(mContext, sensorName, sensorValue);
        }
    };

    private View.OnLongClickListener onSensorTileLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            TextView tvTitle = (TextView)(view.findViewById(R.id.tv_sensor_name));
            TextView tvValue = (TextView)(view.findViewById(R.id.tv_sensor_output_level));

            String sensorName  = tvTitle.getText().toString();

            Utils.sendDummyPN(mContext, sensorName, String.valueOf(100));
            return false;
        }
    };

    public void setSensorOutputs(ArrayList<SensorOutput> sensorOutputs) {
        this.sensorOutputs = sensorOutputs;
    }
}
