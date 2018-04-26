package braille.kofefe.app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;

import braille.kofefe.app.modules_.Invite_.CreateProfileScreen;
import braille.kofefe.app.modules_.Invite_.InviteCodeScreen;
import braille.kofefe.app.modules_.Invite_.ValidatePhoneNumber;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.mainpostfeed.MainActivity;
import braille.kofefe.app.modules_.starting_.adapter.AdapterViewPagerStarting;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.check_permissions.PermissionCheck;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class StartingScreen extends CommonActivity {

    //   view pager decalarations
    private Activity mContext = this;
    private ViewPager pager;
    private CircleIndicator indicator;
    private UiHandleMethods uihandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
        ButterKnife.inject(this);

        initViews();
        applyContent();

    }


    private void applyContent() {

    AdapterViewPagerStarting mAdapter = new AdapterViewPagerStarting(getSupportFragmentManager(), 3);

        pager.setAdapter(mAdapter);
        indicator.setViewPager(pager);

    }

    public void goToStart(View v) {
        //   uihandle.goForNextScreen(InviteCodeScreen.class);
        goForCheckRegistrationMode();
        //  uihandle.goForNextScreen(CreateProfileScreen.class);
    }

    private void initViews() {

        indicator = (CircleIndicator) findViewById(braille.kofefe.app.R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager);
        uihandle = new UiHandleMethods(mContext);

        // getSessionInstance().setUserProfilePic("https://media.kofefe.s3-us-west-2.amazonaws.com/image/profile/
        // 646505e0-fe0c-4130-94bd-7809e53107a5.jpeg");
        if (getIntent().getExtras() != null) {

    String mSt = getIntent().getStringExtra("notification");
    String valueD = getIntent().getExtras().getString("data");
    String valueM = getIntent().getExtras().getString("message");

            Log.e("FCM", "" + getIntent().getExtras());
   /*
                for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString("data");
                Log.e("FCM", key + "-" + value);
    //          uihandle.showToast(key + "-" + value);
                if (key.equals("chirag")) {
     //         Intent intent = new Intent(this, AnotherActivity.class);
               //   intent.putExtra("value", value);
               //   startActivity(intent);
               //   finish();
               //   txtMsg.setText(value+" :- this is notification .");
                }}  */
        } else {
            Log.e("noti", "empty");
        }

   //     Todo: Change to MainActivity screen after done coding
        if (getSessionInstance().isLogged()) {
            uihandle.goForNext(MainActivity.class);
            return;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!(new PermissionCheck(this).checkLocation())) {

            }
//                      mFlagPermission = true;
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionCheck.PERMISSION_CODE) {
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

    private void goForCheckRegistrationMode() {

        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        //  getSessionInstance().setAuthorizationUUID("0bcaf8b9-0ff3-4685-96b5-a2bf38d66d96");
        //   getSessionInstance().setAuthorizationPassword("0c36ab8e-3916-451e-9ba3-a0229b04f01a");
        //   getSessionInstance().setUserProfilePic("https://media.kofefe.s3-us-west-2.amazonaws.com/image/profile/646505e0-fe0c-4130-94bd-7809e53107a5.jpeg");
        //   getSessionInstance().setVerfiedUserId("0bcaf8b9-0ff3-4685-96b5-a2bf38d66d96");
        //   getSessionInstance().setIsLogged(true);

        //    uihandle.goForNextScreen(CreateProfileScreen.class);
        //   uihandle.goForNextScreen(AddFriendsScreen.class);


        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");
        new HttpRequester(Request.Method.GET, this, map, braille.kofefe.app.modules_.common_util_.Constants.REGISTRATION_MODE,
                URLListApis.URL_REGISTRATION_MODE.replace("REQUESTID_VALUE", getRandomUUID()), this);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            Log.e("Response", response);
            JSONObject jsonObject = new JSONObject(response);
            switch (serviceCode) {
                case braille.kofefe.app.modules_.common_util_.Constants.REGISTRATION_MODE:
                    dismissIOSProgress();
                    if (jsonObject.optString("ack").equals("SUCCESS")) {
                        //  showFancyToast(TastyToast.SUCCESS, jsonObject.optString("methods"));
                        String registrationMode = jsonObject.optString("methods");

                        switch (registrationMode) {
                            case "INVITATION_ONLY":
                                uihandle.goForNextScreen(InviteCodeScreen.class);
                                break;
                            case "OPEN_TO_ALL":
                                uihandle.goForNextScreen(CreateProfileScreen.class);
                                break;
                            case "CLOSED_TO_ALL":
                                showFancyToast(TastyToast.CONFUSING, "We are not accepting new users. Please try again later!");
                                break;
                            case "PHONE_NUMBER":
                                uihandle.goForNextScreen(ValidatePhoneNumber.class);
                                break;
                            case "INVITATION_AND_PHONE_NUMBER":
                                uihandle.goForNextScreen(InviteCodeScreen.class);
                                break;
                        }

                    } else {
                        showFancyToast(TastyToast.ERROR, ERROR_EMPTY_JSON);
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


}
