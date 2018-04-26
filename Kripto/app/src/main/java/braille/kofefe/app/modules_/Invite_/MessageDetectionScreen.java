package braille.kofefe.app.modules_.Invite_;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageDetectionScreen extends CommonActivity {


    @InjectView(R.id.textView_app_name)
    protected TextView mTextHeadingAppName;

    @InjectView(R.id.txt_pin_entry)
    protected PinEntryEditText pinEntry;
    /***
     * Layout third decelarations
     * ***/
    @InjectView(R.id.textView2_wrong_text)
    protected TextView mTextWrongNumber;


    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private String mPhoneNumber = "", mOTP = "";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                //        Todo: Do whatever you want with the Message Received for OTP received
                showToast(message);
            }   }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detection_screen);
        ButterKnife.inject(this);

        initViews();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

    }

    private void initViews() {
        mPhoneNumber = StaticValues.mConfirmPhoneNumber;

        uihandle = new UiHandleMethods(mContext);
        uihandle.changeColorToAppGradient(mTextHeadingAppName);

        mTextWrongNumber.setText(Html.fromHtml("Please enter the verification code sent to\n " + mPhoneNumber + ". <font color=\"#00c6fb\">Wrong number?</font>"));

        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {

                    mOTP = str.toString().trim();

                    if (str.toString().length() == 4) {
                        uihandle.hideSoftKeyboard();
                        fireApi();
                    }

                }
            });
        }
        mTextWrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uihandle.goBack();
            }
        });
    }

    public void regainView(View v) {
        uihandle.goBack();
    }

    public void goToNext(View v) {
        fireApi();
    }

    private void fireApi() {
        if (TextUtils.isEmpty(mOTP)) {
            pinEntry.setError("Invalid pin!");
        }
        uihandle.hideSoftKeyboard();
        goForVerification();

        //   uihandle.goForNextScreen(CreateProfileScreen.class);
    }

    private void goForVerification() {
        if (TextUtils.isEmpty(pinEntry.getText().toString().trim())) {
            pinEntry.setError("Please fill verification Code!!");
            return;
        }
        //     mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        // map.put(Constants.ApiParamSignature.USER_ID, getPrefrences().getLoggedUserId());
        showIOSProgress("Verifing...");
        new HttpRequester(Request.Method.GET, this, map, Constants.CONFIRM_PHONE_VERIFICATION,
                URLListApis.URL_CONFIRM_PHONE_VERIFICATION.replace("NUMBER", mPhoneNumber).replace("OTP", mOTP).replace("REQUESTID_VALUE", getRandomUUID()), this);
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
                case Constants.CONFIRM_PHONE_VERIFICATION:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //   showFancyToast(TastyToast.SUCCESS, jsonObject.optString("password"));
                            if (!jsonObject.optString("password").equals("Verification Failed")) {
                                 /*set values for header in next step */
                                // getSessionInstance().setAuthorizationPhoneNumber(mPhoneNumber);

                                getSessionInstance().setAuthorizationUUID(jsonObject.optString("userUUID"));
                                getSessionInstance().setAuthorizationPassword(jsonObject.optString("password"));

                                new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(),
                                        Constants.COMPLETE_PHONE_VERIFICATION,
                                        URLListApis.URL_COMPLETE_PHONE_VERIFICATION.replace("REQUESTID_VALUE", getRandomUUID()), this);
                            } else {
                                dismissIOSProgress();
                                showFancyToast(TastyToast.ERROR, jsonObject.optString("password"));
                            }
                        } else {
                            dismissIOSProgress();
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("messageToUser"));
                        }
                    } else {
                        dismissIOSProgress();
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.COMPLETE_PHONE_VERIFICATION:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            showFancyToast(TastyToast.SUCCESS, jsonObject.optString("ack"));

                            getSessionInstance().setVerfiedUserId(jsonObject.optString("userUUID"));

                            //             uihandle.goForNextScreen(CreateProfileScreen.class);
                            //             Todo: UnComment below code and remove above code when email verification process will work.
                            uihandle.goForNextScreen(EmailValidationScreen.class);
                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("ack"));
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
            //  JSONObject jsonObject = new JSONObject(errorResponse);
            /*switch (serviceCode) {
                case Constants.COMPLETE_PHONE_VERIFICATION:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            showFancyToast(TastyToast.SUCCESS, jsonObject.optString("ack"));
                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("ack"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }

                    break;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
    }
}
