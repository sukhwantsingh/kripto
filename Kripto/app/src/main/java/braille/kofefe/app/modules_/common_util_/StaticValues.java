package braille.kofefe.app.modules_.common_util_;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Snow-Dell-05 on 12/8/2017.
 */

public class StaticValues {
    public static boolean mEmailCheckVerifier = false;
    public static String mConfirmPhoneNumber = "";
    public static String mVideoPathToPlay = "";
    public static int mLockUnlockSide = 0;

    /***
     * set uuid for searched users
     * ***/
    public static String mSearechedUserUUID = "";

    public static String mShareContentToInviteFriendForInstall = "";

    public static String mPostIdForReactionsDisplay = "";
    public static String mTextToBePreview = "";
    public static ArrayList<String> mUuidForMaintainUser = new ArrayList<>();
    public static HashSet<String> mHashUuidusers = new HashSet<>();
    public static String mPostId = "";
    public static String mPostComment = "";
    public static boolean mNotificationBlinking = false;
    public static boolean mFlagCommentCountManager = false;
    public static String mCommentCountFinal = "0";
    public static String mUuidForFollowers = "";

    public static class EditProfileContent {
        public static String mProfilePic = "";
        public static String mFullName = "";
        public static String mUsername = "";
        public static String mLocation = "";
        public static String mBio = "";

        public static String mLat = "0";
        public static String mLon = "0";


        public static String mFacebook = "";
        public static String mInsta = "";
        public static String mTwitter = "";
        public static String mSnap = "";


        public static boolean mProfileEdited = false;
        public static boolean mFlagMainFeedContentChanged = false;

    }

    public static class ReactionsCount {
        public static String mLikeCount = "";
        public static String mLaughCount = "";
        public static String mSadCount = "";
        public static String mLoveCount = "";
        public static String mAngryCount = "";
    }




}
