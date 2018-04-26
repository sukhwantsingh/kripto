package braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
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
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.adapter.AdapterFragmentLock;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.adapter.AdapterFragmentUnlock;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.callback.IPostLockActivityListCallback;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.fragment.FragmentLock;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.fragment.FragmentUnlock;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.model.ModelPostActivityList;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class PostLockActivityList extends CommonActivity implements IPostLockActivityListCallback {


    @InjectView(R.id.txt1)
    protected TextView mTextHeading;
    @InjectView(R.id.imageView2)
    protected ImageView mImgCrossHeader;
    @InjectView(R.id.viewpager)
    protected ViewPager viewPager;
    @InjectView(R.id.tabs)
    protected TabLayout tabLayout;
    int mLockstatus = 0;
    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private int[] tabIcons = {R.mipmap.b_lock_28, R.mipmap.b_unlock_28};
    private TextView tablock, tabUnlock;
    private String mPostIdReceived = "";
    private RecyclerView mRecycleUnlocked;
    private List<ModelPostActivityList> mListPostActivityUnlocked;
    private AdapterFragmentUnlock mAdapterUnlocked;
    private RecyclerView mRecycleUnlockInProgress;
    private List<ModelPostActivityList> mListUnlockInProgress;
    private AdapterFragmentLock mAdapterUnlockInProgress;
    private TextView mFollowButtonStatus;
    private int mLockUnlockSide = 0;
    private ModelPostActivityList mModelPostLockActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_unlock);
        ButterKnife.inject(this);

        getPreviousValues();
        initViews();
        implementListeners();
    }

    private void getPreviousValues() {
        mLockUnlockSide = StaticValues.mLockUnlockSide;
        mPostIdReceived = StaticValues.mPostId;
    }

    private void implementListeners() {
        mImgCrossHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uihandle.goBack();
            }
        });

    }

    private void initViews() {
        uihandle = new UiHandleMethods(mContext);
        mListPostActivityUnlocked = new ArrayList<>();
        mListUnlockInProgress = new ArrayList<>();

        tablock = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabUnlock = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        /***
         * setting icons
         *  ***/
        tablock.setCompoundDrawablesWithIntrinsicBounds(tabIcons[0], 0, 0, 0);
        tabUnlock.setCompoundDrawablesWithIntrinsicBounds(tabIcons[1], 0, 0, 0);

        //   setup for tabs with view pager
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab = tabLayout.getTabAt(mLockUnlockSide);
        if (tab != null) {
            tab.select();
        }
    }

    //   , , ; tabIcons
    private void setupTabIcons() {
        try {
            /**
             * * attach custom tabs
             * ***/
            tabLayout.getTabAt(0).setCustomView(tablock);
            tabLayout.getTabAt(1).setCustomView(tabUnlock);

            /*
            tabLayout.getTabAt(1).setIcon(tabIcons[0]);
            tabLayout.getTabAt(2).setIcon(tabIcons[1]);
            tabLayout.getTabAt(3).setIcon(tabIcons[2]);
            tabLayout.getTabAt(4).setIcon(tabIcons[3]);*/

            // tabLayout.getTabAt(1).setText("23");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentLock(), "");
        adapter.addFragment(new FragmentUnlock(), "");

        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if (position == 0) {
                    mTextHeading.setText("Unlock in Progress");
                } else if (position == 1) {
                    mTextHeading.setText("Unlocked");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
    }

    private void getLockAndUnlockedActivity() {
        //      mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //      Todo: Remove after testing and uncomment above
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");
        new HttpRequester(Request.Method.GET, this, map,
                Constants.POST_LOCK_ACTIVITY_LIST_GET, URLListApis.URL_GET_POST_LOCK_ACTIVITY_LIST
                .replace("POST_ID", mPostIdReceived)
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
                case Constants.POST_LOCK_ACTIVITY_LIST_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            refineJSON(jsonObject);
                            //     List<ModelFeed> mTempList = refineJSON(jsonObject);
                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
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
                            mModelPostLockActivity.setRelationship(ConstantStatusInAPI.RELATIONSHIP_FOLLOWED);
                            if (mLockstatus == 0) {
                                mAdapterUnlockInProgress.notifyDataSetChanged();
                            } else {
                                mAdapterUnlocked.notifyDataSetChanged();
                            }
                            //

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

                            mModelPostLockActivity.setRelationship(ConstantStatusInAPI.RELATIONSHIP_NONE);
                            if (mLockstatus == 0) {
                                mAdapterUnlockInProgress.notifyDataSetChanged();
                            } else {
                                mAdapterUnlocked.notifyDataSetChanged();
                            }
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

            Log.e("response", mJsonObj.toString());

            String totalUnlockInProgressUsers = mJsonObj.optString("totalUnlockInProgressUsers");
            String totalUnlockedUsers = mJsonObj.optString("totalUnlockedUsers");

            /*** setting text to tabs ***/
            tablock.setText(totalUnlockInProgressUsers);
            tabUnlock.setText(totalUnlockedUsers);
            // setup tabs
            setupTabIcons();


            JSONArray nArrayUnlockInProgress = mJsonObj.optJSONArray("latestUnlockInProgressList");
            if (nArrayUnlockInProgress != null && nArrayUnlockInProgress.length() > 0) {
                /* if (!mListSearchedUsers.isEmpty()) {
                    mListSearchedUsers.clear();
                }*/
                for (int i = 0; i < nArrayUnlockInProgress.length(); i++) {

                    JSONObject jUserIndex = nArrayUnlockInProgress.optJSONObject(i);
                    JSONObject jData = jUserIndex.optJSONObject("user");


                    String name = jData.optString("name");
                    String userName = jData.optString("userName");
                    String profilePic = jData.optString("profilePic");
                    String relationship = jData.optString("relationship");
                    String uuid = jData.optString("uuid");

                    String bio = jData.optString("bio");
                    String followersCount = jData.optString("followersCount");
                    String followingCount = jData.optString("followingCount");
                    String joined = displayQuoteTime(Long.parseLong(jData.optString("joined")));

                    JSONObject mJsonLoc = jData.optJSONObject("location");

                    String loc_name = mJsonLoc.optString("name");
                    String loc_city = mJsonLoc.optString("city");
                    String loc_state = mJsonLoc.optString("state");
                    String loc_country = mJsonLoc.optString("country");
                    String activityDate = displayQuoteTime(Long.parseLong(jUserIndex.optString("activityDate")));

                    mListUnlockInProgress.add(new ModelPostActivityList(name, userName, profilePic, relationship, uuid,
                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country, activityDate));
                }
                //   mRecycleUnlocked.setAdapter(new AdapterFragmentUnlock(this));
                mAdapterUnlockInProgress = new AdapterFragmentLock(mContext, mListUnlockInProgress);
                mRecycleUnlockInProgress.setAdapter(mAdapterUnlockInProgress);

            } else {
                Log.e("response_else", "empty");
            }
            // unlocked users list
            JSONArray nArray = mJsonObj.optJSONArray("latestUnlockedUsersList");
            if (nArray != null && nArray.length() > 0) {
                /* if (!mListSearchedUsers.isEmpty()) {
                    mListSearchedUsers.clear();
                }*/
                for (int i = 0; i < nArray.length(); i++) {

                    JSONObject jUserIndex = nArray.optJSONObject(i);
                    JSONObject jData = jUserIndex.optJSONObject("user");


                    String name = jData.optString("name");
                    String userName = jData.optString("userName");
                    String profilePic = jData.optString("profilePic");
                    String relationship = jData.optString("relationship");
                    String uuid = jData.optString("uuid");

                    String bio = jData.optString("bio");
                    String followersCount = jData.optString("followersCount");
                    String followingCount = jData.optString("followingCount");
                    String joined = displayQuoteTime(Long.parseLong(jData.optString("joined")));

                    JSONObject mJsonLoc = jData.optJSONObject("location");

                    String loc_name = mJsonLoc.optString("name");
                    String loc_city = mJsonLoc.optString("city");
                    String loc_state = mJsonLoc.optString("state");
                    String loc_country = mJsonLoc.optString("country");
                    String activityDate = displayQuoteTime(Long.parseLong(jUserIndex.optString("activityDate")));

                    mListPostActivityUnlocked.add(new ModelPostActivityList(name, userName, profilePic, relationship, uuid,
                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country, activityDate));
                }


                //   mRecycleUnlocked.setAdapter(new AdapterFragmentUnlock(this));
                mAdapterUnlocked = new AdapterFragmentUnlock(mContext, mListPostActivityUnlocked);
                mRecycleUnlocked.setAdapter(mAdapterUnlocked);

            } else {
                Log.e("response_else", "empty");
            }
        } catch (Exception ex) {
            Log.e("feed", ex.toString());
        }
    }

    @Override
    public void getUnlockedUsers(RecyclerView mRecyclerView) {
        this.mRecycleUnlocked = mRecyclerView;
        // calling api for get unlocked users
        getLockAndUnlockedActivity();

    }

    @Override
    public void getLockInProgressUsers(RecyclerView mRecycleView) {
        this.mRecycleUnlockInProgress = mRecycleView;

        //    mRecyclerPosts.setAdapter(new AdapterFragmentLock(getActivity()));

    }

    @Override
    public void onRelationshipClickLock(String mRelation, ModelPostActivityList mModelPostActivity, TextView mTextFollowBtn) {
        this.mModelPostLockActivity = mModelPostActivity;
        mLockstatus = 0;
        if (mTextFollowBtn.getText().toString().trim().equals("Follow")) {
            goForFollowUser(mModelPostActivity.getUuid(), mTextFollowBtn);
        } else {
            alertForUnFollowUserLock(mTextFollowBtn);
        }

    }

    @Override
    public void onRelationshipClickUnlocked(String mRelation, ModelPostActivityList mModelPostActivity, TextView mTextFollowBtn) {
        this.mModelPostLockActivity = mModelPostActivity;
        mLockstatus = 1;
        if (mTextFollowBtn.getText().toString().trim().equals("Follow")) {
            goForFollowUser(mModelPostActivity.getUuid(), mTextFollowBtn);
        } else {
            alertForUnFollowUserUnlocked(mTextFollowBtn);
        }

    }

    private void alertForUnFollowUserLock(final TextView mTextFollowBtn) {

        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("Unfollow " + mModelPostLockActivity.getName())
                .positiveText("Unfollow")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        goForUnFollowUser(mModelPostLockActivity.getUuid(), mTextFollowBtn);
                    }
                })
                .negativeText("Cancel")
                .show();

    }

    private void alertForUnFollowUserUnlocked(final TextView mTextFollowingBtn) {
        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("Unfollow " + mModelPostLockActivity.getName())
                .positiveText("Unfollow")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        goForUnFollowUser(mModelPostLockActivity.getUuid(), mTextFollowingBtn);
                    }
                })
                .negativeText("Cancel")
                .show();

    }

    private void goForFollowUser(String receiverUUID, TextView mFollowBtn) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }
        mFollowButtonStatus = mFollowBtn;

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Following...");


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


        new HttpRequester("", Request.Method.POST, this, Constants.UNFOLLOW_USER_POST, URLListApis.URL_UNFOLLOW_USER.
                replace("user_UUID", receiverUUID).replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    @Override
    public void goForProfile(String uuid) {


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
