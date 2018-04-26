package braille.kofefe.app.modules_.reactions.callback;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import braille.kofefe.app.modules_.reactions.model.ModelReactionCommon;

/**
 * Created by Snow-Dell-05 on 16-Jan-18.
 */

public interface IReactionCallback {
    void startLoadingLiked(RecyclerView mRecycleLiked);

    void startLoadingAnger(RecyclerView mRecycleAnger);

    void startLoadingLaugh(RecyclerView mRecycleLaugh);

    void startLoadingLove(RecyclerView mRecycleLove);

    void startLoadingSad(RecyclerView mRecycleSad);

    void onFollowerBtnClick(String mClickedType, int position, ModelReactionCommon mModelReactionCommon, TextView mTextFollowerBtn);

    void goForUserProfile(String mUserUUId);


}




