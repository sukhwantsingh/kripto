package braille.kofefe.app.supports_.api_request;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import braille.kofefe.app.modules_.KofefeApplication;
import braille.kofefe.app.modules_.common_util_.AppHelper;
import braille.kofefe.app.supports_.SessionKofefeApp;

public class HttpRequester {

    private static final String LINE_FEED = "\r\n";
    private String BOUNDARY = "s2retfgsGSRFsERFGHfgdfgw734yhFHW567TYHSrf4yarg"; //This the boundary which is used by the server to split the post parameters.
    private String MULTIPART_FORMDATA = "multipart/form-data; boundary=" + BOUNDARY;

    // private String MULTIPART_FORMDATA = "multipart/form-data; boundary=alamofire.boundary.4a208b3a63bfdcfe";
    private String mCreatePost;

    private int statusCode;
    private Map<String, String> map;
    private int serviceCode;
    private boolean isGetMethod = false;
    private Activity activity;
    private String strReqUrl = "";
    private AsyncTaskCompleteListener mAsynclistener;
    private String mAuthUsername, mAuthPassword;


    private SessionKofefeApp mSession;
    private Activity mContext;

    private ImageView mImageView;
    private String mFileExtension = "";
    ;// = null;


    public HttpRequester(int mMethod, Activity activity, Map<String, String> map, int serviceCode, String url, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mSession = new SessionKofefeApp(activity);
        this.map = map;
        this.serviceCode = serviceCode;
        this.activity = activity;
        this.isGetMethod = false;
        this.strReqUrl = url.replace(" ", "%20");
        this.mAsynclistener = asyncTaskCompleteListener;
        this.mAuthUsername = mSession.getAuthorizationsetUUID();
        this.mAuthPassword = mSession.getAuthorizationPassword();

        HandleHttpRequest(mMethod);
        //  HandleJsonHttpRequest();

    }

    public HttpRequester(String mIdentity, int mMethod, Activity activity, Map<String, String> map, int serviceCode, String url, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mSession = new SessionKofefeApp(activity);
        this.map = map;
        this.serviceCode = serviceCode;
        this.activity = activity;
        this.isGetMethod = false;
        this.strReqUrl = url.replace(" ", "%20");

        this.mAsynclistener = asyncTaskCompleteListener;
        this.mAuthUsername = mSession.getAuthorizationsetUUID();
        this.mAuthPassword = mSession.getAuthorizationPassword();

        HandleHttpPutRequest(mMethod);
        //  HandleJsonHttpRequest();

    }

    public HttpRequester(String mCreatePost, int mMethod, Activity activity, int serviceCode, String url, AsyncTaskCompleteListener asyncTaskCompleteListener) {

        this.mSession = new SessionKofefeApp(activity);
        this.mCreatePost = mCreatePost;
        this.serviceCode = serviceCode;
        this.activity = activity;
        this.isGetMethod = false;
        this.strReqUrl = url.replace(" ", "%20");
        this.mAsynclistener = asyncTaskCompleteListener;
        this.mAuthUsername = mSession.getAuthorizationsetUUID();
        this.mAuthPassword = mSession.getAuthorizationPassword();

        HandleHttpRequestCreatePost(mMethod);
        //  HandleJsonHttpRequest();
    }


    public HttpRequester(Activity mContext, int mMethod, ImageView mImageView, String mFileExtension, int serviceCode,
                         String url, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mContext = mContext;
        this.mFileExtension = mFileExtension;
        this.mImageView = mImageView;
        this.serviceCode = serviceCode;
        this.strReqUrl = url.replace(" ", "%20");
        this.mAsynclistener = asyncTaskCompleteListener;

        volleyMultipartUploadMedia(mMethod);
    }


    /**
     * * function to verify login details in mysql db
     **/
    private void HandleHttpRequest(int mMethod) {
        //       Tag used to cancel the request

        String tag_string_req = "req_connect";
        StringRequest strReq = new StringRequest(mMethod, strReqUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request:" + getClass().getSimpleName(), response);
                if (mAsynclistener != null) {
                    mAsynclistener.onTaskCompleted(response, serviceCode);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request:", error.getMessage() + "");
                error.printStackTrace();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode != HttpURLConnection.HTTP_OK) {
                    //                  HTTP Status Code: 401 Unauthorized
                    if (mAsynclistener != null) {
                        String jsonError = new String(networkResponse.data);
                        try {
                            JSONArray jArray = new JSONObject(jsonError).optJSONArray("errors");
                            JSONObject jData = jArray.optJSONObject(0);

                            String res_data = jData.optString("id");
                            String res_message = jData.optString("message");


                            mAsynclistener.onErrorFound(res_data, serviceCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String mErrorMessage = "Sorry. Something unexpected happened. We are looking into it. Please try again later!";

                            mAsynclistener.onErrorFound(mErrorMessage, serviceCode);
                        }
                        /*
                        {"ack":"FAILURE",
                        "errors":[{"id":"Unsupported Media Type",
                        "message":"HTTP 415 Unsupported Media Type",
                        "errorType":"SYSTEM",
                        "errorSeverity":"FATAL"}],
                        "warnings":[]}
                            */
                    }
                   /* if (networkResponse != null && networkResponse.data != null) {
                        String jsonError = new String(networkResponse.data);
                        // Print Error!
                    }*/
                }
            }
        }) {


            @Override
            public String getBodyContentType() {
                return MULTIPART_FORMDATA;
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                return createPostBody(getParams()).getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = mAuthUsername + ":" + mAuthPassword;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                //    headers.put("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                //    headers.put("Content-Type", "application/json;  charset=utf-8");
                //  headers.put("Accept", "application/json");
                //   headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return map;
            }
        };

        //         Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(180000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KofefeApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void HandleHttpPutRequest(int mMethod) {
        // Tag used to cancel the request
        String tag_string_req = "req_connect2";
        StringRequest strReq = new StringRequest(mMethod, strReqUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request:" + getClass().getSimpleName(), response + "" + statusCode);
                if (mAsynclistener != null) {
                    mAsynclistener.onTaskCompleted(response, serviceCode);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request:", error.getMessage() + "");
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode != HttpURLConnection.HTTP_OK) {
                    //   HTTP Status Code: 401 Unauthorized
                    if (mAsynclistener != null) {
                        String jsonError = new String(networkResponse.data);
                        try {
                            JSONArray jArray = new JSONObject(jsonError).optJSONArray("errors");
                            JSONObject jData = jArray.optJSONObject(0);

                            String res_data = jData.optString("id");
                            String res_message = jData.optString("message");

                            mAsynclistener.onErrorFound(res_data, serviceCode);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            String mErrorMessage = "Sorry. Something unexpected happened. We are looking into it. Please try again later!";
                            mAsynclistener.onErrorFound(mErrorMessage, serviceCode);
                        }
                        /*
                        {
                        "ack":"FAILURE",
                        "errors":[{"id":"Unsupported Media Type",
                        "message":"HTTP 415 Unsupported Media Type",
                        "errorType":"SYSTEM",
                        "errorSeverity":"FATAL"}],
                        "warnings":[]}
                        */
                    }
                     /*
                      if (networkResponse != null && networkResponse.data != null) {
                        String jsonError = new String(networkResponse.data);
                        // Print Error!
                    }
                    */

                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return MULTIPART_FORMDATA;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return createPostBody(getParams()).getBytes();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return map;
            }

        };

        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(180000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KofefeApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String createPostBody(Map<String, String> params) {
        StringBuilder sbPost = new StringBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {

                    sbPost.append(LINE_FEED + "--" + BOUNDARY + LINE_FEED);
                    sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + LINE_FEED + LINE_FEED);
                    sbPost.append(params.get(key));
                }
            }
            sbPost.append(LINE_FEED + "--" + BOUNDARY + "--" + LINE_FEED);
        }

        return sbPost.toString();
    }


    private void HandleHttpRequestCreatePost(int mMethod) {
        //       Tag used to cancel the request

        String tag_string_req = "req_connect";
        StringRequest strReq = new StringRequest(mMethod, strReqUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request:" + getClass().getSimpleName(), response);

                if (mAsynclistener != null) {
                    mAsynclistener.onTaskCompleted(response, serviceCode);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request:", error.getMessage() + "");
                error.printStackTrace();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode != HttpURLConnection.HTTP_OK) {
                    //   HTTP Status Code: 401 Unauthorized
                    if (mAsynclistener != null) {
                        String jsonError = new String(networkResponse.data);
                        try {
                            JSONArray jArray = new JSONObject(jsonError).optJSONArray("errors");
                            JSONObject jData = jArray.optJSONObject(0);

                            String res_data = jData.optString("id");
                            String res_message = jData.optString("message");

                            mAsynclistener.onErrorFound(res_data, serviceCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String mErrorMessage = "Sorry. Something unexpected happened. We are looking into it. Please try again later!";
                            mAsynclistener.onErrorFound(mErrorMessage, serviceCode);
                        }
                    }
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mCreatePost.getBytes();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = mAuthUsername + ":" + mAuthPassword;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                //    headers.put("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                //  headers.put("Content-Type", "application/json;  charset=utf-8");
                //  headers.put("Accept", "application/json");
                //   headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);

                return headers;
            }

        };
        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(180000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KofefeApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void volleyMultipartUploadMedia(int mMethod) {

        String tag_string_req = "req_connect";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(mMethod, strReqUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.i("request:" + getClass().getSimpleName(), response.toString());
                        if (mAsynclistener != null) {
                            mAsynclistener.onTaskCompleted(response.toString(), serviceCode);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request:", error.getMessage() + "");
                error.printStackTrace();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode != HttpURLConnection.HTTP_OK) {
                    //                  HTTP Status Code: 401 Unauthorized
                    if (mAsynclistener != null) {
                        String jsonError = new String(networkResponse.data);
                        try {
                            JSONArray jArray = new JSONObject(jsonError).optJSONArray("errors");
                            JSONObject jData = jArray.optJSONObject(0);

                            String res_data = jData.optString("id");
                            String res_message = jData.optString("message");

                            mAsynclistener.onErrorFound(res_data, serviceCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String mErrorMessage = "Sorry. Something unexpected happened. We are looking into it. Please try again later!";
                            mAsynclistener.onErrorFound(mErrorMessage, serviceCode);
                        }
                        /*
                        {"ack":"FAILURE",
                        "errors":[{"id":"Unsupported Media Type",
                        "message":"HTTP 415 Unsupported Media Type",
                        "errorType":"SYSTEM",
                        "errorSeverity":"FATAL"}],
                        "warnings":[]}
                            */
                    }
                   /* if (networkResponse != null && networkResponse.data != null) {
                        String jsonError = new String(networkResponse.data);
                        // Print Error!
                    }*/
                }
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                //    file name could found file base or direct access from real path
                //    for now just get bitmap data from ImageView
                params.put("avatar", new DataPart("",
                        AppHelper.getFileDataFromDrawable(mContext, mImageView.getDrawable()), "image/" + mFileExtension));

                return params;
            }
        };
        //         Adding request to request queue
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(180000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KofefeApplication.getInstance().addToRequestQueue(multipartRequest, tag_string_req);

    }

  /*  // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
*/
}
