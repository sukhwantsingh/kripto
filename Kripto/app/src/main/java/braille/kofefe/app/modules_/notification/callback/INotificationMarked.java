package braille.kofefe.app.modules_.notification.callback;

import android.widget.TextView;

import braille.kofefe.app.modules_.notification.model.ModelNotification;

/**
 * Created by Snow-Dell-05 on 15-Feb-18.
 */

public interface INotificationMarked {

    void getUuidForPost(ModelNotification mModelNotification, TextView mTextTitle, String mNotificationType, String mNotificationId, String mUserUUID);
}
