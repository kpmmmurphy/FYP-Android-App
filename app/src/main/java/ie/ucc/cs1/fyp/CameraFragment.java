package ie.ucc.cs1.fyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import ie.ucc.cs1.fyp.Model.APIResponse;
import ie.ucc.cs1.fyp.Model.CameraResponse;
import ie.ucc.cs1.fyp.Model.Packet;
import ie.ucc.cs1.fyp.Model.Session;
import ie.ucc.cs1.fyp.Network.API;
import ie.ucc.cs1.fyp.Socket.SocketManager;

/**
 * Created by kpmmmurphy on 04/11/14.
 */
public class CameraFragment extends Fragment{

    private static final String LOGTAG = "__CAMERA_FRAGMENT";

    private static final String CAMERA_URL = Constants.API_URL + Constants.API_CAMERA;
    private ArrayList<String> imgList;
    private ArrayList<String> videoList;
    private String piPublicIP;

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
    @InjectView(R.id.camera_btn_request_image)
    Button btnRequestImage;
    @InjectView(R.id.camera_btn_request_stream)
    Button getBtnRequestStream;
    @InjectView(R.id.camera_direct_controls)
    LinearLayout llCameraControls;
    @InjectView(R.id.camera_web_content)
    RelativeLayout rlCameraWebContent;

    @OnItemClick(R.id.lv_recent_videos)
    public void onItemSelected(int position){
        Utils.methodDebug(LOGTAG);
        String videoName  = videoList.get(position);
        setTimeAndDate(videoName);
        playURIWithVV(currentVideo, videoName, false);
    }

    @OnClick(R.id.camera_btn_request_image)
    public void onClick(){
        Utils.methodDebug(LOGTAG);
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            String requestImagePacket = Utils.toJson(new Packet(Constants.SERVICE_REQUEST_IMAGE, null));
            SocketManager.getInstance(getActivity()).sendPacketToPi(requestImagePacket);
        }else{
            API.getInstance(getActivity()).requestImageCapture(captureImageListener, streamErrorListener);
        }
    }

    @OnClick(R.id.camera_btn_request_stream)
    void onClickStream(){
        Utils.methodDebug(LOGTAG);
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            SocketManager.getInstance(getActivity().getApplicationContext()).sendPacketToPi(Utils.toJson(new Packet(Constants.SERVICE_REQUEST_STREAM, null)));
            String uri = String.format("%s%s:%d/", "http:/", Session.getInstance(getActivity()).getPiIPAddress().toString(), Constants.VIDEO_STREAM_PORT);
            if(BuildConfig.DEBUG){
                Log.i(LOGTAG, "Streaming at URL : " + uri);
            }
            playURIWithVV(currentVideo, uri, true);
        }else{
            API.getInstance(getActivity()).requestVideoStream(startStreamListener, streamErrorListener);
        }
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
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            new RetrieveImagesOverFTPTask().execute(true);
            MyApplication.scheduleTask(new Runnable() {
                @Override
                public void run() {
                    new RetrieveImagesOverFTPTask().execute(false);
                }
            }, 10, 10, LOGTAG);
        }else{
            MyApplication.scheduleTask(new Runnable() {
                @Override
                public void run() {
                    API.getInstance(getActivity()).requestListImages(successListener, errorListener);
                }
            }, 0, 10, LOGTAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
        MyApplication.unscheduleTask(LOGTAG);
    }

    private Response.Listener<CameraResponse> successListener = new Response.Listener<CameraResponse>() {
        @Override
        public void onResponse(CameraResponse response) {
            Utils.methodDebug(LOGTAG);
            filterAssets(response.getImages());
            displayImagesAndVideos();
        }
    };

    private void displayImagesAndVideos(){
        if(imgList != null && !imgList.isEmpty()){

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recentImages.removeAllViews();
                }
            });

            boolean isConnectToPi = Session.getInstance(getActivity()).isConnectedToPi();

            for(final String imagePath : imgList){
                NetworkImageView nIV = new NetworkImageView(getActivity());
                nIV.setLayoutParams(new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT));

                if(isConnectToPi){
                    File imgFile  = getActivity().getFileStreamPath(imagePath);
                    Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 450, 700);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    nIV.setBackground(bitmapDrawable);
                }else{
                    API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, nIV);
                }

                nIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        displayLargeImage(view);
                    }
                });

                //Display the latest Image
//                if(imgList.indexOf(imagePath) == 0){
//                    if(isConnectToPi){
//                        final File imgFile = getActivity().getFileStreamPath(imagePath);
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.toString());
//                                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//                                currentImage.setBackground(bitmapDrawable);
//                            }
//                        });
//
//                    }else{
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                API.getInstance(getActivity()).requestImage(CAMERA_URL + imagePath, currentImage);
//                            }
//                        });
//                    }
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            setTimeAndDate(imagePath);
//                        }
//                    });
//                }

                final NetworkImageView cloneNIV = nIV;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recentImages.addView(cloneNIV);
                    }
                });

            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                populateVideoList(videoList);
            }
        });
    }

    private void displayLargeImage(View view){
        YoYo.with(Techniques.Pulse)
                .duration(700)
                .playOn(view);
        String imgName = imgList.get(recentImages.indexOfChild(view));

        if(Session.getInstance(getActivity()).isConnectedToPi()){
            File imgFile  = getActivity().getFileStreamPath(imgName);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.toString());
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            currentImage.setBackground(bitmapDrawable);
        }else{
            API.getInstance(getActivity()).requestImage(CAMERA_URL + imgName, currentImage);
        }

        currentVideo.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeOut).duration(700).playOn(currentVideo);
        YoYo.with(Techniques.FadeIn).duration(700).playOn(currentImage);
        setTimeAndDate(imgName);
    }

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
        }
    };

    private Response.Listener<APIResponse> startStreamListener = new Response.Listener<APIResponse>() {
        @Override
        public void onResponse(APIResponse response) {
            Utils.methodDebug(LOGTAG);
            piPublicIP = response.pi_public_ip;
            String uri = String.format("%s%s:%d/", "http://", piPublicIP, Constants.VIDEO_STREAM_PORT);
            if(BuildConfig.DEBUG){
                Log.i(LOGTAG, "Streaming at URL : " + uri);
            }
            playURIWithVV(currentVideo, uri, true);
        }
    };

    private Response.Listener<APIResponse> captureImageListener = new Response.Listener<APIResponse>() {
        @Override
        public void onResponse(APIResponse response) {
            Utils.methodDebug(LOGTAG);
            API.getInstance(getActivity()).requestListImages(successListener, errorListener);
        }
    };

    private Response.ErrorListener streamErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
        }
    };

    private void populateVideoList(ArrayList<String> videos){
        if(!videos.isEmpty()){
            ArrayAdapter<String> videoAdaptor = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,videos);
            recentVideos.setAdapter(videoAdaptor);
        }else{
            recentVideos.setAdapter(null);
        }
    }

    private void filterAssets(List<String> assets){
        assets.remove(".");
        assets.remove("..");
        assets.remove(".directory");
        for(String item : assets){
            String format = item.substring(item.length() - 3, item.length());
            if(!imgList.contains(item) && format.equalsIgnoreCase("jpg")){
                //Slot new image files at end each time
                imgList.add(0, item);
            }else if(!videoList.contains(item) && format.equalsIgnoreCase("mp4")){
                //Slot in at end
                videoList.add(0, item);
            }
        }
    }

    private String reformatDate(String date){
        String[] items = date.split("_");
        return String.format("%s  %s : %s", items[0], items[1].substring(0,2), items[1].substring(2,4)).replace("-", " - ");
    }

    private void setTimeAndDate(final String fileName){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.FadeOut).duration(500).playOn(imageTimeAndDate);
                imageTimeAndDate.setText(getString(R.string.tv_captured) + " " + reformatDate(fileName.substring(0, fileName.length() - 4)));
                YoYo.with(Techniques.FadeIn).duration(500).playOn(imageTimeAndDate);
            }
        });
    }

    private void playURIWithVV(VideoView vv, String videoName, boolean isStream){
        Utils.methodDebug(LOGTAG);
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(vv);
        vv.setMediaController(mediaController);

        Uri uri = null;
        if(isStream){
            uri = Uri.parse(videoName);
        }else{
            if(Session.getInstance(getActivity()).isConnectedToPi()){
                uri = Uri.fromFile(new File(getActivity().getFileStreamPath(videoName).getPath()));
            }else{
                uri = Uri.parse(CAMERA_URL + videoName);
            }
        }

        imageTimeAndDate.setText(String.format( "Streaming from URI : %s", uri));
        vv.setVideoURI(uri);
        vv.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeOut).duration(700).playOn(currentImage);
        YoYo.with(Techniques.FadeIn).duration(700).playOn(vv);
        vv.requestFocus();
        final VideoView vv_final = vv;
        final Uri uri_final = uri;
        vv.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if(BuildConfig.DEBUG){
                    Log.i(LOGTAG, "Playing URI :: " + uri_final.toString());
                }
                vv_final.start();
            }
        });
    }

    private class RetrieveImagesOverFTPTask extends AsyncTask<Boolean, Void, Void>{

        @Override
        protected Void doInBackground(Boolean... bools) {
            getLatetestFilesOverFTP();
            //On first call we must get any previously stored images.
            if(bools[0]){
                getBackupedFilesOverFTP();
            }
            List<String> storedFiles = Arrays.asList(getActivity().getFilesDir().list());
            filterAssets(storedFiles);
            displayImagesAndVideos();
            return null;
        }
    }

    private synchronized com.jcraft.jsch.Session connectToFTPServer() throws JSchException {
        JSch jsch = new JSch();
        com.jcraft.jsch.Session session = null;
        session = jsch.getSession("pi", "192.168.42.1", 22);

        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword("111314826");
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        return session;
    }

    private synchronized void getLatetestFilesOverFTP(){
        String CURRENT_STILL_DIR  = "./still";
        String CURRENT_VIDEOS_DIR = "./video";

        try {
            com.jcraft.jsch.Session session = connectToFTPServer();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp)channel;

            sftpChannel.cd("./FinalYearProject/camera");

            //GET CURRENT IMAGE
            sftpChannel.cd(CURRENT_STILL_DIR);
            transferAllFileInCurrentDir(sftpChannel);

            //GET CURRENT VIDEO
            sftpChannel.cd("..");
            sftpChannel.cd(CURRENT_VIDEOS_DIR);
            transferAllFileInCurrentDir(sftpChannel);

            if(BuildConfig.DEBUG){
                Log.d(LOGTAG, "Closing SFTPChannel");
            }
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized void getBackupedFilesOverFTP(){
        String STILLS_DIR         = "./still_backup";
        String VIDEOS_DIR         = "./video_backup";

        try {
            com.jcraft.jsch.Session session = connectToFTPServer();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp)channel;

            sftpChannel.cd("./FinalYearProject/camera");

            //GET BACKUPED STILLS
            sftpChannel.cd("..");
            sftpChannel.cd(STILLS_DIR);
            transferAllFileInCurrentDir(sftpChannel);

            //GET BACKEDUP VIDEO
            sftpChannel.cd("..");
            sftpChannel.cd(VIDEOS_DIR);
            transferAllFileInCurrentDir(sftpChannel);

            if(BuildConfig.DEBUG){
                Log.d(LOGTAG, "Closing SFTPChannel");
            }
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void transferAllFileInCurrentDir(ChannelSftp sftpChannel) throws SftpException, FileNotFoundException {
        FileOutputStream fileOutputStream;
        @SuppressWarnings("unchecked")
        Vector<ChannelSftp.LsEntry> entries = sftpChannel.ls(".");
        for (ChannelSftp.LsEntry entry : entries) {
            if(BuildConfig.DEBUG){
                Log.i(LOGTAG, "Retrieving File : " + entry.getFilename());
            }
            File file = getActivity().getFileStreamPath(entry.getFilename());
            if( file.exists() || entry.getFilename().equals(".") || entry.getFilename().equals("..")){
                continue;
            }
            fileOutputStream = getActivity().openFileOutput(entry.getFilename(), Context.MODE_PRIVATE);
            //Transfer the file to local storage
            sftpChannel.get(entry.getFilename(),fileOutputStream);
            //Remove file from server
            sftpChannel.rm(entry.getFilename());
        }
    }
}


