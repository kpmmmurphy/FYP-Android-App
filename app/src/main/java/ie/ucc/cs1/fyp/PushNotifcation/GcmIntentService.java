package ie.ucc.cs1.fyp.PushNotifcation;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ie.ucc.cs1.fyp.BuildConfig;
import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.MainActivity;
import ie.ucc.cs1.fyp.R;

/**
 * Created by kpmmmurphy on 04/02/15.
 */
public class GcmIntentService extends IntentService {
    private final String LOGTAG = GcmIntentService.class.getSimpleName();
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), null);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(), null);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(LOGTAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(LOGTAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.

                sendNotification(constructMsg(extras), null);
                Log.i(LOGTAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public void sendNotification(String msg, Context context) {
        Context mContext = this;

        if(BuildConfig.DEBUG){
            Log.i(LOGTAG, "Push Payload : " + msg);
        }

        //When testing, we need to pass in current context
        if(context != null){
            mContext = context;
        }

        mNotificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent mainActivityIntent = new Intent(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.PN_FROM_PENDING_INTENT, true);
        mainActivityIntent.putExtras(bundle);

        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                mainActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT, bundle);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle(Constants.PN_TITLE)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private String constructMsg(Bundle extras){
        String sensor = extras.getString(Constants.SENSOR_NAME);
        String sensorValue = extras.getString(Constants.SENSOR_VALUE);
        String msgText = "Alert Threshold Reached";
        if(sensor.equalsIgnoreCase(Constants.SENSOR_CAMERA)){
            msgText = "Image Captured!";
        }else if(sensor.equalsIgnoreCase(Constants.SENSOR_CAMERA)){
            msgText = "Activity Detected!";
        }

        //Store current alert details in SharedPrefs
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("fyp", Context.MODE_PRIVATE);
        prefs.edit().putString(Constants.SENSOR_NAME, sensor).apply();
        prefs.edit().putString(Constants.SENSOR_VALUE, sensorValue).apply();

        return String.format("%s : %s", sensor.toUpperCase(), msgText);
    }
}
