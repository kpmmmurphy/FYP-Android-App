package ie.ucc.cs1.fyp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
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

    @InjectView(R.id.view_wrapper)
    RelativeLayout viewWrapper;
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
    @InjectView(R.id.lv_recent_videos)
    ListView recentVideos;
    @InjectView(R.id.camera_fragment_error_text)
    TextView errorText;

    @OnItemClick(R.id.lv_recent_videos)
    void onItemSelected(int position){
        Utils.methodDebug(LOGTAG);
        String videoName  = videoList.get(position);
        setTimeAndDate(videoName);
        VideoView vv = new VideoView(getActivity());
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(currentVideo);
        currentVideo.setMediaController(mediaController);
        currentVideo.setVideoURI(Uri.parse(CAMERA_URL + videoName));
        //currentImage.setVisibility(View.GONE);
        currentVideo.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeOut).duration(700).playOn(currentImage);
        YoYo.with(Techniques.FadeIn).duration(700).playOn(currentVideo);
        currentVideo.requestFocus();
        currentVideo.start();
    }

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
                    viewWrapper.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);

                    recentImages.removeAllViews();
                    for(final String imagePath : imgList){
                        NetworkImageView nIV = new NetworkImageView(getActivity());
                        nIV.setLayoutParams(new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT));
                        API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, nIV);

                        if(imgList.indexOf(imagePath) == 0){
                            API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, currentImage);
                            setTimeAndDate(imagePath);
                        }

                        nIV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                YoYo.with(Techniques.Pulse)
                                        .duration(700)
                                        .playOn(view);
                                String imgName = imgList.get(recentImages.indexOfChild(view));
                                String urlOfImg = CAMERA_URL + imgName;
                                API.getInstance(getActivity()).requestImage(urlOfImg, currentImage);

                                //currentImage.setVisibility(View.VISIBLE);
                                currentVideo.setVisibility(View.GONE);
                                YoYo.with(Techniques.FadeOut).duration(700).playOn(currentVideo);
                                YoYo.with(Techniques.FadeIn).duration(700).playOn(currentImage);
                                setTimeAndDate(imgName);
                            }
                        });
                        recentImages.addView(nIV);
                    }
                    populateVideoList(videoList);
                    for(final String videoPath : videoList){
                        Log.e(LOGTAG, videoPath);

                    }
                }else{
                    viewWrapper.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.camera_no_images_to_display);
                }
            }else{
                viewWrapper.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                errorText.setText(R.string.camera_no_images_to_display);
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
            viewWrapper.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText(R.string.camera_unable_to_display_images);
        }
    };

    private void populateVideoList(ArrayList<String> videos){
        ArrayAdapter<String> videoAdaptor = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,videos);
        recentVideos.setAdapter(videoAdaptor);
    }

    private void filterAssets(ArrayList<String> assets){

        imgList.clear();
        videoList.clear();

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
        return String.format("%s  %s : %s", items[0], items[1].substring(0,2), items[1].substring(2,4)).replace("-", " - ");
    }

    private void setTimeAndDate(String fileName){
        YoYo.with(Techniques.FadeOut).duration(500).playOn(imageTimeAndDate);
        imageTimeAndDate.setText(getString(R.string.tv_captured) + " " + reformatDate(fileName.substring(0, fileName.length() - 4)));
        YoYo.with(Techniques.FadeIn).duration(500).playOn(imageTimeAndDate);
        
    }
}


