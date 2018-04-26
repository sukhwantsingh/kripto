package braille.kofefe.app.modules_.mainpostfeed.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.common_util_.VideoPlayScreen;
import braille.kofefe.app.modules_.mainpostfeed.SearchingActivity;
import braille.kofefe.app.modules_.mainpostfeed.callback_.IRelationshipOnClickCallback;
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeedMedia;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelScrambledMedia;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelSearchedUsers;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.supports_.UiHandleMethods;

import static braille.kofefe.app.modules_.common_util_.Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OPEN;
import static braille.kofefe.app.modules_.common_util_.Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OWNER_NO_LOCK;
import static braille.kofefe.app.modules_.common_util_.Constants.ConstantPostLockStatus.POST_LOCK_STATUS_UNLOCKED;

/**
 * Created by Snow-Dell-05 on 04-Jan-18.
 */

public class AdapterSearchedUsers extends RecyclerView.Adapter<AdapterSearchedUsers.MyViewHolder> {

    private Context context;
    private UiHandleMethods uihandle;
    private List<ModelSearchedUsers> mListSearchedResult;
    private IRelationshipOnClickCallback mCallbackRelationClicked;
    private boolean mShowPostFlag = false, mShowPeopleFlag = false;
    private int mPositionPost;

    public AdapterSearchedUsers(Activity context, List<ModelSearchedUsers> mListSearchedResult) {
        this.context = context;
        this.mListSearchedResult = mListSearchedResult;
        this.uihandle = new UiHandleMethods(context);

        if (context instanceof SearchingActivity) {
            mCallbackRelationClicked = (IRelationshipOnClickCallback) context;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_searched_uesrs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ModelSearchedUsers mData = mListSearchedResult.get(position);
        final ModelFeedMedia[] mMedia = mData.getMedia();
        final ModelScrambledMedia[] mScrambledMedia = mData.getScrambledMedia();


        /***
         * * setting values to views
         * ***/
        if (!mData.ismFlagCheckPost()) {

            holder.mRelativeInfo.setVisibility(View.VISIBLE);
            holder.mLinearPostlayout.setVisibility(View.GONE);

            holder.mTextHeadingPost.setVisibility(View.GONE);
            holder.mTextPeopleHeading.setVisibility(View.VISIBLE);

            if (position == 0) {
                holder.mTextPeopleHeading.setVisibility(View.VISIBLE);
                //  mShowPeopleFlag = true;
            } else {
                holder.mTextPeopleHeading.setVisibility(View.GONE);
            }

            /**
             *  setting value to views
             * **/
            holder.mTextName.setText(mData.getName());
            holder.mTextuserFollow.setText(mData.getUserName());
            holder.mTextUsername.setText("Joined " + mData.getJoined());
            //      Todo: change dynamic values
            //      holder.mTextUserBio.setText(mData.getBio());
            holder.mTextUserBio.setText(mData.getLoc_city() + ", " + mData.getLoc_state() + ", " + mData.getLoc_country());
            holder.mTextJoinedInfo.setText(mData.getFollowersCount() + " Followers  \u273b  " + mData.getFollowingCount() + " Following");

            uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

            /***
             * switch case for the relationship
             * **/
            switch (mData.getRelationship()) {
                case ConstantStatusInAPI.RELATIONSHIP_SELF:
                    holder.mFollowBtn.setEnabled(false);
                    holder.mFollowBtn.setText("    You    ");
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                    break;
                case ConstantStatusInAPI.RELATIONSHIP_NONE:
                    holder.mFollowBtn.setText("    Follow    ");
                    holder.mFollowBtn.setEnabled(true);
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                    break;
                case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER:
                    holder.mFollowBtn.setText("    Follow    ");
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName() + "<font  color='#00c6fb'> Follows you</font>"));
                    holder.mFollowBtn.setEnabled(true);
                    break;
                case ConstantStatusInAPI.RELATIONSHIP_FOLLOWED:
                    holder.mFollowBtn.setEnabled(true);
                    holder.mFollowBtn.setText("   Following   ");
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                    break;
                case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER_AND_FOLLOWED:
                    holder.mFollowBtn.setEnabled(true);
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName() + "<font color='#00c6fb'> Follows you</font>"));
                    holder.mFollowBtn.setText("   Following   ");
                    break;
                default:
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                    break;
            }

        } else {
            holder.mRelativeInfo.setVisibility(View.GONE);
            holder.mLinearPostlayout.setVisibility(View.VISIBLE);
            holder.mTextPeopleHeading.setVisibility(View.GONE);

            if (!mShowPostFlag) {
                holder.mTextHeadingPost.setVisibility(View.VISIBLE);
                mPositionPost = position;
                mShowPostFlag = true;
            }

            if (position == mPositionPost) {
                holder.mTextHeadingPost.setVisibility(View.VISIBLE);
            } else {
                holder.mTextHeadingPost.setVisibility(View.GONE);
            }

            holder.mPostUserName.setText(mData.getName());
            holder.mPostTime.setText(mData.getPostTime());

          /*
          if (!mData.getPostContent().equals("null")) {
                holder.mPostText.setText(mData.getPostContent());
            } else {
                holder.mPostText.setText(mData.getContent());
            }
            */
            //         show location if not empty

            if (!mData.getLocation_name().equals("")) {
                holder.mTextLocationP.setText(mData.getLocation_name() + ", " + mData.getLocation_city() + ", " + mData.getLocation_state() + ", " + mData.getLocation_country());
                holder.mTextLocationP.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.loc_16, 0, 0, 0);
            }


            switch (mData.getPostLockStatus()) {
                case POST_LOCK_STATUS_UNLOCKED:
                case POST_LOCK_STATUS_OPEN:
                case POST_LOCK_STATUS_OWNER_NO_LOCK:

                    //      set the actual content
                    if (!mData.getContent().equals("null") || mData.getContent() != null) {
                        holder.mTextViewPost.setText(mData.getContent());
                    }

                    //      set the media if available
                    if (mMedia != null) {
                        holder.mRelMediaPostFrame.setVisibility(View.VISIBLE);

                        holder.mFrameProgress.setVisibility(View.GONE);
                        holder.mFrameScrambled.setVisibility(View.GONE);
                        holder.mFramePost.setVisibility(View.VISIBLE);

                        switch (mMedia[0].getMediaType()) {
                            case Constants.ConstantMediaType.MEDIA_TYPE_VIDEO:
                                holder.mPlayButton.setVisibility(View.VISIBLE);
                                uihandle.getImageWithVolley(mMedia[0].getThumbnailUrl(), holder.mVideoImageView);

                                holder.mPlayButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        StaticValues.mPostId = mListSearchedResult.get(position).getUuid();
                                        StaticValues.mVideoPathToPlay = mMedia[0].getMediaUrl();
                                        uihandle.goForNextScreen(VideoPlayScreen.class);
                                    }
                                });
                                break;
                            case Constants.ConstantMediaType.MEDIA_TYPE_IMAGE:
                                holder.mPlayButton.setVisibility(View.GONE);

                                uihandle.getImageWithVolley(mMedia[0].getMediaUrl(), holder.mVideoImageView);
                                holder.mVideoImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onImageClickDialog(holder.mVideoImageView);
                                    }
                                });
                                break;

                            case Constants.ConstantMediaType.MEDIA_TYPE_AUDIO:
                                holder.mPlayButton.setVisibility(View.VISIBLE);
                                holder.mVideoImageView.setImageResource(R.drawable.music_);

                                holder.mPlayButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        StaticValues.mPostId = mListSearchedResult.get(position).getUuid();
                                        StaticValues.mVideoPathToPlay = mMedia[0].getMediaUrl();

                                        uihandle.goForNextScreen(VideoPlayScreen.class);
                                    }
                                });
                                break;
                        }
                    } else {
                        holder.mRelMediaPostFrame.setVisibility(View.GONE);
                   //     holder.mTextViewPost.setHeight(400);
                   //     holder.mTextViewPost.setGravity(Gravity.CENTER);
                        //    holder.mTextViewPost.setBackgroundResource(R.drawable.stroke_blue_gradiant);
                    }
                    break;

                case Constants.ConstantPostLockStatus.POST_LOCK_STATUS_LOCKED:
                case Constants.ConstantPostLockStatus.POST_LOCK_STATUS_UNLOCK_IN_PROGRESS:

                    holder.mRelMediaPostFrame.setVisibility(View.VISIBLE);
                    holder.mFrameProgress.setVisibility(View.GONE);
                    holder.mFrameScrambled.setVisibility(View.VISIBLE);

                    if (mData.getScrambledMedia() != null) {
                        holder.mImageScrambled.setVisibility(View.VISIBLE);
                        holder.mTextScrambled.setVisibility(View.GONE);


                        //      set the actual content
                        if (!mData.getPostContent().equals("null")) {
                            holder.mTextViewPost.setText(mData.getPostContent());
                        }

                        uihandle.loadGIImage(holder.mImageScrambled, mScrambledMedia[0].getMediaUrl());

                    } else {
                        holder.mTextScrambled.setVisibility(View.VISIBLE);
                        holder.mImageScrambled.setVisibility(View.GONE);

                        holder.mTextScrambled.setText(mData.getPostContent());
                    }

                   /* holder.mImgLockPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //        use this to start and trigger a service
                            if (handler != null) {
                                mFeedCallback.unlockPost(position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                        holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                                //   scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                                //   holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                            } else {
                                handler = new Handler();
                                mFeedCallback.unlockPost(position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                        holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                                //       scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                                //       holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                            } holder.mImgLockPost.setEnabled(false);  }
                    });*/
                    break;

                case Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OLD_POST_FIRST_TIME_VIEW:

                    holder.mRelMediaPostFrame.setVisibility(View.VISIBLE);
                    holder.mFrameProgress.setVisibility(View.GONE);
                    holder.mFrameScrambled.setVisibility(View.VISIBLE);

                    if (mData.getScrambledMedia() != null) {
                        holder.mImageScrambled.setVisibility(View.VISIBLE);
                        holder.mTextScrambled.setVisibility(View.GONE);


                        //      set the actual content
                        if (!mData.getPostContent().equals("null")) {
                            holder.mTextViewPost.setText(mData.getPostContent());
                        }


                        uihandle.loadGIImage(holder.mImageScrambled, mScrambledMedia[0].getMediaUrl());

                    } else {
                        holder.mTextScrambled.setVisibility(View.VISIBLE);
                        holder.mImageScrambled.setVisibility(View.GONE);

                        holder.mTextScrambled.setText(mData.getPostContent());
                    }

                   /* holder.mImgLockPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //         use this to start and trigger a service
                            if (handler != null) {
                                mFeedCallback.unlockPost(position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                        holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                                //   scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                                //   holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                            } else {
                                handler = new Handler();
                                mFeedCallback.unlockPost(position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                        holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                                //       scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                                //       holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                            }

                            holder.mImgLockPost.setEnabled(false);
                        }
                    });*/
                    break;
            }

            uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mPostImgUser);

          /*  holder.mLinearPostlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StaticValues.mSearechedUserUUID = mListSearchedResult.get(position).getUuid();
                    uihandle.goForNextScreen(ProfileActivity.class);
                }
            });*/


        }

        //    uihandle.setImageWithPicassoCircle(holder.mImgUser, mData.getProfilePic());
    }


    @Override
    public int getItemCount() {
        return mListSearchedResult.size();
    }

    private void onImageClickDialog(ImageView mImageView) {
        Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.dilaog_full_screen_image_view);
        ImageView mImage = d.findViewById(R.id.image);
        mImage.setImageBitmap(((SearchingActivity) context).getBitmapFromView(mImageView));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.show();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextPeopleHeading;
        private SquareRoundCornerImageView mImgUser;
        private TextView mTextName, mTextUsername, mTextUserBio, mTextJoinedInfo, mFollowBtn;
        private TextView mTextuserFollow;
        private LinearLayout mLinearlay;
        /**
         * Post layout declarations
         **/
        private RelativeLayout mRelativeInfo;
        private TextView mTextHeadingPost;
        private LinearLayout mLinearPostlayout;
        private RelativeLayout mRealtivePost;

        private SquareRoundCornerImageView mPostImgUser;
        private TextView mPostUserName, mPostTime, mTextViewPost;

        private RelativeLayout mRelMediaPostFrame;
        private FrameLayout mFramePost;
        private FrameLayout mFrameProgress;
        private FrameLayout mFrameScrambled;

        private ImageView mVideoImageView;
        private TextView mTextScrambled;
        private ImageView mImageScrambled;

        private TextView mTextLocationP;   //  mTextJoinedP, mTextLocationP, mTextFollowrsCountP;
        private ImageView mPlayButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTextLocationP = itemView.findViewById(R.id.textView_location_p);
            mPlayButton = itemView.findViewById(R.id.imageView_video_play);

            mTextScrambled = itemView.findViewById(R.id.txt_scrambled);
            mFrameScrambled = itemView.findViewById(R.id.frame_scrambled);
            mFramePost = itemView.findViewById(R.id.frame_post);
            mFrameProgress = itemView.findViewById(R.id.progress_unscrambled);
            mImageScrambled = itemView.findViewById(R.id.imageView_scrambled);

            mVideoImageView = itemView.findViewById(R.id.imageView_video_back);
            //        mTextLocation = itemView.findViewById(R.id.textView_loacation);
            mRelMediaPostFrame = itemView.findViewById(R.id.relative);


            mTextPeopleHeading = itemView.findViewById(R.id.text_people_bar);
            mRelativeInfo = itemView.findViewById(R.id.rel_info);
            mTextHeadingPost = itemView.findViewById(R.id.text_post_bar);
            mLinearPostlayout = itemView.findViewById(R.id.linear_post_layout);

            mPostImgUser = itemView.findViewById(R.id.circleImageView);
            mPostUserName = itemView.findViewById(R.id.txt_namee);

            mPostTime = itemView.findViewById(R.id.txt_time);
            mTextViewPost = itemView.findViewById(R.id.txt_post_txt);

            mImgUser = itemView.findViewById(R.id.circleImageView2);

            mTextuserFollow = itemView.findViewById(R.id.txt_username);

            mTextUsername = itemView.findViewById(R.id.txt_joined);
            mTextUserBio = itemView.findViewById(R.id.txt_userProfesion);
            mTextJoinedInfo = itemView.findViewById(R.id.txt_address);
            mTextName = itemView.findViewById(R.id.txt_name);
            mFollowBtn = itemView.findViewById(R.id.btn_follow);

            mLinearlay = itemView.findViewById(R.id.linear_users_lay);
            mRealtivePost = itemView.findViewById(R.id.rel_post);

            mFollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbackRelationClicked.onRelationshipClick(mListSearchedResult.get(getPosition()).getRelationship(),
                            mListSearchedResult.get(getPosition()), mFollowBtn);
                }
            });

            mLinearlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StaticValues.mSearechedUserUUID = mListSearchedResult.get(getPosition()).getUuid();
                    uihandle.goForNextScreen(ProfileActivity.class);
                }
            });

            mRealtivePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StaticValues.mSearechedUserUUID = mListSearchedResult.get(getPosition()).getUuid();
                    uihandle.goForNextScreen(ProfileActivity.class);
                }
            });

            mPostImgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickDialog(mPostImgUser);
                }
            });

            mImgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickDialog(mImgUser);
                }
            });

        }
    }
}


