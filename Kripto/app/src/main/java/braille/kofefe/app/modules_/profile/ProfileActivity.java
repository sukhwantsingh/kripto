package braille.kofefe.app.modules_.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.mainpostfeed.AddPostActivity;
import braille.kofefe.app.modules_.mainpostfeed.CommentScreen;
import braille.kofefe.app.modules_.mainpostfeed.SearchingActivity;
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.PostLockActivityList;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeedMedia;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelScrambledMedia;
import braille.kofefe.app.modules_.notification.NotificationActivity;
import braille.kofefe.app.modules_.profile.adapter.AdapterFollowers;
import braille.kofefe.app.modules_.profile.adapter.AdapterFollowings;
import braille.kofefe.app.modules_.profile.adapter.AdapterPost;
import braille.kofefe.app.modules_.profile.adapter.ViewPagerAdapter;
import braille.kofefe.app.modules_.profile.callback_.IFollowersFollowingCallback;
import braille.kofefe.app.modules_.profile.callback_.IPostCallbackInProfile;
import braille.kofefe.app.modules_.profile.fragment.FollowersFragment;
import braille.kofefe.app.modules_.profile.fragment.FollowingFragment;
import braille.kofefe.app.modules_.profile.fragment.PostsFragment;
import braille.kofefe.app.modules_.profile.model.ModelFollowers;
import braille.kofefe.app.modules_.profile.model.ModelFollowing;
import braille.kofefe.app.modules_.profile.model.ModelLocation;
import braille.kofefe.app.modules_.profile.model.ModelRecentPosts;
import braille.kofefe.app.modules_.profile.model.ModelTopFollowers;
import braille.kofefe.app.modules_.profile.model.ModelTopFollowing;
import braille.kofefe.app.modules_.profile.model.ModelUserProfileInfo;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.fcm.Config;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static braille.kofefe.app.modules_.common_util_.StaticValues.mUuidForMaintainUser;
import static com.android.volley.Request.Method.GET;

public class ProfileActivity extends CommonActivity implements IFollowersFollowingCallback, IPostCallbackInProfile, View.OnClickListener {

    @InjectView(R.id.img_registered_user)
    protected ImageView mImgRegisteredUser;
    @InjectView(R.id.img_user_profile)
    protected SquareRoundCornerImageView mImgUserProfile;
    @InjectView(R.id.ic_overflow_option_user_profile)
    protected ImageView mImageIcOverflow;
    @InjectView(R.id.floating_add_post)
    protected FloatingActionButton mFloatAddPost;
    @InjectView(R.id.notification_frame)
    protected FrameLayout mFrameNotificationTopRight;
    @InjectView(R.id.btn_follow)
    protected TextView mTextFollowButton;
    @InjectView(R.id.txt_bio)
    protected TextView mTextBio;

    @InjectView(R.id.txt_name)
    protected TextView mTextUserFullName;
    @InjectView(R.id.txt_address)
    protected TextView mTextUserAddress;
    @InjectView(R.id.text_joined)
    protected TextView mTextJoinedTime;

    @InjectView(R.id.imageView3)
    protected ImageView mImgProfileIcon;

    @InjectView(R.id.relative_wight)
    protected RelativeLayout mRelBottomSheet;
    @InjectView(R.id.img_blue_dot_notification)
    protected ImageView mImgBlueDOtNotification;

    @InjectView(R.id.mImgFb)
    protected ImageView mImgFb;
    @InjectView(R.id.mImgTw)
    protected ImageView mImgTw;
    @InjectView(R.id.mImgIn)
    protected ImageView mImgin;
    @InjectView(R.id.mImgSn)
    protected ImageView mImgSn;
    @InjectView(R.id.switch_facebook)
    protected ImageView mFbTick;

    @InjectView(R.id.switch_twitter)
    protected ImageView mTwitterTick;

    @InjectView(R.id.switch_instagram)
    protected ImageView mInstaTick;

    @InjectView(R.id.switch_snapchat)
    protected ImageView mSnapTick;

    @InjectView(R.id.txt_social_links)
    protected TextView mTextSocialLinks;

    @InjectView(R.id.linear_1)
    protected LinearLayout mLinearSocialIconContainer;

    @InjectView(R.id.face_book)
    protected LinearLayout face_book;

    @InjectView(R.id.face_twitter)
    protected LinearLayout face_twitter;

    @InjectView(R.id.face_instagram)
    protected LinearLayout face_instagram;

    @InjectView(R.id.face_snapchat)
    protected LinearLayout face_snapchat;

    protected ProgressBar mProgressLoadingPosts;
    protected ProgressBar mProgressLoadingFollowers, mProgressLoadingFollowings;

    DonutProgress mProgressi;
    TextView mTextPosti;
    FrameLayout mFrameProgi;
    TextView mTextUnlockTimeri;
    TextView mTextTimer_backgroundi;
    private BubbleLayout bubbleLayout;
    private PopupWindow popupWindow;
    private String mRelationshipStatus = "";
    /***
     * List of Posts in profile
     * ***/
    private ArrayList<ModelRecentPosts> mListRecentPosts;
    private RecyclerView mRecycleRecentPosts;
    private AdapterPost mAdapterRecentPosts;
    private TabLayout mTabsLayout;
    private ViewPager mPager;
    private UiHandleMethods uihandle;
    private Activity mContext = this;
    private TextView mTextPosts, mTextFollowing, mTextFollowers;
    private String mUserUUID = "";
    private String uuiD = "";
    /***
     * For get Followings
     * ***/
    private AdapterFollowings mAdapterFollowings;
    private RecyclerView mRecycleFollowing;
    private List<ModelFollowing> mListFollowing;
    private List<ModelFollowing> mListFollowingTemp;
    private int mFollowinglastPosition = 0;
    private boolean mNoFollowingData = false;
    /***
     * For Get Followers
     * ***/
    private AdapterFollowers mAdapterFollowers;
    private RecyclerView mRecycleFollowers;
    private List<ModelFollowers> mListFollowers;
    private List<ModelFollowers> mListFollowersTemp;
    private int mFollowersLastPosition = 0;
    private boolean mNoFollowersData = false;
    private TextView mFollowButtonStatus;
    private boolean mFlagFirstTimeVisit = false;
    private HashSet<String> mHashUuidUsers;
    private ModelRecentPosts mFeedData;
    private int timerInSecs;
    private int po;
    private ImageView mImgLike, mImgLaugh, mImgSad, mImgLove, mImgAngry;
    private ImageView mSmilyLike, mSmilyLaugh, mSmilySad, mSmilyLove, mSmilyAngry;
    private ImageView mImgTempReaction = null;
    private String mPostIdForReactions;
    private int po_openUnlocked_post;
    private int post_more_position;
    private boolean mFlagMorePost = false;
    private boolean _hasLoadedOnceFollower = false;    //       your boolean field
    private boolean _hasLoadedOnceFollowing = false;   //         your boolean field
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private ModelFollowers mModelFollowers;
    private ModelFollowing mModelFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.inject(this);

        initViews();
        getValuesFromPrevious();
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


  /* Gcm initialisationstarts ends Here */


    }

    @OnClick(R.id.img_home)
    public void onHomeClick() {
        uihandle.goBack();
    }

    /*  @OnClick(R.id.img_user_profile)
      public void showEnlargePick() {
      }*/
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

    private void onImageClickDialog(ImageView mImageView) {
        Dialog d = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.dilaog_full_screen_image_view);
        ImageView mImage = d.findViewById(R.id.image);
        mImage.setImageBitmap(getBitmapFromView(mImageView));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.show();
    }

    @OnClick(R.id.btn_follow)
    public void onFollowClick() {
        switch (mRelationshipStatus) {
            case ConstantStatusInAPI.RELATIONSHIP_NONE:
                goForFollowUser(mUserUUID);
                break;

            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER:
                goForFollowUser(mUserUUID);
                break;

            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWED:
                goForUnFollowUser(mUserUUID);
                break;

            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER_AND_FOLLOWED:
                goForUnFollowUser(mUserUUID);
                break;
        }


    }

    private void goForMarkNotificationAsViewed(String mNotificationId) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }
        new HttpRequester("", Request.Method.POST, this,
                Constants.MARK_NOTIFICATION_AS_VIEWED_POST, URLListApis.URL_MARK_NOTIFICATION_AS_VIEWED
                .replace("ID_PP", mNotificationId), this);
    }

    private void getValuesFromPrevious() {

        /*
         * check whether the intent is coming from notification or not if yes then let it be go with flow
         */
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            boolean cameFromNotification = b.getBoolean("fromNotification");
            if (!cameFromNotification) {
                getSessionInstance().setUuidFromNotification("");
            } else {
                // go for mark notification
                StaticValues.mNotificationBlinking = false;
                mImgBlueDOtNotification.clearAnimation();
                mImgBlueDOtNotification.setVisibility(View.GONE);

                goForMarkNotificationAsViewed(getSessionInstance().getNotificationIdFromNotificatio());
            }
        } else {
            getSessionInstance().setUuidFromNotification("");
        }

        if (!getSessionInstance().getUuidFromNotification().equals("")) {
            mUserUUID = getSessionInstance().getUuidFromNotification();


            mImgProfileIcon.setImageResource(R.drawable.icn_user);

            getSessionInstance().setUuidFromNotification("");

        } else if (StaticValues.mSearechedUserUUID.equals("")) {
            mUserUUID = getSessionInstance().getAuthorizationsetUUID();
//      getImageWithVolley(getSessionInstance().getUserProfilePic(), mImgUserProfile);
        } else {

            mUserUUID = StaticValues.mSearechedUserUUID;
            mImgProfileIcon.setImageResource(R.drawable.icn_user);

        }

//      add for initial uuid for user maintained
        if (StaticValues.mUuidForMaintainUser.isEmpty()) {
            StaticValues.mUuidForMaintainUser.add(mUserUUID);
        } else if (!StaticValues.mUuidForMaintainUser.contains(mUserUUID)) {
            StaticValues.mUuidForMaintainUser.add(mUserUUID);
        }

        /*
          if(!StaticValues.mUuidForMaintainUser.contains(StaticValues.mSearechedUserUUID)) {
               StaticValues.mUuidForMaintainUser.add(StaticValues.mSearechedUserUUID);
        }
        */


    }

    private void initViews() {

        uihandle = new UiHandleMethods(mContext);
        mHashUuidUsers = new HashSet<>();

        mRelBottomSheet.setAnimation(uihandle.slideDown());
        mRelBottomSheet.setAnimation(uihandle.slideUp());


        bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.emoji_views, null);
        popupWindow = BubblePopupHelper.create(this, bubbleLayout);

        mImgLaugh = bubbleLayout.findViewById(R.id.rv1_laugh);
        mImgLike = bubbleLayout.findViewById(R.id.rv2_like);
        mImgSad = bubbleLayout.findViewById(R.id.rv3_sad);
        mImgLove = bubbleLayout.findViewById(R.id.rv4_love);
        mImgAngry = bubbleLayout.findViewById(R.id.rv5_angry);


        mListRecentPosts = new ArrayList<>();
        mListFollowing = new ArrayList<>();
        mListFollowers = new ArrayList<>();


        mListFollowingTemp = new ArrayList<>();
        mListFollowersTemp = new ArrayList<>();

//      Tab Layouts
        mTextPosts = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        mTextFollowing = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        mTextFollowers = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);


        mTabsLayout = (TabLayout) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        // OverScrollDecoratorHelper.setUpOverScroll(mPager);

        mPager.setOffscreenPageLimit(2);

        mFrameNotificationTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticValues.mNotificationBlinking = false;
                mImgBlueDOtNotification.clearAnimation();
                mImgBlueDOtNotification.setVisibility(View.GONE);

                startActivity(new Intent(mContext, NotificationActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {


                // we check that the fragment is becoming visible
                if (position == 1 && !_hasLoadedOnceFollower) {
                    showIOSProgress("Loading...");

                    new HttpRequester(Request.Method.GET, mContext, new HashMap<String, String>(), Constants.GET_FOLLOWERS_GET,
                            URLListApis.URL_GET_FOLLOWERS.replace("UserUUID", mUserUUID), ProfileActivity.this);

                    _hasLoadedOnceFollower = true;
                } else if (position == 2 && !_hasLoadedOnceFollowing) {
                    showIOSProgress("Loading...");
                    new HttpRequester(Request.Method.GET, mContext, new HashMap<String, String>(), Constants.GET_FOLLOWING_GET,
                            URLListApis.URL_GET_FOLLOWING.replace("UserUUID", mUserUUID), ProfileActivity.this);
                    _hasLoadedOnceFollowing = true;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //  Todo: setup view pager
            /*
            setViewPager();
            mTabsLayout.setupWithViewPager(mPager);
            setupTabIcons();
            */
    }

    @OnClick(R.id.imageView3)
    protected void onProfileClickBottom() {
        uihandle.refresh();
    }

    private void callingApi() {
        getUserProfile();
    }

    private void implementListeners() {


        mImgLaugh.setOnClickListener(this);
        mImgLike.setOnClickListener(this);
        mImgSad.setOnClickListener(this);
        mImgLove.setOnClickListener(this);
        mImgAngry.setOnClickListener(this);

        mFloatAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AddPostActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        mImageIcOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ProfileActivity.this, mImageIcOverflow);
                //     Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.profile, popup.getMenu());


                //      hide the edit option for other user profile
                if (!getSessionInstance().getAuthorizationsetUUID().equals(uuiD)) {
                    Menu popupMenu = popup.getMenu();
                    popupMenu.findItem(R.id.action_edit_profile).setVisible(false);
                }


                //     registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_share) {
                            uihandle.shareMessage(getSessionInstance().getShareContentForInviteFriendScreen(), "");
                        } else if (item.getItemId() == R.id.action_edit_profile) {
                            startActivity(new Intent(mContext, EditProfileScreen.class));
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        }
                        //     Toast.makeText(ProfileActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();   //       showing popup menu
            }
        });



        /*.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && mFloatingPlus.isShown()) {
                    mFloatingPlus.hide();
                }
                if (dy < 0 && !mFloatingPlus.isShown()) {
                    mFloatingPlus.show();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
*//*
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mFloatingPlus.show();
                }*//*
                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (mImgTempReaction != null) {
            mImgTempReaction.setVisibility(View.GONE);
            mImgTempReaction.setImageBitmap(null);
            mImgTempReaction.destroyDrawingCache();
        }
        switch (id) {
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

    private void setupTabIcons() {
        try {
            /***
             * Attach Custom Tabs
             * ***/
            mTabsLayout.getTabAt(0).setCustomView(mTextPosts);
            mTabsLayout.getTabAt(1).setCustomView(mTextFollowers);
            mTabsLayout.getTabAt(2).setCustomView(mTextFollowing);

          /*  mTabsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                      try {
                        TextView mTaxtView = (TextView) tab.getCustomView();
                        mTaxtView.setTextColor(R.color.colorPrimaryDark);
                    } catch (Exception w) {
                      w.printStackTrace();
                    }}

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    try {
                        TextView mTaxtView = (TextView) tab.getCustomView();
                        mTaxtView.setTextColor(R.color.color_black);
                    } catch (Exception w) {
                        w.printStackTrace();
                    }
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }

            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewPager() {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        mAdapter.addFragment(new PostsFragment(), "");
        mAdapter.addFragment(new FollowersFragment(), "");
        mAdapter.addFragment(new FollowingFragment(), "");

        mPager.setAdapter(mAdapter);
    }

    public void goForSearch(View v) {
        uihandle.goForNextScreen(SearchingActivity.class);
    }

    private void getUserProfile() {
        showIOSProgress("Loading...");
        HashMap<String, String> map = new HashMap<>();

        if (mUserUUID.equals("")) {
            new HttpRequester(GET, this, map, Constants.GET_USER_PROFILE_GET,
                    URLListApis.URL_GET_USER_PROFILE.replace("USER_UUID", getSessionInstance().getAuthorizationsetUUID()).replace("REQUESTID_VALUE", getRandomUUID()), this);
        } else {
            new HttpRequester(GET, this, map, Constants.GET_USER_PROFILE_GET,
                    URLListApis.URL_GET_USER_PROFILE.replace("USER_UUID", mUserUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);

            //        set values to  ""  empty string so that to remove redundancy
            StaticValues.mSearechedUserUUID = "";
        }
    }

    private void goForFollowUser(String receiverUUID) {

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        showIOSProgress("Following...");

        /*new HttpRequester(Request.Method.POST, this, map,
                Constants.FOLLOW_USER_POST, URLListApis.URL_FOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);*/
        new HttpRequester("", Request.Method.POST, this, Constants.FOLLOW_USER_POST, URLListApis.URL_FOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    private void goForUnFollowUser(String receiverUUID) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        showIOSProgress("UnFollowing...");

        /*new HttpRequester(Request.Method.POST, this, map,
                Constants.FOLLOW_USER_POST, URLListApis.URL_FOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);*/

        new HttpRequester("", Request.Method.POST, this, Constants.UNFOLLOW_USER_POST, URLListApis.URL_UNFOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);

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
                case Constants.MARK_NOTIFICATION_AS_VIEWED_POST:
                    //    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //       showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            if (jsonObject.optString("status").equals("SUCCESS")) {
                                getSessionInstance().setNotificationIdFromNotification("");
                                Log.e("fromnotification_come", "Success");
                            }


                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.GET_USER_PROFILE_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        String res_ = jsonObject.optString("ack");
                        if (res_.equals("SUCCESS")) {
                            refineJSON(jsonObject);
                        } else {
                            JSONObject jData = jsonObject.optJSONArray("errors").optJSONObject(0);
                            // String res_data = jData.optString("id");
                            String res_message = jData.optString("remedy");
                            showFancyToast(TastyToast.SUCCESS, res_message);
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    /*
                    {"ack":"FAILURE",
                    "errors":[
                   { "id":"USER_PROFILE_ERROR_1001",
                     "message":"Requested username is no longer available. Please try a different one!",
                     "errorType":"USER","errorSeverity":"ERROR","remedy":"FIX_THE_INPUT_DATA_AND_RETRY"}],"warnings":[], "userProfile":null} */
                    break;

                case Constants.GET_FOLLOWING_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        String res_ = jsonObject.optString("ack");

                        mListFollowing.clear();
                        if (!mListFollowingTemp.isEmpty()) {
                            mListFollowingTemp.clear();
                        }
                        if (res_.equals("SUCCESS")) {

                            JSONArray nArray = jsonObject.optJSONArray("followings");
                            if (nArray != null && nArray.length() > 0) {
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


                                    mListFollowingTemp.add(new ModelFollowing(name, userName, profilePic, relationship, uuid,
                                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country));
                                }

                                mListFollowing.addAll(mListFollowingTemp);
                                mAdapterFollowings = new AdapterFollowings(mContext, mListFollowing);
                                mRecycleFollowing.setAdapter(mAdapterFollowings);

                            } else {
                                Log.e("response_else", "empty");
                                dismissIOSProgress();

                            }

                        } else {
                            String res_message = jsonObject.optString("messageToUser");
                            if (!res_message.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, res_message);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, res_);
                            }
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                        dismissIOSProgress();
                    }
                    break;

                case Constants.GET_FOLLOWING_AFTER_GET:
                    mProgressLoadingFollowings.setVisibility(View.GONE);
                    if (jsonObject != null) {
                        String res_ = jsonObject.optString("ack");

                        if (!mListFollowingTemp.isEmpty()) {
                            mListFollowingTemp.clear();
                        }
                        if (res_.equals("SUCCESS")) {
                            JSONArray nArray = jsonObject.optJSONArray("followings");
                            if (nArray != null && nArray.length() > 0) {
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


                                    mListFollowingTemp.add(new ModelFollowing(name, userName, profilePic, relationship, uuid,
                                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country));
                                }
                                mListFollowing.addAll(mFollowinglastPosition, mListFollowingTemp);
                                mAdapterFollowings.notifyDataSetChanged();
                                //      mRecycleFollowing.scrollToPosition(mFollowinglastPosition);

                            } else {
                                mNoFollowingData = true;
                                Log.e("response_else", "empty");
                            }
                        } else {
                            String res_message = jsonObject.optString("messageToUser");
                            if (!res_message.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, res_message);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, res_);
                            }
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.GET_FOLLOWERS_GET:
                    dismissIOSProgress();

                    if (jsonObject != null) {
                        mListFollowers.clear();

                        if (!mListFollowersTemp.isEmpty()) {
                            mListFollowersTemp.clear();
                        }

                        String res_ = jsonObject.optString("ack");
                        if (res_.equals("SUCCESS")) {
                            JSONArray nArray = jsonObject.optJSONArray("followers");
                            if (nArray != null && nArray.length() > 0) {
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


                                    mListFollowersTemp.add(new ModelFollowers(name, userName, profilePic, relationship, uuid,
                                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country));
                                }

                                mListFollowers.addAll(mListFollowersTemp);
                                mAdapterFollowers = new AdapterFollowers(mContext, mListFollowers);
                                mRecycleFollowers.setAdapter(mAdapterFollowers);

                            } else {
                                Log.e("response_else", "empty");
                                dismissIOSProgress();
                            }

                        } else {
                            String res_message = jsonObject.optString("messageToUser");
                            if (!res_message.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, res_message);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, res_);
                            }
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                        dismissIOSProgress();
                    }
                    break;

                case Constants.GET_FOLLOWERS_AFTER_GET:
                    //    mProgressLoadingFollowers.setAnimation(uihandle.bottomDown());
                    mProgressLoadingFollowers.setVisibility(View.GONE);
                    // Start the animation
                 /*   mProgressLoadingFollowers.animate()
                            .translationY(mProgressLoadingFollowers.getHeight())
                            .alpha(1.0f)
                            .setListener(null);
                    *///      Start animation

                    if (jsonObject != null) {

                        if (!mListFollowersTemp.isEmpty()) {
                            mListFollowersTemp.clear();
                        }
                        String res_ = jsonObject.optString("ack");
                        if (res_.equals("SUCCESS")) {
                            JSONArray nArray = jsonObject.optJSONArray("followers");
                            if (nArray != null && nArray.length() > 0) {
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

                                    mListFollowersTemp.add(new ModelFollowers(name, userName, profilePic, relationship, uuid,
                                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country));
                                }

                                mListFollowers.addAll(mFollowersLastPosition, mListFollowersTemp);
                                mAdapterFollowers.notifyDataSetChanged();
                                //   mRecycleFollowers.scrollToPosition(mFollowersLastPosition);

                            } else {
                                mNoFollowersData = true;
                                //        uihandle.showToast("no more data");
                                Log.e("response_else", "empty");
                            }


                        } else {
                            String res_message = jsonObject.optString("messageToUser");
                            if (!res_message.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, res_message);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, res_);
                            }
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

                            mTextFollowButton.setText("Following");
                            //  mTextFollowButton.setEnabled(false);
                            mRelationshipStatus = "FOLLOWED";
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
                            mTextFollowButton.setText("Follow");
                            mRelationshipStatus = "NONE";

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
                case Constants.FOLLOW_USER_:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            //   refineJSON(jsonObject);
                            if (mModelFollowers != null) {
                                mModelFollowers.setRelationship(ConstantStatusInAPI.RELATIONSHIP_FOLLOWER_AND_FOLLOWED);
                                mAdapterFollowers.notifyDataSetChanged();

                            } else {
                                mModelFollowing.setRelationship(ConstantStatusInAPI.RELATIONSHIP_FOLLOWED);
                                mAdapterFollowings.notifyDataSetChanged();
                            }

                            //    mFollowButtonStatus.setEnabled(false);
                            String messageToUser = jsonObject.optString("messageToUser");

                           /* if (!messageToUser.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, messageToUser);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            }*/

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;
                case Constants.UNFOLLOW_USER_:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            //   refineJSON(jsonObject);
                            mFollowButtonStatus.setText("Follow");
                            String messageToUser = jsonObject.optString("messageToUser");
                            if (mModelFollowers != null) {
                                mModelFollowers.setRelationship(ConstantStatusInAPI.RELATIONSHIP_FOLLOWER);
                                mAdapterFollowers.notifyDataSetChanged();
                            } else {
                                mModelFollowing.setRelationship(ConstantStatusInAPI.RELATIONSHIP_NONE);
                                mAdapterFollowings.notifyDataSetChanged();
                            }


                          /*  if (!messageToUser.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, messageToUser);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            }*/

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

                            mFeedData.setUnlocked(jsonObject.optString("unlockedCount"));
                            mFeedData.setUnlockInProgress(jsonObject.optString("unlockInProgressCount"));
                            mAdapterRecentPosts.notifyDataSetChanged();

                            //    call timer on success ack
                            if (mFeedData.getPostLockStatus().equals(Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OLD_POST_FIRST_TIME_VIEW)) {
                                mProgressi.setMax(14);
                                scheduleTimer(po_openUnlocked_post, mTextTimer_backgroundi, mFeedData, 15, mTextUnlockTimeri, this.mProgressi);
                            } else {
                                mProgressi.setMax(Integer.parseInt(mTimerInSecs) - 1);
                                scheduleTimer(po_openUnlocked_post, mTextTimer_backgroundi, mFeedData, Integer.parseInt(mTimerInSecs), mTextUnlockTimeri, this.mProgressi);
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

                           /* JSONObject mJData = jsonObject.optJSONObject("post");
                            String mPostLockStatus = mJData.optString("postLockStatus");
                            String mUnlocked = mJData.optString("unlocked");

                            //       set the lock status
                            mFeedData.setUnlocked(mUnlocked);
                            mFeedData.setPostLockStatus(mPostLockStatus);
                            mAdapterRecentPosts.notifyDataSetChanged();*/

                            ModelRecentPosts mTempList = refineJsonModel(jsonObject);
                            if (mTempList != null) {
                                mAdapterRecentPosts.replaceItem(mTempList, po_openUnlocked_post);
                                mAdapterRecentPosts.notifyDataSetChanged();

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

                            mListRecentPosts.get(po).setTotalReactions(mUpdatedCount);
                            mAdapterRecentPosts.notifyDataSetChanged();

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

                case Constants.GET_USER_POSTS_MORE_GET:
                    mProgressLoadingPosts.setVisibility(View.GONE);
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //    showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             ** refine response from server
                             ***/
                            List<ModelRecentPosts> mTempList = refinePostsJSON(jsonObject);
                            if (mTempList != null) {
                                mListRecentPosts.addAll(post_more_position, mTempList);
                                mAdapterRecentPosts.notifyDataSetChanged();
                            } else {
                                mFlagMorePost = true;
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

    private void scheduleTimer(final int mPost_pos, final TextView mTimerScrambled, final ModelRecentPosts mFeedDt, int timerInSecs, final TextView mTextTimer, final DonutProgress mDonutProgress) {
        new CountDownTimer(timerInSecs * 1000, 1000) {

            TextView mTextTimerScrambled = mTimerScrambled;
            TextView mTextTimerr = mTextTimer;
            DonutProgress mDonutProgressr = mDonutProgress;

            int mProgressCount = 0;

            //    int mCount = 60;
            public void onTick(long millisUntilFinished) {
                //   here you can have your logic to set text to edittext
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
                    mTextTimerr.setText(timeStr);
                    mTextTimerScrambled.setText(timeStr);

                    Log.e("count", String.valueOf(mProgressCount));
                } else {
                    mProgressCount++;

                    mDonutProgressr.setDonut_progress(String.valueOf(mProgressCount));
                    mTextTimerr.setText(timeStr);
                    mTextTimerScrambled.setText(timeStr);

                    Log.e("count", String.valueOf(mProgressCount));

                }
            }

            public void onFinish() {
                // go for unlocked api
                goForOpenAnUnlocked(mFeedDt.getUuid());
                po_openUnlocked_post = mPost_pos;
                //    mFeedData.setUnlocked("");
                //    mFeedData.setPostLockStatus("UNLOCKED");
                //   mAdapterFeed.notifyDataSetChanged();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StaticValues.EditProfileContent.mProfileEdited) {
            //  callingApi();
            uihandle.refresh();
            StaticValues.EditProfileContent.mProfileEdited = false;
        }

        if (StaticValues.mFlagCommentCountManager) {
            mListRecentPosts.get(po).setCommentsCount(StaticValues.mCommentCountFinal);
            mAdapterRecentPosts.notifyDataSetChanged();
            StaticValues.mFlagCommentCountManager = false;
        }


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

    private void refineJSON(JSONObject jsonObject) {
        try {
            Log.e("Response", jsonObject.toString());

            JSONObject jUserProfileData = jsonObject.optJSONObject("userProfile");
            if (jUserProfileData != null) {
                /***
                 ** User profile
                 * ***/
                String uuid = jUserProfileData.optString("uuid");
                String name = jUserProfileData.optString("name");
                String username = jUserProfileData.optString("username");
                String bio = jUserProfileData.optString("bio");
                String profilePic = jUserProfileData.optString("profilePic");
                String coverPic = jUserProfileData.optString("coverPic");
                Integer profileViewCount = jUserProfileData.optInt("profileViewCount");
                Integer totalPostViewCount = jUserProfileData.optInt("totalPostViewCount");
                String twitter = jUserProfileData.optString("twitter").trim();
                String facebook = jUserProfileData.optString("facebook").trim();
                String instagram = jUserProfileData.optString("instagram").trim();
                String snapchat = jUserProfileData.optString("snapchat").trim();

                String followersCount = numberInShortFormat(Long.parseLong(jUserProfileData.optString("followersCount")));
                String followingCount = numberInShortFormat(Long.parseLong(jUserProfileData.optString("followingCount")));
                String totalPosts = numberInShortFormat(Long.parseLong(jUserProfileData.optString("totalPosts")));

                String joined = jUserProfileData.optString("joined");
                Boolean verified = jUserProfileData.optBoolean("verified");
                String relationship = jUserProfileData.optString("relationship");

                /***
                 * * Location object
                 * ***/
                JSONObject jLocationData = jUserProfileData.optJSONObject("location");

                String city = jLocationData.optString("city");
                String state = jLocationData.optString("state");
                String country = jLocationData.optString("country");
                double latitude = jLocationData.optDouble("latitude");
                double longitude = jLocationData.optDouble("longitude");

                ModelLocation modelLocation = new ModelLocation(city, state, country, latitude, longitude);

                /***
                 * * Top Followers
                 * ***/
                JSONArray jTopFollowers = jUserProfileData.optJSONArray("topFollowers");
                ModelTopFollowers mTopFollowers = new ModelTopFollowers();

                /***
                 * * Top Following
                 * ***/
                JSONArray jTopFollowing = jUserProfileData.optJSONArray("topFollowing");
                ModelTopFollowing mTopFollowing = new ModelTopFollowing();

                /***
                 * * Recent Posts
                 * ***/
                JSONArray jRecentPosts = jUserProfileData.optJSONArray("recentPosts");
                ModelRecentPosts[] mRecentPosts = null;
                if (jRecentPosts != null) {
                    mRecentPosts = new ModelRecentPosts[jRecentPosts.length()];

                    for (int i = 0; i < jRecentPosts.length(); i++) {

                        JSONObject jData = jRecentPosts.optJSONObject(i);
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
                        String commentsCount = numberInShortFormat(Long.parseLong(jData.optString("commentsCount")));
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

                        String post_uuid = jData.optString("uuid");

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

                        String postLockStatus = jData.optString("postLockStatus");
                        String totalReactions = jData.optString("totalReactions");

                        mRecentPosts[i] = new ModelRecentPosts(user_name, user_userName, user_profilePic, user_relationship, user_uuid, user_bio, user_followersCount,
                                user_followingCount, user_joined, user_loc_name, user_loc_city, user_loc_state, user_loc_country, content, defaultScrambledContent, postedDate,
                                unlockInProgress, unlocked, liked, anger, sad, love, laugh, timerInSecs, commentsCount,
                                lockExpiry, reactedUser, reactionType, referenceReactionType, location_name, location_city, location_state, location_country, post_uuid, mModelFeedMedia, mModelScrambledMedia, postLockStatus, totalReactions);

                        //    setting    recentPosts
                        mListRecentPosts.add(mRecentPosts[i]);
                    }

                } else {
                    mFlagMorePost = true;
                }


                /***
                 ** Passing values to main Object user profile
                 * ***/
                ModelUserProfileInfo mUserInfo = new ModelUserProfileInfo(uuid, name, username, bio, profilePic, coverPic,
                        profileViewCount, totalPostViewCount, twitter, facebook, instagram, snapchat, followersCount,
                        followingCount, totalPosts, modelLocation, joined, verified, relationship,
                        mTopFollowers, mTopFollowing, mRecentPosts);

                //          Todo: Implement values to different views
                mTextUserFullName.setText(UiHandleMethods.capitalizeString(name) + " (" + username + ")");
                mTextUserAddress.setText(city + ", " + state + ", " + country);
                mTextJoinedTime.setText("Since " + getDataTimeFromMilliseconds(Long.parseLong(joined)));
                mTextBio.setText(bio);

                uuiD = uuid;
                StaticValues.mUuidForFollowers = uuiD;

                if (getSessionInstance().getAuthorizationsetUUID().equals(uuid)) {
                    //   mTextFollowButton.setVisibility(View.GONE);
                }

                if (!facebook.isEmpty() && !facebook.equals("null")) {
                    //     mFbTick.setImageResource(R.mipmap.green_tick);
                    //     mFbTick.setVisibility(View.VISIBLE);
                    face_book.setVisibility(View.VISIBLE);
                } /*else {
                    //   mFbTick.setImageResource(R.mipmap.question_mark);
                    mFbTick.setVisibility(View.INVISIBLE);
                }*/

                if (!twitter.isEmpty() && !twitter.equals("null")) {
                    //    mTwitterTick.setImageResource(R.mipmap.green_tick);
                    //    mTwitterTick.setVisibility(View.VISIBLE);
                    face_twitter.setVisibility(View.VISIBLE);
                }/* else {
                    //    mTwitterTick.setImageResource(R.mipmap.question_mark);
                    mTwitterTick.setVisibility(View.INVISIBLE);
                }*/

                if (!instagram.isEmpty() && !instagram.equals("null")) {
                    //     mInstaTick.setImageResource(R.mipmap.green_tick);
                    //    mInstaTick.setVisibility(View.VISIBLE);
                    face_instagram.setVisibility(View.VISIBLE);
                } /*else {
                    //      mInstaTick.setImageResource(R.mipmap.question_mark);
                    mInstaTick.setVisibility(View.INVISIBLE);
                } */

                if (!snapchat.isEmpty() && !snapchat.equals("null")) {
                    //     mSnapTick.setImageResource(R.mipmap.green_tick);
                    //     mSnapTick.setVisibility(View.VISIBLE);
                    face_snapchat.setVisibility(View.VISIBLE);

                } /* else {
           //        mSnapTick.setImageResource(R.mipmap.question_mark);
                     mSnapTick.setVisibility(View.INVISIBLE);
                } */

                if (instagram.equals("") || instagram.equals("null") && twitter.equals("") || twitter.equals("null") && facebook.equals("") || facebook.equals("null") && snapchat.equals("") || snapchat.equals("null")) {
                    mTextSocialLinks.setVisibility(View.VISIBLE);
                    mLinearSocialIconContainer.setVisibility(View.GONE);
                    uihandle.changeColorToAppGradient(mTextSocialLinks);
                } /*else {
                    mLinearSocialIconContainer.setVisibility(View.VISIBLE);
                }*/


                mRelationshipStatus = relationship;

                /***
                 * Switch Case for the Relationship
                 * **/
                switch (mRelationshipStatus) {
                    case ConstantStatusInAPI.RELATIONSHIP_SELF:
                        mTextFollowButton.setText("You");
                        mTextFollowButton.setEnabled(false);
                        break;
                    case ConstantStatusInAPI.RELATIONSHIP_NONE:
                        mTextFollowButton.setText("Follow");
                        break;
                    case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER:
                        mTextFollowButton.setText("Follow");
                        break;
                    case ConstantStatusInAPI.RELATIONSHIP_FOLLOWED:
                        mTextFollowButton.setText("Following");
                        break;
                    case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER_AND_FOLLOWED:
                        mTextFollowButton.setText("Following");
                        break;
                }

             /*
            //    Create an object for subclass of AsyncTask
                  String uurl = "https://media.kofefe.s3-us-west-2.amazonaws.com/image/profile/646505e0-fe0c-4130-94bd-7809e53107a5.jpeg";
            //    String uurlN="https:\\/\\/media.kofefe.s3-us-west-2.amazonaws.com\\/image\\/profile\\/fad49bd5-cd33-4d43-9ea7-c1d818a078d1.jpg";
                  String uurlN = "https://media.kofefe.s3-us-west-2.amazonaws.com/image/profile/fad49bd5-cd33-4d43-9ea7-c1d818a078d1.jpeg";
             */

                getImageWithVolley(profilePic, mImgUserProfile);
                mImgUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onImageClickDialog(mImgUserProfile);
                    }
                });

                Log.i("data", mUserInfo.toString());

                /***
                 * setting text to tabs
                 * ***/
                mTextPosts.setText("Posts(" + totalPosts + ")");
                mTextFollowers.setText("Followers(" + followersCount + ")");
                mTextFollowing.setText("Followings(" + followingCount + ")");


                // setting view pager with values
                setViewPager();
                mTabsLayout.setupWithViewPager(mPager);
                setupTabIcons();

                // save values for editprofile

                StaticValues.EditProfileContent.mProfilePic = profilePic;
                StaticValues.EditProfileContent.mFullName = name;
                StaticValues.EditProfileContent.mUsername = username;
                StaticValues.EditProfileContent.mBio = bio;
                StaticValues.EditProfileContent.mLocation = city + "," + state + "," + country;

                StaticValues.EditProfileContent.mLat = String.valueOf(latitude);
                StaticValues.EditProfileContent.mLon = String.valueOf(longitude);

                StaticValues.EditProfileContent.mFacebook = facebook;
                StaticValues.EditProfileContent.mTwitter = twitter;
                StaticValues.EditProfileContent.mInsta = instagram;
                StaticValues.EditProfileContent.mSnap = snapchat;

            } else {
                showFancyToast(TastyToast.ERROR, ERROR_EMPTY_JSON);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private List<ModelRecentPosts> refinePostsJSON(JSONObject mJsonObj) {
        try {
            List<ModelRecentPosts> mRecentPostsLocal = new ArrayList<>();
            Log.e("response", mJsonObj.toString());
            JSONArray nArray = mJsonObj.optJSONArray("posts");
            if (nArray != null && nArray.length() > 0) {

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


                    mRecentPostsLocal.add(new ModelRecentPosts(user_name, user_userName, user_profilePic, user_relationship, user_uuid, user_bio, user_followersCount,
                            user_followingCount, user_joined, user_loc_name, user_loc_city, user_loc_state, user_loc_country, content, defaultScrambledContent, postedDate,
                            unlockInProgress, unlocked, liked, anger, sad, love, laugh, timerInSecs, commentsCount,
                            lockExpiry, reactedUser, reactionType, referenceReactionType, location_name, location_city, location_state, location_country, uuid, mModelFeedMedia, mModelScrambledMedia, postLockStatus, totalReactions));


                }

                return mRecentPostsLocal;
            } else {
                Log.i("feed", "empty");
                return null;
            }

        } catch (Exception ex) {
            Log.i("feed", ex.toString());
            return null;
        }


    }

    private ModelRecentPosts refineJsonModel(JSONObject mJsonObj) {

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


            return new ModelRecentPosts(user_name, user_userName, user_profilePic, user_relationship, user_uuid, user_bio, user_followersCount,
                    user_followingCount, user_joined, user_loc_name, user_loc_city, user_loc_state, user_loc_country, content, defaultScrambledContent, postedDate,
                    unlockInProgress, unlocked, liked, anger, sad, love, laugh, timerInSecs, commentsCount,
                    lockExpiry, reactedUser, reactionType, referenceReactionType, location_name, location_city, location_state,
                    location_country, uuid, mModelFeedMedia, mModelScrambledMedia, postLockStatus, totalReactions);
        } else {
            return null;
        }
    }

    @Override
    public void onErrorFound(String errorResponse, int serviceCode) {
        super.onErrorFound(errorResponse, serviceCode);
        try {
            mProgressLoadingPosts.setVisibility(View.GONE);
            mProgressLoadingFollowings.setVisibility(View.GONE);
            mProgressLoadingFollowers.setVisibility(View.GONE);

            dismissIOSProgress();
            Log.e("Response", errorResponse);
            showFancyToast(TastyToast.ERROR, errorResponse);
            //  JSONObject jsonObject = new JSONObject(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getFollowings(RecyclerView mRecycle, View v) {
        this.mRecycleFollowing = mRecycle;
        putOnScroolOnRecycleView(mRecycle);
        mProgressLoadingFollowings = v.findViewById(R.id.loading_progress);

    }

    @Override
    public void getFollowers(RecyclerView mRecycle, View v) {
        this.mRecycleFollowers = mRecycle;
        putOnScroolOnRecycleView(mRecycleFollowers);
        mProgressLoadingFollowers = v.findViewById(R.id.loading_progress);

        //    mProgressLoadingFollowers.setAnimation(uihandle.bottomDown());
        //     mProgressLoadingFollowers.startAnimation(uihandle.slideDown());

    }

    private void putOnScroolOnRecycleView(RecyclerView mRecyc) {
        mRecyc.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                }
            }
        });
    }

    @Override
    public void getFollowingsAfter(int position, ModelFollowing mModleFollowings) {
        mFollowinglastPosition = position + 1;
        if (!mNoFollowingData) {
            mProgressLoadingFollowings.setVisibility(View.VISIBLE);
            new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(), Constants.GET_FOLLOWING_AFTER_GET,
                    URLListApis.URL_GET_FOLLOWING_AFTER.replace("UserUUID", mUserUUID).replace("AKHRI_PERSON", mModleFollowings.getUuid()), this);
        }
        Log.e("followings", position + "\n" + mModleFollowings.getUserName());
    }

    @Override
    public void getRecentPosts(RecyclerView mRecycle, View v) {
        mRecycleRecentPosts = mRecycle;
        putOnScroolOnRecycleView(mRecycle);
        mProgressLoadingPosts = v.findViewById(R.id.loading_progress);

        mAdapterRecentPosts = new AdapterPost(mContext, mListRecentPosts);
        mRecycleRecentPosts.setAdapter(mAdapterRecentPosts);

    }

    //     Hit api for putReactions
    private void goForPutReactions(String mPostId, String mReactionType) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");
        //      rest/reaction/POST_ID/WHAT_YOU_WANT?requestId=REQUESTID_VALUE"
        new HttpRequester(Request.Method.PUT, this, map, Constants.PUT_REACTION_PUT,
                URLListApis.URL_PUT_REACTIONS
                        .replace("POST_ID", mPostId)
                        .replace("WHAT_YOU_WANT", mReactionType)
                        .replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    @Override
    public void onFollowerUserClick(int position, ModelFollowers mModelFollowers) {
        StaticValues.mSearechedUserUUID = mModelFollowers.getUuid();
        if (StaticValues.mUuidForMaintainUser.isEmpty()) {
            StaticValues.mUuidForMaintainUser.add(mModelFollowers.getUuid());
        } else if (!StaticValues.mUuidForMaintainUser.contains(mModelFollowers.getUuid())) {
            StaticValues.mUuidForMaintainUser.add(mModelFollowers.getUuid());
        }

        uihandle.refresh();
    }

    @Override
    public void onFollowingUserClick(int position, ModelFollowing mModleFollowings) {
        StaticValues.mSearechedUserUUID = mModleFollowings.getUuid();
        if (StaticValues.mUuidForMaintainUser.isEmpty()) {
            StaticValues.mUuidForMaintainUser.add(mModleFollowings.getUuid());
        } else if (!StaticValues.mUuidForMaintainUser.contains(mModleFollowings.getUuid())) {
            StaticValues.mUuidForMaintainUser.add(mModleFollowings.getUuid());
        }

        uihandle.refresh();
    }

    @Override
    public void onFollowerBtnClick(int position, ModelFollowers mModelFollowers, TextView mTextFollowBtn) {
        //        Todo: follows  Users
        this.mModelFollowers = mModelFollowers;

        if (mTextFollowBtn.getText().toString().trim().equals("Follow")) {
            goForFollowUser(mModelFollowers.getUuid(), mTextFollowBtn);
        } else {
            alertForUnFollowUserFollower(mTextFollowBtn);
        }
    }

    private void alertForUnFollowUserFollower(final TextView mTextFollowBtn) {

        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("Unfollow " + mModelFollowers.getName())
                .positiveText("Unfollow")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        goForUnFollowUser(mModelFollowers.getUuid(), mTextFollowBtn);
                    }
                })
                .negativeText("Cancel")
                .show();

    }

    @Override
    public void onFollowingBtnClick(int position, ModelFollowing mModleFollowings, TextView mTextFollowingBtn) {
        //          Todo: Follows Users
        this.mModelFollowers = null;
        this.mModelFollowing = mModleFollowings;

        if (mTextFollowingBtn.getText().toString().trim().equals("Follow")) {
            goForFollowUser(mModleFollowings.getUuid(), mTextFollowingBtn);
        } else {

            alertForUnFollowUserFollowing(position, mTextFollowingBtn);
        }

    }

    private void alertForUnFollowUserFollowing(int position, final TextView mTextFollowingBtn) {
        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("Unfollow " + mModelFollowing.getName())
                .positiveText("Unfollow")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        goForUnFollowUser(mModelFollowing.getUuid(), mTextFollowingBtn);
                    }
                })
                .negativeText("Cancel")
                .show();

    }

    @Override
    public void getFollowersAfter(int position, ModelFollowers mModelFollowers) {
        Log.e("followers", position + "\n" + mModelFollowers.getUserName());
        mFollowersLastPosition = position + 1;
        if (!mNoFollowersData) {
            mProgressLoadingFollowers.setVisibility(View.VISIBLE);
            new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(), Constants.GET_FOLLOWERS_AFTER_GET,
                    URLListApis.URL_GET_FOLLOWERS_AFTER.replace("UserUUID", mUserUUID).replace("AKHRI_PERSON", mModelFollowers.getUuid()), this);
        }


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
        new HttpRequester("", Request.Method.POST, this, Constants.FOLLOW_USER_, URLListApis.URL_FOLLOW_USER.
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

        new HttpRequester("", Request.Method.POST, this, Constants.UNFOLLOW_USER_, URLListApis.URL_UNFOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    @Override
    public void onBackPressed() {
        //    super.onBackPressed();
        if (StaticValues.mUuidForMaintainUser.isEmpty()) {
            this.finish();
            return;
        } else {
            if (StaticValues.mUuidForMaintainUser.size() > 1) {
                if (mUuidForMaintainUser.get(mUuidForMaintainUser.size() - 1).contains(StaticValues.mSearechedUserUUID)) {
                    StaticValues.mUuidForMaintainUser.remove(mUuidForMaintainUser.size() - 1);
                    StaticValues.mSearechedUserUUID = mUuidForMaintainUser.get(mUuidForMaintainUser.size() - 1);
                }
            } else {
                StaticValues.mUuidForMaintainUser.clear();
                this.finish();
                return;
            }

        }

        uihandle.refresh();

    }

    @Override
    public void unlockPost(TextView mTextTimer_background, int mPosition, ModelRecentPosts mFeedData, int timerInSecs, DonutProgress mProgress, TextView mTextPost, FrameLayout mFrameProg, TextView mTextUnlockTimer) {
        po_openUnlocked_post = mPosition;

        this.mFeedData = mFeedData;
        this.timerInSecs = timerInSecs;
        this.mTextTimer_backgroundi = mTextTimer_background;
        this.mProgressi = mProgress;
        this.mTextPosti = mTextPost;
        this.mFrameProgi = mFrameProg;
        this.mTextUnlockTimeri = mTextUnlockTimer;


        goForUnlockPost(mFeedData.getUuid());

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

    @Override
    public void getPostLockActivityList(ModelRecentPosts mModelFeed, int position) {
        String mPostId = mModelFeed.getUuid();

        StaticValues.mPostId = mPostId;
        uihandle.goForNextScreen(PostLockActivityList.class);

    }

    @Override
    public void getPostsPosition(int position) {
        po = position;
        uihandle.goForNextScreen(CommentScreen.class);
    }

    @Override
    public void onShowEmojis(ModelRecentPosts mData, int position, TextView v, ImageView Like, ImageView Laugh, ImageView Sad, ImageView Love, ImageView Angry) {
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
    public void onLikedClick(ModelRecentPosts mFeedData, int position, ImageView mImgLike) {
        mPostIdForReactions = mFeedData.getUuid();

       /* if (mImgTempReaction != null) {
            mImgTempReaction.setVisibility(View.GONE);
            mImgTempReaction.setImageBitmap(null);
            mImgTempReaction.destroyDrawingCache();
        }
*/
        this.mSmilyLike = mImgLike;
        this.mSmilyLike.setImageResource(R.mipmap.like_noti);
        mImgTempReaction = this.mSmilyLike;
        goForPutReactions(mPostIdForReactions, Constants.ConstantPutReactions.REACTION_LIKED);
    }

    @Override
    public void getPostsAfter(int position, ModelRecentPosts mModleRecents) {

        post_more_position = position + 1;
        if (post_more_position > 5) {
            if (!mFlagMorePost) {

                if (!isNetworkConnected()) {
                    showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
                    return;
                }
                mProgressLoadingPosts.setVisibility(View.VISIBLE);
                new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(), Constants.GET_USER_POSTS_MORE_GET,
                        URLListApis.URL_GET_MORE_POSTS.replace("UserUUID", mUserUUID)
                                .replace("REQUESTID_VALUE", getRandomUUID())
                                .replace("AKHRI_PERSON", mModleRecents.getUuid()), this);
            }

            Log.e("posts", position + "\n" + mModleRecents.getUser_name());

        }


    }


}
