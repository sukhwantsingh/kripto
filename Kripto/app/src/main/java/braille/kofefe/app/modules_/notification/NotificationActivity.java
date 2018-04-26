package braille.kofefe.app.modules_.notification;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import braille.kofefe.app.modules_.common_util_.LinearSmoothScrollingCustom;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.notification.callback.INotificationMarked;
import braille.kofefe.app.modules_.notification.model.ModelNotification;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Snow-Dell-05 on 11/7/2017.
 */

public class NotificationActivity extends CommonActivity implements INotificationMarked {

    @InjectView(R.id.rv_older)
    protected RecyclerView mRecyclerOlder;

    private UiHandleMethods uihandle;
    private Activity mContext = this;

    private boolean mFlagNoMoreNotifications = false;

    private AdapterNotifications mAdapterNotification;
    private List<ModelNotification> mListNotification, mListNotificationTemp;
    private String mNotificationType, mUserUUIDG;
    private TextView mTextTitleMessageG;
    private ModelNotification mModelNotificationn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.inject(this);

        initViews();
        callingApi();
    }

    private void callingApi() {
        goForFetchNotifications();
    }

    private void initViews() {
        uihandle = new UiHandleMethods(mContext);

        mListNotification = new ArrayList<>();
        mListNotificationTemp = new ArrayList<>();

        mRecyclerOlder.setLayoutManager(new LinearSmoothScrollingCustom(mContext));
    }

    public void goForClose(View v) {
        uihandle.goBack();

    }

    private void goForFetchNotifications() {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");
        new HttpRequester(Request.Method.GET, this, map,
                Constants.NOTIFICATIONS_GET, URLListApis.URL_NOTIFICATIONS, this);

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

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            JSONObject jsonObject = null;
            Log.e("Response", response);
            if (!TextUtils.isEmpty(response) || !response.equals("")) {
                jsonObject = new JSONObject(response);
            }
            switch (serviceCode) {
                case Constants.NOTIFICATIONS_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            List<ModelNotification> mTempList = refineJSON(jsonObject);
                            if (mTempList != null) {
                                mFlagNoMoreNotifications = false;

                                mListNotification.clear();
                                mListNotification.addAll(mTempList);

                                mAdapterNotification = new AdapterNotifications(mContext, mListNotification);
                                mRecyclerOlder.setAdapter(mAdapterNotification);

                            } else {
                                mAdapterNotification = new AdapterNotifications(mContext, mListNotification);
                                mRecyclerOlder.setAdapter(mAdapterNotification);

                                //   true flag to avoid run botton scroll api for more posts
                                mFlagNoMoreNotifications = true;
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;
                case Constants.GET_COMMENTS_AFTER_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            //  List<ModelComments> mTempList = refineJSON(jsonObject);


                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;
                case Constants.MARK_NOTIFICATION_AS_VIEWED_POST:
                    //    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //       showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            if (jsonObject.optString("status").equals("SUCCESS")) {


                                if (mNotificationType.equals("USER")) {
                                    StaticValues.mSearechedUserUUID = mUserUUIDG;
                                    uihandle.goForNextScreen(ProfileActivity.class);
                                } else if (mNotificationType.equals("POST")) {


                                } else if (mNotificationType.equals("ADMIN")) {

                                }

                               /*
                               * set notification viewd for viewd notification
                               */

                                mModelNotificationn.setViewed(true);
                                mAdapterNotification.notifyDataSetChanged();

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

    private List<ModelNotification> refineJSON(JSONObject mJsonObj) {
        try {
            List<ModelNotification> mListCommentLocal = new ArrayList<>();
            Log.e("response", mJsonObj.toString());

            JSONArray nArray = mJsonObj.optJSONArray("notifications");
            if (nArray != null && nArray.length() > 0) {
                if (!mListNotificationTemp.isEmpty()) {
                    mListNotificationTemp.clear();
                }
                for (int i = 0; i < nArray.length(); i++) {
                    JSONObject jData = nArray.optJSONObject(i);

                    String notificationId = jData.optString("notificationId");
                    String notificationType = jData.optString("notificationType");
                    String title = jData.optString("title");
                    String message = jData.optString("message");
                    String titleImage = jData.optString("titleImage");
                    String bodyImage = jData.optString("bodyImage");
                    String notifiedDate = displayQuoteTime(Long.parseLong(jData.optString("notifiedDate")));
                    Boolean viewed = jData.optBoolean("viewed");

                    JSONObject nData = jData.optJSONObject("data");

                    String uuid = nData.optString("uuid");

                    mListCommentLocal.add(new ModelNotification(notificationId, notificationType, title, message,
                            titleImage, bodyImage, notifiedDate, viewed, uuid));

                }
                return mListCommentLocal;
            } else {
                Log.i("feed", "empty");
                return null;
            }

        } catch (Exception ex) {
            Log.i("feed", ex.toString());
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
    }

    @Override
    public void getUuidForPost(ModelNotification mModelNotification, TextView mTextTitleMessage, String mNotificationTyp,
                               String mNotificationID_UUID, String mUserUUID) {
        this.mNotificationType = mNotificationTyp;
        this.mTextTitleMessageG = mTextTitleMessage;
        this.mModelNotificationn = mModelNotification;
        this.mUserUUIDG = mUserUUID;

        /** calling marked as viewd**/
        goForMarkNotificationAsViewed(mNotificationID_UUID);

    }
}
