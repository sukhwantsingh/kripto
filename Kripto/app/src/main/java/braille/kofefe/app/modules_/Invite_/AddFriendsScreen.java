package braille.kofefe.app.modules_.Invite_;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.adapter.AdapterAddFriends;
import braille.kofefe.app.modules_.Invite_.callback.IAddFriendsCallback;
import braille.kofefe.app.modules_.Invite_.model.ModelAddFriends;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.ModelContacts;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.check_permissions.PermissionCheck;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Snow-Dell-05 on 12-Jan-18.
 */

public class AddFriendsScreen extends CommonActivity implements IAddFriendsCallback {

    @InjectView(R.id.img_back)
    protected ImageView mImgBack;
    @InjectView(R.id.text_skip)
    protected TextView mTextSkip;
    @InjectView(R.id.recycle_contacts_to_invite)
    protected RecyclerView mRecycleAddFriends;
    protected AdapterAddFriends mAdapterFriends;
    String mToSendUuids = "", mContacts = "";
    private Activity mContext = this;
    private List<ModelAddFriends> mListAddFriends;
    private List<ModelContacts> mListContacts;

    private UiHandleMethods uihandle;
    private ArrayList<String> mUuids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contacts_add_friends_);

        ButterKnife.inject(this);
        initViews();

    }

    private void initViews() {
        try {

            uihandle = new UiHandleMethods(this);
            mListAddFriends = new ArrayList<>();
            mListContacts = new ArrayList<>();
            mUuids = new ArrayList<>();

            mRecycleAddFriends.setLayoutManager(new LinearLayoutManager(mContext));

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (!new PermissionCheck(this).checkReadContacts()) {
                    //       carry on the normal flow, as the case of  permissions  granted.
                    getContactsDetailFromPhone();
                }
                //        mFlagPermission = true;
            } else {
                getContactsDetailFromPhone();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getContactsDetailFromPhone() {

        showIOSProgress("Loading...");
        Thread t = new Thread(new Runnable() {
            public void run() {
        /*
         * Do something
         */
                if (getContactsList().size() > 0) {
                    mListContacts.addAll(getContactsList());

                    for (int i = 0; i < mListContacts.size(); i++) {
                        if (i == mListContacts.size() - 1) {
                            mContacts = mContacts + "\"" + refinePhoneNumber(mListContacts.get(i).getmPhoneNumber()) + "\"";
                        } else {
                            mContacts = mContacts + "\"" + refinePhoneNumber(mListContacts.get(i).getmPhoneNumber()) + "\"" + ",";
                        }
                    }

                    Log.e("contacts:", mContacts);
                    goForCheckFriends(mContacts);
                }
            }
        });
        t.start();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionCheck.PERMISSION_CODE) {
            for (int i = 0; i < grantResults.length; i++) {

                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    if (i == grantResults.length - 1) {
                        //    go for api check friends
                        getContactsDetailFromPhone();

                    }
                } else {

                    //  uihandle.showToast("Permission ungranted");
                    // mContext.finish();
                    // mContext.moveTaskToBack(true);
                    return;
                }
            }
        }
    }


    @OnClick(R.id.btn_quick_add)
    public void quickAddUsers() {

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }


        mToSendUuids = "";

        for (int i = 0; i < mUuids.size(); i++) {
            if (i == mUuids.size() - 1) {
                mToSendUuids = mToSendUuids + "\"" + mUuids.get(i) + "\"";
            } else {
                mToSendUuids = mToSendUuids + "\"" + mUuids.get(i) + "\"" + ",";
            }
        }

        // Fire Api to quick add friends
        goForQuickAddUsers(mToSendUuids);

    }


    @OnClick(R.id.text_skip)
    public void onSkip() {
        uihandle.goForNextScreen(InviteFriendsScreen.class);
        //  this.finish();
    }

    private void goForCheckFriends(String receiverContacts) {

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }


        String mContacts = "{\"friends\" : [ " + receiverContacts + " ]}";

        Log.e("send: ", mContacts);

        new HttpRequester(mContacts, Request.Method.POST, this, Constants.CHECK_FRIENDS_POST, URLListApis.URL_CHECK_FRIENDS.
                replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    private void goForQuickAddUsers(String receiverUuids) {

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        showIOSProgress("Loading...");

        String mRegisteredUuid = "{\"usersToFollow\" : [ " + receiverUuids + " ]}";
        // String mContacts = "{\"friends\" : [ \"+18699202626\", \"+917508765880\", \"+919988533158\"]}";

        Log.e("send_uuids: ", mRegisteredUuid);

        new HttpRequester(mRegisteredUuid, Request.Method.POST, this, Constants.INITIALIZE_FOLLOWERS_POST, URLListApis.URL_INITIALIZE_FOLLOWERS.
                replace("REQUESTID_VALUE", getRandomUUID()), this);
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
                case Constants.CHECK_FRIENDS_POST:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            refineJSON(jsonObject);
                        } else {
                            dismissIOSProgress();
                            showFancyToast(TastyToast.ERROR, "No result for query!");
                        }
                    } else {
                        dismissIOSProgress();
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;

                case Constants.INITIALIZE_FOLLOWERS_POST:
                    dismissIOSProgress();
                    if (jsonObject.optString("ack").equals("SUCCESS")) {
                        //   showFancyToast(TastyToast.SUCCESS, jsonObject.optString("password"));
                        if (jsonObject.optString("status").equals("SUCCESS")) {

                            String msg = jsonObject.optString("messageToUser");
                            if (!msg.equals("null")) {
                                showFancyToast(TastyToast.SUCCESS, msg);
                            } else {
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            }

                            uihandle.goForNextScreen(InviteFriendsScreen.class);

                            //        showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            //        keep polling server for verified link

                        }
                    } else {
                        showFancyToast(TastyToast.ERROR, "User account does not exist!");
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
       /* **  * {       "ack": "SUCCESS",       "errors": [],       "warnings": [],       "messageToUser": null,       "matchingPosts": [],     "matchingUsers": []
       }       ***/

            JSONArray nArray = mJsonObj.optJSONArray("registered");
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


                    mListAddFriends.add(new ModelAddFriends(true, name, userName, profilePic, relationship, uuid,
                            bio, followersCount, followingCount, joined, loc_name, loc_city, loc_state, loc_country));

                    mUuids.add(uuid);
                    Log.e("uid", uuid);
                }

                mAdapterFriends = new AdapterAddFriends(mContext, mListAddFriends);
                mRecycleAddFriends.setAdapter(mAdapterFriends);
            }


        } catch (Exception ex) {
            Log.e("feed", ex.toString());
        }
    }

    @Override
    public void getUncheckedUsers(boolean mFlag, int position, String uuid) {

        if (mFlag) {
            mUuids.add(uuid);
        } else {
            mUuids.remove(uuid);
        }

        for (String uuidd : mUuids) {
            Log.e("uid", uuidd);
        }

    }

    @Override
    public void onBackPressed() {

    }
}
