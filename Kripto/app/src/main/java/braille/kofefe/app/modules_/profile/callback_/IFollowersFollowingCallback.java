package braille.kofefe.app.modules_.profile.callback_;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import braille.kofefe.app.modules_.profile.model.ModelFollowers;
import braille.kofefe.app.modules_.profile.model.ModelFollowing;

/**
 * Created by Snow-Dell-05 on 08-Jan-18.
 */

public interface IFollowersFollowingCallback {

    void getFollowers(RecyclerView mRecycle, View v);

    void getFollowersAfter(int position, ModelFollowers mModelFollowers);

    void getFollowings(RecyclerView mRecycle, View v);

    void getFollowingsAfter(int position, ModelFollowing mModleFollowings);


    void getRecentPosts(RecyclerView mRecycle, View v);


    void onFollowerUserClick(int position, ModelFollowers mModelFollowers);

    void onFollowingUserClick(int position, ModelFollowing mModleFollowings);


    void onFollowerBtnClick(int position, ModelFollowers mModelFollowers, TextView mTextFollowerBtn);

    void onFollowingBtnClick(int position, ModelFollowing mModleFollowings, TextView mTextFollowingBtn);


}
