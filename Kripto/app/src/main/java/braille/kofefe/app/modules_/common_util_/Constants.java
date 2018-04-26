package braille.kofefe.app.modules_.common_util_;

/**
 * Created by Snow-Dell-05 on 12/6/2017.
 */

public class Constants {

    /***
     * Service codes for Api request send in queue
     ***/

    public static final int CHECK_BY_INVITATION_CODE = 1;
    public static final int VALIDATE_PHONE_NUMBER = 2;
    public static final int INITIATE_PHONE_NUMBER = 3;
    public static final int CONFIRM_PHONE_VERIFICATION = 4;
    public static final int COMPLETE_PHONE_VERIFICATION = 5;


    public static final int REGISTRATION_MODE = 11;
    public static final int CHECK_BY_PHONE_NUMBER = 12;
    public static final int UPLOAD_MEDIA_TO_AMAZONE_PRE_FIRE_API = 13;
    public static final int UPLOAD_PROFILE_IMAGE_TO_AMAZONE = 14;
    public static final int INITIATE_EMAIL_VERIFICATION = 15;
    public static final int CHECK_EMAIL_VERIFICATION_STATUS = 16;


    /****
     * Feeds
     * */
    public static final int GET_FEED_STATUS = 7;
    public static final int GET_FEED_MORE = 8;
    public static final int GET_FEED_LATEST = 9;


    /****
     * User Profile
     * */
    public static final int GET_CHECK_USER_AVAILABILITY = 10;
    public static final int CREATE_PROFILE = 6;
    public static final int GET_USER_PROFILE_GET = 17;
    public static final int UPDATE_USER_PROFILE_PUT = 18;

    public static final int GET_FOLLOWERS_GET = 41;
    public static final int GET_FOLLOWERS_AFTER_GET = 42;
    public static final int GET_FOLLOWING_GET = 43;
    public static final int GET_FOLLOWING_AFTER_GET = 44;


    /****
     * follow
     * */
    public static final int CHECK_FRIENDS_POST = 19;
    public static final int INITIALIZE_FOLLOWERS_POST = 20;
    public static final int SYNC_FRIENDS_POST = 21;
    public static final int FOLLOW_USER_POST = 22;
    public static final int UNFOLLOW_USER_POST = 23;

    public static final int FOLLOW_USER_ = 49;
    public static final int UNFOLLOW_USER_ = 50;

    /****
     * Reaction
     * */
    public static final int PUT_REACTION_PUT = 24;
    public static final int GET_ALL_REACTIONS_OF_TYPE_LIKED = 25;

    public static final int GET_ALL_REACTIONS_OF_TYPE_LOVE = 45;
    public static final int GET_ALL_REACTIONS_OF_TYPE_SAD = 46;
    public static final int GET_ALL_REACTIONS_OF_TYPE_LAUGH = 47;
    public static final int GET_ALL_REACTIONS_OF_TYPE_ANGER = 48;


    /****
     * Post
     * */
    public static final int CREATE_POST_POST = 26;
    public static final int GET_POST_GET = 27;
    public static final int PREVIEW_POST = 28;
    public static final int GET_USER_POSTS_MORE_GET = 53;

    /****
     * Post Unlock
     * */
    public static final int OPEN_AN_UNLOCKED_POST_POST = 29;
    public static final int UNLOCK_POST_GET = 30;
    public static final int POST_LOCK_ACTIVITY_LIST_GET = 51;


    /****
     * Miscellaneous
     * */
    public static final int RESET_PASSWORD_GET = 31;

    /****
     * Admin Apis
     * */
    public static final int API_TO_GENERATE_INVITATION_CODE_GET = 32;
    public static final int API_TO_WHITELIST_A_PHONE_NUMBER_GET = 33;
    public static final int FOLLOW_PUT = 34;

    /****
     * Comment
     * */
    public static final int ADD_COMMENT_POST = 35;
    public static final int DELETE_COMMENT_POST = 36;
    public static final int GET_LATEST_COMMENTS_GET = 37;
    public static final int GET_COMMENTS_AFTER_GET = 38;

    /****
     * Device
     * */
    public static final int REGISTER_DEVICE_POST = 39;

    /****
     * Search
     * */
    public static final int SEARCH_GET = 40;

    /****
     * Notifications
     * */
    public static final int NOTIFICATIONS_GET = 52;
    public static final int MARK_NOTIFICATION_AS_VIEWED_POST = 54;

  //.............//..............//...............//...............//..................//................//............//.............


    /***
     * Constants for operation included in tha application
     ***/
    public static final int TAKE_PICTURE = 0;
    public static final int SELECT_FILE = 1;
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int PICK_SONG = 2;
    public static final int RECORD_VIDEO_ = 212;
    public static final int READ_REQUEST_CODE = 200;
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 201;

    /***
     * Permissions code
     ***/
    public static final int WAIT_SECONDS = 5000;

    public final static class ConstantTwitter {

        public static final String TWITTER_CONSUMER_KEY = "5hvRXecoamD7tNjrGxX8l45jS";
        public static final String TWITTER_CONSUMER_SECRET = "2szmVtMotaUCUDXpFg5EsmMHKK2nKnbraVwMgVGsUHRX85ZHZd";
    }

    public final static class ConstantInstagram {
        public static final String CLIENT_ID = "7a9560935d9047cabe39aaf7b7684d7b";
        public static final String CLIENT_SECRET = "c85b71c852a74e51808757101b9babe2";
        public static final String CALLBACKURL = "http://www.snowflakessoftware.com/";
        // Used to specify the API version which we are going to use.
        public static final String APIURL = "https://api.instagram.com/v1";
        // Used for Authentication.
        public static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
        // Used for getting token and User details.
        public static final String TOKENURL = "https://api.instagram.com/oauth/access_token";

        public static final String authURLString = AUTHURL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships";
        public static final String tokenURLString = TOKENURL + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + CALLBACKURL + "&grant_type=authorization_code";
    }

    public final static class ConstantMediaType {
        //  IMAGE, VIDEO, AUDIO
        public static final String MEDIA_TYPE_IMAGE = "IMAGE";
        public static final String MEDIA_TYPE_VIDEO = "VIDEO";
        public static final String MEDIA_TYPE_AUDIO = "AUDIO";
    }

    public final static class ConstantUseCase {
        //   PROFILE, POST, COMMENT, UNLOCK
        public static final String USECASE_PROFILE = "PROFILE";
        public static final String USECASE_POST = "POST";
        public static final String USECASE_COMMENT = "COMMENT";
        public static final String USECASE_UNLOCK = "UNLOCK";
    }

    public final static class ConstantFileExtention {
        // .jpeg, png, bmp
        public static final String TYPE_JPEG = ".jpeg";
        public static final String TYPE_JPG = ".jpg";
        public static final String TYPE_PNG = ".png";
        public static final String TYPE_BMP = ".bmp";
        public static final String TYPE_ALL = "*";


    }

    public final static class ConstantPutReactions {
        //  LIKED, ANGER, SAD, LOVE, LAUGH.
        public static final String REACTION_LIKED = "LIKED";
        public static final String REACTION_ANGER = "ANGER";
        public static final String REACTION_SAD = "SAD";
        public static final String REACTION_LOVE = "LOVE";
        public static final String REACTION_LAUGH = "LAUGH";
    }

    public final static class ConstantPostLockStatus {
        //   OPEN / UNLOCKED LOCKED UNLOCK_IN_PROGRESS OLD_POST_FIRST_TIME_VIEW OWNER_NO_LOCK
        public static final String POST_LOCK_STATUS_OPEN = "OPEN";
        public static final String POST_LOCK_STATUS_UNLOCKED = "UNLOCKED";
        public static final String POST_LOCK_STATUS_LOCKED = "LOCKED";
        public static final String POST_LOCK_STATUS_UNLOCK_IN_PROGRESS = "UNLOCK_IN_PROGRESS";
        public static final String POST_LOCK_STATUS_OLD_POST_FIRST_TIME_VIEW = "OLD_POST_FIRST_TIME_VIEW";
        public static final String POST_LOCK_STATUS_OWNER_NO_LOCK = "OWNER_NO_LOCK";
    }


}
