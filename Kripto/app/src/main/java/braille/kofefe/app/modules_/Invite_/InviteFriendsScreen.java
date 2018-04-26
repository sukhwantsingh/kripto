package braille.kofefe.app.modules_.Invite_;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.adapter.AdapterInviteFriends;
import braille.kofefe.app.modules_.Invite_.callback.IInviteCallback;
import braille.kofefe.app.modules_.Invite_.model.ModelInviteFriends;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.ModelContacts;
import braille.kofefe.app.modules_.mainpostfeed.MainActivity;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.check_permissions.PermissionCheck;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Snow-Dell-05 on 15-Jan-18.
 */

public class InviteFriendsScreen extends CommonActivity implements IInviteCallback {

    @InjectView(R.id.recycle_contacts_to_invite)
    protected RecyclerView mRecycleInviteFriends;
    String mContacts = "";
    private Activity mContext = this;
    private List<ModelInviteFriends> mListInviteFriends;
    private List<ModelContacts> mListContacts;

    private AdapterInviteFriends mAdapter;

    private UiHandleMethods uihandle;
    private String mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contacts_invite_);
        ButterKnife.inject(this);

        initViews();

    }

    private void initViews() {
        try {
            uihandle = new UiHandleMethods(this);
            mListInviteFriends = new ArrayList<>();
            mListContacts = new ArrayList<>();
            mRecycleInviteFriends.setLayoutManager(new LinearLayoutManager(mContext));

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (!new PermissionCheck(this).checkReadContacts()) {
                    // carry on the normal flow, as the case of  permissions  granted.
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

    @OnClick(R.id.textView3)
    public void mDone() {
        uihandle.goForNext(MainActivity.class);

    }

    private void goForCheckFriends(String receiverContacts) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }
        String mContacts = "{\"friends\" : [ " + receiverContacts + " ]}";
        // String mContacts = "{\"friends\" : [ \"+18699202626\", \"+917508765880\", \"+919988533158\"]}";

        Log.e("send: ", mContacts);
        new HttpRequester(mContacts, Request.Method.POST, this, Constants.CHECK_FRIENDS_POST, URLListApis.URL_CHECK_FRIENDS.
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
       /* **  *
          {"ack": "SUCCESS","errors": [],"warnings": [], "messageToUser": null, "matchingPosts": [],  "matchingUsers": []
          } ***/

            Log.e("response", mJsonObj.toString());
            JSONArray nArray = mJsonObj.optJSONArray("availableToInvite");

            if (nArray != null && nArray.length() > 0) {
                for (int i = 0; i < nArray.length(); i++) {
                    String mNumber = nArray.optString(i);

                    mListInviteFriends.add(new ModelInviteFriends(mNumber));
                }

                mAdapter = new AdapterInviteFriends(mContext, mListInviteFriends, mListContacts);
                mRecycleInviteFriends.setAdapter(mAdapter);

            }


        } catch (Exception ex) {
            Log.e("feed", ex.toString());
        }
    }

    @OnClick(R.id.img_back)
    public void goBack() {
        uihandle.goBack();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void fireInviteFriend(ModelInviteFriends mInviteModel, String mNumber) {
        this.mNumber = mNumber;
        //  uihandle.showToast(getSessionInstance().getShareContentForInviteFriendScreen());
        uihandle.shareMessage(getSessionInstance().getShareContentForInviteFriendScreen(), mNumber);

    }


}

