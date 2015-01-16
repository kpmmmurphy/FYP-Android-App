package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.CameraResponse;
import ie.ucc.cs1.fyp.Network.API;

/**
 * Created by kpmmmurphy on 04/11/14.
 */
public class CameraFragment extends Fragment{

    private static String LOGTAG = "__CAMERA_FRAGMENT";

    @InjectView(R.id.iv_current_image)
    NetworkImageView currentImage;
    @InjectView(R.id.sv_recent_images)
    LinearLayout recentImages;

    public CameraFragment() {
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
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
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
        API.getInstance(getActivity()).requestImage("http://www.hollywoodreporter.com/sites/default/files/2014/07/adventure_time.jpg", currentImage);
        API.getInstance(getActivity()).requestListImages(successListener, errorListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
    }

    private Response.Listener<CameraResponse> successListener = new Response.Listener<CameraResponse>() {
        @Override
        public void onResponse(CameraResponse response) {
            Utils.methodDebug(LOGTAG);
            if(response.getImages() != null){
                response.getImages().remove(".");
                response.getImages().remove("..");
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
        }
    };
}


