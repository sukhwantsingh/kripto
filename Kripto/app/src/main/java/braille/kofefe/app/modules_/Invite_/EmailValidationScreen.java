package braille.kofefe.app.modules_.Invite_;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.fusedlocationapi.LocationFinder;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class EmailValidationScreen extends CommonActivity {


    @InjectView(R.id.textView_app_name)
    protected TextView mTextHeadingAppName;

    @InjectView(R.id.edt_email_validation)
    protected EditText mEdtEmailId;

    private String mEmailId = "";

    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private Handler handler;
    // fetch automatic current location
    private LocationFinder mLocationFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_validation_screen);
        ButterKnife.inject(this);

        initViews();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
    }

    private void initViews() {

        mLocationFinder = new LocationFinder(mContext);
        mLocationFinder.startApiClient();


        uihandle = new UiHandleMethods(mContext);
        uihandle.changeColorToAppGradient(mTextHeadingAppName);
        mEdtEmailId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.e("pressed", "Enter pressed");
                    mEdtEmailId.setFocusable(false);
                    fireApi();

                }
                return false;
            }
        });
        mEdtEmailId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mEdtEmailId.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    public void regainView(View v) {
        StaticValues.mEmailCheckVerifier = false;
        //     stopHandler();
        uihandle.goBack();

    }

    private void showConfirmDialog(String eMailId) {
        new MaterialDialog.Builder(this)
                .title("Email Verification!")
                .titleGravity(GravityEnum.CENTER)
                .content(Html.fromHtml("A verification email is sent to <font color='#00c6fb'><u>" + eMailId + "</u></font>. Please check your email and confirm!"))
                .cancelable(false)
                .positiveText("OK")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Log.e("pressed", "dialog pressed");
                        //  startHandler();
                        //Todo: go to next once the email is sent to your inbox
                        uihandle.goForNextScreen(CreateProfileScreen.class);

                    }
                })
                .show();
    }


    public void goToNext(View v) {
      fireApi();
     //    uihandle.goForNextScreen(CreateProfileScreen.class);
    }

    private void fireApi() {
        mEmailId = mEdtEmailId.getText().toString().trim();
        uihandle.hideSoftKeyboard();
        if (TextUtils.isEmpty(mEmailId)) {
            mEdtEmailId.setError("Enter your eMail");
            return;
        } else if (!UiHandleMethods.isValidEmail(mEmailId)) {
            mEdtEmailId.setError("Invalid eMail");
            return;
        } else {
            goForEmailVerification();
        }

    }

    private void goForEmailVerification() {

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
//      map.put(Constants.ApiParamSignature.USER_ID, getPrefrences().getLoggedUserId());
        showIOSProgress("Initiating...");
        new HttpRequester(Request.Method.GET, this, map, Constants.INITIATE_EMAIL_VERIFICATION,
                URLListApis.URL_INITIATE_EMAIL_VERIFICATION.replace("EMAIL_ID", mEmailId).replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            Log.e("Response", response);
            JSONObject jsonObject = new JSONObject(response);
            switch (serviceCode) {
                case Constants.INITIATE_EMAIL_VERIFICATION:
                    if (jsonObject.optString("ack").equals("SUCCESS")) {
                        //   showFancyToast(TastyToast.SUCCESS, jsonObject.optString("password"));
                        if (jsonObject.optString("status").equals("SUCCESS")) {
                            dismissIOSProgress();
                            //        showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            //        keep polling server for verified link
                            showConfirmDialog(mEdtEmailId.getText().toString());


                        } else {
                            dismissIOSProgress();
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("password"));
                        }
                    } else {

                        dismissIOSProgress();
                        showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                    }
                    break;

                case Constants.CHECK_EMAIL_VERIFICATION_STATUS:
                    dismissIOSProgress();
                    Log.i("emailVerificationStatus", jsonObject.optString("emailVerificationStatus"));
                    if (jsonObject.optString("ack").equals("SUCCESS")) {
                        if (!jsonObject.optString("emailVerificationStatus").equals("VERIFICATION_INITIATED_AND_PENDING")) {
                            /*****
                             * set values for header in next step
                             * *****/
                            StaticValues.mEmailCheckVerifier = false;
                            //           stopHandler();
                            showFancyToast(TastyToast.SUCCESS, jsonObject.optString("emailVerificationStatus"));

                            uihandle.goForNextScreen(CreateProfileScreen.class);
                            //   uihandle.goForNext(MainActivity.class);
                            // stop handler for back ground thread
                        } else {
                            //   showFancyToast(TastyToast.ERROR, jsonObject.optString("emailVerificationStatus"));
                        }
                    } else {
                        showFancyToast(TastyToast.ERROR, jsonObject.optString("ack"));
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
           /* JSONObject jsonObject = new JSONObject(errorResponse);
            switch (serviceCode) {
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

                    break;*/
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //      stopHandler();
        StaticValues.mEmailCheckVerifier = false;
        uihandle.goBack();

    }

    @Override
    protected void onStop() {
        //      stopHandler();
        super.onStop();
        mLocationFinder.stopApiClient();
    }
/*
    private void stopHandler() {
        if (handler != null) {
            handler.post(null);
            handler.removeCallbacksAndMessages(null);
        }

    }*/

   /* @Override
    protected void onResume() {
        super.onResume();
        if (StaticValues.mEmailCheckVerifier) {
            startHandler();
        } else {
            stopHandler();
        }
    }*/

    /* private void startHandler() {
         StaticValues.mEmailCheckVerifier = true;

         if (handler != null) {
             scheduleToRatingUser();
         } else {
             handler = new Handler();
             scheduleToRatingUser();
         }
     }
 */
    private void goForCheckEmailVerificationStatus() {
        if (StaticValues.mEmailCheckVerifier) {
            showIOSProgress("Checking for verify email...");

            new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(), Constants.CHECK_EMAIL_VERIFICATION_STATUS,
                    URLListApis.URL_CHECK_EMAIL_VERIFICATION_STATUS.replace("EMAIL_ID", mEmailId).replace("REQUESTID_VALUE", getRandomUUID()), this);
        } else {
            //     stopHandler();
        }
    }

   /* public void scheduleToRatingUser() {
        handler.postDelayed(new Runnable() {
            public void run() {
                goForCheckEmailVerificationStatus();
                // this method will contain your almost-finished HTTP calls
                handler.postDelayed(this, Constants.WAIT_SECONDS);
            }
        }, Constants.WAIT_SECONDS);
    }
*/

}
