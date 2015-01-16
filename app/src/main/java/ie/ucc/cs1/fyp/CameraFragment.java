package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.CameraResponse;
import ie.ucc.cs1.fyp.Network.API;

/**
 * Created by kpmmmurphy on 04/11/14.
 */
public class CameraFragment extends Fragment{

    private static final String LOGTAG = "__CAMERA_FRAGMENT";

    private static final String CAMERA_URL = Constants.API_URL + Constants.API_CAMERA;
    private ArrayList<String> imgList;

    @InjectView(R.id.iv_current_image)
    NetworkImageView currentImage;
    @InjectView(R.id.sv_recent_images)
    LinearLayout recentImages;
    @InjectView(R.id.tv_image_time_and_date)
    TextView imageTimeAndDate;
    @InjectView(R.id.camera_fragment_error_layout)
    LinearLayout errorLayout;
    @InjectView(R.id.camera_fragment_error_text)
    TextView errorText;

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
            imgList = response.getImages();
            if(imgList != null){
                imgList.remove(".");
                imgList.remove("..");
                if(imgList.size() > 0){
                    errorLayout.setVisibility(View.GONE);
                    Collections.reverse(imgList);

                    for(final String imagePath : imgList){
                        NetworkImageView nIV = new NetworkImageView(getActivity());
                        nIV.setLayoutParams(new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT));
                        nIV.setPadding(5, 5, 5, 5);
                        API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, nIV);
                        if(response.getImages().indexOf(imagePath) == 0){
                            API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, currentImage);
                            imageTimeAndDate.setText(imagePath.substring(0, imagePath.length() - 4));
                        }

                        nIV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String imgName = imgList.get(recentImages.indexOfChild(view));
                                String urlOfImg = CAMERA_URL + imgName;
                                API.getInstance(getActivity()).requestImage(urlOfImg, currentImage);
                                imageTimeAndDate.setText(imgName.substring(0, imgName.length() - 4));
                            }
                        });

                        recentImages.addView(nIV);
                    }
                }else{
                    errorLayout.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.camera_no_images_to_display);
                }
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText(R.string.camera_unable_to_display_images);
        }
    };
}


