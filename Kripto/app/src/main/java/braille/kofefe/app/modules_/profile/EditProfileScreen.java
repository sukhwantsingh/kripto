package braille.kofefe.app.modules_.profile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.model.InstagramUser;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.api.AsyncCreateProfile;
import braille.kofefe.app.modules_.Invite_.social_presenter.SocialAuthorizationCallback;
import braille.kofefe.app.modules_.Invite_.social_presenter.SocialFbLogin;
import braille.kofefe.app.modules_.Invite_.social_presenter.SocialTwitterLogin;
import braille.kofefe.app.modules_.common_util_.BaseInterface;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.CustomizedPlaceFragment;
import braille.kofefe.app.modules_.common_util_.GeocoderLocale;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.supports_.CheckConnectivity;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.check_permissions.PermissionCheck;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import braille.kofefe.app.supports_.url_keys.URLListKeys;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class EditProfileScreen extends CommonActivity implements BaseInterface, SocialAuthorizationCallback {
    private static final String TAG = EditProfileScreen.class.getSimpleName();

    @InjectView(R.id.textView_app_name)
    protected TextView mTextHeadingAppName;


    @InjectView(R.id.img_profile_buisness)
    protected SquareRoundCornerImageView mProfileImage;

    @InjectView(R.id.edt_full_name)
    protected EditText mEdtFullName;

    @InjectView(R.id.edt_username)
    protected EditText mEdtUsername;


    @InjectView(R.id.edt_location)
    protected EditText mEdtLocation;

    @InjectView(R.id.edt_bio)
    protected EditText mEdtBio;

    @InjectView(R.id.switch_facebook)
    protected ImageView mFbTick;

    @InjectView(R.id.switch_twitter)
    protected ImageView mTwitterTick;

    @InjectView(R.id.switch_instagram)
    protected ImageView mInstaTick;

    @InjectView(R.id.switch_snapchat)
    protected ImageView mSnapTick;

    @InjectView(R.id.textView2)
    protected ImageView mFbImage;

    @InjectView(R.id.textView21)
    protected ImageView mTwitterImage;

    @InjectView(R.id.img_instagram)
    protected ImageView mInstaImage;

    @InjectView(R.id.img_snapchat)
    protected ImageView mSnapImage;


    private String extention = "";
    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private boolean mFlagFacebook = false, mFlagTwitter = false, mFlagInsta = false;
    private SocialFbLogin mSocialLoginPresenter;
    private SocialTwitterLogin mTwitterLogin;
    private CallbackManager mCallbackManager;
    //      instagram
    private InstagramHelper instagramHelper;
    //      private ResponseReceiver receiver;
    private TwitterLoginButton loginButton;

    private CustomizedPlaceFragment mSearchLocationFragment;

    private String presignedUrl = "", mFinalUploadedProfileLink = "";
    private String path = "", image = "", mLat = "0.0", mlon = "0.0", mFullName = "", mUsername = "", mBio = "",
            mCountryName = "", mStateName = "", mCityName = "", mFb = "", mTwitter = "", mInsta = "", mSnap = "";


    private boolean presignedUrlT = false, mFullNameT = false, mUsernameT = false, mBioT = false,
            mCountryNameT = false, mFbT = false, mTwitterT = false, mInstaT = false, mSnapT = false;

    private ImageView mTempImageView;
    private String mAvailability = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);
        /***
         * Facebook
         * ***/
        FacebookSdk.sdkInitialize(mContext);
        AppEventsLogger.activateApp(this);

        ButterKnife.inject(this);
        initViews();


        //  generate hash key
        //  generateHashKey();
        intiTwitterDecalaration();
        searchLocationFragment();
        previousValues();


    }

    private void previousValues() {

        mFinalUploadedProfileLink = getSessionInstance().getUserProfilePic();

        String[] lloc = StaticValues.EditProfileContent.mLocation.split(",");

        mEdtFullName.setText(StaticValues.EditProfileContent.mFullName);
        mEdtUsername.setText(StaticValues.EditProfileContent.mUsername);
        mEdtBio.setText(StaticValues.EditProfileContent.mBio);
        mEdtLocation.setText(StaticValues.EditProfileContent.mLocation);

        mCityName = lloc[0];
        mStateName = lloc[1];
        mCountryName = lloc[2];

        mLat = StaticValues.EditProfileContent.mLat;
        mlon = StaticValues.EditProfileContent.mLon;


        mFb = StaticValues.EditProfileContent.mFacebook;
        mTwitter = StaticValues.EditProfileContent.mTwitter;
        mInsta = StaticValues.EditProfileContent.mInsta;
        mSnap = StaticValues.EditProfileContent.mSnap;


        if (!mFb.isEmpty() && !mFb.equals("null")) {
            mFbTick.setImageResource(R.mipmap.green_tick);
        } else {
            mFbTick.setImageResource(R.mipmap.question_mark);
        }

        if (!mTwitter.isEmpty() && !mTwitter.equals("null")) {
            mTwitterTick.setImageResource(R.mipmap.green_tick);
        } else {
            mTwitterTick.setImageResource(R.mipmap.question_mark);
        }

        if (!mInsta.isEmpty() && !mInsta.equals("null")) {
            mInstaTick.setImageResource(R.mipmap.green_tick);
        } else {
            mInstaTick.setImageResource(R.mipmap.question_mark);
        }

        if (!mSnap.isEmpty() && !mSnap.equals("null")) {
            mSnapTick.setImageResource(R.mipmap.green_tick);
        } else {
            mSnapTick.setImageResource(R.mipmap.question_mark);
        }

        getImageWithVolleyCircleImage(getSessionInstance().getUserProfilePic(), mProfileImage);
    }

    private void initViews() {

        mTempImageView = new ImageView(mContext);

        mEdtLocation.setFocusable(false);
        mEdtLocation.setClickable(true);

        // social login to verify a user
        if (FacebookSdk.isInitialized()) {
            LoginManager.getInstance().logOut();
        }
        mCallbackManager = CallbackManager.Factory.create();
        mSocialLoginPresenter = new SocialFbLogin(mContext, mCallbackManager, mProfileImage, mEdtFullName);

        uihandle = new UiHandleMethods(mContext);
        //    uihandle.changeColorToAppGradient(mTextHeadingAppName);
        mEdtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 3) {
                    checkUserAvailability(s.toString());
                } else {
                    mEdtUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }
        });

    }

    private Boolean checkForChangesInfo() {


        return true;
    }

    @OnClick(R.id.btn_get_started)
    public void updateProfile() {

        uihandle.hideSoftKeyboard();
        goForUpdateProfile();
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

    private void intiTwitterDecalaration() {
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
    }

    @OnClick(R.id.floating_button_image_select)
    void mImageClick() {
        // Todo: Remove when not propoer or make it proper

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!(new PermissionCheck(this).checkCamera())) {
                goForImgClick();
            }
        } else {
            goForImgClick();
        }

    }

    private void goForImgClick() {
        clearAllFlags();
        if (uihandle.hasCamera()) {
            Image_Picker_Dialog();
        } else {
            uihandle.showToast("Sorry! Camera is not available");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionCheck.PERMISSION_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (i == grantResults.length - 1) {
                        goForImgClick();
                    }
                } else {

                    // uihandle.showToast("Permission ungranted");
                    // mContext.finish();
                    // mContext.moveTaskToBack(true);

                    return;
                }
            }
        }
    }

    @OnClick(R.id.edt_location)
    void onLocationClick() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        mSearchLocationFragment.setFilter(typeFilter);
        mSearchLocationFragment.setHint("Search location");
        mSearchLocationFragment.zzJk();
    }

    //  Pass the Activity result to the onActivityResult method
    @OnClick(R.id.textView2)
    void onfacebookClick() {
        try {
            if (!(new CheckConnectivity(EditProfileScreen.this)).isNetworkAvailable()) {
                showFancyToast(TastyToast.WARNING, "Sorry! Not connected to internet");
            } else {

                clearAllFlags();
                mFlagFacebook = true;

                LoginManager.getInstance().logInWithReadPermissions(mContext, Arrays.asList("public_profile", "email", "pages_show_list", "user_posts", "user_birthday"));
                mSocialLoginPresenter.loginFacebook();
            }

        } catch (Exception e) {
            Log.e("error:", e.toString());
        }


    }

    @OnClick(R.id.textView21)
    void onTwitterClick() {

        clearAllFlags();
        mFlagTwitter = true;
        mTwitterLogin = new SocialTwitterLogin(mContext, loginButton, mEdtUsername);

    }

    @OnClick(R.id.img_instagram)
    void onInstaClick() {

        clearAllFlags();
        mFlagInsta = true;

        String scope = "basic+public_content+follower_list+comments+relationships+likes";
        //     scope is for the permissions
        instagramHelper = new InstagramHelper.Builder()
                .withClientId(Constants.ConstantInstagram.CLIENT_ID)
                .withRedirectUrl(Constants.ConstantInstagram.CALLBACKURL)
                .withScope(scope)
                .build();

        instagramHelper.loginFromActivity(mContext);

    }

    @OnClick(R.id.img_snapchat)
    void onSnapClick() {
        mSnap = "";
    }

    // on back arrow header clieck move to back
    public void regainView(View v) {
        uihandle.goBack();
    }

    // hit api for create profile
    private void goForUpdateProfile() {


        mFullName = mEdtFullName.getText().toString().trim();
        mUsername = mEdtUsername.getText().toString().trim();
        mBio = mEdtBio.getText().toString().trim();

        if (TextUtils.isEmpty(mFullName)) {
            mEdtFullName.setError("Please provide name!");
            return;
        } else if (TextUtils.isEmpty(mUsername)) {
            mEdtUsername.setError("Please provide username!");
            return;
        } else if (!mAvailability.equals("AVAILABLE")) {
            mEdtUsername.setError("Username not available!");
            return;
        } else if (TextUtils.isEmpty(mEdtLocation.getText().toString().trim())) {
            mEdtLocation.setError("Please provide location!");
            return;
        } else if (TextUtils.isEmpty(mBio)) {
            mEdtBio.setError("Please provide bio!");
            return;
        }

        //  mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //           Todo: remove after testing and uncomment above
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        //    goForCCreateProfile();

        HashMap<String, String> map = new HashMap<>();

        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_BIO, mBio);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_FACEBOOK, mFb);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_INSTAGRAM, mInsta);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_SNAPCHAT, mSnap);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_TWITTER, mTwitter);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_PROFILE_PIC, mFinalUploadedProfileLink);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_USERNAME, mUsername);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_CITY, mCityName);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_STATE, mStateName);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_COUNTRY, mCountryName);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_LAT, mLat);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_LON, mlon);
        map.put(URLListKeys.CreateProfile.CREATE_PROFILE_NAME, mFullName);

        showIOSProgress("Loading...");

        new HttpRequester(Request.Method.PUT, this, map, Constants.UPDATE_USER_PROFILE_PUT,
                URLListApis.URL_UPDATE_USER_PROFILE.replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mFlagFacebook) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (mFlagTwitter) {
            // Pass the activity result to the login button.
            loginButton.onActivityResult(requestCode, resultCode, data);
        } else if (mFlagInsta) {
            if (requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK) {
                InstagramUser user = instagramHelper.getInstagramUser(this);
                //   Picasso.with(this).load(user.getData().getProfilePicture()).into(userPhoto);
                // TODO: pass values to instagram variable
                mInsta = user.getData().getUsername();
                mEdtFullName.setText(user.getData().getUsername() + "\n" +
                        user.getData().getFullName() + "\n" + user.getData().getWebsite());
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == Constants.TAKE_PICTURE && resultCode == RESULT_OK) {
            try {
                onCaptureImageResult(photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //  path = uihandle.getRealPathFromURI(data.getData());
            //  uihandle.showToast(path);

        } else if (requestCode == Constants.SELECT_FILE && resultCode == RESULT_OK) {

            path = uihandle.getImagePath(data);
            image = path;
            // uihandle.showToast(path);
            try {
                onSelectFromGalleryResult(image, data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void getPresignedS3url(String mFileExtention) {
        //  calling api to upload image to server
        showIOSProgress("");
        HashMap<String, String> map = new HashMap<>();
        new HttpRequester(Request.Method.GET, this, map, Constants.UPLOAD_MEDIA_TO_AMAZONE_PRE_FIRE_API, URLListApis.URL_UPLOAD_MEDIA.
                replace("MEDIA_TYPE", Constants.ConstantMediaType.MEDIA_TYPE_IMAGE).replace("USECASE", Constants.ConstantUseCase.USECASE_PROFILE).
                replace("FILE_EXTENTION", mFileExtention).replace("REQUESTID_VALUE", getRandomUUID()), this);


    }

    private void checkUserAvailability(String mInputUser) {
        HashMap<String, String> map = new HashMap<String, String>();
        new HttpRequester(Request.Method.GET, this, map, Constants.GET_CHECK_USER_AVAILABILITY,
                URLListApis.URL_CHECK_USER_AVAILABILITY
                        .replace("REQUESTID_VALUE", getRandomUUID())
                        .replace("INPUT_USERNAME", mInputUser), this);
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
                                    .execute(presignedUrl, image);


                          /*    new HttpRequester(mContext, Request.Method.PUT, mTempImageView, extention,
                                      Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE,
                                    presignedUrl, this);
*/

                            // public HttpRequester(Activity mContext, int mMethod, ImageView mImageView, String mFileExtension, int serviceCode,
                            //          String url, AsyncTaskCompleteListener asyncTaskCompleteListener) {


                            //   uploadMediaToserverS3Bucket(presignedUrl, image);
                            //   goForUpload(presignedUrl, image,Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE);
                        } else {
                            showFancyToast(TastyToast.CONFUSING, "Something went wrong");
                            dismissIOSProgress();
                        }
                    } else {
                        dismissIOSProgress();
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE:
                    dismissIOSProgress();
                    String[] mTesturl = presignedUrl.split("\\?");
                    mFinalUploadedProfileLink = mTesturl[0];
                    showFancyToast(TastyToast.SUCCESS, "Successfully uploaded!");
                    // presignedUrlT = true;
                    break;

                case Constants.UPDATE_USER_PROFILE_PUT:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        String res_ = jsonObject.optString("ack");
                        if (res_.equals("SUCCESS")) {

                            JSONObject jUserProfile = jsonObject.optJSONObject("userProfile");
                            String mProfilePic = jUserProfile.optString("profilePic");

                            String mInvitation = jUserProfile.optString("invitationCode");
                            String mUsername = jUserProfile.optString("username");
                            String mName = jUserProfile.optString("name");

                            /***
                             * setting profile pic of registered user
                             * ***/
                            String sharing_content = "Hi There, I just tried the sensational KOFEFE app. I am totally loving it. I thought you might like it too. " +
                                    "Install the app and find me @" + mUsername + " .\nHere is my invitation code for u: " + mInvitation + "\n" +
                                    "Join the party here: www.thekofefeapp.com !";

                            getSessionInstance().setUserProfilePic(mProfilePic);
                            getSessionInstance().setLoggedInUsername(mUsername);
                            getSessionInstance().setLoggedInName(mName);
                            getSessionInstance().setLoggedInInvitationCode(mInvitation);
                            getSessionInstance().setShareContentForInviteFriendScreen(sharing_content);

                            showFancyToast(TastyToast.SUCCESS, res_);

                            //    uihandle.goForNext(AddFriendsScreen.class);
                            StaticValues.EditProfileContent.mProfileEdited = true;
                            StaticValues.EditProfileContent.mFlagMainFeedContentChanged = true;

                            uihandle.goBack();


                        } else {
                            try {
                                JSONObject jData = jsonObject.optJSONArray("errors").optJSONObject(0);
                                String res_data = jData.optString("id");
                                String res_message = jData.optString("message");
                                if (res_message.equals("null")) {
                                    showFancyToast(TastyToast.ERROR, res_data);
                                } else {
                                    showFancyToast(TastyToast.ERROR, res_message);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                showFancyToast(TastyToast.ERROR, jsonObject.optString("ack"));
                            }
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                 /* {
                    "ack":"FAILURE",
                    "errors":[
                   { "id":"USER_PROFILE_ERROR_1001",
                  "message":"Requested username is no longer available. Please try a different one!",
                  "errorType":"USER","errorSeverity":"ERROR","remedy":"FIX_THE_INPUT_DATA_AND_RETRY"}],"warnings":[], "userProfile":null} */

                    break;

                case Constants.GET_CHECK_USER_AVAILABILITY:
                    try {
                        //            { "ack":"SUCCESS","errors":[],"warnings":[],"userAvailability":"AVAILABLE" }
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            mAvailability = jsonObject.optString("userAvailability");

                            if (jsonObject.optString("userAvailability").equals("AVAILABLE")) {
                                mEdtUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.green_tick, 0);
                            } else {
                                mEdtUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.red_tick, 0);
                            }

                            //       showFancyToast(TastyToast.SUCCESS, jsonObject.optString("userAvailability"));
                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        showFancyToast(TastyToast.ERROR, e.toString());
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
            dismissIOSProgress();
            Log.e("Response", errorResponse);
            showFancyToast(TastyToast.ERROR, errorResponse);
            //  JSONObject jsonObject = new JSONObject(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
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

                mEdtLocation.setText(mCityName + ", " + mStateName + ", " + mCountryName);

                Log.e("LOC_DATA", mCountryName + "\n" + mStateName + "\n" + mCityName);
            }

        } catch (IOException e) {
            e.printStackTrace();
            new GeocoderLocale(mContext, EditProfileScreen.this).execute(URLListApis.URL_GEOCODER_LOCALE.replace("LATT_",
                    String.valueOf(latt_)).replace("PTGLG_", String.valueOf(long_)));

        }
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
        //   mProfileImage.setImageBitmap(null);
        //   mTempImageView.invalidateDrawable(null);

        Bitmap mBitM = uihandle.modifyOrientation(bm, mImgPath);

        mProfileImage.setImageBitmap(mBitM);
        mTempImageView.setImageBitmap(mBitM);

        extention = mImgPath.substring(mImgPath.lastIndexOf("."));
        getPresignedS3url(extention);

    }

    // processing after select image from camera as well as gallery
    private void onCaptureImageResult(Uri data) throws IOException {
        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        image = photoFile.getAbsolutePath();

        //  image = destination.getAbsolutePath();
        //  uihandle.showToast(image);
        Bitmap mBitM = uihandle.modifyOrientation(thumbnail, image);
        mProfileImage.setImageBitmap(mBitM);
        mTempImageView.setImageBitmap(mBitM);

        extention = image.substring(image.lastIndexOf("."));
        getPresignedS3url(extention);
    }

    @Override
    public void onAddressFound(String s) {
        uihandle.showToast(s);
        Log.e("location", s);

    }

    @Override
    public void onFacebookAuthorization(String mUsername) {
        mFb = mUsername;
    }

    @Override
    public void onSnapchatAuthorization(String mUsername) {
        mSnap = mUsername;
    }

    @Override
    public void onTwitterAuthorization(String mUsername) {
        mTwitter = mUsername;
    }

    private void clearAllFlags() {

        mFlagFacebook = false;
        mFlagInsta = false;
        mFlagTwitter = false;

    }

}
