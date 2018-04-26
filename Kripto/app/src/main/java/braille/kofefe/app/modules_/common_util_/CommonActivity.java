package braille.kofefe.app.modules_.common_util_;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import braille.kofefe.app.R;
import braille.kofefe.app.supports_.SessionKofefeApp;
import braille.kofefe.app.supports_.api_request.AsyncTaskCompleteListener;
import braille.kofefe.app.supports_.check_permissions.CheckNetworkState;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static braille.kofefe.app.modules_.common_util_.Constants.SELECT_FILE;

/**
 * Created by HGS on 12/11/2015.
 */
public abstract class CommonActivity extends BaseActivity implements AsyncTaskCompleteListener {

    public static final int MEDIA_TYPE_VIDEO = 202;
    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");
    //         Messages
    public String ERROR_EMPTY_JSON = "Something went wrong! Please check internet connection and retry again";
    public String NETWORK_ERROR = "Internet not found";
    public int PLACE_PICKER_REQUEST = 221;
    protected Uri photoURI;
    protected File photoFile = null;
    protected File mediaFile;
    private ProgressDialog mDialog = null;
    private KProgressHUD mKProgressHUD,mKProgressHUDWithText;
    //    Record video in mp4 format
    private Uri fileUri;
    private String mCurrentPhotoPath = "";

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted
        File mediaStorageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
        Log.d("path", mediaStorageDir + "");

        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // ("Failed to create directory MyCameraVideo.");
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
            //send broadcast to gallery to show new incomimng data

        } else {
            return null;
        }
        return mediaFile;
    }

    public static String getlongtoago(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
        Date date = null;
        date = new Date(createdAt);
        String crdate1 = dateFormatNeeded.format(date);

        // Date Calculation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        crdate1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + "day ago ";
            } else {
                time = diffDays + "days ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + "hr ago";
                } else {
                    time = diffHours + "hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + "min ago";
                    } else {
                        time = diffMinutes + "mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + "secs ago";
                    }
                }

            }

        }
        return time;
    }

    public static String toDuration(long duration) {

        StringBuffer res = new StringBuffer();

        for (int i = 0; i < CommonActivity.times.size(); i++) {
            Long current = CommonActivity.times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp).append(" ").append(CommonActivity.timesString.get(i))
                        .append(temp != 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 seconds ago";
        else
            return res.toString();
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }

    protected void goForPlacePickerDialog() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public List<ModelContacts> getContactsList() {
        List<ModelContacts> mContacts = new ArrayList<>();
        try {
            ContentResolver cr = getContentResolver();

            Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            Log.e("Cursor Size", "" + c.getCount() + "\n");
            c.moveToFirst();
            while (c.moveToNext()) {

                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                /*
                Cursor pCur = cr .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " ='" + id + "'", null, null);
                */
                String phone = "";
                if (pCur.getCount() > 0) {
                    if (pCur.moveToNext()) {
                        pCur.moveToFirst();
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }

                    mContacts.add(new ModelContacts(name, refinePhoneNumber(phone), id));
                }

                Log.e("contacts:", name + " -- " + refinePhoneNumber(phone));
            }

            Collections.sort(mContacts, new Comparator<ModelContacts>() {
                public int compare(ModelContacts obj1, ModelContacts obj2) {
                    // TODO Auto-generated method stub
                    return obj1.getmName().compareToIgnoreCase(obj2.getmName());
                }
            });

            return mContacts;

        } catch (Exception ex) {

            return mContacts;

        }
    }

    // choose image dialog from Gallary and Camera
    protected void Image_Picker_Dialog() {
        //      AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        System.gc();
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage("Choose");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //      call gallary intent
                galleryIntent();
            }
        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                //      calling camera intent
                cameraIntent();
            }
        });

        myAlertDialog.show();

    }

    // Choose image from gallery
    protected void galleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, SELECT_FILE);     // one can be replaced with any action code

    }

    // click image from camera
    protected void cameraIntent() {
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.d("mylog", "Exception while creating file: " + ex.toString());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Log.d("mylog", "Photofile not null");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
            } else {
                photoURI = Uri.parse("file:" + photoFile.getAbsolutePath());
            }

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, Constants.TAKE_PICTURE);
        }
        //   startActivityForResult(intent, Constants.TAKE_PICTURE); //zero can be replaced with any action code

    }

    private File createImageFile() throws IOException {
        //       Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("mylog", "Path: " + mCurrentPhotoPath);
        return image;
    }

    // pick a audio from android
    protected void audioIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, Constants.PICK_SONG);     // one can be replaced with any action code

      /*  Intent intent_upload = new Intent();
        intent_upload.setType("audio*//*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,Constants.PICK_SONG);*/
    }

    protected void Video_Picker_Dialog() {
        //      AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        System.gc();
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage("Choose");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                galleryVideoIntent();
            }
        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                fireCaptureVideo();

            }
        });

        myAlertDialog.show();

    }

    // record video from camera
    public void fireCaptureVideo() {

        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoCaptureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", mediaFile);
        } else {
            fileUri = Uri.parse("file:" + mediaFile.getAbsolutePath());
        }
        // create a file to save the video
        //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        // videoCaptureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10 * 1024 * 1024);
        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 59); // upto 59 second this video will be recorded

        // set the image file name
        videoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoCaptureIntent, Constants.RECORD_VIDEO_);
        }

    }

    //    Choose image from gallery
    protected void galleryVideoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, Constants.REQUEST_TAKE_GALLERY_VIDEO);

    }

    public String numberInShortFormat(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    protected void onPlaySongWithInbuiltMusicApps() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri data = Uri.parse("file:///sdcard/Music");
        String type = "audio/mp3";
        intent.setDataAndType(data, type);
        startActivityForResult(intent, Constants.PICK_SONG);

    }

    public String getLanguage() {

        Locale systemLocale = getResources().getConfiguration().locale;
        String strLanguage = systemLocale.getLanguage();

        return strLanguage;
    }

    public void byPassCall(Class c) {
        startActivity(new Intent(this, c));
    }

    public void showProgressDialog(String message) {
        mDialog = ProgressDialog.show(this, "Please Wait", message, false, false);
    }

    public void dismissProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public SessionKofefeApp getSessionInstance() {
        return new SessionKofefeApp(this);
    }

    public void showIOSProgress(String msg) {
        mKProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                //  .setLabel("Please wait")
                //  .setDetailsLabel(msg)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(.2f)
                .show();
    }

    public void dismissIOSProgress() {
        if (mKProgressHUD.isShowing()) {
            mKProgressHUD.dismiss();
        }
    }

    public void showIOSProgressWithText(String msg) {
        mKProgressHUDWithText = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel(msg)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(.2f)
                .show();
    }
    public void dismissIOSProgressWithText() {
        if (mKProgressHUDWithText.isShowing()) {
            mKProgressHUDWithText.dismiss();
        }
    }


    public String refinePhoneNumber(String mFormatedPhone) {
        return mFormatedPhone.replaceAll("[-.^:(,)]", "").replaceAll(" ", "");
    }

    public String refineNumberFromExtraSymbols(String num) {
        return num.replaceAll("[-+.^:(,)]", "").replaceAll(" ", "");
    }

    public boolean isNetworkConnected() {
        return new CheckNetworkState(this).checkNetwork();
    }

    public void showFancyToast(int toastType, String msg) {
        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, toastType).show();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

    }

    @Override
    public void onErrorFound(String errorResponse, int serviceCode) {

    }

    public Bitmap getBitmapFromView(ImageView mView) {
        try {
            if (mView != null && mView.getDrawable() != null) {
                return ((BitmapDrawable) mView.getDrawable()).getBitmap();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return null;

    }

    public void generateHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "braille.kofefe.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String displayQuoteTime(long time_ago) {
        time_ago = time_ago / 1000;
        long cur_time = (Calendar.getInstance().getTimeInMillis()) / 1000;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        // Seconds
        if (seconds <= 60) {
            return "Just now";
        }
        //Minutes
        else {
            int minutes = Math.round(time_elapsed / 60);

            if (minutes <= 60) {
                if (minutes == 1) {
                    return "a min ago";
                } else {
                    return minutes + " mins ago";
                }
            }
            //Hours
            else {
                int hours = Math.round(time_elapsed / 3600);
                if (hours <= 24) {
                    if (hours == 1) {
                        return "An hour ago";
                    } else {
                        return hours + " hrs ago";
                    }
                }
                //Days
                else {
                    int days = Math.round(time_elapsed / 86400);
                    if (days <= 7) {
                        if (days == 1) {
                            return "Yesterday";
                        } else {
                            return days + " days ago";
                        }
                    }
                    //Weeks
                    else {
                        int weeks = Math.round(time_elapsed / 604800);
                        if (weeks <= 4.3) {
                            if (weeks == 1) {
                                return "A week ago";
                            } else {
                                return weeks + " weeks ago";
                            }
                        }
                        //Months
                        else {
                            int months = Math.round(time_elapsed / 2600640);
                            if (months <= 12) {
                                if (months == 1) {
                                    return "A month ago";
                                } else {
                                    return months + " months ago";
                                }
                            }
                            //Years
                            else {
                                int years = Math.round(time_elapsed / 31207680);
                                if (years == 1) {
                                    return "One year ago";
                                } else {
                                    return years + " years ago";
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public String getDataTimeFromMillisecondsForReacted(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        SimpleDateFormat f = new SimpleDateFormat("MMM");

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        int mHour = calendar.get(Calendar.HOUR);
        int mMin = calendar.get(Calendar.MINUTE);
        int mSecond = calendar.get(Calendar.SECOND);

        return "" + mDay + ", " + getMonthForInt(mMonth);

    }

    public String getDataTimeFromMilliseconds(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        SimpleDateFormat f = new SimpleDateFormat("MMM");

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        int mHour = calendar.get(Calendar.HOUR);
        int mMin = calendar.get(Calendar.MINUTE);
        int mSecond = calendar.get(Calendar.SECOND);

        return "" + getMonthForInt(mMonth) + ", " + mYear;

    }

    private String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month.replace(month.substring(3), "");
    }

    public String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void setImageWithPicasso(ImageView imageHolder, String path, int mImgOffline) {
        if (TextUtils.isEmpty(path)) {
            Picasso.with(this).load(mImgOffline)
                    .error(mImgOffline)
                    .placeholder(mImgOffline)
                    .fit()
                    .into(imageHolder);
        } else {
            Picasso.with(this).load(path)
                    .error(mImgOffline)
                    .placeholder(mImgOffline)
                    .fit()
                    .into(imageHolder);
        }
    }

    protected void getImageWithVolley(final String profilePic, final ImageView mImgUserPro) {
    /*    ImageLoader mImageLoader = KofefeApplication.getInstance().getImageLoader();
        boolean mb = mImageLoader.isCached(profilePic, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        mImageLoader.get(profilePic.replaceFirst("s", ""), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    mImgUserPro.setImageBitmap(response.getBitmap());
                } else {
                    mImgUserPro.setImageResource(R.drawable.user_75);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                //   NetworkResponse mResCode = error.networkResponse;
                //   Log.e("error", error.getMessage() + mResCode.statusCode);
            }
        });*/
        Glide.with(this)
                .load(profilePic.replaceFirst("s", "").replaceAll(" ", "%20"))
                .asBitmap()
                .placeholder(R.drawable.sq_img_feed)
                .error(R.drawable.sq_img_feed)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImgUserPro);
    }


    /**
     * Here is the key method to apply the animation
     */
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    protected void getImageWithVolleyCircleImage(final String profilePic, final SquareRoundCornerImageView mImgUserPro) {
       /* ImageLoader mImageLoader = KofefeApplication.getInstance().getImageLoader();
        mImageLoader.get(profilePic.replaceFirst("s",""), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    mImgUserPro.setImageBitmap(response.getBitmap());
                } else {
                    mImgUserPro.setImageResource(R.drawable.user_75);
                }}
            @Override
            public void onErrorResponse(VolleyError error) {
                //   NetworkResponse mResCode = error.networkResponse;
                //   Log.e("error", error.getMessage() + mResCode.statusCode);
            }
        });*/
        Glide.with(this)
                .load(profilePic.replaceFirst("s", ""))
                .placeholder(R.drawable.sq_img_feed)
                .error(R.drawable.sq_img_feed)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImgUserPro);
    }

    public void uploadMediaToserverS3Bucket(String bucket_name, String mPathOfImage) throws Exception {
        //   String existingBucketName = "*** Provide existing bucket name ***";
        String keyName = "";

        TransferManager tm = new TransferManager(new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return null;
            }

            @Override
            public void refresh() {

            }
        });

        Log.e("upload", "Hello");
        // TransferManager processes all transfers asynchronously,
        // so this call will return immediately.
        Upload upload = tm.upload(bucket_name, keyName, new File(mPathOfImage));
        Log.e("upload", "Hello2");

        try {
            // Or you can block and wait for the upload to finish
            upload.waitForCompletion();
            Log.e("upload", "Upload complete.");
        } catch (AmazonClientException amazonClientException) {
            Log.e("upload", "Unable to upload file, upload was aborted.e.");
            amazonClientException.printStackTrace();
        }

    }

    public AlphaAnimation getBlinkingAnimation() {
        AlphaAnimation blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(300); // duration
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        return blinkanimation;
    }

    public abstract static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;
        private OnItemClickListener mListener;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }
    }

}
