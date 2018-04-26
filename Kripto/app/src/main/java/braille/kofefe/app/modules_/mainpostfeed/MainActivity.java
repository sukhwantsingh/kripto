package braille.kofefe.app.modules_.mainpostfeed;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.LinearSmoothScrollingCustom;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.mainpostfeed.adapter.AdapterMainPost;
import braille.kofefe.app.modules_.mainpostfeed.callback_.MainFeedCallback;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.PostLockActivityList;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeed;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeedMedia;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelScrambledMedia;
import braille.kofefe.app.modules_.mainpostfeed.util.SwipeController;
import braille.kofefe.app.modules_.notification.NotificationActivity;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.fcm.Config;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Snow-Dell-05 on 11/7/2017.
 */

public class MainActivity extends CommonActivity implements View.OnClickListener, MainFeedCallback {

    @InjectView(R.id.img_bottom_profile)
    protected ImageView mImageProfile;
    @InjectView(R.id.img_bottom_search)
    protected ImageView mImageSearch;

    @InjectView(R.id.notification_frame)
    protected FrameLayout mFrameNotificationTopRight;
    @InjectView(R.id.mImg_registered_user)
    protected ImageView mImgRegisteredUser;
    @InjectView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.floating_add_post)
    protected FloatingActionButton mFloatAddPost;
    @InjectView(R.id.loading_progress)
    protected ProgressBar mProgressLoading;

    @InjectView(R.id.lst_posts)
    protected RecyclerView mRecyclerPosts;

    @InjectView(R.id.img_blue_dot_notification)
    protected ImageView mImgBlueDOtNotification;

    @InjectView(R.id.relative_wight)
    protected RelativeLayout mRelBottomSheet;

    DonutProgress mProgressi;
    TextView mTextPosti;
    FrameLayout mFrameProgi;
    TextView mTextUnlockTimeri;
    SwipeController swipeController;
    int po_openUnlocked_post;
    TextView mTextbackground;
    private int po;
    private LinearLayoutManager mLinearLayoutManager;
    private AdapterMainPost mAdapterFeed;
    private List<ModelFeed> mListFeed;
    private List<ModelFeed> mListFeedTemp;
    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private int mPositionOfFeedAtBottom = 0;
    private boolean mFlagNoMoreFeed = false;
    private String mPostHeadId = "";
    private boolean mFlagPostHeadId = true;
    private boolean mFlagPostAvailable = false;
    private boolean mFlagScrolledUp = true;
    private ModelFeed mFeedData;
    private int timerInSecs;
    private BubbleLayout bubbleLayout;
    private PopupWindow popupWindow;
    private ImageView mImgLike, mImgLaugh, mImgSad, mImgLove, mImgAngry;
    private ImageView mSmilyLike, mSmilyLaugh, mSmilySad, mSmilyLove, mSmilyAngry;
    private String mPostIdForReactions;
    private ImageView mImgTempReaction = null;
    /* FCM decalaration  */
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        //    getContactss();

        initViews();
        implementListeners();
        callingApi();

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

        //    goForRegisterDevice(getFcmToken(), Build.VERSION.RELEASE, Build.MODEL);
        Log.e("token_run", getFcmToken());


  /* Gcm initialisationstarts ends Here */


    }

    //  Fetches reg id from shared preferences
    //  And displays on the screen
    private String getFcmToken() {
        String deviceToken = getSessionInstance().getDeviceFCMToken();
        if (deviceToken != null) {
            return deviceToken;
        } else {
            Log.e("msg", "Firebase Reg Id is not received yet!");
            return null;
        }

    }

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

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)
                        .show();
            } else {
                Log.i("gcm", "device not supported");
                finish();
            }
            return false;
        }
        return true;
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

    private void initViews() {


        mListFeed = new ArrayList<>();
        mListFeedTemp = new ArrayList<>();

        uihandle = new UiHandleMethods(mContext);

        mRelBottomSheet.setAnimation(uihandle.slideDown());
        mRelBottomSheet.setAnimation(uihandle.slideUp());


        bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.emoji_views, null);
        popupWindow = BubblePopupHelper.create(this, bubbleLayout);


        mRecyclerPosts.setLayoutManager(new LinearSmoothScrollingCustom(mContext));
       /* // Vertical
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerPosts, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);*/
        mRecyclerPosts.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mFlagScrolledUp = false;
                    Log.e("scrolled", "Scrolled Downwards");  // hide krna
                    //   mRelBottomSheet

                    mFloatAddPost.hide();
                    mRelBottomSheet.setVisibility(View.GONE);
                } else if (dy < 0) {
                    Log.e("scrolled", "Scrolled Upwards"); // show  krna

                    mRelBottomSheet.setVisibility(View.VISIBLE);
                    mFloatAddPost.show();

                } else if (dy == 0) {
                    mFlagScrolledUp = true;
                    Log.e("scrolled", "Scrolled Top");
                } else {
                    mFlagScrolledUp = true;
                    Log.e("scrolled", "No Vertical Scrolled");
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mImgLaugh = bubbleLayout.findViewById(R.id.rv1_laugh);
        mImgLike = bubbleLayout.findViewById(R.id.rv2_like);
        mImgSad = bubbleLayout.findViewById(R.id.rv3_sad);
        mImgLove = bubbleLayout.findViewById(R.id.rv4_love);
        mImgAngry = bubbleLayout.findViewById(R.id.rv5_angry);


        // calling register device api

        if (getSessionInstance().isDeviceTokenRefreshedFlag()) {
            goForRegisterDevice(getFcmToken(), Build.VERSION.RELEASE, Build.MODEL);

        }


    }


    @Override
    protected void onResume() {
        super.onResume();


        if (StaticValues.mNotificationBlinking) {
            mImgBlueDOtNotification.setVisibility(View.VISIBLE);
            mImgBlueDOtNotification.startAnimation(getBlinkingAnimation());
        } else {
            mImgBlueDOtNotification.clearAnimation();
            mImgBlueDOtNotification.setVisibility(View.GONE);
        }

        if (StaticValues.mFlagCommentCountManager) {
            mListFeed.get(po).setCommentsCount(StaticValues.mCommentCountFinal);
            mAdapterFeed.notifyDataSetChanged();
            StaticValues.mFlagCommentCountManager = false;
        }


        // registering broadcast
        try {
            registerReceiver();
        } catch (Exception ex) {
            Log.e("error: ", ex.toString());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.notification_frame:
                StaticValues.mNotificationBlinking = false;
                mImgBlueDOtNotification.clearAnimation();
                mImgBlueDOtNotification.setVisibility(View.GONE);
                uihandle.goForNextScreen(NotificationActivity.class);
                break;

            case R.id.img_bottom_profile:
                startActivity(new Intent(mContext, ProfileActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.img_bottom_search:
                startActivity(new Intent(mContext, SearchingActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.floating_add_post:
                startActivity(new Intent(mContext, AddPostActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;


            case R.id.rv1_laugh:
                //    this.mSmilyLaugh.setVisibility(View.VISIBLE);
                this.mSmilyLaugh.setImageResource(R.mipmap.laugh_24);
                mImgTempReaction = this.mSmilyLaugh;
                goForPutReactions(mPostIdForReactions, Constants.ConstantPutReactions.REACTION_LAUGH);
                popupWindow.dismiss();
                break;

            case R.id.rv2_like:
                //     this.mSmilyLike.setVisibility(View.VISIBLE);
                this.mSmilyLike.setImageResource(R.mipmap.like_noti);
                mImgTempReaction = this.mSmilyLike;
                goForPutReactions(mPostIdForReactions, Constants.ConstantPutReactions.REACTION_LIKED);
                popupWindow.dismiss();

                break;

            case R.id.rv3_sad:
                //     this.mSmilySad.setVisibility(View.VISIBLE);
                this.mSmilySad.setImageResource(R.mipmap.sad_noti);
                mImgTempReaction = this.mSmilySad;
                goForPutReactions(mPostIdForReactions, Constants.ConstantPutReactions.REACTION_SAD);
                popupWindow.dismiss();

                break;

            case R.id.rv4_love:
                //     this.mSmilyLove.setVisibility(View.VISIBLE);
                this.mSmilyLove.setImageResource(R.mipmap.heart_noti);
                mImgTempReaction = this.mSmilyLove;
                goForPutReactions(mPostIdForReactions, Constants.ConstantPutReactions.REACTION_LOVE);
                popupWindow.dismiss();
                break;

            case R.id.rv5_angry:
                //    this.mSmilyAngry.setVisibility(View.VISIBLE);
                this.mSmilyAngry.setImageResource(R.mipmap.sleep_noti);
                mImgTempReaction = this.mSmilyAngry;
                goForPutReactions(mPostIdForReactions, Constants.ConstantPutReactions.REACTION_ANGER);
                popupWindow.dismiss();
                break;

        }

    }

    private void implementListeners() {
        mFrameNotificationTopRight.setOnClickListener(this);
        mImageProfile.setOnClickListener(this);
        mImageSearch.setOnClickListener(this);
        mFloatAddPost.setOnClickListener(this);

        mImgLaugh.setOnClickListener(this);
        mImgLike.setOnClickListener(this);
        mImgSad.setOnClickListener(this);
        mImgLove.setOnClickListener(this);
        mImgAngry.setOnClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //      Refresh items
                refreshItems();
            }
        });

        //    CommentScreen.java


    }

    private void callingApi() {
        goForLatestFeed();
        //   mAdapterFeed = new AdapterMainPost(mContext, mListFeed);
        //   mRecyclerPosts.setAdapter(mAdapterFeed);
    }

    @OnClick(R.id.img_home_)
    public void onHomeClick() {
        mRecyclerPosts.smoothScrollToPosition(0);
    }


    @Override
    public void getFeedInfo(int position) {
        uihandle.goForNextScreen(ProfileActivity.class);
    }

    @Override
    public void getMoreOldFeeds(int position) {
        mPositionOfFeedAtBottom = position + 1;

        if (!mFlagNoMoreFeed) {
            //    showFancyToast(TastyToast.SUCCESS, "Bottom Success" + position);
            goForMoreOldFeeds();
        }
    }

    @Override
    public void getPostLockActivityList(ModelFeed mModelFeed, int position) {
        StaticValues.mPostId = mModelFeed.getUuid();
        uihandle.goForNextScreen(PostLockActivityList.class);


    }

    @Override
    public void unlockPost(TextView mTextBackgroundTimer, int position, ModelFeed mFeedData, int timerInSecs, final DonutProgress mProgress, final TextView mTextPost,
                           final FrameLayout mFrameProg, final TextView mTextUnlockTimer) {

        this.po_openUnlocked_post = position;
        this.mFeedData = mFeedData;
        this.timerInSecs = timerInSecs;

        this.mTextbackground = mTextBackgroundTimer;
        this.mProgressi = mProgress;
        this.mTextPosti = mTextPost;
        this.mFrameProgi = mFrameProg;
        this.mTextUnlockTimeri = mTextUnlockTimer;

        goForUnlockPost(mFeedData.getUuid());

    }

    private void goForLatestFeed() {
        mFlagPostHeadId = true;

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");

        new HttpRequester(Request.Method.GET, this, map,
                Constants.GET_FEED_LATEST, URLListApis.URL_GET_FEED_LATEST.replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForUnlockPost(String postId) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        //  showIOSProgress("Loading...");

        new HttpRequester(Request.Method.GET, this, map,
                Constants.UNLOCK_POST_GET, URLListApis.URL_UNLOCK_POST.replace("POST_ID", postId)
                .replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForOpenAnUnlocked(String postId) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        //  showIOSProgress("Loading...");

        new HttpRequester(Request.Method.GET, this, map,
                Constants.OPEN_AN_UNLOCKED_POST_POST, URLListApis.URL_OPEN_AN_UNLOCKED_POST.replace("POST_ID", postId)
                .replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForMoreOldFeeds() {
        //      mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //      Todo: Remove after testing and uncomment above
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        mProgressLoading.setVisibility(View.VISIBLE);
        new HttpRequester(Request.Method.GET, this, map,
                Constants.GET_FEED_MORE, URLListApis.URL_GET_FEED_MORE.replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForCheckNewPostAvailableOrNot(String TopHeadPostId) {
        //      mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //      Todo: Remove after testing and uncomment above
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");
        new HttpRequester(Request.Method.GET, this, map,
                Constants.GET_FEED_STATUS, URLListApis.URL_GET_FEED_STATUS.replace("TOP_CHECK_MAT", TopHeadPostId)
                .replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForRegisterDevice(String mToken, String mOsVersion, String mDeviceModel) {

        String mFinalDataToSend = "{\"deviceType\" : \"ANDROID\",\"osVersion\" : \"" + mOsVersion + "\",\"deviceModel\" : \"" + mDeviceModel + "\",\"fcmToken\" : \"" + mToken + "\"}";

        //  public HttpRequester(String mCreatePost, int mMethod, Activity activity, int serviceCode,
        // String url, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        new HttpRequester(mFinalDataToSend, Request.Method.POST, this,
                Constants.REGISTER_DEVICE_POST, URLListApis.URL_DEVICE_REGISTER, this);
    }

    //     hit api for putReactions
    private void goForPutReactions(String mPostIdd, String mReactionType) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");
        //      rest/reaction/POST_ID/WHAT_YOU_WANT?requestId=REQUESTID_VALUE"
        new HttpRequester(Request.Method.PUT, this, map, Constants.PUT_REACTION_PUT,
                URLListApis.URL_PUT_REACTIONS
                        .replace("POST_ID", mPostIdd)
                        .replace("WHAT_YOU_WANT", mReactionType)
                        .replace("REQUESTID_VALUE", getRandomUUID()), this);
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
                case Constants.REGISTER_DEVICE_POST:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //            showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            String mStatus = jsonObject.optString("status");
                            if (mStatus.equals("SUCCESS")) {
                                Log.e("device", "Device Register Successfully");
                                getSessionInstance().setDeviceTokenRefreshedFlag(false);
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;
                case Constants.GET_FEED_LATEST:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            List<ModelFeed> mTempList = refineJSON(jsonObject);
                            if (mTempList != null) {
                                mFlagNoMoreFeed = false;

                                mListFeed.clear();

                                mListFeed.addAll(mTempList);
                                mAdapterFeed = new AdapterMainPost(mContext, mListFeed);
                                mRecyclerPosts.setAdapter(mAdapterFeed);

                            } else {

                                mAdapterFeed = new AdapterMainPost(mContext, mListFeed);
                                mRecyclerPosts.setAdapter(mAdapterFeed);
                                //       true flag to avoid run botton scroll api for more posts
                                mFlagNoMoreFeed = true;
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;
                case Constants.GET_FEED_STATUS:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //            showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            boolean mNewPostAvailable = jsonObject.optBoolean("newPostAvailable");

                            if (mNewPostAvailable) {
                                uihandle.showToast("post available");

                                mFlagPostAvailable = true;
                                mFlagPostHeadId = true;

                                //  go for latest field
                                goForLatestFeed();

                            } else {
                                mFlagPostAvailable = false;
                                mFlagPostHeadId = false;

                                uihandle.showToast("No new post available!");
                            }

                            // refineJSON(jsonObject);
                            // To check for new post available or not


                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;
                case Constants.GET_FEED_MORE:
                    mProgressLoading.setVisibility(View.GONE);
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //    showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             ** refine response from server
                             ***/
                            List<ModelFeed> mTempList = refineJSON(jsonObject);
                            if (mTempList != null) {
                                mListFeed.addAll(mPositionOfFeedAtBottom, mTempList);
                                mAdapterFeed.notifyDataSetChanged();
                            } else {
                                mFlagNoMoreFeed = true;
                            }
                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;

                case Constants.UNLOCK_POST_GET:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //    showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             ** refine response from server
                             ***/
                            String mTimerInSecs = jsonObject.optString("timeerInSecs");


                            if (!jsonObject.optString("messageToUser").equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("messageToUser"));
                            }
                            //   private void scheduleTimer(int timerInSecs, CountDownTimer mCountTimer, final DonutProgress mProgress,
                            //   final TextView mTextPost, final FrameLayout mFrameProg, final TextView mTextUnlockTimer, final ImageView mImageCover) {

                            //    call timer on success ack

                            mFeedData.setUnlocked(jsonObject.optString("unlockedCount"));
                            mFeedData.setUnlockInProgress(jsonObject.optString("unlockInProgressCount"));
                            mAdapterFeed.notifyDataSetChanged();

                            if (mFeedData.getPostLockStatus().equals(Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OLD_POST_FIRST_TIME_VIEW)) {
                                mProgressi.setMax(14);
                                scheduleTimer(po_openUnlocked_post, mFeedData, 15, mTextUnlockTimeri, this.mProgressi, mTextbackground);
                            } else {
                                mProgressi.setMax(Integer.parseInt(mTimerInSecs) - 1);
                                scheduleTimer(po_openUnlocked_post, mFeedData, Integer.parseInt(mTimerInSecs), mTextUnlockTimeri, this.mProgressi, mTextbackground);
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;
                case Constants.OPEN_AN_UNLOCKED_POST_POST:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //    showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             ** refine response from server
                             ***/
                            if (!jsonObject.optString("messageToUser").equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("messageToUser"));
                            }

                          /*  JSONObject mJData = jsonObject.optJSONObject("post");
                            String mPostLockStatus = mJData.optString("postLockStatus");
                            String mUnlocked = mJData.optString("unlocked");

                            //       set the lock status
                            mFeedData.setUnlocked(mUnlocked);
                            mFeedData.setPostLockStatus(mPostLockStatus);
                            mAdapterFeed.notifyDataSetChanged();
*/
                            ModelFeed mTempList = refineJsonModel(jsonObject);
                            if (mTempList != null) {
                                mAdapterFeed.replaceItem(mTempList, po_openUnlocked_post);
                                mAdapterFeed.notifyDataSetChanged();

                            }
                            //        call   timer  on  success ack
                            //        mProgressi.setMax(Integer.parseInt(mTimerInSecs));

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;
                case Constants.PUT_REACTION_PUT:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //    showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             ** refine response from server
                             ***/
                            if (!jsonObject.optString("messageToUser").equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("messageToUser"));
                            }

                            if (jsonObject.optString("status").equals("SUCCESS")) {
                                mImgTempReaction.setVisibility(View.VISIBLE);
                            } else {
                                showFancyToast(TastyToast.ERROR, "Something went wrong!");
                            }


                            String mUpdatedCount = jsonObject.optString("updatedCount");
                            mFeedData.setTotalReactions(mUpdatedCount);
                            mAdapterFeed.notifyDataSetChanged();

                           /* JSONObject mJData = jsonObject.optJSONObject("post");
                            String mPostLockStatus = mJData.optString("postLockStatus");
                            String mUnlocked = mJData.optString("unlocked");

                            //       set the lock status
                            mFeedData.setUnlocked(mUnlocked);
                            mFeedData.setPostLockStatus(mPostLockStatus);
                            mAdapterFeed.notifyDataSetChanged();
*/
                            //        call   timer  on  success ack
                            //        mProgressi.setMax(Integer.parseInt(mTimerInSecs));

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
            mProgressLoading.setVisibility(View.GONE);
            Log.e("Response", errorResponse);
            showFancyToast(TastyToast.ERROR, errorResponse);
            //    JSONObject jsonObject = new JSONObject(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private List<ModelFeed> refineJSON(JSONObject mJsonObj) {
        try {
            Log.e("response", mJsonObj.toString());
            JSONArray nArray = mJsonObj.optJSONArray("posts");
            if (nArray != null && nArray.length() > 0) {

                if (!mListFeedTemp.isEmpty()) {
                    mListFeedTemp.clear();
                }
                for (int i = 0; i < nArray.length(); i++) {

                    JSONObject jData = nArray.optJSONObject(i);
                    JSONObject jUser = jData.optJSONObject("user");

                    String user_name = jUser.optString("name");
                    String user_userName = jUser.optString("userName");
                    String user_profilePic = jUser.optString("profilePic");
                    String user_relationship = jUser.optString("relationship");
                    String user_uuid = jUser.optString("uuid");

                    String user_bio = jUser.optString("bio");
                    String user_followersCount = jUser.optString("followersCount");
                    String user_followingCount = jUser.optString("followingCount");
                    String user_joined = getDataTimeFromMilliseconds(Long.parseLong(jUser.optString("joined")));

                    JSONObject jUserLocation = jUser.optJSONObject("location");

                    String user_loc_name = jUserLocation.optString("name");
                    String user_loc_city = jUserLocation.optString("city");
                    String user_loc_state = jUserLocation.optString("state");
                    String user_loc_country = jUserLocation.optString("country");


                    String content = jData.optString("content");
                    String defaultScrambledContent = jData.optString("defaultScrambledContent");
                    String postedDate = displayQuoteTime(Long.parseLong(jData.optString("postedDate")));
                    String unlockInProgress = numberInShortFormat(Long.parseLong(jData.optString("unlockInProgress")));
                    String unlocked = numberInShortFormat(Long.parseLong(jData.optString("unlocked")));


                    String liked = numberInShortFormat(Long.parseLong(jData.optString("liked")));
                    String anger = numberInShortFormat(Long.parseLong(jData.optString("anger")));
                    String sad = numberInShortFormat(Long.parseLong(jData.optString("sad")));
                    String love = numberInShortFormat(Long.parseLong(jData.optString("love")));
                    String laugh = numberInShortFormat(Long.parseLong(jData.optString("laugh")));

                    String timerInSecs = jData.optString("timerInSecs");
                    String commentsCount = jData.optString("commentsCount");
                    String lockExpiry = jData.optString("lockExpiry");

                    String reactedUser = jData.optString("referenceUser");
                    String reactionType = jData.optString("userReactionType");
                    String referenceReactionType = jData.optString("referenceReactionType");

                    JSONObject jPostLocation = jData.optJSONObject("location");
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

                    String uuid = jData.optString("uuid");

                    if (mFlagPostHeadId) {
                        mPostHeadId = uuid;
                        mFlagPostHeadId = false;
                    }

                    JSONArray jMediaData = jData.optJSONArray("media");
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

                    JSONArray jScrambledMediaData = jData.optJSONArray("scrambledMedia");
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

                    String postLockStatus = jData.optString("postLockStatus");  //
                    String totalReactions = jData.optString("totalReactions");


                    mListFeedTemp.add(new ModelFeed(user_name, user_userName, user_profilePic, user_relationship, user_uuid, user_bio, user_followersCount,
                            user_followingCount, user_joined, user_loc_name, user_loc_city, user_loc_state, user_loc_country, content, defaultScrambledContent, postedDate,
                            unlockInProgress, unlocked, liked, anger, sad, love, laugh, timerInSecs, commentsCount,
                            lockExpiry, reactedUser, reactionType, referenceReactionType, location_name, location_city, location_state, location_country, uuid, mModelFeedMedia, mModelScrambledMedia, postLockStatus, totalReactions));


                }

                return mListFeedTemp;
            } else {
                Log.i("feed", "empty");
                return null;
            }

        } catch (Exception ex) {
            Log.i("feed", ex.toString());
            return null;
        }


    }

    private ModelFeed refineJsonModel(JSONObject mJsonObj) {


        JSONObject jData = mJsonObj.optJSONObject("post");
        if (jData != null) {

            JSONObject jUser = jData.optJSONObject("user");

            String user_name = jUser.optString("name");
            String user_userName = jUser.optString("userName");
            String user_profilePic = jUser.optString("profilePic");
            String user_relationship = jUser.optString("relationship");
            String user_uuid = jUser.optString("uuid");

            String user_bio = jUser.optString("bio");
            String user_followersCount = jUser.optString("followersCount");
            String user_followingCount = jUser.optString("followingCount");
            String user_joined = getDataTimeFromMilliseconds(Long.parseLong(jUser.optString("joined")));

            JSONObject jUserLocation = jUser.optJSONObject("location");

            String user_loc_name = jUserLocation.optString("name");
            String user_loc_city = jUserLocation.optString("city");
            String user_loc_state = jUserLocation.optString("state");
            String user_loc_country = jUserLocation.optString("country");

            String content = jData.optString("content");
            String defaultScrambledContent = jData.optString("defaultScrambledContent");
            String postedDate = displayQuoteTime(Long.parseLong(jData.optString("postedDate")));
            String unlockInProgress = numberInShortFormat(Long.parseLong(jData.optString("unlockInProgress")));
            String unlocked = numberInShortFormat(Long.parseLong(jData.optString("unlocked")));

            String liked = numberInShortFormat(Long.parseLong(jData.optString("liked")));
            String anger = numberInShortFormat(Long.parseLong(jData.optString("anger")));
            String sad = numberInShortFormat(Long.parseLong(jData.optString("sad")));
            String love = numberInShortFormat(Long.parseLong(jData.optString("love")));
            String laugh = numberInShortFormat(Long.parseLong(jData.optString("laugh")));

            String timerInSecs = jData.optString("timerInSecs");
            String commentsCount = jData.optString("commentsCount");
            String lockExpiry = jData.optString("lockExpiry");

            String reactedUser = jData.optString("referenceUser");
            String reactionType = jData.optString("userReactionType");
            String referenceReactionType = jData.optString("referenceReactionType");

            JSONObject jPostLocation = jData.optJSONObject("location");
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

            String uuid = jData.optString("uuid");

            if (mFlagPostHeadId) {
                mPostHeadId = uuid;
                mFlagPostHeadId = false;
            }

            JSONArray jMediaData = jData.optJSONArray("media");
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

            JSONArray jScrambledMediaData = jData.optJSONArray("scrambledMedia");
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

            String postLockStatus = jData.optString("postLockStatus");  //
            String totalReactions = jData.optString("totalReactions");


            return new ModelFeed(user_name, user_userName, user_profilePic, user_relationship, user_uuid, user_bio, user_followersCount,
                    user_followingCount, user_joined, user_loc_name, user_loc_city, user_loc_state, user_loc_country, content, defaultScrambledContent, postedDate,
                    unlockInProgress, unlocked, liked, anger, sad, love, laugh, timerInSecs, commentsCount,
                    lockExpiry, reactedUser, reactionType, referenceReactionType, location_name, location_city, location_state,
                    location_country, uuid, mModelFeedMedia, mModelScrambledMedia, postLockStatus, totalReactions);
        } else {
            return null;
        }
    }

    void refreshItems() {
        //           Load items
        //           getAroundMeEvents();
        //           Load complete

        goForCheckNewPostAvailableOrNot(mPostHeadId);
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        //  Update the adapter and notify data set changed
        //  ...
        //  Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        //        this.finish();
        popupWindow.dismiss();
        if (mFlagScrolledUp) {
            super.onBackPressed();
        } else {
            mRecyclerPosts.smoothScrollToPosition(0);
            mFlagScrolledUp = true;
        }


    }

    private void scheduleTimer(final int position_post_replced, final ModelFeed mFeedDt, int timerInSecs, final TextView mTextTimer, final DonutProgress mDonutProgress, final TextView mTextBackgroundTimei) {

        new CountDownTimer(timerInSecs * 1000, 1000) {

            TextView mTextTimerr = mTextTimer;
            TextView mTextTimerrBack = mTextBackgroundTimei;
            DonutProgress mDonutProgressr = mDonutProgress;
            // int mPost_lock_position = position_post_replced;
            int mProgressCount = 0;

            //      int mCount = 60;
            public void onTick(long millisUntilFinished) {
                //      Here you can have your logic to set text to edittext
                String timeStr = "";
                int mselfCount = (int) millisUntilFinished / 1000;

                int hours = (mselfCount / 3600);
                int minutes = (mselfCount / 60);
                int seconds = (mselfCount % 60);

                timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                if (seconds == 1) {
                    timeStr = String.format("%02d:%02d:%02d", hours, minutes, 0);
                    mProgressCount++;

                    mDonutProgressr.setDonut_progress(String.valueOf(mProgressCount));
                    mTextTimerr.setText("" + timeStr);
                    mTextTimerrBack.setText("" + timeStr);

                    Log.e("count", String.valueOf(mProgressCount));
                } else {
                    mProgressCount++;

                    mDonutProgressr.setDonut_progress(String.valueOf(mProgressCount));
                    mTextTimerr.setText("" + timeStr);
                    mTextTimerrBack.setText("" + timeStr);

                    Log.e("count", String.valueOf(mProgressCount));

                }
            }

            public void onFinish() {
                // go for unlocked api
                goForOpenAnUnlocked(mFeedDt.getUuid());
                po_openUnlocked_post = position_post_replced;
                //    mFeedData.setUnlocked("");
                //    mFeedData.setPostLockStatus("UNLOCKED");
                //   mAdapterFeed.notifyDataSetChanged();
            }
        }.start();
    }

    @Override
    public void onShowEmojis(ModelFeed mData, int position, TextView v, ImageView Like, ImageView Laugh, ImageView Sad, ImageView Love, ImageView Angry) {

        mPostIdForReactions = mData.getUuid();

        this.mSmilyLike = Like;
        this.mSmilyLaugh = Laugh;
        this.mSmilySad = Sad;
        this.mSmilyLove = Love;
        this.mSmilyAngry = Angry;

        int[] location = new int[2];
        v.getLocationInWindow(location);
        popupWindow.setElevation(4.0f);
        popupWindow.showAsDropDown(v, -5, -200);

    }

    @Override
    public void getModelFeed(int position) {
        po = position;
        uihandle.goForNextScreen(CommentScreen.class);
    }

    @Override
    public void onLikedClick(ModelFeed mFeedData, int position, ImageView Like) {
        this.mFeedData = mFeedData;
        mPostIdForReactions = mFeedData.getUuid();

       /* if (mImgTempReaction != null) {
            mImgTempReaction.setVisibility(View.GONE);
            mImgTempReaction.setImageBitmap(null);
            mImgTempReaction.destroyDrawingCache();
        }*/

        this.mSmilyLike = Like;
        this.mSmilyLike.setImageResource(R.mipmap.like_noti);
        mImgTempReaction = this.mSmilyLike;

        goForPutReactions(mPostIdForReactions, Constants.ConstantPutReactions.REACTION_LIKED);

    }

    @Override
    public void displayImage(ImageView mImageView) {
        onImageClickDialog(mImageView);

    }

    private void onImageClickDialog(ImageView mImageView) {
        Dialog d = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.dilaog_full_screen_image_view);
        ImageView mImage = d.findViewById(R.id.image);
        mImage.setImageBitmap(getBitmapFromView(mImageView));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.show();
    }
}
