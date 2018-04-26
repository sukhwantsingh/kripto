package braille.kofefe.app.supports_.url_keys;

public class URLListApis {


    // Api key for places data not for map
    public static final String api_key = "AIzaSyDOM5wTOgj9wreSSTiNzhD8PjPJ4VLBu8A";
    public static final String URL_SEARCH_NEAR_BY_PLACE; // /user/login **
    public static final String URL_PLACES_DETAILS;
    //  formated address
    public static final String URL_GEOCODER_LOCALE; // http://maps.googleapis.com/maps/api/geocode/json?latlng=latt,lngg&sensor=true&language=eng"


    //            phone verification
    public static final String URL_VALIDATE_PHONE_NUMBER;    // http://kofefe.us-east-2.elasticbeanstalk.com/rest/phonenumber/validate/+918699202626?requestId=
    public static final String URL_INITIATE_PHONE_NUMBER;    //http://{{BaseURL}}/rest/phonenumber/verification/initiate/+918699202627?debug=true&invitationCode=invitationCode&requestId=86282334-7304-42c7-8a53-4f5a7e5c2d61

    public static final String URL_CONFIRM_PHONE_VERIFICATION;    // http://{{BaseURL}}/rest/phonenumber/verification/process/+918699202626/5555?requestId=
    public static final String URL_COMPLETE_PHONE_VERIFICATION;   // http://{{BaseURL}}/rest/phonenumber/verification/complete?requestId=
    //            Email verification
    public static final String URL_INITIATE_EMAIL_VERIFICATION;  // http://{{BaseURL}}/rest/email/verification/initiate/labs.braille@gmail.com?requestId=862
    //            check email verification status
    public static final String URL_CHECK_EMAIL_VERIFICATION_STATUS;     // http://{{BaseURL}}/rest/email/verification/status/testnabba@gmail.com?requestId=862823
    //            Invitation
    public static final String URL_CHECK_BY_PHONE_NUMBER;    // http://{{BaseURL}}/rest/register/eligibility/phone/+831256514?requestId=86282334-7304-42c7-8a53-4f5a7e5c2d61
    //            invitaion check by phone number
    public static final String URL_CHECK_BY_INVITATION_CODE;     //http://{{BaseURL}}/rest/register/eligibility/phone/+831256514?requestId=86282334-7304-42c7-8a53-4f5a7e5c2d61

    //            restful api used in application
    public static final String URL_REGISTRATION_MODE;      // http://{{BaseURL}}/rest/register/modes?requestId=86282334-7304-42c7-8a53-4f5a7e5c2d61
    //            check user availability
    public static final String URL_CHECK_USER_AVAILABILITY;    // http://{{BaseURL}}/rest/user/profile/username/checkavailability/QEWER343?requestId=
    //            get user profile (GET)
    public static final String URL_GET_USER_PROFILE;  // http://{{BaseURL}}/rest/user/profile/requestId=86282
    //            update user profile (PUT)
    public static final String URL_UPDATE_USER_PROFILE;   // http://{{BaseURL}}/rest/user/profile?requestId=86282334-7304-42c7-8a53-4f5a7e5c2d61
    //            Upload  media link
    public static final String URL_UPLOAD_MEDIA; // http://{{BaseURL}}/rest/media?mediaType=IMAGE&usecase=PROFILE&fileExt=.jpeg&requestId=
    //            Create profile
    public static final String URL_CREATE_PROFILE;  // http://{{BaseURL}}/rest/user/profile?requestId=
    //            get Feed -latest
    public static final String URL_GET_FEED_LATEST;   //http://{{BaseURL}}/rest/feed?requestId=
    //            getFeed - more
    public static final String URL_GET_FEED_MORE;       // http://{{BaseURL}}/rest/feed/more?requestId=
    //            getFeed -status
    public static final String URL_GET_FEED_STATUS;       // http://{{BaseURL}}/rest/feed/status/52?requestId=
    //            Search users
    public static final String URL_SEARCH_USERS;  // http://{{BaseURL}}/rest/search?q=angel

    //            create post
    public static final String URL_CREATE_POST;   // http://{{BaseURL}}/rest/post?requestId=8c2d61
    //             preview post
    public static final String URL_PREVIEW_POST;    // http://{{BaseURL}}/rest/post/preview?requestId=86282-4f5a7e5c2d61
    //            get Post
    public static final String URL_GET_POST;  //http://{{BaseURL}}/rest/post/db4c37cc-6?requestId=86281
    //            follow user
    public static final String URL_FOLLOW_USER;    // http://{{BaseURL}}/rest/followers/follow/4fe9c6ca-afe6-4023-8df7-5e8a4c2c9e03?requestId=86282
    //            unfollow user
    public static final String URL_UNFOLLOW_USER; //http://{{BaseURL}}/rest/followers/unfollow/4fe9c6ca-afe2c9e03?requestId=86282334-7304-42c7-8a53-4f5a7e5c2d61
    //            Check Friends
    public static final String URL_CHECK_FRIENDS;  // http://{{BaseURL}}/rest/followers/check?requestId=86282334-730f5a7e5c2d61
    //            initialise followers at invite friend screen
    public static final String URL_INITIALIZE_FOLLOWERS;   // http://{{BaseURL}}/rest/followers/init?requestId=862823
    //         get followings
    public static final String URL_GET_FOLLOWING;   // http://{{BaseURL}}/rest/user/profile/user_id/following
    //         get following after
    public static final String URL_GET_FOLLOWING_AFTER;  // http://{{BaseURL}}/rest/user/profile/user_id/following?lastUserUuid=332b9389-f57
    //         get followers
    public static final String URL_GET_FOLLOWERS;   // http://{{BaseURL}}/rest/user/profile/2394b12a-b4036a0/followers
    //         get follower after
    public static final String URL_GET_FOLLOWERS_AFTER;     // http://{{BaseURL}}/rest/user/profile/b8695c9b-a/followers?lastUserUuid=9bbc6070-e
    //          get reactions of all type
    public static final String URL_GET_REACTIONS_OF_ALL_TYPE;    // http://{{BaseURL}}/rest/reaction/34ce1cbcfea/LIKED
    //           getPostLockActivityList
    public static final String URL_GET_POST_LOCK_ACTIVITY_LIST;     // http://{{BaseURL}}/rest/postlockactivity/{{RecentPostUUID}}?requestId=86
    //           unlock post
    public static final String URL_UNLOCK_POST;      //  http://{{BaseURL}}/rest/postunlock/unlock/54?requestId=86282334-
    //           openAnUnlockedPost
    public static final String URL_OPEN_AN_UNLOCKED_POST;        // http://{{BaseURL}}/rest/postunlock/open/POST_ID?requestId=86282
    //           getlatestComments
    public static final String URL_GET_LATEST_COMMENTS;      //   http://{{BaseURL}}/rest/comment/addda-82c64be0?requestId=862823
    //           getCommentsAfter
    public static final String URL_GET_COMMENTS_AFTER;      //   http://{{BaseURL}}/rest/comment/ad5ad003-2849-4dd64be0?lastCommentId=2&requestId=86282334-7304-
    //          addComments
    public static final String URL_ADD_COMMENTS;     // http://{{BaseURL}}/rest/comment/ad5ad00?requestId=86282334-73
    //         deletecomment
    public static final String URL_DELETE_COMMENTS;    //  http://{{BaseURL}}/rest/comment/delete?requestId=862823
    //          getNotifications
    public static final String URL_NOTIFICATIONS;    // http://{{BaseURL}}/rest/notification
    //         putReaction
    public static final String URL_PUT_REACTIONS;   // http://{{BaseURL}}/rest/reaction/ad5ad003-2849-4dda-829d-8cca31c64be0/LIKED?requestId=8
    //        registerDevice
    public static final String URL_DEVICE_REGISTER;     //  http://{{BaseURL}}/rest/device/register
    //            get more posts
    public static final String URL_GET_MORE_POSTS;    // http://{{BaseURL}}/rest/user/profile/{{UserUUID}}/posts?requestId=86282d61&lastPostUuid=4386c9a9-6
    //             mark notification as viewed
    public static final String URL_MARK_NOTIFICATION_AS_VIEWED;     // http://{{BaseURL}}/rest/notification/viewed/de353f8c-e44


    //   URL_MARK_NOTIFICATION_AS_VIEWED = (new StringBuilder(commonApiURL)).append("rest/notification/viewed/de353f8c-e44").toString();


    private static final String URL_COMMON_IMAGES;


    static String commonGoogleURL;
    static String commonImageURL;
    static String commonApiURL;


    static {

        commonGoogleURL = "https://maps.googleapis.com/maps/api/place";
        //  http://112.196.5.114/asa/dist/img/ads/3328Wirsing-SendingAndReceivingDataViaBluetoothWithAnAndroidDevice.pdf
        commonImageURL = "";

        //  commonApiURL = "http://ec2-34-212-227-45.us-west-2.compute.amazonaws.com/adminpanel"
        commonApiURL = "http://kofefe.us-east-2.elasticbeanstalk.com/";

        // &rankby = distance
        URL_GEOCODER_LOCALE = "http://maps.googleapis.com/maps/api/geocode/json?latlng=LATT_,PTGLG_&sensor=true&language=eng";
        URL_SEARCH_NEAR_BY_PLACE = (new StringBuilder(commonGoogleURL)).append("/nearbysearch/json?location=lt_lg&radius=20000&type=xyz&key=" + api_key).toString();
        URL_PLACES_DETAILS = (new StringBuilder(commonGoogleURL)).append("/details/json?placeid=pl_id&key=" + api_key).toString();//
        // URL_NEXT_PAGE_WITH_TOKEN = (new StringBuilder(commonGoogleURL)).append("/nearbysearch/json?pagetoken=PAGE_TOKEN&key=" + api_key).toString();

        URL_COMMON_IMAGES = commonImageURL;

        // Rest full api urls
        URL_VALIDATE_PHONE_NUMBER = (new StringBuilder(commonApiURL)).append("rest/phonenumber/validate/P_NUMBER?requestId=REQUESTID_VALUE").toString();
        URL_INITIATE_PHONE_NUMBER = (new StringBuilder(commonApiURL)).append("rest/phonenumber/verification/initiate/NUMBER?debug=true&invitationCode=invitationCode&requestId=REQUESTID_VALUE").toString();
        URL_CONFIRM_PHONE_VERIFICATION = (new StringBuilder(commonApiURL)).append("rest/phonenumber/verification/process/NUMBER/OTP?requestId=REQUESTID_VALUE").toString();
        URL_COMPLETE_PHONE_VERIFICATION = (new StringBuilder(commonApiURL)).append("rest/phonenumber/verification/complete?requestId=REQUESTID_VALUE").toString();
        URL_CHECK_BY_INVITATION_CODE = (new StringBuilder(commonApiURL)).append("rest/register/eligibility/invitation/INVITATION_CODE?requestId=REQUESTID_VALUE").toString();
        URL_CHECK_USER_AVAILABILITY = (new StringBuilder(commonApiURL)).append("rest/user/profile/username/checkavailability/INPUT_USERNAME?requestId=REQUESTID_VALUE").toString();
        URL_GET_USER_PROFILE = (new StringBuilder(commonApiURL)).append("rest/user/profile/USER_UUID?requestId=REQUESTID_VALUE").toString();
        URL_UPDATE_USER_PROFILE = (new StringBuilder(commonApiURL)).append("rest/user/profile?requestId=REQUESTID_VALUE").toString();

        URL_REGISTRATION_MODE = (new StringBuilder(commonApiURL)).append("rest/register/modes?requestId==REQUESTID_VALUE").toString();
        URL_CHECK_BY_PHONE_NUMBER = (new StringBuilder(commonApiURL)).append("rest/register/eligibility/phone/NUMBER?requestId=REQUESTID_VALUE").toString();
        // email verification
        URL_INITIATE_EMAIL_VERIFICATION = (new StringBuilder(commonApiURL)).append("rest/email/verification/initiate/EMAIL_ID?requestId=REQUESTID_VALUE").toString();

        // CHECK EMAIL VERIFICATION STATUS
        URL_CHECK_EMAIL_VERIFICATION_STATUS = (new StringBuilder(commonApiURL)).append("rest/email/verification/status/EMAIL_ID?requestId=REQUESTID_VALUE").toString();
        URL_SEARCH_USERS = (new StringBuilder(commonApiURL)).append("rest/search?q=SEARCHED_STRING").toString();

        URL_CREATE_PROFILE = (new StringBuilder(commonApiURL)).append("rest/user/profile?requestId=REQUESTID_VALUE").toString();
        URL_GET_FEED_LATEST = (new StringBuilder(commonApiURL)).append("rest/feed?requestId=REQUESTID_VALUE").toString();
        URL_GET_FEED_MORE = (new StringBuilder(commonApiURL)).append("rest/feed/more?requestId=REQUESTID_VALUE").toString();
        URL_GET_FEED_STATUS = (new StringBuilder(commonApiURL)).append("rest/feed/status/TOP_CHECK_MAT?requestId=REQUESTID_VALUE").toString();

        /*****
         * mediaType allowed values:
         IMAGE, VIDEO, AUDIO
         Usecase allowed values:
         PROFILE, POST, COMMENT, UNLOCK
         fileExt => .jpeg
         Timelimit: 3 min for pictures, 5 min for audio, 10 min for video ;
         the media has to be uploaded within this time limit (since generation) otherwise the link will expire.
         ******/
        URL_UPLOAD_MEDIA = (new StringBuilder(commonApiURL)).append("rest/media?mediaType=MEDIA_TYPE&usecase=USECASE&fileExt=FILE_EXTENTION&requestId=REQUESTID_VALUE").toString();

        URL_CREATE_POST = (new StringBuilder(commonApiURL)).append("rest/post?requestId=REQUESTID_VALUE").toString();
        URL_GET_POST = (new StringBuilder(commonApiURL)).append("rest/post/POST_UUID?requestId=REQUESTID_VALUE").toString();
        URL_PREVIEW_POST = (new StringBuilder(commonApiURL)).append("rest/post/preview?requestId=REQUESTID_VALUE").toString();

        // FOLLOW
        URL_CHECK_FRIENDS = (new StringBuilder(commonApiURL)).append("rest/followers/check?requestId=REQUESTID_VALUE").toString();
        URL_INITIALIZE_FOLLOWERS = (new StringBuilder(commonApiURL)).append("rest/followers/init?requestId=REQUESTID_VALUE").toString();
        URL_FOLLOW_USER = (new StringBuilder(commonApiURL)).append("rest/followers/follow/user_UUID?requestId=REQUESTID_VALUE").toString();
        URL_UNFOLLOW_USER = (new StringBuilder(commonApiURL)).append("rest/followers/unfollow/user_UUID?requestId=REQUESTID_VALUE").toString();

        // USER_PROFILE
        URL_GET_FOLLOWING = (new StringBuilder(commonApiURL)).append("rest/user/profile/UserUUID/following").toString();
        URL_GET_FOLLOWING_AFTER = (new StringBuilder(commonApiURL)).append("rest/user/profile/UserUUID/following?lastUserUuid=AKHRI_PERSON").toString();
        URL_GET_FOLLOWERS = (new StringBuilder(commonApiURL)).append("rest/user/profile/UserUUID/followers").toString();
        URL_GET_FOLLOWERS_AFTER = (new StringBuilder(commonApiURL)).append("rest/user/profile/UserUUID/followers?lastUserUuid=AKHRI_PERSON").toString();

        URL_GET_MORE_POSTS = (new StringBuilder(commonApiURL)).append("rest/user/profile/UserUUID/posts?requestId=REQUESTID_VALUE&lastPostUuid=AKHRI_PERSON").toString();

        URL_GET_REACTIONS_OF_ALL_TYPE = (new StringBuilder(commonApiURL)).append("rest/reaction/POST_ID/WHAT_YOU_WANT").toString();

        // POST_UNLOCK
        URL_UNLOCK_POST = (new StringBuilder(commonApiURL)).append("rest/postunlock/unlock/POST_ID?requestId=REQUESTID_VALUE-").toString();
        URL_OPEN_AN_UNLOCKED_POST = (new StringBuilder(commonApiURL)).append("rest/postunlock/open/POST_ID?requestId=REQUESTID_VALUE").toString();
        URL_GET_POST_LOCK_ACTIVITY_LIST = (new StringBuilder(commonApiURL)).append("rest/postlockactivity/POST_ID?requestId=REQUESTID_VALUE").toString();

        // COMMENTS
        URL_GET_LATEST_COMMENTS = (new StringBuilder(commonApiURL)).append("rest/comment/POST_ID?requestId=REQUESTID_VALUE").toString();
        URL_GET_COMMENTS_AFTER = (new StringBuilder(commonApiURL)).append("rest/comment/POST_ID?lastCommentId=LAST_WALI_PEHCHAN&requestId=REQUESTID_VALUE").toString();
        URL_ADD_COMMENTS = (new StringBuilder(commonApiURL)).append("rest/comment/POST_ID?requestId=REQUESTID_VALUE").toString();
        URL_DELETE_COMMENTS = (new StringBuilder(commonApiURL)).append("rest/comment/delete?requestId=REQUESTID_VALUE").toString();

        // NOTIFIACATIONS
        URL_NOTIFICATIONS = (new StringBuilder(commonApiURL)).append("rest/notification").toString();
        URL_MARK_NOTIFICATION_AS_VIEWED = (new StringBuilder(commonApiURL)).append("rest/notification/viewed/ID_PP").toString();


        // PUT REACTIONS
        URL_PUT_REACTIONS = (new StringBuilder(commonApiURL)).append("rest/reaction/POST_ID/WHAT_YOU_WANT?requestId=REQUESTID_VALUE").toString();
        //  REGISTER DEVICE
        URL_DEVICE_REGISTER = (new StringBuilder(commonApiURL)).append("rest/device/register").toString();

    }


}










