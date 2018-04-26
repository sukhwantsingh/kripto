package braille.kofefe.app.modules_.Invite_.callback;

import braille.kofefe.app.modules_.Invite_.model.ModelInviteFriends;

/**
 * Created by Snow-Dell-05 on 15-Jan-18.
 */

public interface IInviteCallback {
    void fireInviteFriend(ModelInviteFriends mInviteModel,String phoneNumber);
}
