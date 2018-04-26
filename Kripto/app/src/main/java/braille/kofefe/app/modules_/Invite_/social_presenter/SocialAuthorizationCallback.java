package braille.kofefe.app.modules_.Invite_.social_presenter;

/**
 * Created by Snow-Dell-05 on 12/21/2017.
 */

public interface SocialAuthorizationCallback {
    void onFacebookAuthorization(String mUsername);

    void onSnapchatAuthorization(String mUsername);

    void onTwitterAuthorization(String mUsername);

}
