package braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.callback;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.model.ModelPostActivityList;

/**
 * Created by Snow-Dell-05 on 30-Jan-18.
 */

public interface IPostLockActivityListCallback {
    void getUnlockedUsers(RecyclerView mRecyclerView);
    void getLockInProgressUsers(RecyclerView mRecycleView);

    void onRelationshipClickLock(String mRelation, ModelPostActivityList mModelPostActivity, TextView mTextFollowBtn);
    void onRelationshipClickUnlocked(String mRelation, ModelPostActivityList mModelPostActivity, TextView mTextFollowBtn);
    void goForProfile(String uuid);


}
