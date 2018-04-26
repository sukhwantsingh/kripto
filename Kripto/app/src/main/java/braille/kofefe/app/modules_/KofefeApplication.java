package braille.kofefe.app.modules_;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.supports_.api_request.LruBitmapCache;

/**
 * Created by Snow-Dell-05 on 12/6/2017.
 */

public class KofefeApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = KofefeApplication.class.getSimpleName();
    static Activity mContext;
    private static KofefeApplication _instance;
    public RequestQueue _requestQueue;
    public ImageLoader _imageLoader;


    public static synchronized KofefeApplication getInstance() {
        return _instance;
    }

    public static Activity getActivityContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.ConstantTwitter.TWITTER_CONSUMER_KEY,
                        Constants.ConstantTwitter.TWITTER_CONSUMER_SECRET))
                .debug(false)
                .build();
        Twitter.initialize(config);
        //  Twitter.initialize(this);
        //  MultiDex.install(this);
        _instance = this;
        //  Disable_Certificate_Validation_Java_SSL_Connections();
        NukeSSLCerts.nuke();
        // initialize contacts


    }

    public RequestQueue getRequestQueue() {

        if (_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return _requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (_imageLoader == null) {
            _imageLoader = new ImageLoader(this._requestQueue, new LruBitmapCache());
        }
        return this._imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (_requestQueue != null) {
            _requestQueue.cancelAll(tag);
        }
    }

    public void Disable_Certificate_Validation_Java_SSL_Connections() {
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        mContext = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}