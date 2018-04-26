package braille.kofefe.app.modules_.mainpostfeed;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Snow-Dell-05 on 20-Jan-18.
 */

public class PreviewPostScreen extends CommonActivity {

    @InjectView(R.id.txt_post_txt)
    protected TextView mTextScrambled;
    @InjectView(R.id.imageView_cover)
    protected ImageView mImageCover;
    private UiHandleMethods uihandle;
    private String mDataToSend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_screen);
        ButterKnife.inject(this);
        uihandle = new UiHandleMethods(this);
        mTextScrambled.setMovementMethod(new ScrollingMovementMethod());
        previousValues();

    }

    private void previousValues() {
        mDataToSend = StaticValues.mTextToBePreview;
        //     calling api
        goForPreviewPost(mDataToSend);
    }

    @OnClick(R.id.img_header_back)
    public void goToBack() {
        uihandle.goBack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();
    }


    private void goForPreviewPost(String dataToSend) {
        //      mPhoneNumber = (new StringBuilder(mEdtCountryCode.getText().toString().trim())).append(refineNumberFromExtraSymbols(mEditPhoneNumber.getText().toString().trim())).toString();
        //      Todo: Remove after testing and uncomment above
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        showIOSProgress("Preparing...");
        new HttpRequester(dataToSend, Request.Method.POST, this,
                Constants.PREVIEW_POST, URLListApis.URL_PREVIEW_POST.replace("REQUESTID_VALUE", getRandomUUID()), this);
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

                case Constants.PREVIEW_POST:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            String scrambledBody = jsonObject.optString("scrambledBody");
                            //      Todo : show preview post that how the post will lock like
                            mTextScrambled.setText(scrambledBody);


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
            //    JSONObject jsonObject = new JSONObject(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}





