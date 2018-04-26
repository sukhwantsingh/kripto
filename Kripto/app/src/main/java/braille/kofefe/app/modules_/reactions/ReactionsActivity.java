package braille.kofefe.app.modules_.reactions;

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
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.modules_.reactions.adapter.AdapterAnger;
import braille.kofefe.app.modules_.reactions.adapter.AdapterLaugh;
import braille.kofefe.app.modules_.reactions.adapter.AdapterLiked;
import braille.kofefe.app.modules_.reactions.adapter.AdapterLove;
import braille.kofefe.app.modules_.reactions.adapter.AdapterSad;
import braille.kofefe.app.modules_.reactions.callback.IReactionCallback;
import braille.kofefe.app.modules_.reactions.fragment.FragmentAnger;
import braille.kofefe.app.modules_.reactions.fragment.FragmentLaugh;
import braille.kofefe.app.modules_.reactions.fragment.FragmentLiked;
import braille.kofefe.app.modules_.reactions.fragment.FragmentLove;
import braille.kofefe.app.modules_.reactions.fragment.FragmentSad;
import braille.kofefe.app.modules_.reactions.model.ModelReactionCommon;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;

public class ReactionsActivity extends CommonActivity implements
        IReactionCallback {

    private Activity mContext = this;
    private UiHandleMethods uihandle;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {R.mipmap.like, R.mipmap.sadd, R.mipmap.heart, R.mipmap.sleep, R.mipmap.laugh_24};
    private TextView tabLaugh, tabLike, tabSad, tabLove, tabAnger;
    private ImageView mImgCrossHeader;

    private String postId = "";
    private RecyclerView mRecycleLiked, mRecycleSad, mRecycleLove, mRecycleAnger, mRecycleLaugh;
    private AdapterLiked mAdapterLiked;

    private AdapterSad mAdapterSad;
    private AdapterLove mAdapterLove;
    private AdapterAnger mAdapterAnger;
    private AdapterLaugh mAdapterLaugh;
    private TextView mFollowButtonStatus;
    private String mClickedType;
    private ModelReactionCommon mModelCommonReactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactions);

        initViews();
        getValuesFromPrevious();
        implementListeners();

    }

    private void getValuesFromPrevious() {
        postId = StaticValues.mPostIdForReactionsDisplay;

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


        mImgCrossHeader = (ImageView) findViewById(R.id.imageView2);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    //   , ,  //   //    tabIcons
    private void setupTabIcons() {
        try {

            tabLaugh = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabLike = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabSad = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabLove = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabAnger = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

            /***
             * setting icons
             * ***/

            tabLike.setCompoundDrawablesWithIntrinsicBounds(tabIcons[0], 0, 0, 0);
            tabSad.setCompoundDrawablesWithIntrinsicBounds(tabIcons[1], 0, 0, 0);
            tabLove.setCompoundDrawablesWithIntrinsicBounds(tabIcons[2], 0, 0, 0);
            tabAnger.setCompoundDrawablesWithIntrinsicBounds(tabIcons[3], 0, 0, 0);
            tabLaugh.setCompoundDrawablesWithIntrinsicBounds(tabIcons[4], 0, 0, 0);


            tabLike.setText(StaticValues.ReactionsCount.mLikeCount);
            tabLaugh.setText(StaticValues.ReactionsCount.mLaughCount);
            tabSad.setText(StaticValues.ReactionsCount.mSadCount);
            tabLove.setText(StaticValues.ReactionsCount.mLoveCount);
            tabAnger.setText(StaticValues.ReactionsCount.mAngryCount);

            /*** attach custom tabs  ***/
            tabLayout.getTabAt(0).setCustomView(tabLike);
            tabLayout.getTabAt(1).setCustomView(tabLaugh);
            tabLayout.getTabAt(2).setCustomView(tabSad);
            tabLayout.getTabAt(3).setCustomView(tabLove);
            tabLayout.getTabAt(4).setCustomView(tabAnger);


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

        adapter.addFragment(new FragmentLiked(), "Liked");
        adapter.addFragment(new FragmentLaugh(), "Laugh");
        adapter.addFragment(new FragmentSad(), "Sad");
        adapter.addFragment(new FragmentLove(), "Love");
        adapter.addFragment(new FragmentAnger(), "Anger");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
    }

    @Override
    public void startLoadingLiked(RecyclerView mRecycleLiked) {

        this.mRecycleLiked = mRecycleLiked;
        goForLikedReactions(Constants.ConstantPutReactions.REACTION_LIKED);
        //     mRecycleLiked.setAdapter(new AdapterLiked(mContext));

    }

    @Override
    public void startLoadingSad(RecyclerView mRecycleSad) {
        this.mRecycleSad = mRecycleSad;
        goForLikedReactions(Constants.ConstantPutReactions.REACTION_SAD);
    }

    @Override
    public void onFollowerBtnClick(String mClickedType, int position, ModelReactionCommon mModelCommonReaction,
                                   TextView mTextFollowerBtn) {
        //            Todo: follows     users
        this.mClickedType = mClickedType;
        this.mModelCommonReactions = mModelCommonReaction;

        if (mTextFollowerBtn.getText().toString().trim().equals("Follow")) {
            goForFollowUser(mModelCommonReaction.getUuid(), mTextFollowerBtn);
        } else {
            alertForUnFollowUserUnlocked(mTextFollowerBtn);

        }

    }

    private void alertForUnFollowUserUnlocked(final TextView mTextFollowingBtn) {
        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("Unfollow " + mModelCommonReactions.getName())
                .positiveText("Unfollow")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        goForUnFollowUser(mModelCommonReactions.getUuid(), mTextFollowingBtn);
                    }
                })
                .negativeText("Cancel")
                .show();

    }

    @Override
    public void startLoadingLove(RecyclerView mRecycleLove) {
        this.mRecycleLove = mRecycleLove;
        goForLikedReactions(Constants.ConstantPutReactions.REACTION_LOVE);
    }

    @Override
    public void startLoadingAnger(RecyclerView mRecycleAnger) {
        this.mRecycleAnger = mRecycleAnger;
        goForLikedReactions(Constants.ConstantPutReactions.REACTION_ANGER);
    }

    @Override
    public void startLoadingLaugh(RecyclerView mRecycleLaugh) {
        this.mRecycleLaugh = mRecycleLaugh;
        goForLikedReactions(Constants.ConstantPutReactions.REACTION_LAUGH);
    }


    private void goForLikedReactions(String mReaction) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }
        //    showIOSProgress("UnFollowing...");    demo post_id: "34ce1c73-345b-4335-9ebc-8c70e78bcfea"
        switch (mReaction) {
            case Constants.ConstantPutReactions.REACTION_LIKED:

                new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(),
                        Constants.GET_ALL_REACTIONS_OF_TYPE_LIKED,
                        URLListApis.URL_GET_REACTIONS_OF_ALL_TYPE.replace("POST_ID", postId).
                                replace("WHAT_YOU_WANT", Constants.ConstantPutReactions.REACTION_LIKED), this);
                break;
            case Constants.ConstantPutReactions.REACTION_LAUGH:

                new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(),
                        Constants.GET_ALL_REACTIONS_OF_TYPE_LAUGH,
                        URLListApis.URL_GET_REACTIONS_OF_ALL_TYPE.replace("POST_ID", postId).
                                replace("WHAT_YOU_WANT", Constants.ConstantPutReactions.REACTION_LAUGH), this);
                break;
            case Constants.ConstantPutReactions.REACTION_LOVE:

                new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(),
                        Constants.GET_ALL_REACTIONS_OF_TYPE_LOVE,
                        URLListApis.URL_GET_REACTIONS_OF_ALL_TYPE.replace("POST_ID", postId).
                                replace("WHAT_YOU_WANT", Constants.ConstantPutReactions.REACTION_LOVE), this);
                break;
            case Constants.ConstantPutReactions.REACTION_SAD:

                new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(),
                        Constants.GET_ALL_REACTIONS_OF_TYPE_SAD,
                        URLListApis.URL_GET_REACTIONS_OF_ALL_TYPE.replace("POST_ID", postId).
                                replace("WHAT_YOU_WANT", Constants.ConstantPutReactions.REACTION_SAD), this);
                break;
            case Constants.ConstantPutReactions.REACTION_ANGER:

                new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(),
                        Constants.GET_ALL_REACTIONS_OF_TYPE_ANGER,
                        URLListApis.URL_GET_REACTIONS_OF_ALL_TYPE.replace("POST_ID", postId).
                                replace("WHAT_YOU_WANT", Constants.ConstantPutReactions.REACTION_ANGER), this);
                break;

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
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            JSONObject jsonObject = null;
            Log.e("Response", response);

            if (!TextUtils.isEmpty(response) || !response.equals("")) {
                jsonObject = new JSONObject(response);
            }

            switch (serviceCode) {
                case Constants.GET_ALL_REACTIONS_OF_TYPE_LIKED:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            // showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            if (refineJSON(jsonObject) != null) {
                                mAdapterLiked = new AdapterLiked(mContext, refineJSON(jsonObject));
                                mRecycleLiked.setAdapter(mAdapterLiked);
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, "No result for query!");
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;


                case Constants.GET_ALL_REACTIONS_OF_TYPE_LAUGH:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            // showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            if (refineJSON(jsonObject) != null) {
                                mAdapterLaugh = new AdapterLaugh(mContext, refineJSON(jsonObject));
                                mRecycleLaugh.setAdapter(mAdapterLaugh);
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, "No result for query!");
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.GET_ALL_REACTIONS_OF_TYPE_LOVE:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            // showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            if (refineJSON(jsonObject) != null) {
                                mAdapterLove = new AdapterLove(mContext, refineJSON(jsonObject));
                                mRecycleLove.setAdapter(mAdapterLove);
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, "No result for query!");
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.GET_ALL_REACTIONS_OF_TYPE_SAD:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            // showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            if (refineJSON(jsonObject) != null) {
                                mAdapterSad = new AdapterSad(mContext, refineJSON(jsonObject));
                                mRecycleSad.setAdapter(mAdapterSad);
                            }
                        } else {
                            showFancyToast(TastyToast.ERROR, "No result for query!");
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.GET_ALL_REACTIONS_OF_TYPE_ANGER:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            // showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            if (refineJSON(jsonObject) != null) {
                                mAdapterAnger = new AdapterAnger(mContext, refineJSON(jsonObject));
                                mRecycleAnger.setAdapter(mAdapterAnger);
                            }
                        } else {
                            showFancyToast(TastyToast.ERROR, "No result for query!");
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
                            mFollowButtonStatus.setText("Following");
                            mModelCommonReactions.setRelationship(ConstantStatusInAPI.RELATIONSHIP_FOLLOWED);
                            if (mClickedType.equals("An")) {
                                mAdapterAnger.notifyDataSetChanged();
                            } else if (mClickedType.equals("La")) {
                                mAdapterLaugh.notifyDataSetChanged();
                            } else if (mClickedType.equals("Li")) {
                                mAdapterLiked.notifyDataSetChanged();
                            } else if (mClickedType.equals("Lo")) {
                                mAdapterLove.notifyDataSetChanged();
                            } else if (mClickedType.equals("Sa")) {
                                mAdapterSad.notifyDataSetChanged();
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
                            mModelCommonReactions.setRelationship(ConstantStatusInAPI.RELATIONSHIP_NONE);
                            if (mClickedType.equals("An")) {
                                mAdapterAnger.notifyDataSetChanged();
                            } else if (mClickedType.equals("La")) {
                                mAdapterLaugh.notifyDataSetChanged();
                            } else if (mClickedType.equals("Li")) {
                                mAdapterLiked.notifyDataSetChanged();
                            } else if (mClickedType.equals("Lo")) {
                                mAdapterLove.notifyDataSetChanged();
                            } else if (mClickedType.equals("Sa")) {
                                mAdapterSad.notifyDataSetChanged();
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

    private List<ModelReactionCommon> refineJSON(JSONObject mJsonObj) {
        ArrayList<ModelReactionCommon> mListCommon = new ArrayList<>();
        try {
       /* **
       * { "ack": "SUCCESS", "errors": [], "warnings": [], "messageToUser": null, "matchingPosts": [], "matchingUsers": [] } ***/

            Log.e("response", mJsonObj.toString());
            JSONArray nArray = mJsonObj.optJSONArray("reactions");

            if (nArray != null && nArray.length() > 0) {
             /*   if (!mListSearchedUsers.isEmpty()) {
                    mListSearchedUsers.clear();
                }*/
                for (int i = 0; i < nArray.length(); i++) {
                    JSONObject jDataIndex = nArray.optJSONObject(i);
                    JSONObject jData = jDataIndex.optJSONObject("user");

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

                    String reaction_type = jDataIndex.optString("reactionType");
                    String reacted_date = jDataIndex.optString("reactedDate");

                    mListCommon.add(new ModelReactionCommon(name, userName, profilePic, relationship, uuid,
                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country,
                            reaction_type, displayQuoteTime(Long.parseLong(reacted_date))));
                }


                return mListCommon;
            } else {
                Log.e("response_else", "empty");
                return null;
            }
        } catch (Exception ex) {

            Log.e("feed", ex.toString());
            return null;
        }
    }

    @Override
    public void goForUserProfile(String mUserUUId) {
        StaticValues.mSearechedUserUUID = mUserUUId;
        uihandle.goForNextScreen(ProfileActivity.class);

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
