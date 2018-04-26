package braille.kofefe.app.modules_.Invite_.social_presenter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.CreateProfileScreen;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.supports_.SessionKofefeApp;
import braille.kofefe.app.supports_.UiHandleMethods;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Snow-Dell-07 on 8/2/2017.
 */

public class SocialFbLogin {

    public static int RC_SIGN_IN = 101;
    private String birthDay_google = "";
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Activity mContext;
    private CallbackManager callbackManager;
    private UiHandleMethods mUiHandleMethods;

    private SessionKofefeApp mSession;
    private String image = "";

    private SquareRoundCornerImageView mProfile;
    private EditText mFullName;
    //  private EditText mEmail;
    //Image Loader object
    private ImageLoader imageLoader;
    private SocialAuthorizationCallback mCallback;

    public SocialFbLogin(Activity mContext, CallbackManager callbackManager, SquareRoundCornerImageView mProfile, EditText mFullName) {

        this.mContext = mContext;
        this.callbackManager = callbackManager;
        this.mUiHandleMethods = new UiHandleMethods(mContext);
        this.mSession = new SessionKofefeApp(mContext);

        this.mProfile = mProfile;
        this.mFullName = mFullName;

        if (mContext instanceof CreateProfileScreen) {
            mCallback = (SocialAuthorizationCallback) mContext;
        }


    }

    public void loginFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //  handlePendingAction();
                        Log.e("Accces_Token", loginResult.getAccessToken().getToken());
                        //  Toast.makeText(mContext, "Successfully Logged in\nUser id:" + loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
                        getFbSocialInfo(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.e("cancel", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Facebook", exception.getMessage());
                    }

                });
    }

    private void getFbSocialInfo(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.i("facebook: ", object.toString());
                    //   {"id":"1667439286607853","name":"Sukh Chauhan","email":"singh.sukhwant68@gmail.com","gender":"male"}
                    AccessToken accessToken = loginResult.getAccessToken();
                    accessTokenTracker = new AccessTokenTracker() {
                        @Override
                        protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {
                        }
                    };
                    accessTokenTracker.startTracking();
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                        }
                    };
                    profileTracker.startTracking();
                    Profile profile = Profile.getCurrentProfile();

                    String fName = "";
                    String lName = "";
                    String imageUrl = "";

                    if (profile != null) {
                        //get data here
                        fName = profile.getFirstName();
                        lName = profile.getLastName();
                        Object profile_pic = profile.getProfilePictureUri(400, 400);
                        imageUrl = ((Uri) (profile_pic)).toString();
                    }

                    //    Application code
                    if (object.has("email")) {

                        String id = object.optString("id");
                        String name = object.optString("name");
                        String email = object.optString("email");
                        String birthday = object.optString("birthday");

                        //  mEmail.setText(email);
                        //   mFullName.setText(name);

                        mCallback.onFacebookAuthorization(name);

                      /*    String fName = profile.getFirstName();
                            String lName = profile.getLastName(); */
                        //  Object profile_pic = profile.getProfilePictureUri(400, 400);
                        //  String imageUrl = ((Uri) (profile_pic)).toString();

                      //    loadImageDisplay(mProfile, imageUrl);
                        //  String birthday = object.getString("birthday"); // 01/31/1980 format
                        //  mCallback.registerSocially(name, email, loginResult.getAccessToken().getUserId());

                        //  goForLoginApiSocial(fName, lName, email, id, "facebook", imageUrl, birthday);


                    } else {
                        if (FacebookSdk.isInitialized()) {
                            LoginManager.getInstance().logOut();
                        }
                        mUiHandleMethods.showToast("Email not found");
                    }
                } catch (Exception e) {
                    Log.e("Social Exception", e.toString());
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void loadImageDisplay(CircleImageView img, String uri) {
        //  Loading image
        //   imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
        //    imageLoader.get(uri, ImageLoader.getImageListener(mProfile, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        Picasso.with(mContext)
                .load(uri)
                .placeholder(R.color.color_black_less)
                .into(img);

    }
}
