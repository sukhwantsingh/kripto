package braille.kofefe.app.modules_.Invite_.social_presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import braille.kofefe.app.modules_.Invite_.CreateProfileScreen;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Snow-Dell-05 on 12/16/2017.
 */

public class SocialTwitterLogin {
    private static final String TWITTER_KEY = "29gPA4RLqNcl5zfr44eCapy39";
    private static final String TWITTER_SECRET = "NjGQVuufgaJGpRKM3WJZZi5thtfTDAyVPVJ0vAJtKsQw2uAWfz";
    private Activity mActivity;


    private EditText mUsername;
    private CircleImageView mProfilePic;
    private TwitterLoginButton twitterLoginButton;
    private TwitterSession session;
    private SocialAuthorizationCallback mCallback;

    public SocialTwitterLogin(final Activity mActivity, TwitterLoginButton twitterLoginButton, final EditText mUsername) {
        //   final TextView mLogout, final TextView mProfileName, final EditText mEmail, final CircleImageView mProfilePic) {

        this.mActivity = mActivity;
        //      this.mLogout = mLogout;
        this.mUsername = mUsername;
        //      this.mProfileName = mProfileName;
        //      this.mProfilePic = mProfilePic;
        this.twitterLoginButton = twitterLoginButton;

        if (mActivity instanceof CreateProfileScreen) {
            mCallback = (SocialAuthorizationCallback) mActivity;
        }

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> userResult) {
                // Do something with result, which provides a TwitterSession for making API calls
                // Do something with the result, which provides the email address
                try {
                    TwitterSession user = userResult.data;
                    String twitterusername = user.getUserName();
                    long twitterId = user.getUserId();
                    // mUsername.setText(twitterusername);

                    Log.e("name", twitterusername);
                    Log.e("id", "" + twitterId);

                    mCallback.onTwitterAuthorization(twitterusername);
                    logoutTwitter();

                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onTwitterAuthorization("");
                    logoutTwitter();
                  // showToast(e.toString());
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                showToast(exception.getMessage());
                mCallback.onTwitterAuthorization("");
            }
        });

        login_twitter();

    }


    public void loadImageDisplay(CircleImageView img, String uri) {/*
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mActivity).build();
        ImageLoader.getInstance().init(config);

        ImageLoader imageLoader= ImageLoader.getInstance();
        imageLoader.displayImage(uri, img);
*/

    }

    private void login_twitter() {
        twitterLoginButton.performClick();
    }

    private void logoutTwitter() {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            clearTwitterCookies(mActivity);
            TwitterCore.getInstance().getSessionManager().clearActiveSession();
            //  Twitter.logOut();
        }
    }

    private void clearTwitterCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();

        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();


    }

}

