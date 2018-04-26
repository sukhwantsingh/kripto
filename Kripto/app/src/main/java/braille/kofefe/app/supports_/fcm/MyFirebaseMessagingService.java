package braille.kofefe.app.supports_.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.mainpostfeed.MainActivity;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.supports_.SessionKofefeApp;


/**
 * Created by Snow-Dell-05 on 05-Feb-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param // messageBody FCM message body received.
     */
    Intent intent;
    private NotificationUtils notificationUtils;
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
   /* private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }*/

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // { body=gxgxgxg commented on your post. Click here to view the post!,
        // type=POST,
        // uuid=7b867639-053b-4b26-82ac-0cca53f3e76a,
        // body_img_url=https://media.kofefe.s3-us-west-2.amazonaws.com/image/profile/b304ee36-ba68-4d59-9243-aee624ccbcbb.jpeg,
        // title=Somebody commented on your post!,
        // notificationId=2060c74f-51ad-4778-ae25-e3653219c7ed,
        // title_img_url=https://media.kofefe.thumbnail.s3-us-west-2.amazonaws.com/image/profile/d2257d58-d396-457c-89c4-788eb5665936.jpeg}


        //  TODO(developer): Handle FCM messages here.
        //  Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "DATA: " + remoteMessage);
        StaticValues.mNotificationBlinking = true;
        Map<String, String> mMap = remoteMessage.getData();

        //  Check if message contains a notification payload.
      /*  if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }*/

        //   Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData().toString());
            //   handleNow(remoteMessage.getData().toString());
            try {
                handleNow(mMap);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(Map<String, String> mMapp) {
        Log.e(TAG, "Short lived task is done.");
        sendNotification(mMapp);
    }

    private void sendNotification(Map<String, String> mMapp) {
        //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //     PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String mTitle = "", mUuid = "", mBody = "", mBody_img_url = "", mType = "", title_img_url = "", mNotification = "";

        if (mMapp.get("body") != null) {
            mBody = mMapp.get("body");
        }
        if (mMapp.get("type") != null) {
            mType = mMapp.get("type");
        }
        if (mMapp.get("body_img_url") != null) {
            mBody_img_url = mMapp.get("body_img_url");
        }
        if (mMapp.get("title") != null) {
            mTitle = mMapp.get("title");
        }
        if (mMapp.get("title_img_url") != null) {
            title_img_url = mMapp.get("title_img_url");
        }
        if (mMapp.get("notificationId") != null) {
            mNotification = mMapp.get("notificationId");
        }
        if (mMapp.get("uuid") != null) {
            mUuid = mMapp.get("uuid");
        }

        //    check whether the type is post or user
        if (mType.equals("USER")) {
            intent = new Intent(this, ProfileActivity.class);

            //   setting uuid for profile and notification id
            new SessionKofefeApp(this).setUuidFromNotification(mUuid);
            new SessionKofefeApp(this).setNotificationIdFromNotification(mNotification);

        } else if (mType.equals("POST")) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.showNotificationMessage(mTitle, mBody, intent, mBody_img_url);

        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", mBody);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

      /*  NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)

                        .setSmallIcon(R.mipmap.ic_alphabet_k)
                        .setContentTitle(mMapp.get("title"))
                        .setContentText(mMapp.get("body"))
                        .setAutoCancel(true)
                        .setColor(R.color.colorPrimaryDark)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification*//* , notificationBuilder.build());
     */   // play notification sound


        //  notificationUtils.playNotificationSound();
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            //     is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification

        }
    }
}
