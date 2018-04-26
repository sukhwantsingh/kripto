package braille.kofefe.app.modules_.mainpostfeed.callback_;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeed;

/**
 * Created by Snow-Dell-05 on 11/30/2017.
 */

public interface MainFeedCallback {
    void getFeedInfo(int position);

    void getMoreOldFeeds(int position);

    void getPostLockActivityList(ModelFeed mModelFeed, int position);

    void unlockPost( final TextView mTextBackTimer,int position, ModelFeed mFeedData, int timerInSecs, final DonutProgress mProgress, final TextView mTextPost, final FrameLayout mFrameProg, final TextView mTextUnlockTimer);

    void onShowEmojis(ModelFeed mFeedData, int position, TextView mTextLikeView, ImageView mImgLike, ImageView mImgLaugh, ImageView mImgSad, ImageView mImgLove, ImageView mImgAngry);

    void onLikedClick(ModelFeed mFeedData, int position,ImageView mImgLike);

    void getModelFeed(int position);

    void displayImage(ImageView mImageView);


}
