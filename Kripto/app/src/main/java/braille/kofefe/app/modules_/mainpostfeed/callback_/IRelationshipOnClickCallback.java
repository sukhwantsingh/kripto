package braille.kofefe.app.modules_.mainpostfeed.callback_;

import android.widget.TextView;

import braille.kofefe.app.modules_.mainpostfeed.model.ModelSearchedUsers;

/**
 * Created by Snow-Dell-05 on 08-Jan-18.
 */

public interface IRelationshipOnClickCallback {
    void onRelationshipClick(String mRelation, ModelSearchedUsers mModelSearchUsers, TextView mTextFollowBtn);
}
