package braille.kofefe.app.supports_.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import braille.kofefe.app.R;

/**
 * Created by Snow-Dell-05 on 05-Feb-18.
 */

public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();
    int numMessages = 0;
    Bitmap bitmap = null;
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void showNotificationMessage(String title, String message, Intent intent) {
        showNotificationMessage(title, message, intent, null);
    }

    public void showNotificationMessage(final String title, final String message, Intent intent, String imageUrl) {
        //      Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        //        notification icon
        final int icon = R.mipmap.ic_alphabet_k;
        intent.putExtra("fromNotification", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        //   final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
        //      + "://" + mContext.getPackageName() + "/raw/notification");

        final Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_NOTIFICATION);

        if (!TextUtils.isEmpty(imageUrl)) {
            // bitmap = getBitmapFromURL(imageUrl);
            if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                new GeneratePictureStyleNotification(mBuilder, icon, title, message,
                        resultPendingIntent, alarmSound, imageUrl).execute();
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
            playNotificationSound();
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message,
                                       PendingIntent resultPendingIntent, Uri alarmSound) {

        //      NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        // inboxStyle.


        //    inboxStyle.addLine(message);
        // Sets an ID for the notification, so it can be updated
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setColor(R.color.color_red_kofefe_noti)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setDefaults(Notification.BADGE_ICON_SMALL)
                .extend(new NotificationCompat.Extender() {
                    @Override
                    public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                        return builder;
                    }
                })
                .setSmallIcon(R.mipmap.ic_alphabet_k)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))


                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setColor(R.color.color_red_kofefe_noti)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(bigPictureStyle)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_alphabet_k)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL.replaceFirst("s", ""));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GeneratePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        private NotificationCompat.Builder mBuilderr;
        private int mIconn;
        private String mTitlel, mMessages;
        private PendingIntent mResultPendingIntentt;
        private Uri mAlaramSound;
        private String mImgUrl;

        public GeneratePictureStyleNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message,
                                                PendingIntent resultPendingIntent, Uri alarmSound, String mImgUrll) {
            this.mBuilderr = mBuilder;
            this.mIconn = icon;
            this.mTitlel = title;
            this.mMessages = message;
            this.mResultPendingIntentt = resultPendingIntent;
            this.mImgUrl = mImgUrll;
            this.mAlaramSound = alarmSound;

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(mImgUrl.replaceFirst("s", ""));
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("error", e.toString());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("error", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                showBigNotification(result, mBuilderr, mIconn, mTitlel, mMessages, mResultPendingIntentt, mAlaramSound);
            } else {
                showSmallNotification(mBuilderr, mIconn, mTitlel, mMessages, mResultPendingIntentt, mAlaramSound);
            }
        }
    }
}
