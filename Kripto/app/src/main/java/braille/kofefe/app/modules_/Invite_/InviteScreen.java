package braille.kofefe.app.modules_.Invite_;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.mainpostfeed.MainActivity;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class InviteScreen extends CommonActivity {

    @InjectView(R.id.textView3)
    protected TextView mTextCounting;
    @InjectView(R.id.edt_country_code)
    protected EditText mEdtCountryCode;
    @InjectView(R.id.img_back)
    protected ImageView mImageViewBack;
    @InjectView(R.id.img_flag)
    protected ImageView mImageFlag;
    @InjectView(R.id.floatingActionButton)
    protected ImageView mImgActionButton;
    @InjectView(R.id.textView_app_name)
    protected TextView mTextHeadingAppName;
    @InjectView(R.id.viewflipper_invite_)
    protected ViewFlipper mFlipperInvite;

    /***
     * Layout one decalaration
     ***/
    @InjectView(R.id.textView6)
    protected TextView mTextDontHaveInvitationCode;
    @InjectView(R.id.txt_pin_entry)
    protected PinEntryEditText pinEntry;

    /***
     * Layout two decelarations
     * ***/
    @InjectView(R.id.edt_phone_number)
    protected EditText mEditPhoneNumber;
    /***
     * Layout third decelarations
     * ***/
    @InjectView(R.id.textView2_wrong_text)
    protected TextView mTextWrongNumber;

    private Activity mContext = this;
    private int mCount = 0;
    private UiHandleMethods uihandle;
    private LayoutInflater inflaterFlipperLayout;
    private View mFlipperView;
    /***
     * Layout five decalaration
     ***/
    private Button mButtonGetStarted;
    private EditText mEdtFullName, mEdtUsername, mEdtLocation, mEdtBio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButterKnife.inject(this);

        initViews();
        initViewsForFirstLayout();
        initViewsThirdlayout();
        initViewsForLayoutTwo();
        initViewsForLayoutProfile();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
    }

    private void initViewsForLayoutProfile() {

        // layout five initialisation
        mButtonGetStarted = mFlipperView.findViewById(R.id.btn_get_started);
        mEdtFullName = mFlipperView.findViewById(R.id.edt_full_name);
        mEdtUsername = mFlipperView.findViewById(R.id.edt_username);
        mEdtLocation = mFlipperView.findViewById(R.id.edt_location);
        mEdtBio = mFlipperView.findViewById(R.id.edt_bio);

        mEdtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() <= 5 || s.toString().equalsIgnoreCase("qwerty")) {
                    mEdtUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.red_tick, 0);
                } else if (s.length() > 5) {
                    mEdtUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.green_tick, 0);
                } else {
                    mEdtUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        mButtonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uihandle.goForNextScreen(MainActivity.class);  }
        });
    }

    private void initViewsThirdlayout() {
        mTextWrongNumber.setText(Html.fromHtml("Waiting to automatically detect and SMS sent to\n +62 1234567890. " + "<font color=\"#00c6fb\">Wrong number?</font>"));
        mTextWrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackFrame();
            }
        });

    }

    private void initViewsForFirstLayout() {
        uihandle.changeColorToAppGradient(mTextDontHaveInvitationCode);
   }

    private void initViewsForLayoutTwo() {

        uihandle.phoneNumberFormat(mEditPhoneNumber);
    }

    private void initViews() {

        uihandle = new UiHandleMethods(mContext);

        inflaterFlipperLayout = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFlipperView = inflaterFlipperLayout.inflate(R.layout.layout_invite_five, null);


        // change colors of heading to gradient
        uihandle.changeColorToAppGradient(mTextHeadingAppName);
        uihandle.changeColorToAppGradient(pinEntry);


        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.toString().equals("123456")) {
                        uihandle.hideSoftKeyboard();
                        Toast.makeText(mContext, "SUCCESS", Toast.LENGTH_SHORT).show();
                    } else {
                        uihandle.hideSoftKeyboard();
                        Toast.makeText(mContext, "FAIL", Toast.LENGTH_SHORT).show();
                        pinEntry.setText(null);
                    }
                }
            });
        }

        // setting initial flag
        mEdtCountryCode.setFocusable(false);
        mEdtCountryCode.setClickable(true);
        mImageFlag.setImageResource(R.drawable.united_states);


        // uihandle.setStatusBarGradiant();

    }

    public void goToNext(View v) {
        //goForValidatePhoneNumber();
        forward();
    }

    @Override
    public void onBackPressed() {
        //      super.onBackPressed();
        goBackFrame();
    }

    public void countryCallback(View v) {
        CountryPickerDialog countryPicker;

       /* Name of your Custom JSON list */
        //  int resourceId = getResources().getIdentifier("country_avail", "raw", getApplicationContext().getPackageName());

        countryPicker = new CountryPickerDialog(InviteScreen.this, new CountryPickerCallbacks() {
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

    public void regainView(View v) {
        goBackFrame();
    }


    public void dontHaveInvitaionCodeClick(View v) {
        forward();
    }

    private void forward() {
        mCount++;

        if (mCount > 4) {
            return;
        } else if (mCount == 4) {
            mTextHeadingAppName.setText("Profile");
            mFlipperInvite.addView(mFlipperView);
            mTextCounting.setText((mCount + 1) + "" + "/5");
            mFlipperInvite.setDisplayedChild(mCount);
            mImgActionButton.setVisibility(View.GONE);

        } else {
            mTextHeadingAppName.setText("KOFEFE");
            mTextCounting.setText((mCount + 1) + "/5");
            mFlipperInvite.setDisplayedChild(mCount);
        }


    }

    public void goBackFrame() {
        if (mCount <= 0) {
            this.finish();
            return;
        }
        if (mFlipperView.getVisibility() == View.VISIBLE) {
            mFlipperInvite.removeView(mFlipperView);
        }

        mImgActionButton.setVisibility(View.VISIBLE);
        mCount--;
        mTextCounting.setText((mCount + 1) + "/5");
        mFlipperInvite.setDisplayedChild(mCount);

        if (mCount == 4) {
            mTextHeadingAppName.setText("Profile");
        } else {
            mTextHeadingAppName.setText("KOFEFE");
        }
    }


    private void goForValidatePhoneNumber() {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        // map.put(Constants.ApiParamSignature.USER_ID, getPrefrences().getLoggedUserId());
        showIOSProgress("Validating phone number");
        new HttpRequester(Request.Method.GET, this, map, Constants.VALIDATE_PHONE_NUMBER, URLListApis.URL_VALIDATE_PHONE_NUMBER + "+919888619593", this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            dismissIOSProgress();
            Log.e("Response", response);
            JSONObject jsonObject = new JSONObject(response);
            switch (serviceCode) {
                case Constants.VALIDATE_PHONE_NUMBER:
                    if (jsonObject != null) {
                        if (jsonObject.optBoolean("ack")) {
                            showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
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
           /* JSONObject jsonObject = new JSONObject(errorResponse);
            switch (serviceCode) {
                case Constants.VALIDATE_PHONE_NUMBER:
                    if (jsonObject != null) {
                        if (jsonObject.optBoolean("ack")) {
                            showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
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
}
