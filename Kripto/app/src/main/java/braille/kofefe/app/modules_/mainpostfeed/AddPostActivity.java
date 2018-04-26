package braille.kofefe.app.modules_.mainpostfeed;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.api.AsyncCreateProfile;
import braille.kofefe.app.modules_.common_util_.BaseInterface;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.CustomizedPlaceFragment;
import braille.kofefe.app.modules_.common_util_.GeocoderLocale;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.check_permissions.PermissionCheck;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Snow-Dell-05 on 11/8/2017.
 */

public class AddPostActivity extends CommonActivity implements BaseInterface {

    @InjectView(R.id.btn_preview)
    protected Button mButtonPreview;

    @InjectView(R.id.circleImageView2)
    protected SquareRoundCornerImageView mImageUser;

    @InjectView(R.id.img_gallary)
    protected ImageView mImgGallary;

    @InjectView(R.id.imageView7)
    protected ImageView mImageSelected;

    @InjectView(R.id.imageView2)
    protected ImageView mImgCross;

    @InjectView(R.id.text_content_count)
    protected TextView mTextContentCounter;

    @InjectView(R.id.mUsername)
    protected TextView mTextUsername;

    @InjectView(R.id.edt_content_)
    protected EditText mEditWritesOnMind;

    @InjectView(R.id.mTextLocation)
    protected TextView mTextLocation;

    @InjectView(R.id.img_cross_image)
    protected ImageView mImgCancelMedia;
    int mCOn = 0;
    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private String path = "", image = "";
    private String audio_path = "", audio = "";
    private String presignedUrl = "", mFinalUploadedMedia_link = "";
    private String extention = "";
    private String mContent = "";
    private String datatoSend = "";
    private String mLoggedUser = "";
    private CustomizedPlaceFragment mSearchLocationFragment;
    private String mPlaceName = "", mLat = "", mlon = "", mCountryName = "", mStateName = "", mCityName = "";
    private Uri uri;
    private String mImageVideo = "";
    private String mMediaType = "";
    private String mMediaPath = "";
    private String mLocation_ = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity);
        ButterKnife.inject(this);

        initViews();
        //  searchLocationFragment();
        getValuesFromPrevious();
        implementListeners();

    }

    private void searchLocationFragment() {

        mSearchLocationFragment = (CustomizedPlaceFragment) getSupportFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);
        mSearchLocationFragment.setAllowReturnTransitionOverlap(true);
        mSearchLocationFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                //                TODO: Get info about the selected place.
                //                Log.i("TAG", "Place: " + place.getName()); LATT_ , LANG_

                //   uihandle.showToast(place.getAddress().toString());
                mPlaceName = String.valueOf(place.getName());
                mLat = String.valueOf(place.getLatLng().latitude);
                mlon = String.valueOf(place.getLatLng().longitude);
                getGeocodeAddress(place.getLatLng().latitude, place.getLatLng().longitude);
                //    uiHandle.onLocationChanged(mTextMyLocationNameHeader);
                //    mTextMyLocationNameHeader.setText("Your ModelLocation\n" + place.getName());
                //    mFrameLocation.setVisibility(View.GONE);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
                uihandle.showToast("please provide accurate detail");
            }
        });
    }

    private void getGeocodeAddress(double latt_, double long_) {
        try {
            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latt_, long_, 1);

            if (addresses.size() > 0) {

                /**
                 * get country name
                 * **/
                //        mCountryName = addresses.get(0).getCountryName();
                mCountryName = addresses.get(0).getCountryCode();

                /** get State name **/
                String stateName = addresses.get(0).getAdminArea();
                String subStateName = addresses.get(0).getSubAdminArea();

                if (stateName != null) {
                    mStateName = stateName;
                } else if (subStateName != null) {
                    mStateName = subStateName;
                }

                /** get city name **/
                String cityName = addresses.get(0).getLocality();
                String subCityName = addresses.get(0).getSubLocality();

                if (cityName != null) {
                    mCityName = cityName;
                } else if (subCityName != null) {
                    mCityName = subCityName;
                }

                //              Todo:  set vslues to views her when rewuired
                //            mEdtLocation.setText(mCityName + ", " + mStateName + ", " + mCountryName);
                Log.e("LOC_DATA", mCountryName + "\n" + mStateName + "\n" + mCityName);
                mTextLocation.setVisibility(View.VISIBLE);
                mTextLocation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.loc_16, 0, 0, 0);

                //        mTextLocation.setText(mPlaceName + ", " + mCityName + ", " + mStateName + ", " + mCountryName);
                mTextLocation.setText(mPlaceName + ", " + mCityName);
                mLocation_ = "\"location\" : {\"name\" : \"" + mPlaceName + "\",\"city\" : \"" + mCityName + "\",\"state\" : \"" + mStateName + "\", \"country\" : \"" + mCountryName + "\", \"latitude\" : " + mLat + ",\"longitude\" :" + mlon + "}";

            }

        } catch (IOException e) {
            e.printStackTrace();
            new GeocoderLocale(mContext, AddPostActivity.this).execute(URLListApis.URL_GEOCODER_LOCALE.replace("LATT_",
                    String.valueOf(latt_)).replace("PTGLG_", String.valueOf(long_)));

        }
    }

    private void getValuesFromPrevious() {
        mLoggedUser = getSessionInstance().getLoggedInName();
        mTextUsername.setText(mLoggedUser);

        // laod image of registered user
        getImageWithVolley(getSessionInstance().getUserProfilePic(), mImageUser);


    }

    private void implementListeners() {
        mImgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uihandle.hideSoftKeyboard();
                uihandle.goBack();
            }
        });


        mEditWritesOnMind.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              /*
               if (mTextContentCounter.getLayout().getLineCount() <= 4) {
               Log.e("line_test", "" + mTextContentCounter.getLayout().getLineCount());
              } */

                Log.e("mCon", "" + mEditWritesOnMind.getLineCount());

                if (s.length() > 0) {
                    mButtonPreview.setEnabled(true);
                } else {
                    mButtonPreview.setEnabled(false);
                }

                if (mEditWritesOnMind.getLineCount() > 10) {
                    return;
                }

                //   mEditWritesOnMind.setText(s.toString());
                mTextContentCounter.setText((s.length()) + "/153");


            }
        });


    }

    private void initViews() {
        uihandle = new UiHandleMethods(mContext);

    }

    @OnClick(R.id.img_cross_image)
    public void mCancelMedia() {
        removeImage();

    }


    @OnClick(R.id.btn_preview)
    public void mPreviewPost() {

        mContent = mEditWritesOnMind.getText().toString().trim();
        StaticValues.mTextToBePreview = mContent;
        uihandle.goForNextScreen(PreviewPostScreen.class);

    }

    @OnClick(R.id.btn_add_post)
    public void mAddPost() {

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        mContent = mEditWritesOnMind.getText().toString().trim();

        if (mContent.trim().equals("") && mLocation_.trim().equals("") && mMediaPath.equals("")) {
            showFancyToast(TastyToast.WARNING, "Please provide data!");
            return;
        }
        if (TextUtils.isEmpty(mContent)) {
            mContent = "";
        }

        if (mMediaPath.equals("")) {
            mFinalUploadedMedia_link = "";
        }
        // fire api to send data top server
        goForCreatePost();

    }

    @OnClick(R.id.img_gallary)
    public void onGallaryClick() {

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!(new PermissionCheck(this).checkCamera())) {
                goForImgClick();
            }
        } else {
            goForImgClick();
        }


    }

    @OnClick(R.id.img_video_selection)
    public void onVideoSelection() {

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!(new PermissionCheck(this).checkCamera())) {
                goForVideoClick();
            }
        } else {
            goForVideoClick();
        }
    }

    @OnClick(R.id.img_audio_selection)
    public void onAudioSelectionn() {
        audioIntent();
    }

    @OnClick(R.id.img_location_selection)
    void onLocationClick() {
   /*     AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build();
        mSearchLocationFragment.setFilter(typeFilter);
        mSearchLocationFragment.setHint("Search location...");
        mSearchLocationFragment.zzJk();*/
        goForPlacePickerDialog();

    }

    private void goForImgClick() {

        if (uihandle.hasCamera()) {
            Image_Picker_Dialog();
        } else {
            uihandle.showToast("Sorry! Camera is not available");
        }

    }

    private void goForVideoClick() {

        if (uihandle.hasCamera()) {
            Video_Picker_Dialog();
        } else {
            uihandle.showToast("Sorry! Camera is not available");
        }

    }

    public void removeImage() {

        mImageSelected.setImageBitmap(null);
        mImageSelected.destroyDrawingCache();
        mImageSelected.setVisibility(View.GONE);
        //  mFinalUploadedMedia_link = "";
        mMediaPath = "";
        mImgCancelMedia.setVisibility(View.GONE);

    }

    private void onSelectFromGalleryResult(String mImgPath, Intent data) throws IOException {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mImageSelected.setVisibility(View.VISIBLE);
        mImgCancelMedia.setVisibility(View.VISIBLE);
        mImageSelected.setImageBitmap(uihandle.modifyOrientation(bm, mImgPath));
        extention = mImgPath.substring(mImgPath.lastIndexOf("."));


    }

    // processing after select image from camera as well as gallery
    private void onCaptureImageResult(Uri data) throws IOException {
        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //     thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        //   File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");

      /*  String path = destination.getPath();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mMediaPath = photoFile.getAbsolutePath();
        mMediaType = Constants.ConstantMediaType.MEDIA_TYPE_IMAGE;
        //  uihandle.showToast(image);


        mImageSelected.setVisibility(View.VISIBLE);
        mImgCancelMedia.setVisibility(View.VISIBLE);
        mImageSelected.setImageBitmap(uihandle.modifyOrientation(thumbnail, mMediaPath));
        extention = mMediaPath.substring(mMediaPath.lastIndexOf("."));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionCheck.PERMISSION_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (i == grantResults.length - 1) {
                        //   goForImgClick();
                    }
                } else {

                    //  uihandle.showToast("Permission ungranted");
                    // mContext.finish();
                    // mContext.moveTaskToBack(true);
                    return;
                }
            }
        }

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.TAKE_PICTURE && resultCode == RESULT_OK) {
            removeImage();
            try {
                onCaptureImageResult(photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //path = uihandle.getRealPathFromURI(data.getData());

            //  uihandle.showToast(path);
        } else if (requestCode == Constants.SELECT_FILE && resultCode == RESULT_OK) {
            removeImage();
            path = uihandle.getImagePath(data);
            mMediaPath = path;
            mMediaType = Constants.ConstantMediaType.MEDIA_TYPE_IMAGE;
            // uihandle.showToast(path);
            // clear imageview to for next image

            try {
                onSelectFromGalleryResult(mMediaPath, data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == Constants.PICK_SONG && resultCode == RESULT_OK) {
            //  the selected audio.
            //   Uri uri = data.getData();
            removeImage();
            audio_path = uihandle.getAudioPath(data);
            mMediaPath = audio_path;
            mMediaType = Constants.ConstantMediaType.MEDIA_TYPE_AUDIO;
            extention = mMediaPath.substring(mMediaPath.lastIndexOf("."));

            mImageSelected.setVisibility(View.VISIBLE);
            mImgCancelMedia.setVisibility(View.VISIBLE);
            mImageSelected.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_music));
            //   showToast(audio_path+"\n -"+audio);  mImageSelected

        } else if (requestCode == super.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                mPlaceName = String.valueOf(place.getName());
                mLat = String.valueOf(place.getLatLng().latitude);
                mlon = String.valueOf(place.getLatLng().longitude);
                getGeocodeAddress(place.getLatLng().latitude, place.getLatLng().longitude);

                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == Constants.RECORD_VIDEO_ && resultCode == RESULT_OK) {
            onVideoRecordOrSelect(data);
            //  path = uihandle.getRealPathFromURI(data.getData());
            //  uihandle.showToast(path);
        } else if (requestCode == Constants.REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK) {
            if (data != null) {
                onVideoSelectFromGallary(data);
            }
        }

    }

    public void onVideoSelectFromGallary(Intent mData) {
        try {
            removeImage();
            mImageSelected.setVisibility(View.VISIBLE);
            mImgCancelMedia.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                mImageVideo = mData.getData().getPath();      //    uihandle.getVideoPath(mData);
            } else {
                mImageVideo = uihandle.getVideoPath(mData);   //    mData.getData().getPath();
            }

            //   mImageVideo = uihandle.getVideoPath(mData);       //    mData.getData().getPath();

            mMediaPath = mImageVideo;

            mMediaType = Constants.ConstantMediaType.MEDIA_TYPE_VIDEO;
            extention = mMediaPath.substring(mMediaPath.lastIndexOf("."));

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mImageVideo, MediaStore.Video.Thumbnails.MICRO_KIND);
            mImageSelected.setImageBitmap(thumb);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void onVideoRecordOrSelect(Intent mData) {
        removeImage();
        mImageSelected.setVisibility(View.VISIBLE);
        mImgCancelMedia.setVisibility(View.VISIBLE);

        uri = mData.getData();
        if (EasyPermissions.hasPermissions(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            mImageVideo = mediaFile.getAbsolutePath();
            mMediaPath = mImageVideo;

            mMediaType = Constants.ConstantMediaType.MEDIA_TYPE_VIDEO;
            extention = mMediaPath.substring(mMediaPath.lastIndexOf("."));

            //  uihandle.showToast(mImageVideo + "  -- path");
            try {
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mImageVideo, MediaStore.Video.Thumbnails.MICRO_KIND);
                mImageSelected.setImageBitmap(thumb);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                if (!mImageSelected.isShown()) {
                    mImageSelected.setVisibility(View.VISIBLE);
                    mImgCancelMedia.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mBtnUpload.setText("    Attached Video    ");
        } else {
            EasyPermissions.requestPermissions(mContext, getString(R.string.read_file), Constants.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }

    }

    private void goForCreatePost() {
        //      mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //      Todo: Remove after testing and uncomment above
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        if (!mMediaPath.equals("")) {
            getPresignedS3url(extention);
        } else {
            fireCreatepost("");
        }
    }

    private void fireCreatepost(String mLink) {
        //     String mRar="{\"media\" : [{ \"mediaUrl\" : \"https://media.kofefe.s3-us-west-2.amazonaws.com/image/profile/646505e0-fe0c-4130-94bd-7809e53107a5.jpeg\",\"mediaType\" : \"IMAGE\"}]}";
        if (TextUtils.isEmpty(mLink)) {
            if (!TextUtils.isEmpty(mContent)) {
                datatoSend = "\"content\" : \"" + mContent + "\"";
            } else {
                showFancyToast(TastyToast.WARNING, "Fields are empty!");
                return;
            }
        } else {
            if (!TextUtils.isEmpty(mContent)) {
                datatoSend = "\"content\" : \"" + mContent + "\",\"media\" : [{ \"mediaUrl\" : \"" + mLink + "\",\"mediaType\" : \"" + mMediaType + "\"}]";
            } else {
                datatoSend = "\"media\" : [{ \"mediaUrl\" : \"" + mLink + "\",\"mediaType\" : \"" + mMediaType + "\"}]";
            }
        }

        //   Check for location
        if (!TextUtils.isEmpty(mLocation_)) {
            datatoSend = datatoSend + "," + mLocation_;
        }


        String mFinalDataToSend = "{" + datatoSend + "}";

        //     public HttpRequester(String mCreatePost, int mMethod, Activity activity, int serviceCode,
        //     String url, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        showIOSProgressWithText("Creating post...");
        new HttpRequester(mFinalDataToSend.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n"), Request.Method.POST, this,
                Constants.CREATE_POST_POST, URLListApis.URL_CREATE_POST.replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void getPresignedS3url(String mFileExtention) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        //   calling api to upload image to server
        HashMap<String, String> map = new HashMap<>();
        showIOSProgressWithText("Uploading …");

        new HttpRequester(Request.Method.GET, this, map, Constants.UPLOAD_MEDIA_TO_AMAZONE_PRE_FIRE_API,
                URLListApis.URL_UPLOAD_MEDIA.replace("MEDIA_TYPE", mMediaType).
                        replace("USECASE", Constants.ConstantUseCase.USECASE_POST).
                        replace("FILE_EXTENTION", mFileExtention).replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            JSONObject jsonObject = null;
            Log.e("Response", response);
            if (!TextUtils.isEmpty(response) || !response.equals("")) {
                jsonObject = new JSONObject(response);
            }
            switch (serviceCode) {
                case Constants.UPLOAD_MEDIA_TO_AMAZONE_PRE_FIRE_API:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            presignedUrl = jsonObject.optString("presignedUrl");
                            //   HashMap<String, String> map = new HashMap<>();
                            //   map.put("", image);
                            //   new HttpRequester("put", Request.Method.PUT, this, map, Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE, presignedUrl, this);
                            new AsyncCreateProfile(mContext, Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE, this)
                                    .execute(presignedUrl, mMediaPath);
                            //   uploadMediaToserverS3Bucket(presignedUrl, image);
                            //   goForUpload(presignedUrl, image,Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE);
                        } else {
                            dismissIOSProgressWithText();
                            showFancyToast(TastyToast.CONFUSING, "Something went wrong");
                        }
                    } else {
                        dismissIOSProgressWithText();
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE:
                    String[] mTesturl = presignedUrl.split("\\?");
                    mFinalUploadedMedia_link = mTesturl[0];
                    dismissIOSProgressWithText();
                    // fire crate post
                    fireCreatepost(mFinalUploadedMedia_link);
                    break;

                case Constants.CREATE_POST_POST:
                    dismissIOSProgressWithText();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //          showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /**** refine response from server                             ***/

                            refineJSON(jsonObject);

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("ack"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;


                case Constants.PREVIEW_POST:
                    dismissIOSProgressWithText();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            String scrambledBody = jsonObject.optString("scrambledBody");
                            //      Todo : show preview post that how the post will lock like


                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorFound(String errorResponse, int serviceCode) {
        super.onErrorFound(errorResponse, serviceCode);
        try {

            dismissIOSProgressWithText();
            Log.e("Response", errorResponse);
            showFancyToast(TastyToast.ERROR, errorResponse);
            //    JSONObject jsonObject = new JSONObject(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void refineJSON(JSONObject mJsonObj) {
        try {
            Log.e("response", mJsonObj.toString());

            String messageToUser = mJsonObj.optString("messageToUser");
            if (!messageToUser.equals("null")) {
                showFancyToast(TastyToast.SUCCESS, messageToUser);
            } else {
                showFancyToast(TastyToast.SUCCESS, "SUCCESS");
            }

            StaticValues.EditProfileContent.mProfileEdited = true;
            this.finish();
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            //    TOdo: pending after finish textwatcher come to this
            //    JSONObject  jPostData

           /* JSONArray nArray = mJsonObj.optJSONArray("matchingUsers");
            if (nArray != null && nArray.length() > 0) {

                for (int i = 0; i < nArray.length(); i++) {
                    JSONObject jData = nArray.optJSONObject(i);

                    String name = jData.optString("name");
                    String userName = jData.optString("userName");
                    String profilePic = jData.optString("profilePic");
                    String relationship = jData.optString("relationship");
                    String uuid = jData.optString("uuid");

                    //     mListSearchedUsers.add(new ModelSearchedUsers(name, userName, profilePic, relationship, uuid));
                }
                //   mAdapterSearchedUser = new AdapterSearchedUsers(mContext, mListSearchedUsers);
                //   mRecycleSearchedResult.setAdapter(mAdapterSearchedUser);

            } else {
                Log.e("response_else", "empty");
            }*/
        } catch (Exception ex) {
            Log.e("feed", ex.toString());

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.hideSoftKeyboard();
        uihandle.goBack();

        // Animation animMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        //  animMove.setAnimationListener(mContext);
    }

    @Override
    public void onAddressFound(String s) {
        uihandle.showToast(s);
        Log.e("location", s);
    }


}
