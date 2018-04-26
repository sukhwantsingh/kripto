package braille.kofefe.app.modules_.Invite_.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;

import braille.kofefe.app.modules_.common_util_.AWSS3Uploader;
import braille.kofefe.app.supports_.SessionKofefeApp;
import braille.kofefe.app.supports_.api_request.AsyncTaskCompleteListener;

/**
 * Created by Snow-Dell-05 on 12/14/2017.
 */

public class AsyncCreateProfile extends AsyncTask<String, String, String> {

    private JSONObject jObj;
    private StringBuilder result;
    private Activity mContext;

    private SessionKofefeApp mSession;
    private String auth;
    private String credentials;
    private String mAuthUsername, mAuthPassword;
    private int mServiceCode;
    private AsyncTaskCompleteListener mAsynclistener;

    public AsyncCreateProfile(Activity mContext, int mServiceCode, AsyncTaskCompleteListener asyncTaskCompleteListener) {

        this.mContext = mContext;
        this.mServiceCode = mServiceCode;
        this.mAsynclistener = asyncTaskCompleteListener;
        this.mSession = new SessionKofefeApp(mContext);
        this.mAuthUsername = mSession.getAuthorizationsetUUID();
        this.mAuthPassword = mSession.getAuthorizationPassword();

        String credentials = mAuthUsername + ":" + mAuthPassword;
        this.auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


    }

    @Override
    protected String doInBackground(String... url) {
        try {
            return goForUploadImage(url[0], url[1]);
        } catch (Exception e) {
            //    uihandle.stopProgressDialog();
            Log.d("question", e.toString());
            return "";
        }

    }

    @Override
    protected void onPostExecute(String mResponse) {
        super.onPostExecute(mResponse);
        if (mAsynclistener != null) {
            mAsynclistener.onTaskCompleted(mResponse, mServiceCode);
        }
    }

    private String goForUploadImage(String requestURL, String mImagePath) {

        try {
            AWSS3Uploader mUpload = new AWSS3Uploader();
            boolean mFlagResult = mUpload.uploadObject(requestURL, new File(mImagePath));

            if (mFlagResult) {
                return "{\"mkey\":\"SUCCESS\"}";
            } else {
                return "{\"mkey\":\"FAILURE\"}";
            }
        } catch (Exception ex) {
            System.err.println(ex);
            return "{\"mkey\":\"FAILURE\"}";
        }
    }


}


