package braille.kofefe.app.modules_.common_util_;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

import braille.kofefe.app.supports_.JSONParser;

/**
 * * Created by Snow-Dell-07 on 08-Dec-17.
 **/

public class GeocoderLocale extends AsyncTask<String, Void, JSONObject> {
    ProgressDialog mDialog;
    BaseInterface mInterface;
    Context mContext;

    public GeocoderLocale(Context mContext, BaseInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = ProgressDialog.show(mContext, "Please Wait", "Finding Address Information");
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        return new JSONParser().makeHttpRequest(params[0], "GET", new HashMap<String, String>());
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        try {
            mInterface.onAddressFound(jsonObject.optJSONArray("results").optJSONObject(0).optString("formatted_address"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
