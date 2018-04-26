package braille.kofefe.app.supports_;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Snow-Dell-05 on 7/25/2017.
 */

public class SessionKofefeApp {

    private static final String PREF_NAME = "kofefe_app";
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    private int PRIVATE_MODE;
    private Context _context;

    public SessionKofefeApp(Context context) {
        PRIVATE_MODE = 0;
        _context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsLogged(boolean flag) {
        editor.putBoolean("is_Login", flag);
        editor.commit();
    }

    public boolean isLogged() {
        return pref.getBoolean("is_Login", false);
    }


    public String getLoggedInUsername() {
        return pref.getString("logged_in_username", "");
    }

    public void setLoggedInUsername(String username) {
        editor.putString("logged_in_username", username);
        editor.commit();
    }

    public String getLoggedInName() {
        return pref.getString("logged_in_name_", "");
    }

    public void setLoggedInName(String name) {
        editor.putString("logged_in_name_", name);
        editor.commit();
    }


    public String getLoggedInInvitationCode() {
        return pref.getString("logged_in_invitation_code", "");
    }

    public void setLoggedInInvitationCode(String token) {
        editor.putString("logged_in_invitation_code", token);
        editor.commit();
    }

    public String getAuthorizationsetUUID() {
        return pref.getString("authorisation_UUID_number", "");
    }

    public void setAuthorizationUUID(String token) {
        editor.putString("authorisation_UUID_number", token);
        editor.commit();
    }


    public String getAuthorizationPassword() {
        return pref.getString("authorisation_password", "");
    }

    public void setAuthorizationPassword(String token) {
        editor.putString("authorisation_password", token);
        editor.commit();
    }


    public String getVerfiedUserId() {
        return pref.getString("authorised_user_UUID", "");
    }

    public void setVerfiedUserId(String user_UUID) {
        editor.putString("authorised_user_UUID", user_UUID);
        editor.commit();
    }

    public String getUserProfilePic() {
        return pref.getString("authorised_user_profile_pic", "");
    }

    public void setUserProfilePic(String userProfilePic) {
        editor.putString("authorised_user_profile_pic", userProfilePic);
        editor.commit();
    }


    public String getShareContentForInviteFriendScreen() {
        return pref.getString("logged_sahre_content_for_invite_friend_screen", "");
    }

    public void setShareContentForInviteFriendScreen(String sahre_conten) {
        editor.putString("logged_sahre_content_for_invite_friend_screen", sahre_conten);
        editor.commit();
    }


    public String getDeviceFCMToken() {
        return pref.getString("device_fcm_token", null);
    }

    public void setDeviceFCMToken(String token) {
        editor.putString("device_fcm_token", token);
        editor.commit();
    }

    public boolean isDeviceTokenRefreshedFlag() {
        return pref.getBoolean("is_token_refreshed", false);
    }

    public void setDeviceTokenRefreshedFlag(boolean flag) {
        editor.putBoolean("is_token_refreshed", flag);
        editor.commit();
    }

    public String getUuidFromNotification() {
        return pref.getString("uuid_from_notification_", "");
    }

    public void setUuidFromNotification(String token) {
        editor.putString("uuid_from_notification_", token);
        editor.commit();
    }

    public String getNotificationIdFromNotificatio() {
        return pref.getString("notification_id_from_notification_", "");
    }

    public void setNotificationIdFromNotification(String noti_id) {
        editor.putString("notification_id_from_notification_", noti_id);
        editor.commit();
    }


    ///////////......................//////////............../////////......................./////////////

    public String getLoggedInId() {
        return pref.getString("logged_in_id", "");
    }

    public void setLoggedInId(String id) {
        editor.putString("logged_in_id", id);
        editor.commit();
    }


    public String getLoggedInEmail() {
        return pref.getString("logged_in_email", "");
    }

    public void setLoggedInEmail(String emailId) {
        editor.putString("logged_in_email", emailId);
        editor.commit();
    }


    public String getLoggedInLastName() {
        return pref.getString("logged_in_last_name", "");
    }

    public void setLoggedInLastName(String lastNmae) {
        editor.putString("logged_in_last_name", lastNmae);
        editor.commit();
    }

    public String getPlayerProfilePic() {
        return pref.getString("player_profile_pic", "");
    }


    public void setPlayerProfilePic(String playerImage) {
        //  String img_url = URLListApis.URL_COMMON_IMAGES;
        editor.putString("player_profile_pic", playerImage);
        editor.commit();
    }

    public String getPlayerBannerPic() {
        return pref.getString("player_banner_pic", "");
    }

    public void setPlayerBannerPic(String playerBannerPic) {
        // String img_url = URLListApis.URL_COMMON_IMAGES;
        editor.putString("player_banner_pic", playerBannerPic);
        editor.commit();
    }


    public String getLoggedInUserType() {
        return pref.getString("logged_in_user_type", "");
    }

    public void setLoggedInUserType(String userType) {
        editor.putString("logged_in_user_type", userType);
        editor.commit();
    }




  /*  public String getLoggedInId() {
        return pref.getString("logged_in_id", "");
    }

    public void setLoggedInId(String id) {
        editor.putString("logged_in_id", id);
        editor.commit();
    }
  */  //..........


    public void logoutUser() {
        editor.clear();
        editor.commit();

    }

    public void storeVideoTempPath(String postId, String temPath) {
        editor.putString("Video_" + postId, temPath);
        editor.commit();
    }

    public String getTempVideoPath(String postId) {
        return pref.getString("Video_" + postId, "");
    }

}
