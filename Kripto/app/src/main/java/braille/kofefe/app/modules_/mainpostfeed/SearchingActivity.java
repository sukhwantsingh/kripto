package braille.kofefe.app.modules_.mainpostfeed;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.mainpostfeed.adapter.AdapterSearchedUsers;
import braille.kofefe.app.modules_.mainpostfeed.callback_.IRelationshipOnClickCallback;
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeedMedia;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelScrambledMedia;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelSearchedUsers;
import braille.kofefe.app.modules_.notification.NotificationActivity;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.fcm.Config;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Snow-Dell-05 on 11/7/2017.
 **/

public class SearchingActivity extends CommonActivity implements IRelationshipOnClickCallback {

    @InjectView(R.id.recycle_searched_result)
    protected RecyclerView mRecycleSearchedResult;
    @InjectView(R.id.notification_frame)
    protected FrameLayout mFrameNotificationTopRight;
    @InjectView(R.id.img_blue_dot_notification)
    protected ImageView mImgBlueDOtNotification;

/*    @InjectView(R.id.floating_add_post)
    protected FloatingActionButton mFloatAddPost;*/

    @InjectView(R.id.edt_search)
    protected EditText mEditSearch;

    /*@InjectView(R.id.relative_wight)
    protected RelativeLayout mRelBottomSheet;*/
    @InjectView(R.id.text_people_bar)
    protected TextView mTextPeopleHeading;
    @InjectView(R.id.text_people_under)
    protected TextView mTextPostHeading;
    @InjectView(R.id.text_post_bar)
    protected TextView mTextPeopleUnder;
    @InjectView(R.id.text_post_under)
    protected TextView mTextPostUnder;


    protected MaterialSearchView searchView;


    private AdapterSearchedUsers mAdapterSearchedUser;
    private List<ModelSearchedUsers> mListSearchedUsers;
    private UiHandleMethods uihandle;
    private Activity mContext = this;
    private TextView mFollowButtonStatus;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private ModelSearchedUsers mModelSearchUsers;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        ButterKnife.inject(this);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        //
        initViews();
        implementListeners();

        try {
            // initialize Fcm
            initializeFCM();

        } catch (Exception e) {
            Log.e("error_gcm", e.toString());
        }
    }

    private void initializeFCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    //    gcm successfully registered
                    //    now subscribe to `global` topic to receive app wide notifications
                    //    Todo   FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    String mToken = intent.getStringExtra("token");
                    //    goForRegisterDevice(getFcmToken(), Build.VERSION.RELEASE, Build.MODEL);
                    Log.e("token_run", "--\n" + mToken);

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    //       new push notification is received
                    String message = intent.getStringExtra("message");
                    Log.e("msg", message);
                    StaticValues.mNotificationBlinking = true;

                    mImgBlueDOtNotification.setVisibility(View.VISIBLE);
                    mImgBlueDOtNotification.startAnimation(getBlinkingAnimation());

                    //       Todo: show message successfully
                }
            }
        };


  /* Gcm initialisationstarts ends Here */


    }

    /*  @OnClick(R.id.img_user_profile)
         public void showEnlargePick() {
      }
    */

    public void registerReceiver() {
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        // Todo: clear notification area if app is opened where you want to show message like whatsapp :)
        //  NotificationUtils.clearNotifications(getApplicationContext());


    }

    @Override
    protected void onPause() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            isReceiverRegistered = false;
        } catch (Exception ex) {
            Log.e("error: ", ex.toString());
        }

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void implementListeners() {
      /*  mFloatAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AddPostActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });*/
        mEditSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    //         Perform action on key press
                    uihandle.hideSoftKeyboard();
                    goForSearchUsers(mEditSearch.getText().toString());

                    return true;
                }
                return false;
            }
        });

        mEditSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mEditSearch.getRight() - (mEditSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                        // your action here
                        uihandle.hideSoftKeyboard();
                        goForSearchUsers(mEditSearch.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });


        /*** on item listener to get deatial for the searchecd result  **/
      /*  mRecycleSearchedResult.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        ModelSearchedUsers mdata = mListSearchedUsers.get(position);
                        StaticValues.mSearechedUserUUID = mdata.getUuid();

                        uihandle.goForNext(ProfileActivity.class);
                    }
                }) {

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });*/
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                uihandle.hideSoftKeyboard();
                goForSearchUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searhing_, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    private void initViews() {
        uihandle = new UiHandleMethods(mContext);
        mListSearchedUsers = new ArrayList<>();

        //    mRelBottomSheet.setAnimation(uihandle.slideDown());
        //    mRelBottomSheet.setAnimation(uihandle.slideUp());

        mRecycleSearchedResult.setLayoutManager(new LinearLayoutManager(mContext));
       /* mRecycleSearchedResult.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    Log.e("scrolled", "Scrolled Downwards");  // hide krna
                    //   mRelBottomSheet
                    mFloatAddPost.hide();
                    mRelBottomSheet.setVisibility(View.GONE);

                } else if (dy < 0) {
                    Log.e("scrolled", "Scrolled Upwards"); // show  krna

                    mRelBottomSheet.setVisibility(View.VISIBLE);
                    mFloatAddPost.show();

                } }
        });*/

        mFrameNotificationTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticValues.mNotificationBlinking = false;
                mImgBlueDOtNotification.clearAnimation();
                mImgBlueDOtNotification.setVisibility(View.GONE);

                uihandle.goForNextScreen(NotificationActivity.class);

            }
        });
    }

    public void goForProfile(View v) {
        uihandle.goForNext(ProfileActivity.class);
    }

    private void goForSearchUsers(String mUsernameToBeSearched) {
        //      mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //      Todo: Remove after testing and uncomment above
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Searching...");
        new HttpRequester(Request.Method.GET, this, map,
                Constants.SEARCH_GET, URLListApis.URL_SEARCH_USERS.replace("SEARCHED_STRING", mUsernameToBeSearched), this);
    }

    private void goForFollowUser(String receiverUUID, TextView mFollowBtn) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }
        mFollowButtonStatus = mFollowBtn;

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Following...");

        /*new HttpRequester(Request.Method.POST, this, map,
                Constants.FOLLOW_USER_POST, URLListApis.URL_FOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);*/
        new HttpRequester("", Request.Method.POST, this, Constants.FOLLOW_USER_POST, URLListApis.URL_FOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    private void goForUnFollowUser(String receiverUUID, TextView mFollowBtn) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }
        mFollowButtonStatus = mFollowBtn;
        showIOSProgress("UnFollowing...");

        /*new HttpRequester(Request.Method.POST, this, map,
                Constants.FOLLOW_USER_POST, URLListApis.URL_FOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);*/

        new HttpRequester("", Request.Method.POST, this, Constants.UNFOLLOW_USER_POST, URLListApis.URL_UNFOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (StaticValues.mNotificationBlinking) {
            mImgBlueDOtNotification.setVisibility(View.VISIBLE);
            mImgBlueDOtNotification.startAnimation(getBlinkingAnimation());
        } else {
            mImgBlueDOtNotification.setVisibility(View.GONE);
        }

        // registering broadcast
        try {
            registerReceiver();
        } catch (Exception ex) {
            Log.e("error: ", ex.toString());
        }

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
                case Constants.SEARCH_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            refineJSON(jsonObject);
                        } else {
                            showFancyToast(TastyToast.ERROR, "No result for query!");
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;
                case Constants.FOLLOW_USER_POST:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            //   refineJSON(jsonObject);
                            mFollowButtonStatus.setText("Following");
                            mModelSearchUsers.setRelationship(ConstantStatusInAPI.RELATIONSHIP_FOLLOWED);
                            mAdapterSearchedUser.notifyDataSetChanged();

                            //    mFollowButtonStatus.setEnabled(false);
                            String messageToUser = jsonObject.optString("messageToUser");

                            if (!messageToUser.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, messageToUser);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;
                case Constants.UNFOLLOW_USER_POST:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            //   refineJSON(jsonObject);
                            mFollowButtonStatus.setText("Follow");
                            mModelSearchUsers.setRelationship(ConstantStatusInAPI.RELATIONSHIP_NONE);
                            mAdapterSearchedUser.notifyDataSetChanged();
                            String messageToUser = jsonObject.optString("messageToUser");

                            if (!messageToUser.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, messageToUser);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            }

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
            dismissIOSProgress();
            Log.e("Response", errorResponse);
            showFancyToast(TastyToast.ERROR, errorResponse);
            //    JSONObject jsonObject = new JSONObject(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refineJSON(JSONObject mJsonObj) {
        try {
       /* **
       * {"ack": "SUCCESS", "errors": [], "warnings": [], "messageToUser": null, "matchingPosts": [], "matchingUsers": [] }
       * ***/

            Log.e("response", mJsonObj.toString());
            if (mAdapterSearchedUser != null) {
                mListSearchedUsers.clear();
                mAdapterSearchedUser.notifyDataSetChanged();
            }
            JSONArray nArray = mJsonObj.optJSONArray("matchingUsers");
            if (nArray != null && nArray.length() > 0) {
                /**
                 * Clear the list before entry valus to list
                 **/
                for (int i = 0; i < nArray.length(); i++) {

                    JSONObject jData = nArray.optJSONObject(i);

                    String name = jData.optString("name");
                    String userName = jData.optString("userName");
                    String profilePic = jData.optString("profilePic");
                    String relationship = jData.optString("relationship");
                    String uuid = jData.optString("uuid");

                    String bio = jData.optString("bio");
                    String followersCount = jData.optString("followersCount");
                    String followingCount = jData.optString("followingCount");
                    String joined = getDataTimeFromMilliseconds(Long.parseLong(jData.optString("joined")));

                    JSONObject mJsonLoc = jData.optJSONObject("location");

                    String loc_name = mJsonLoc.optString("name");
                    String loc_city = mJsonLoc.optString("city");
                    String loc_state = mJsonLoc.optString("state");
                    String loc_country = mJsonLoc.optString("country");

                    mListSearchedUsers.add(new ModelSearchedUsers(name, userName, profilePic, relationship, uuid,
                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country, false,
                            "", "", "", "", "", "", "", "", null, null, ""));
                }
            }

            JSONArray nArrayMatchingPost = mJsonObj.optJSONArray("matchingPosts");
            if (nArrayMatchingPost != null && nArrayMatchingPost.length() > 0) {
                for (int i = 0; i < nArrayMatchingPost.length(); i++) {

                    JSONObject jDataa = nArrayMatchingPost.optJSONObject(i);
                    JSONObject jData = jDataa.optJSONObject("user");

                    String name = jData.optString("name");
                    String userName = jData.optString("userName");
                    String profilePic = jData.optString("profilePic");
                    String relationship = jData.optString("relationship");
                    String uuid = jData.optString("uuid");

                    String bio = jData.optString("bio");
                    String followersCount = jData.optString("followersCount");
                    String followingCount = jData.optString("followingCount");
                    String joined = getDataTimeFromMilliseconds(Long.parseLong(jData.optString("joined")));

                    JSONObject mJsonLoc = jData.optJSONObject("location");

                    String loc_name = mJsonLoc.optString("name");
                    String loc_city = mJsonLoc.optString("city");
                    String loc_state = mJsonLoc.optString("state");
                    String loc_country = mJsonLoc.optString("country");

                    String postUserName = userName;
                    String postTime = displayQuoteTime(Long.parseLong(jDataa.optString("postedDate")));
                    String postContent = jDataa.optString("defaultScrambledContent");
                    String content = jDataa.optString("content");

                    JSONObject jPostLocation = jDataa.optJSONObject("location");
                    String location_name = "";
                    String location_city = "";
                    String location_state = "";
                    String location_country = "";

                    if (jPostLocation != null) {
                        location_name = jPostLocation.optString("name");
                        location_city = jPostLocation.optString("city");
                        location_state = jPostLocation.optString("state");
                        location_country = jPostLocation.optString("country");
                    }

                    JSONArray jMediaData = jDataa.optJSONArray("media");
                    ModelFeedMedia[] mModelFeedMedia = null;
                    if (jMediaData != null) {
                        mModelFeedMedia = new ModelFeedMedia[jMediaData.length()];

                        for (int j = 0; j < jMediaData.length(); j++) {
                            JSONObject jMediaObj = jMediaData.optJSONObject(j);

                            String mediaUrl = jMediaObj.optString("mediaUrl");
                            String mediaType = jMediaObj.optString("mediaType");
                            String thumbnailUrl = jMediaObj.optString("thumbnailUrl");
                            mModelFeedMedia[j] = new ModelFeedMedia(mediaUrl, mediaType, thumbnailUrl);
                        }
                    }

                    JSONArray jScrambledMediaData = jDataa.optJSONArray("scrambledMedia");
                    ModelScrambledMedia[] mModelScrambledMedia = null;
                    if (jScrambledMediaData != null) {
                        mModelScrambledMedia = new ModelScrambledMedia[jScrambledMediaData.length()];

                        for (int j = 0; j < jScrambledMediaData.length(); j++) {
                            JSONObject jMediaObj = jScrambledMediaData.optJSONObject(j);

                            String mediaUrl = jMediaObj.optString("mediaUrl");
                            String mediaType = jMediaObj.optString("mediaType");
                            mModelScrambledMedia[j] = new ModelScrambledMedia(mediaUrl, mediaType);
                        }
                    }

                    String postLockStatus = jDataa.optString("postLockStatus");  //

                    mListSearchedUsers.add(new ModelSearchedUsers(name, userName, profilePic, relationship, uuid,
                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country, true,
                            postUserName, postTime, postContent, content, location_name, location_city, location_state,
                            location_country, mModelFeedMedia, mModelScrambledMedia, postLockStatus));
                }
            }


            if (mListSearchedUsers.size() > 0) {
                mRecycleSearchedResult.setVisibility(View.VISIBLE);

                mTextPeopleHeading.setVisibility(View.GONE);
                mTextPostHeading.setVisibility(View.GONE);
                mTextPeopleUnder.setVisibility(View.GONE);
                mTextPostUnder.setVisibility(View.GONE);

                mAdapterSearchedUser = new AdapterSearchedUsers(mContext, mListSearchedUsers);
                mRecycleSearchedResult.setAdapter(mAdapterSearchedUser);
            } else {

                mRecycleSearchedResult.setVisibility(View.GONE);

                mTextPeopleHeading.setVisibility(View.VISIBLE);
                mTextPostHeading.setVisibility(View.VISIBLE);
                mTextPeopleUnder.setVisibility(View.VISIBLE);
                mTextPostUnder.setVisibility(View.VISIBLE);


            }

        } catch (Exception ex) {
            Log.e("feed", ex.toString());
        }
    }

    public void goBackP(View v) {
        uihandle.hideSoftKeyboard();
        uihandle.goBack();
    }

    @Override
    public void onRelationshipClick(String mRelation, ModelSearchedUsers mModelSearchUsers, TextView mFollowBtn) {
        this.mModelSearchUsers = mModelSearchUsers;

        if (mFollowBtn.getText().toString().trim().equals("Follow")) {
            goForFollowUser(mModelSearchUsers.getUuid(), mFollowBtn);
        } else {
            alertForUnFollowUserUnlocked(mFollowBtn);
        }
    }

    private void alertForUnFollowUserUnlocked(final TextView mTextFollowingBtn) {
        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("Unfollow " + mModelSearchUsers.getName())
                .positiveText("Unfollow")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        goForUnFollowUser(mModelSearchUsers.getUuid(), mTextFollowingBtn);
                    }
                })
                .negativeText("Cancel")
                .show();

    }


}
