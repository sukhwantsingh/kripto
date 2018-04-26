package braille.kofefe.app.modules_.Invite_;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class InviteCodeScreen extends CommonActivity {

    @InjectView(R.id.textView6)
    protected TextView mTextDontHaveInvitationCode;

    @InjectView(R.id.txt_pin_entry)
    protected PinEntryEditText pinEntry;

    @InjectView(R.id.textView_app_name)
    protected TextView mTextHeadingAppName;

    private String mInvitationCode = "";

    private Activity mContext = this;
    private UiHandleMethods uihandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code_screen);

        ButterKnife.inject(this);
        initViews();

        implementListeneres();

    }

    private void implementListeneres() {
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    mInvitationCode = str.toString().toUpperCase();

                    if (str.toString().length() == 6) {
                        uihandle.hideSoftKeyboard();
                        fireAPi();
                    }

                }
            });
        }

    }


    private void initViews() {

        uihandle = new UiHandleMethods(mContext);
        uihandle.changeColorToAppGradient(mTextDontHaveInvitationCode);
        //  change colors of heading to gradient
        uihandle.changeColorToAppGradient(mTextHeadingAppName);
        uihandle.changeColorToAppGradient(pinEntry);

    }

    public void dontHaveInvitaionCodeClick(View v) {
        uihandle.goForNextScreen(ValidatePhoneNumber.class);
//   uihandle.goForNextScreen(CreateProfileScreen.class);


    }

    public void regainView(View v) {
        uihandle.goBack();
    }

    public void goToNext(View v) {
        fireAPi();
    }


    private void fireAPi() {
        // TODO: remove when testing done

        if (getSessionInstance().isLogged()) {
            uihandle.goForNext(ValidatePhoneNumber.class);
            return;
        }


        if (TextUtils.isEmpty(mInvitationCode)) {
            pinEntry.setError("Invalid invitaion code!");
        }
        uihandle.hideSoftKeyboard();
        goByInvitationCode();
        //  uihandle.goForNextScreen(ValidatePhoneNumber.class);
    }

    private void goByInvitationCode() {
        if (TextUtils.isEmpty(pinEntry.getText().toString().trim())) {
            pinEntry.setError("Please provide INVITE_CODE!");
            return;
        }
        //  mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //Todo: remove after testing and uncomment above

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        /* `--3OA749``5O5895``5WQ66G``7770Z8``N3CR6T``JR3E50``VW8UE5``3W0U88``553IS2``41Z7HY``DR1244`*/

        HashMap<String, String> map = new HashMap<String, String>();
        // map.put(Constants.ApiParamSignature.USER_ID, getPrefrences().getLoggedUserId());
        showIOSProgress("Loading...");
        new HttpRequester(Request.Method.GET, this, map, Constants.CHECK_BY_INVITATION_CODE,
                URLListApis.URL_CHECK_BY_INVITATION_CODE.replace("INVITATION_CODE", mInvitationCode).replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            Log.e("Response", response);
            JSONObject jsonObject = new JSONObject(response);
            switch (serviceCode) {
                case Constants.CHECK_BY_INVITATION_CODE:
                    dismissIOSProgress();
                    if (jsonObject.optString("ack").equals("SUCCESS")) {
                        //        showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                       /*
                        INVALID_INVITATION_CODE => "Invalid invitation code. Please check the invitation code and try again!"
                        ALREADY_USED_INVITATION_CODE => "Sorry. Somebody already claimed this invitation code.
                        Please try a different one!"
                        Success case:
                        INVITATION_CODE_ACCEPTED => "Congratulations. Your invitation is accepted. Welcome onboard :)"
                       */
                        String registrationMode = jsonObject.optString("status");

                        switch (registrationMode) {
                            case "ACCEPTED":
                                showFancyToast(TastyToast.SUCCESS, jsonObject.optString("messageToUser"));
                                uihandle.goForNextScreen(ValidatePhoneNumber.class);
                                break;
                        }
                    } else {
                        String registrationMode = jsonObject.optString("status");
                        String messageToUser = jsonObject.optString("messageToUser");

                        switch (registrationMode) {
                            case "INVALID":
                                showFancyToast(TastyToast.ERROR, messageToUser);
                                // fire vibration animation on text
                          //      pinEntry.startAnimation(uihandle.shake());
                                break;
                            case "ALREADY_USED":
                                showFancyToast(TastyToast.CONFUSING, messageToUser);
                                // fire vibration animation on text
                            //    pinEntry.startAnimation(uihandle.shake());
                                break;
                        }
                        //     showFancyToast(TastyToast.ERROR, jsonObject.optString("messageToUser"));
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
            JSONObject jsonObject = new JSONObject(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
    }


}
