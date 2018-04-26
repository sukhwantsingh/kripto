package braille.kofefe.app.modules_.mainpostfeed;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.api.AsyncCreateProfile;
import braille.kofefe.app.modules_.common_util_.CommonActivity;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.LinearSmoothScrollingCustom;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.mainpostfeed.adapter.AdapterComments;
import braille.kofefe.app.modules_.mainpostfeed.callback_.ICommentCallback;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelComments;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeed;
import braille.kofefe.app.modules_.mainpostfeed.util.SwipeController;
import braille.kofefe.app.modules_.mainpostfeed.util.SwipeControllerActions;
import braille.kofefe.app.modules_.profile.model.ModelRecentPosts;
import braille.kofefe.app.supports_.UiHandleMethods;
import braille.kofefe.app.supports_.api_request.HttpRequester;
import braille.kofefe.app.supports_.check_permissions.PermissionCheck;
import braille.kofefe.app.supports_.url_keys.URLListApis;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class CommentScreen extends CommonActivity implements ICommentCallback {

    @InjectView(R.id.imageView2)
    protected ImageView mImageCross;
    @InjectView(R.id.rv_reactions)
    protected RecyclerView mRecyclerComments;

    @InjectView(R.id.txt1)
    protected TextView mTextCommentCountHeading;

    @InjectView(R.id.btn_send)
    protected ImageView mImgSend;
    @InjectView(R.id.edt_message_)
    protected EditText mEditMessage;

    @InjectView(R.id.text_comment_limit)
    protected TextView mTextCommentLimit;

    private AdapterComments mAdapterComment;
    private List<ModelComments> mListComments;
    private List<ModelComments> mListCommentsTemp;


    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private String mPostId = "";
    private String mCommentCount = "";
    private boolean mFlagNoMoreComments = false;
    private String mMediaUrl = "";
    private String mContent = "";
    private String mFinalDataToSend = "";
    private String path = "", image = "";
    private String extention = "";
    private String presignedUrl = "", mFinalUploadedProfileLink = "";
    private SwipeController swipeController;
    private int mPositionToDeleteComment;
    private int mPositionToInsertMoreComments;
    private ModelFeed mModelFeedCustmisable;
    private ModelRecentPosts mModelRecentPostsCustmisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_screen);
        ButterKnife.inject(this);

        initViews();
        implementListeners();
        getPreviousValues();
    }

    private void getPreviousValues() {

        mPostId = StaticValues.mPostId;
        mCommentCount = StaticValues.mPostComment;
        mTextCommentCountHeading.setText(numberInShortFormat(Long.parseLong(mCommentCount)) + " Comments");

        goForLatestComments();
    }

    private void implementListeners() {
        mImageCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uihandle.hideSoftKeyboard();
                uihandle.goBack();
            }
        });
        mEditMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextCommentLimit.setText((s.length()) + "/100");


            }
        });

        mEditMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() <= (mEditMessage.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                            if (!(new PermissionCheck(CommentScreen.this).checkCamera())) {
                                goForImgClick();
                            }
                        } else {
                            goForImgClick();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        mEditMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if ((i == EditorInfo.IME_ACTION_SEND)) {
                    sendComment();

                }
                return false;

            }
        });

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                mPositionToDeleteComment = position;

                String mId = mListComments.get(position).getComment_id();
                String mDeleteComment = "{\"postUuid\" : \"" + mPostId + "\",\"commentId\" : " + mId + "}"; // {"postUuid" : "ad5ad003-2849-4dda-829d-8cca31c64be0","commentId" : 3}

                //     go for delete comment
                goForDeleteComment(mDeleteComment);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mRecyclerComments);

        mRecyclerComments.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void initViews() {

        uihandle = new UiHandleMethods(mContext);
        mListComments = new ArrayList<>();
        mListCommentsTemp = new ArrayList<>();

        //      Inflate the layout for this fragment
        mRecyclerComments.setLayoutManager(new LinearSmoothScrollingCustom(mContext));
    }

    @OnClick(R.id.btn_send)
    public void onCommentSend() {

        sendComment();
    }

    private void sendComment() {
        if (TextUtils.isEmpty(mEditMessage.getText().toString().trim()) && TextUtils.isEmpty(mMediaUrl)) {
            return;
        }

        mContent = mEditMessage.getText().toString().trim();

        if (TextUtils.isEmpty(mFinalUploadedProfileLink.trim())) {
            if (!TextUtils.isEmpty(mContent)) {
                mFinalDataToSend = "\"comment\" : \"" + mContent + "\"";
            }
        } else {
            if (!TextUtils.isEmpty(mContent)) {
                mFinalDataToSend = "\"comment\" : \"" + mContent + "\",\"media\" : [{ \"mediaUrl\" : \"" + mFinalUploadedProfileLink + "\",\"mediaType\" : \"IMAGE\"}]";
            } else {
                mFinalDataToSend = "\"media\" : [{ \"mediaUrl\" : \"" + mFinalUploadedProfileLink + "\",\"mediaType\" : \"IMAGE\"}]";
            }
        }
        String mFFinalDataToSend = "{" + mFinalDataToSend + "}";

        goForAddComment(mFFinalDataToSend);
    }

    private void goForImgClick() {
        if (uihandle.hasCamera()) {
            Image_Picker_Dialog();
        } else {
            uihandle.showToast("Sorry! Camera is not available");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionCheck.PERMISSION_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (i == grantResults.length - 1) {
                        goForImgClick();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.TAKE_PICTURE && resultCode == RESULT_OK) {
            try {
                onCaptureImageResult(photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.SELECT_FILE && resultCode == RESULT_OK) {

            path = uihandle.getImagePath(data);
            image = path;

            try {
                onSelectFromGalleryResult(image, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSelectFromGalleryResult(String mImgPath, Intent data) throws IOException {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //   mProfileImage.setImageBitmap(null);
        // mTempImageView.invalidateDrawable(null);
        Bitmap mBitM = uihandle.modifyOrientation(bm, mImgPath);

        extention = mImgPath.substring(mImgPath.lastIndexOf("."));
        getPresignedS3url(extention);

    }

    //       Processing after select image from camera as well as gallery
    private void onCaptureImageResult(Uri data) throws IOException {
        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }


        image = photoFile.getAbsolutePath();
        Bitmap mBitM = uihandle.modifyOrientation(thumbnail, image);
        extention = image.substring(image.lastIndexOf("."));
        getPresignedS3url(extention);
    }

    private void getPresignedS3url(String mFileExtention) {
        //  calling api to upload image to server
        showIOSProgress("");
        HashMap<String, String> map = new HashMap<>();
        new HttpRequester(Request.Method.GET, this, map, Constants.UPLOAD_MEDIA_TO_AMAZONE_PRE_FIRE_API, URLListApis.URL_UPLOAD_MEDIA.
                replace("MEDIA_TYPE", Constants.ConstantMediaType.MEDIA_TYPE_IMAGE).replace("USECASE", Constants.ConstantUseCase.USECASE_COMMENT).
                replace("FILE_EXTENTION", mFileExtention).replace("REQUESTID_VALUE", getRandomUUID()), this);


    }

    private void goForLatestComments() {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");

        new HttpRequester(Request.Method.GET, this, map,
                Constants.GET_LATEST_COMMENTS_GET, URLListApis.URL_GET_LATEST_COMMENTS.replace("POST_ID", mPostId).
                replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForCommnentsAfter(String lastCommentId) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        showIOSProgress("Loading...");
        new HttpRequester(Request.Method.GET, this, map,
                Constants.GET_COMMENTS_AFTER_GET, URLListApis.URL_GET_COMMENTS_AFTER.
                replace("POST_ID", mPostId).
                replace("LAST_WALI_PEHCHAN", lastCommentId).
                replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForAddComment(String dataToSend) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        showIOSProgress("Loading...");
        new HttpRequester(dataToSend, Request.Method.POST, this,
                Constants.ADD_COMMENT_POST, URLListApis.URL_ADD_COMMENTS.
                replace("POST_ID", mPostId).
                replace("REQUESTID_VALUE", getRandomUUID()), this);
    }

    private void goForDeleteComment(String dataToSend) {
        if (!isNetworkConnected()) {
            showFancyToast(TastyToast.CONFUSING, NETWORK_ERROR);
            return;
        }

        showIOSProgress("Loading...");
        new HttpRequester(dataToSend, Request.Method.POST, this,
                Constants.DELETE_COMMENT_POST, URLListApis.URL_DELETE_COMMENTS
                .replace("REQUESTID_VALUE", getRandomUUID()), this);
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

                case Constants.UPLOAD_MEDIA_TO_AMAZONE_PRE_FIRE_API:
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            presignedUrl = jsonObject.optString("presignedUrl");

                            new AsyncCreateProfile(mContext, Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE, this)
                                    .execute(presignedUrl, image);


                        } else {
                            showFancyToast(TastyToast.CONFUSING, "Something went wrong");
                            dismissIOSProgress();
                        }
                    } else {
                        dismissIOSProgress();
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.UPLOAD_PROFILE_IMAGE_TO_AMAZONE:
                    dismissIOSProgress();
                    String[] mTesturl = presignedUrl.split("\\?");
                    mFinalUploadedProfileLink = mTesturl[0];
                    // showFancyToast(TastyToast.SUCCESS, "Successfully uploaded!");
                    break;


                case Constants.GET_LATEST_COMMENTS_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            List<ModelComments> mTempList = refineJSON(jsonObject);
                            if (mTempList != null) {
                                mFlagNoMoreComments = false;

                                mListComments.clear();
                                mListComments.addAll(mTempList);

                                mAdapterComment = new AdapterComments(mContext, mListComments);
                                mRecyclerComments.setAdapter(mAdapterComment);

                            } else {

                                mAdapterComment = new AdapterComments(mContext, mListComments);
                                mRecyclerComments.setAdapter(mAdapterComment);

                                //   true flag to avoid run botton scroll api for more posts
                                mFlagNoMoreComments = true;
                            }


                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;
                case Constants.ADD_COMMENT_POST:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            List<ModelComments> mTempList = refineJSONAddComment(jsonObject);
                            if (mTempList != null) {
                                ModelComments mModCom = mTempList.get(0);
                                mAdapterComment.addItem(mModCom, mRecyclerComments);

                                mEditMessage.setText("");
                                mFinalUploadedProfileLink = "";

                                // update comment count +
                                StaticValues.mFlagCommentCountManager = true;
                                StaticValues.mCommentCountFinal = jsonObject.optString("updatedCount");

                                mTextCommentCountHeading.setText(numberInShortFormat(Long.parseLong(StaticValues.mCommentCountFinal)) + " Comments");

                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;

                case Constants.DELETE_COMMENT_POST:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            String msg = jsonObject.optString("messageToUser");

                            if (jsonObject.optString("status").equals("SUCCESS")) {

                                mAdapterComment.removeItem(mPositionToDeleteComment);

                                // update comment count
                                StaticValues.mFlagCommentCountManager = true;
                                StaticValues.mCommentCountFinal = jsonObject.optString("updatedCount");
                                mTextCommentCountHeading.setText(numberInShortFormat(Long.parseLong(StaticValues.mCommentCountFinal)) + " Comments");

                            } else {
                                showFancyToast(TastyToast.ERROR, "Something wen wrong!");
                            }

                        } else {
                            showFancyToast(TastyToast.ERROR, jsonObject.optString("status"));
                        }
                    } else {
                        showFancyToast(TastyToast.CONFUSING, ERROR_EMPTY_JSON);
                    }
                    break;


                case Constants.GET_COMMENTS_AFTER_GET:
                    dismissIOSProgress();
                    if (jsonObject != null) {
                        if (jsonObject.optString("ack").equals("SUCCESS")) {
                            //      showFancyToast(TastyToast.SUCCESS, jsonObject.optString("status"));
                            /***
                             * refine response from server
                             ***/
                            //  List<ModelComments> mTempList = refineJSON(jsonObject);
                            List<ModelComments> mTempList = refineJSON(jsonObject);
                            if (mTempList != null) {
                                mListComments.addAll(mPositionToInsertMoreComments, mTempList);
                                mAdapterComment.notifyDataSetChanged();
                            } else {
                                mFlagNoMoreComments = true;
                            }

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

    private List<ModelComments> refineJSON(JSONObject mJsonObj) {
        try {
            List<ModelComments> mListCommentLocal = new ArrayList<>();
            Log.e("response", mJsonObj.toString());

            JSONObject nComment = mJsonObj.optJSONObject("comments");
            String postUuid = nComment.optString("postUuid");

            JSONArray nArray = nComment.optJSONArray("comments");
            if (nArray != null && nArray.length() > 0) {
                if (!mListCommentsTemp.isEmpty()) {
                    mListCommentsTemp.clear();
                }

                for (int i = 0; i < nArray.length(); i++) {
                    JSONObject jData = nArray.optJSONObject(i);

                    String commentId = jData.optString("id");

                    JSONObject jUser = jData.optJSONObject("user");

                    String user_name = jUser.optString("name");
                    String user_userName = jUser.optString("userName");
                    String user_profilePic = jUser.optString("profilePic");
                    String user_relationship = jUser.optString("relationship");
                    String user_uuid = jUser.optString("uuid");

                    String user_bio = jUser.optString("bio");
                    String user_followersCount = jUser.optString("followersCount");
                    String user_followingCount = jUser.optString("followingCount");

                    String user_joined = getDataTimeFromMilliseconds(Long.parseLong(jUser.optString("joined")));

                    JSONObject jUserLocation = jUser.optJSONObject("location");
                    String user_loc_name = jUserLocation.optString("name");
                    String user_loc_city = jUserLocation.optString("city");
                    String user_loc_state = jUserLocation.optString("state");
                    String user_loc_country = jUserLocation.optString("country");

                    String comment = jData.optString("comment");
                    String commentedeDate = displayQuoteTime(Long.parseLong(jData.optString("commentedDate")));
                    boolean deleted = jData.optBoolean("deleted");

                    JSONArray jMediaData = jData.optJSONArray("media");

                    String mediaUrl = "";
                    String mediaType = "";
                    String thumbnailUrl = "";

                    if (jMediaData != null) {
                        JSONObject jMediaObj = jMediaData.optJSONObject(0);

                        mediaUrl = jMediaObj.optString("mediaUrl");
                        mediaType = jMediaObj.optString("mediaType");
                        thumbnailUrl = jMediaObj.optString("thumbnailUrl");
                    }

                    mListCommentLocal.add(new ModelComments(postUuid, commentId, user_name, user_userName, user_profilePic,
                            user_relationship, user_uuid, user_bio, user_followersCount, user_followingCount,
                            user_joined, user_loc_name, user_loc_city, user_loc_state,
                            user_loc_country, comment, commentedeDate, deleted, mediaUrl, mediaType, thumbnailUrl));
                }
                return mListCommentLocal;
            } else {
                Log.i("feed", "empty");
                return null;
            }

        } catch (Exception ex) {
            Log.i("feed", ex.toString());
            return null;
        }

    }

    private List<ModelComments> refineJSONAddComment(JSONObject mJsonObj) {
        try {
            List<ModelComments> mListCommentLocal = new ArrayList<>();
            Log.e("response", mJsonObj.toString());

            JSONObject nComment = mJsonObj.optJSONObject("comment");

            String commentId = nComment.optString("id");
            JSONObject jUser = nComment.optJSONObject("user");

            String user_name = jUser.optString("name");
            String user_userName = jUser.optString("userName");
            String user_profilePic = jUser.optString("profilePic");
            String user_relationship = jUser.optString("relationship");
            String user_uuid = jUser.optString("uuid");

            String user_bio = jUser.optString("bio");
            String user_followersCount = jUser.optString("followersCount");
            String user_followingCount = jUser.optString("followingCount");

            String user_joined = getDataTimeFromMilliseconds(Long.parseLong(jUser.optString("joined")));

            JSONObject jUserLocation = jUser.optJSONObject("location");

            String user_loc_name = jUserLocation.optString("name");
            String user_loc_city = jUserLocation.optString("city");
            String user_loc_state = jUserLocation.optString("state");
            String user_loc_country = jUserLocation.optString("country");

            String comment = nComment.optString("comment");
            String commentedeDate = displayQuoteTime(Long.parseLong(nComment.optString("commentedDate")));
            boolean deleted = nComment.optBoolean("deleted");

            JSONArray jMediaData = nComment.optJSONArray("media");

            String mediaUrl = "";
            String mediaType = "";
            String thumbnailUrl = "";

            if (jMediaData != null) {
                JSONObject jMediaObj = jMediaData.optJSONObject(0);

                mediaUrl = jMediaObj.optString("mediaUrl");
                mediaType = jMediaObj.optString("mediaType");
                thumbnailUrl = jMediaObj.optString("thumbnailUrl");
            }

            mListCommentLocal.add(new ModelComments(mPostId, commentId, user_name, user_userName, user_profilePic,
                    user_relationship, user_uuid, user_bio, user_followersCount, user_followingCount,
                    user_joined, user_loc_name, user_loc_city, user_loc_state,
                    user_loc_country, comment, commentedeDate, deleted, mediaUrl, mediaType, thumbnailUrl));

            return mListCommentLocal;


        } catch (Exception ex) {
            Log.i("feed", ex.toString());
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uihandle.goBack();

    }

    @Override
    public void getCommentsAfter(int position, ModelComments mModleComments) {
        mPositionToInsertMoreComments = position + 1;
        if (!mFlagNoMoreComments) {
            goForCommnentsAfter(mModleComments.getComment_id());
        }


    }

}
