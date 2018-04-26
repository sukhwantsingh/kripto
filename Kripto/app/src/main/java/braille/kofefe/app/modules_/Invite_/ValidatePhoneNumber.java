package braille.kofefe.app.modules_.Invite_;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class ValidatePhoneNumber extends CommonActivity {


    @InjectView(R.id.textView_app_name)
    protected TextView mTextHeadingAppName;

    /***
     * Layout two decelarations
     * ***/
    @InjectView(R.id.edt_phone_number)
    protected EditText mEditPhoneNumber;

    @InjectView(R.id.edt_country_code)
    protected EditText mEdtCountryCode;
    @InjectView(R.id.img_flag)
    protected ImageView mImageFlag;

    @InjectView(R.id.textView2)
    protected TextView mTextNote;

    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private String mPhoneNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validation);

        ButterKnife.inject(this);
        initViews();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
    }

    private void initViews() {

        uihandle = new UiHandleMethods(mContext);
        uihandle.changeColorToAppGradient(mTextHeadingAppName);
        uihandle.phoneNumberFormat(mEditPhoneNumber);
        //  Note: Standard message or data rates may apply.”
        mTextNote.setText(Html.fromHtml("<font color='#00c6fb'><b >Note: </b></font><font >Standard message or data rates may apply.</font>"));


        // setting initial flag
        mEdtCountryCode.setFocusable(false);
        mEdtCountryCode.setClickable(true);
        mImageFlag.setImageResource(R.drawable.united_states);


        mEditPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.e("pressed", "Enter pressed");
                    goForCheckByPhoneNumber();
                }
                return false;
            }
        });
    }


   /* @Override
    protected void onStart() {
        super.onStart();
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (new PermissionCheck(this).checkAndRequestPermissions()) {
                // carry on the normal flow, as the case of  permissions  granted.
            }
            //        mFlagPermission = true;
        } else {

        }

    }*/

    /*  @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          if (requestCode == PermissionCheck.REQUEST_ID_MULTIPLE_PERMISSIONS) {
              for (int i = 0; i < grantResults.length; i++) {
                  if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                      if (i == grantResults.length - 1) {

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
  */
    public void regainView(View v) {
        uihandle.goBack();
    }

    public void goToNext(View v) {

        goForCheckByPhoneNumber();
        // uihandle.goForNextScreen(MessageDetectionScreen.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
    }

    public String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();

    }

    private void goForValidatePhoneNumber() {
        HashMap<String, String> map = new HashMap<>();
        new HttpRequester(Request.Method.GET, this, map, Constants.VALIDATE_PHONE_NUMBER,
                URLListApis.URL_VALIDATE_PHONE_NUMBER.replace("P_NUMBER", mPhoneNumber).
                        replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    private void goForCheckByPhoneNumber() {

        // return if empty number send
        if (TextUtils.isEmpty(mEditPhoneNumber.getText().toString().trim())) {
            mEditPhoneNumber.setError("Please provide number!");
            return;
        }
        mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //  Todo: remove after testing and uncomment above
        //    mPhoneNumber = "+919501283651";

        // check connectivity
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        // Hit api
        HashMap<String, String> map = new HashMap<>();
        // map.put(Constants.ApiParamSignature.USER_ID, getPrefrences().getLoggedUserId());
        showIOSProgress("Validating...");
        new HttpRequester(Request.Method.GET, this, map, Constants.CHECK_BY_PHONE_NUMBER,
                URLListApis.URL_CHECK_BY_PHONE_NUMBER.replace("NUMBER", mPhoneNumber).replace("REQUESTID_VALUE", getRandomUUID()), this);


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
                case Constants.CHECK_BY_PHONE_NUMBER:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //           uihandle.goForNextScreen(MessageDetectionScreen.class);
                            //           showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            String status = jsonObject.optString("status");
                            switch (status) {
                                case "ALLOWED":
                                    goForValidatePhoneNumber();
                                    break;

                                case "NOT_ALLOWED":
                                    dismissIOSProgress();
                                    String messageUser = jsonObject.optString("messageToUser");
                                    showFancyToast(TastyToast.CONFUSING, messageUser);
                                    //      mEditPhoneNumber.startAnimation(uihandle.shake());
                                    break;

                                case "ALLOWED_AND_VERIFICATION_CODE_SMS_SENT":

                                    //        new HttpRequester(Request.Method.GET, this, new HashMap<String, String>(), Constants.VALIDATE_PHONE_NUMBER,
                                    //        URLListApis.URL_VALIDATE_PHONE_NUMBER.replace("P_NUMBER", mPhoneNumber).
                                    //        replace("REQUESTID_VALUE", getRandomUUID()), this);
                                    goForValidatePhoneNumber();
                                    break;


                                default:
                                    dismissIOSProgress();
                                    // showFancyToast(TastyToast.ERROR, "Thank you for ur interest in KOFEFE.
                                    // We have added you to our waiting list. You will be notified as and when ur turn is up");
                                    break;
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

                case Constants.VALIDATE_PHONE_NUMBER:
                    if (jsonObject.optString("ack").equals("SUCCESS")) {
                        // showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                        if (jsonObject.optString("status").equals("VALID")) {
                            HashMap<String, String> map = new HashMap<>();

                            new HttpRequester(Request.Method.GET, this, map,
                                    Constants.INITIATE_PHONE_NUMBER, URLListApis.URL_INITIATE_PHONE_NUMBER.
                                    replace("NUMBER", mPhoneNumber).replace("REQUESTID_VALUE", getRandomUUID()), this);
                        } else {
                            dismissIOSProgress();
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status") + " Sorry we couldn’t let you in!");
                        }
                    } else {
                        dismissIOSProgress();
                        showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                    }
                    break;

                case Constants.INITIATE_PHONE_NUMBER:
                    StaticValues.mConfirmPhoneNumber = mPhoneNumber;
                    dismissIOSProgress();

                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            uihandle.goForNextScreen(MessageDetectionScreen.class);
                            // showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("messageToUser"));
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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void countryCallback(View v) {
        CountryPickerDialog countryPicker;

       /* Name of your Custom JSON list */
        //  int resourceId = getResources().getIdentifier("country_avail", "raw", getApplicationContext().getPackageName());

        countryPicker = new CountryPickerDialog(ValidatePhoneNumber.this, new CountryPickerCallbacks() {
            @Override
            public void onCountrySelected(Country country, int flagResId) {

                mImageFlag.setImageResource(flagResId);
                mEdtCountryCode.setText("+" + country.getDialingCode());
                uihandle.setCursorOnLast(mEdtCountryCode, mEdtCountryCode.getText().toString().length());

            }

          /* Set to false if you want to disable Dial Code in the results and true if you want to show it
             Set to zero if you don't have a custom JSON list of countries in your raw file otherwise use
             resourceId for your customly available countries */
        }, true, 0);
        countryPicker.show();
    }

}
