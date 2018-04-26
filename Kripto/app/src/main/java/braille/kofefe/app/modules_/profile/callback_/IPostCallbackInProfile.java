package braille.kofefe.app.modules_.profile.callback_;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import braille.kofefe.app.modules_.profile.model.ModelRecentPosts;

/**
 * Created by Snow-Dell-05 on 31-Jan-18.
 */

public interface IPostCallbackInProfile {
    void unlockPost(TextView mTextTimerScrambled, int position, ModelRecentPosts mFeedData, int timerInSecs, final DonutProgress mProgress, final TextView mTextPost, final FrameLayout mFrameProg, final TextView mTextUnlockTimer);
    void getPostLockActivityList(ModelRecentPosts mModelFeed, int position);
    void getPostsPosition(int position);
    void onShowEmojis(ModelRecentPosts mFeedData, int position, TextView mTextLikeView, ImageView mImgLike, ImageView mImgLaugh, ImageView mImgSad, ImageView mImgLove, ImageView mImgAngry);
    void onLikedClick(ModelRecentPosts mFeedData, int position, ImageView mImgLike);
    void getPostsAfter(int position, ModelRecentPosts mModleFollowings);

}
