package ie.ucc.cs1.fyp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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
    private ArrayList<String> videoList;

    @InjectView(R.id.iv_current_image)
    NetworkImageView currentImage;
    @InjectView(R.id.vv_current_video)
    VideoView currentVideo;
    @InjectView(R.id.sv_recent_images)
    LinearLayout recentImages;
    @InjectView(R.id.tv_image_time_and_date)
    TextView imageTimeAndDate;
    @InjectView(R.id.camera_fragment_error_layout)
    LinearLayout errorLayout;
    @InjectView(R.id.sv_recent_videos)
    LinearLayout recentVideos;
    @InjectView(R.id.camera_fragment_error_text)
    TextView errorText;

    public CameraFragment() {
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.methodDebug(LOGTAG);
        imgList = new ArrayList<String>();
        videoList = new ArrayList<String>();
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
            if(response.getImages() != null){
                filterAssets(response.getImages());
                if(imgList.size() > 0){
                    errorLayout.setVisibility(View.GONE);

                    recentImages.removeAllViews();
                    for(final String imagePath : imgList){
                        NetworkImageView nIV = new NetworkImageView(getActivity());
                        nIV.setLayoutParams(new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT));
                        nIV.setPadding(5, 5, 5, 5);
                        API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, nIV);
                        if(response.getImages().indexOf(imagePath) == 0){
                            API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, currentImage);
                            imageTimeAndDate.setText(reformatDate(imagePath.substring(0, imagePath.length() - 4)));
                        }

                        nIV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String imgName = imgList.get(recentImages.indexOfChild(view));
                                String urlOfImg = CAMERA_URL + imgName;
                                API.getInstance(getActivity()).requestImage(urlOfImg, currentImage);
                                imageTimeAndDate.setText(reformatDate(imgName.substring(0, imgName.length() - 4)));
                            }
                        });
                        recentImages.addView(nIV);
                    }

                    for(final String videoPath : videoList){
                        Log.e(LOGTAG, videoPath);
                        VideoView vv = new VideoView(getActivity());
                        MediaController mediaController = new MediaController(getActivity());
                        mediaController.setAnchorView(vv);
                        vv.setMediaController(mediaController);
                        vv.setVideoURI(Uri.parse(CAMERA_URL + videoPath));
                        vv.setLayoutParams(new FrameLayout.LayoutParams(550, 550));
                        vv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String videoName = videoList.get(recentVideos.indexOfChild(view));
                                String urlOfVideo = CAMERA_URL + videoName;
                                //Toggle views

                                //imageTimeAndDate.setText(reformatDate(imgName.substring(0, imgName.length() - 4)));
                            }
                        });
                        recentVideos.addView(vv, 0);
                    }
                }else{
                    errorLayout.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.camera_no_images_to_display);
                }
            }else{
                errorLayout.setVisibility(View.VISIBLE);
                errorText.setText(R.string.camera_no_images_to_display);
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

    private void filterAssets(ArrayList<String> assets){
        assets.remove(".");
        assets.remove("..");
        assets.remove(".directory");
        for(String item : assets){
            String format = item.substring(item.length() - 3, item.length());
            if(format.equalsIgnoreCase("jpg")){
                imgList.add(item);
            }else{
                videoList.add(item);
            }
        }
        Collections.reverse(imgList);
        Collections.reverse(videoList);
    }

    private String reformatDate(String date){
        String[] items = date.split("_");
        return String.format("%s  %s : %s", items[0], items[1].substring(0,2), items[1].substring(2,4));
    }
}


