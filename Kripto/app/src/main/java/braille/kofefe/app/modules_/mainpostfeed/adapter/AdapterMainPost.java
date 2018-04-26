package braille.kofefe.app.modules_.mainpostfeed.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.Constants;
import braille.kofefe.app.modules_.common_util_.FlipAnimation;
import braille.kofefe.app.modules_.common_util_.OnSwipeTouchListener;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.common_util_.VideoPlayScreen;
import braille.kofefe.app.modules_.mainpostfeed.MainActivity;
import braille.kofefe.app.modules_.mainpostfeed.callback_.MainFeedCallback;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeed;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelFeedMedia;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelScrambledMedia;
import braille.kofefe.app.modules_.reactions.ReactionsActivity;
import braille.kofefe.app.supports_.UiHandleMethods;
import pl.droidsonroids.gif.GifImageView;

import static braille.kofefe.app.modules_.common_util_.Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OPEN;
import static braille.kofefe.app.modules_.common_util_.Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OWNER_NO_LOCK;
import static braille.kofefe.app.modules_.common_util_.Constants.ConstantPostLockStatus.POST_LOCK_STATUS_UNLOCKED;

/**
 * Created by Snow-Dell-05 on 11/7/2017.
 */

public class AdapterMainPost extends RecyclerView.Adapter<AdapterMainPost.MyViewHolder> {

    int clickFlag = 0;
    MediaPlayer mediaplayer;
    private Activity context;
    private UiHandleMethods uihandle;
    private Handler handler = new Handler();
    private MainFeedCallback mFeedCallback;
    private List<ModelFeed> mListFeedData;
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    public AdapterMainPost(Activity context, List<ModelFeed> mListFeedData) {
        this.context = context;
        this.uihandle = new UiHandleMethods(context);
        this.handler = new Handler();
        this.mListFeedData = mListFeedData;

        //  float scale = context.getResources().getDisplayMetrics().density;
        // view.setCameraDistance(1280 * scale);

        if (context instanceof MainActivity) {
            mFeedCallback = (MainFeedCallback) context;
        }

        //   mediaplayer = new MediaPlayer();
        //   mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    private static String scramble(Random random, String inputString) {
        // Convert your string into a simple char array:
        char a[] = inputString.toCharArray();
        // Scramble the letters using the standard Fisher-Yates shuffle,
        for (int i = 0; i < a.length; i++) {
            int j = random.nextInt(a.length);
            // Swap letters
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        return new String(a);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_posts, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //        holder.setIsRecyclable(false);
        ModelFeed mData = mListFeedData.get(position);
        final ModelFeedMedia[] mMedia = mData.getMedia();
        final ModelScrambledMedia[] mScrambledMedia = mData.getScrambledMedia();

        holder.mTextName.setText(UiHandleMethods.capitalizeString(mData.getUser_name()));
        holder.mTextPostTime.setText(mData.getPostedDate());
        holder.mTextLockCount.setText(mData.getUnlockInProgress());
        holder.mTextUnlockCount.setText(mData.getUnlocked());

        //     total reactions if not zero
        holder.mTextCommentCount.setText(UiHandleMethods.numberInShortFormat(Long.parseLong(mData.getCommentsCount())) + " Comments");
     /*   if (!mData.getCommentsCount().equals("0")) {
            holder.mTextCommentCount.setText(UiHandleMethods.numberInShortFormat(Long.parseLong(mData.getCommentsCount())) + " Comments");
        } else {
            holder.mTextCommentCount.setText("");
        }*/

        //          total reactions if not zero
        if (!mData.getTotalReactions().equals("0")) {
            holder.mTextTotalReactions.setText(UiHandleMethods.numberInShortFormat(Long.parseLong(mData.getTotalReactions())));
        } else {
            holder.mTextTotalReactions.setText("");
        }

        //         show location if not empty
        if (!mData.getLocation_name().equals("")) {
            holder.mTextLocation.setText(mData.getLocation_name() + ", " + mData.getLocation_city() + ", " + mData.getLocation_state() + ", " + mData.getLocation_country());
            holder.mTextLocation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.loc_16, 0, 0, 0);
        }

        //         set smily on reactions
        switch (mData.getUserReactionType()) {
            case Constants.ConstantPutReactions.REACTION_LIKED:
                holder.removeViews();
                holder.mSmilyLike.setVisibility(View.VISIBLE);
                holder.mSmilyLike.setImageResource(R.mipmap.like_noti);
                break;
            case Constants.ConstantPutReactions.REACTION_LAUGH:
                holder.removeViews();
                holder.mSmilyLaugh.setVisibility(View.VISIBLE);
                holder.mSmilyLaugh.setImageResource(R.mipmap.laugh_24);
                break;
            case Constants.ConstantPutReactions.REACTION_SAD:
                holder.removeViews();
                holder.mSmilySad.setVisibility(View.VISIBLE);
                holder.mSmilySad.setImageResource(R.mipmap.sad_noti);
                break;
            case Constants.ConstantPutReactions.REACTION_LOVE:
                holder.removeViews();
                holder.mSmilyLove.setVisibility(View.VISIBLE);
                holder.mSmilyLove.setImageResource(R.mipmap.heart_noti);
                break;
            case Constants.ConstantPutReactions.REACTION_ANGER:
                holder.removeViews();
                holder.mSmilyAngry.setVisibility(View.VISIBLE);
                holder.mSmilyAngry.setImageResource(R.mipmap.sleep_noti);
                break;
        }

        //            setting values according to specific conditions
        switch (mData.getPostLockStatus()) {
            case POST_LOCK_STATUS_UNLOCKED:
            case POST_LOCK_STATUS_OPEN:
            case POST_LOCK_STATUS_OWNER_NO_LOCK:
                //      set the actual content
                if (!mData.getContent().equals("null")) {
                    holder.mTextPost.setText(mData.getContent());
                }

                //         set the media if available
                if (mMedia != null) {
                    holder.mRelMediaPostFrame.setVisibility(View.VISIBLE);

                    holder.mFrameProgress.setVisibility(View.GONE);
                    holder.mFrameScrambled.setVisibility(View.GONE);
                    holder.mFramePost.setVisibility(View.VISIBLE);

                    switch (mMedia[0].getMediaType()) {
                        case Constants.ConstantMediaType.MEDIA_TYPE_VIDEO:
                            holder.mVideoImagePlayButton.setVisibility(View.VISIBLE);

                            uihandle.loadImageWithGlideBitmap(holder.mVideoImageView, mMedia[0].getThumbnailUrl());

                            holder.mVideoImagePlayButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    StaticValues.mPostId = mListFeedData.get(position).getUuid();
                                    StaticValues.mVideoPathToPlay = mMedia[0].getMediaUrl();
                                    uihandle.goForNextScreen(VideoPlayScreen.class);
                                }
                            });

                            break;

                        case Constants.ConstantMediaType.MEDIA_TYPE_IMAGE:
                            holder.mVideoImagePlayButton.setVisibility(View.GONE);
                            //      uihandle.getImageWithVolley(mMedia[0].getMediaUrl(), holder.mVideoImageView);
                            //      uihandle.setImageWithLazyLoading(holder.mVideoImageView,mMedia[0].getMediaUrl());
                            uihandle.loadImageWithGlideBitmap(holder.mVideoImageView, mMedia[0].getMediaUrl());
                            uihandle.loadImageWithGlideBitmap(holder.mImgFullBack, mMedia[0].getMediaUrl());
                            //    uihandle.getImageWithVolleyScreenWidth(mMedia[0].getMediaUrl(), holder.mVideoImageView);

                            holder.mVideoImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onImageClickDialog(holder.mImgFullBack);
                                }
                            });

                            break;

                        case Constants.ConstantMediaType.MEDIA_TYPE_AUDIO:
                            //    holder.mVideoImagePlayButton.setVisibility(View.GONE);
                            holder.mVideoImagePlayButton.setVisibility(View.VISIBLE);

                            //  uihandle.loadImageWithGlideBitmap(holder.mVideoImageView, mMedia[0].getThumbnailUrl());
                            holder.mVideoImageView.setImageResource(R.drawable.music_);

                            holder.mVideoImagePlayButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    StaticValues.mPostId = mListFeedData.get(position).getUuid();
                                    StaticValues.mVideoPathToPlay = mMedia[0].getMediaUrl();
                                 /*
                                    if (mediaplayer != null) {
                                   //   mediaplayer.stop();
                                        mediaplayer.release();
                                    }

                                    if (mData.isFlagPlayAudio()) {
                                        holder.mVideoImagePlayButton.setImageResource(R.drawable.ic_play_music);
                                        mData.setFlagPlayAudio(false);

                                    } else {
                                        holder.mVideoImagePlayButton.setImageResource(R.drawable.pause);
                                        mData.setFlagPlayAudio(true);
                                        startPlay(mMedia[0].getMediaUrl());
                                    }*/
                                    uihandle.goForNextScreen(VideoPlayScreen.class);
                                }
                            });
                            break;
                    }
                } else {
                    holder.mRelMediaPostFrame.setVisibility(View.GONE);
                  //  holder.mTextPost.setHeight(400);
                //    holder.mTextPost.setGravity(Gravity.CENTER);
                    //  holder.mTextPost.setBackgroundResource(R.drawable.stroke_blue_gradiant);
                }
                break;

            case Constants.ConstantPostLockStatus.POST_LOCK_STATUS_LOCKED:
            case Constants.ConstantPostLockStatus.POST_LOCK_STATUS_UNLOCK_IN_PROGRESS:
                holder.mRelMediaPostFrame.setVisibility(View.VISIBLE);

                holder.mFrameProgress.setVisibility(View.VISIBLE);
                holder.mFrameScrambled.setVisibility(View.GONE);
                holder.mFramePost.setVisibility(View.GONE);

                if (mData.getScrambledMedia() != null) {
                    holder.mImageScrambled.setVisibility(View.VISIBLE);
                    holder.mTextScrambled.setVisibility(View.GONE);

                    //      set the actual content
                    if (!mData.getDefaultScrambledContent().equals("null")) {
                        holder.mTextPost.setText(mData.getDefaultScrambledContent());
                    }

                    //  uihandle.getImageWithVolleyThumbnail(mScrambledMedia[0].getMediaUrl(), holder.mImageScrambled);
                    uihandle.loadGIImage(holder.mImageScrambled, mScrambledMedia[0].getMediaUrl());

                    //           holder.mImageScrambled.setImageURI(Uri.parse(mScrambledMedia[0].getMediaUrl().replaceFirst("s", "").replaceAll(" ", "%20")));

                } else {
                    holder.mTextScrambled.setVisibility(View.VISIBLE);
                    holder.mImageScrambled.setVisibility(View.GONE);
                    holder.mTextScrambled.setText(mData.getDefaultScrambledContent());
                }


                holder.mImgLockPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //        use this to start and trigger a service
                        if (handler != null) {
                            mFeedCallback.unlockPost(holder.text_timer_backend, position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                    holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                            //   scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                            //   holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                        } else {
                            handler = new Handler();
                            mFeedCallback.unlockPost(holder.text_timer_backend, position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                    holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                            //       scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                            //       holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                        }
                        holder.mImgLockPost.setEnabled(false);
                    }
                });
                break;

            case Constants.ConstantPostLockStatus.POST_LOCK_STATUS_OLD_POST_FIRST_TIME_VIEW:

                holder.mRelMediaPostFrame.setVisibility(View.VISIBLE);

                holder.mFrameProgress.setVisibility(View.VISIBLE);
                holder.mFrameScrambled.setVisibility(View.GONE);
                holder.mFramePost.setVisibility(View.GONE);

                if (mData.getScrambledMedia() != null) {
                    holder.mImageScrambled.setVisibility(View.VISIBLE);
                    holder.mTextScrambled.setVisibility(View.GONE);

                    //      set the actual content
                    if (!mData.getDefaultScrambledContent().equals("null")) {
                        holder.mTextPost.setText(mData.getDefaultScrambledContent());
                    }

                    //         uihandle.getImageWithVolley(mScrambledMedia[0].getMediaUrl(), holder.mImageScrambled);
                    uihandle.loadGIImage(holder.mImageScrambled, mScrambledMedia[0].getMediaUrl());

                } else {
                    holder.mTextScrambled.setVisibility(View.VISIBLE);
                    holder.mImageScrambled.setVisibility(View.GONE);

                    holder.mTextScrambled.setText(mData.getDefaultScrambledContent());
                }

                holder.mImgLockPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //         use this to start and trigger a service
                        if (handler != null) {
                            mFeedCallback.unlockPost(holder.text_timer_backend, position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                    holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                            //   scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                            //   holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                        } else {
                            handler = new Handler();
                            mFeedCallback.unlockPost(holder.text_timer_backend, position, mListFeedData.get(position), Integer.parseInt(mListFeedData.get(position).getTimerInSecs()),
                                    holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer);
                            //       scheduleTimer(Integer.parseInt(mData.getTimerInSecs()), holder.mCountDownTimer,
                            //       holder.mProgress, holder.mTextPost, holder.mFrameProgress, holder.mTextUnlockTimer, holder.mImgCover);
                        }
                        holder.mImgLockPost.setEnabled(false);
                    }
                });
                break;
        }

        //    uncomment if https is to used Todo:  uihandle.getImageWithVolley(mData.getUser_profilePic().trim(), holder.mUserImage);
        //    uihandle.setImageWithLazyLoading(holder.mUserImage,mData.getUser_profilePic().trim());

        uihandle.loadImageWithGlideBitmap(holder.mUserImage, mData.getUser_profilePic().trim());


        holder.mLikeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeedCallback.onLikedClick(mListFeedData.get(position), position, holder.mSmilyLike);
            }
        });

        holder.mLikeTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mFeedCallback.onShowEmojis(mListFeedData.get(position), position, holder.mLikeTextView, holder.mSmilyLike, holder.mSmilyLaugh, holder.mSmilySad, holder.mSmilyLove, holder.mSmilyAngry);
                return true;
            }
        });

        holder.mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeedCallback.displayImage(holder.mUserImage);
            }
        });

      /*   holder.mTextShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             uihandle.shareMessage("This is testing , Kofefe App");
            }
        });
        **** */

        // for next page detail setup
        if (position == mListFeedData.size() - 1) {
            mFeedCallback.getMoreOldFeeds(position);
        }

    }

    @Override
    public int getItemCount() {
        return mListFeedData.size();

        // return 40;
    }

    public void replaceItem(final ModelFeed newItem, final int position) {
        mListFeedData.set(position, newItem);
        notifyItemChanged(position);
    }

    private void onImageClickDialog(ImageView mImageView) {
        Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.dilaog_full_screen_image_view);
        ImageView mImage = d.findViewById(R.id.image);
        mImage.setImageBitmap(((MainActivity) context).getBitmapFromView(mImageView));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.show();
    }


    private void startPlay(String AudioURL) {
        try {
            mediaplayer = MediaPlayer.create(context, Uri.parse(AudioURL));
            //   mediaplayer.setDataSource(AudioURL);
            mediaplayer.prepare();

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mediaplayer != null) {
            mediaplayer.start();
        }
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        FlipAnimation flipAnimation;
        int mFlag = 0;

        private RelativeLayout mRelRoot;
        private TextView mLikeTextView;
        private ImageView mSmilyLike, mSmilyLaugh, mSmilySad, mSmilyLove, mSmilyAngry;
        private TextView mTextTotalReactions;
        private SquareRoundCornerImageView mUserImage;
        private ImageView mImgLockUpperPost, mImgUnlockUpperPost;
        private TextView mTextLockCount, mTextUnlockCount;
        private TextView mTextPost, mTextName, mTextPostTime, mTextLocation;
        private LinearLayout mLinearReactions;
        private TextView mTextComment, mTextCommentCount;    //    ,mTextShare;
        private DonutProgress mProgress;
        private ImageView mImgLockPost;
        private TextView mTextUnlockTimer;
        private ImageView mVideoImageView, mImgFullBack;
        private ImageView mVideoImagePlayButton;
        private RelativeLayout mRelMediaPostFrame;
        private CountDownTimer mCountDownTimer;
        private FrameLayout mFramePost;
        private FrameLayout mFrameProgress;
        private FrameLayout mFrameScrambled;
        private GifImageView mImageScrambled;
        private TextView mTextScrambled;
        private TextView text_timer_backend;


        public MyViewHolder(View itemView) {
            super(itemView);


            mImgFullBack = itemView.findViewById(R.id.img_full_back);
            text_timer_backend = itemView.findViewById(R.id.text_timer_backend);
            mFrameScrambled = itemView.findViewById(R.id.frame_scrambled);
            mImageScrambled = itemView.findViewById(R.id.imageView_scrambled);
            mTextScrambled = itemView.findViewById(R.id.txt_scrambled);

            mFramePost = itemView.findViewById(R.id.frame_post);

            mRelMediaPostFrame = itemView.findViewById(R.id.relative);

            mUserImage = itemView.findViewById(R.id.circleImageView);
            mTextName = itemView.findViewById(R.id.txt_name);
            mTextPostTime = itemView.findViewById(R.id.textView);
            mTextLocation = itemView.findViewById(R.id.textView_loacation);

            mImgLockUpperPost = itemView.findViewById(R.id.img_lock);
            mImgUnlockUpperPost = itemView.findViewById(R.id.img_unlock);

            mTextLockCount = itemView.findViewById(R.id.txt_lock_count);
            mTextUnlockCount = itemView.findViewById(R.id.txt_unlock_count);


            mImgLockPost = itemView.findViewById(R.id.img_lockpost);
            mFrameProgress = itemView.findViewById(R.id.progress_unscrambled);

            mTextUnlockTimer = itemView.findViewById(R.id.txt_timer);
            mProgress = itemView.findViewById(R.id.donut_progress);
            mTextPost = itemView.findViewById(R.id.txt_post_txt);

            mVideoImageView = itemView.findViewById(R.id.imageView_video_back);
            mVideoImagePlayButton = itemView.findViewById(R.id.imageView_video_play);


            mRelRoot = itemView.findViewById(R.id.root_);
            mLikeTextView = itemView.findViewById(R.id.textView2);
            //  final PopupReactionView popupView = new PopupReactionView(itemView.getContext());

            mTextComment = itemView.findViewById(R.id.textView234);
            // mTextShare = itemView.findViewById(R.id.textView24);
            mTextCommentCount = itemView.findViewById(R.id.text_comment_count);
            mLinearReactions = itemView.findViewById(R.id.reactions_);

            mSmilyLike = itemView.findViewById(R.id.img_batch_like);
            mSmilyLaugh = itemView.findViewById(R.id.img_batch_laugh);
            mSmilySad = itemView.findViewById(R.id.img_batch_sad);
            mSmilyLove = itemView.findViewById(R.id.img_batch_love);
            mSmilyAngry = itemView.findViewById(R.id.img_batch_angry);
            mTextTotalReactions = itemView.findViewById(R.id.textView22);

            //   setting scrollbar on text to view full text
            mTextScrambled.setMovementMethod(new ScrollingMovementMethod());
            mTextPost.setMovementMethod(new ScrollingMovementMethod());

            mFrameProgress.setOnTouchListener(new OnSwipeTouchListener(context) {
                public void onSwipeRight() {
                    if (!mTextUnlockTimer.getText().toString().trim().equals("Unlock")) {
                        flipCardToRight(flipAnimation, mFrameProgress, mFrameScrambled, mRelMediaPostFrame);
                    }
                }

                public void onSwipeLeft() {
                    if (!mTextUnlockTimer.getText().toString().trim().equals("Unlock")) {
                        flipCardToLeft(flipAnimation, mFrameProgress, mFrameScrambled, mRelMediaPostFrame);
                    }
                }
            });
            mFrameScrambled.setOnTouchListener(new OnSwipeTouchListener(context) {
                public void onSwipeRight() {
                    flipCardToRightt(flipAnimation, mFrameProgress, mFrameScrambled, mRelMediaPostFrame);
                }

                public void onSwipeLeft() {
                    flipCardToLeftt(flipAnimation, mFrameProgress, mFrameScrambled, mRelMediaPostFrame);
                }

            });

            mTextPost.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            //   Disallow ScrollView to intercept touch events.
                            //      .requestDisallowInterceptTouchEvent(true);
                            //   Disable touch on transparent view
                            return false;
                        case MotionEvent.ACTION_UP:
                            //   Allow ScrollView to intercept touch events.
                            //       mScrollView.requestDisallowInterceptTouchEvent(false);
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            //       mScrollView.requestDisallowInterceptTouchEvent(true);
                            return false;

                        default:
                            return true;
                    }
                }
            });


            mLinearReactions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   StaticValues.mPostIdForReactionsDisplay ="34ce1c73-345b-4335-9ebc-8c70e78bcfea"; //     mData.getId();
                    StaticValues.mPostIdForReactionsDisplay = mListFeedData.get(getPosition()).getUuid(); //     mData.getId();

                    StaticValues.ReactionsCount.mLikeCount = mListFeedData.get(getPosition()).getLiked();
                    StaticValues.ReactionsCount.mLaughCount = mListFeedData.get(getPosition()).getLaugh();
                    StaticValues.ReactionsCount.mSadCount = mListFeedData.get(getPosition()).getSad();
                    StaticValues.ReactionsCount.mLoveCount = mListFeedData.get(getPosition()).getLove();
                    StaticValues.ReactionsCount.mAngryCount = mListFeedData.get(getPosition()).getAnger();

                    uihandle.goForNextScreen(ReactionsActivity.class);
                }
            });

            mTextName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticValues.mSearechedUserUUID = mListFeedData.get(getPosition()).getUser_uuid();
                    mFeedCallback.getFeedInfo(getPosition());
                }
            });

            mImgLockUpperPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticValues.mLockUnlockSide = 0;
                    mFeedCallback.getPostLockActivityList(mListFeedData.get(getPosition()), getPosition());
                }
            });

            mImgUnlockUpperPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticValues.mLockUnlockSide = 1;
                    mFeedCallback.getPostLockActivityList(mListFeedData.get(getPosition()), getPosition());
                }
            });


            mTextLockCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticValues.mLockUnlockSide = 0;
                    mFeedCallback.getPostLockActivityList(mListFeedData.get(getPosition()), getPosition());
                }
            });

            mTextUnlockCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticValues.mLockUnlockSide = 1;
                    mFeedCallback.getPostLockActivityList(mListFeedData.get(getPosition()), getPosition());
                }
            });


            mTextComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StaticValues.mPostId = mListFeedData.get(getPosition()).getUuid();
                    StaticValues.mPostComment = mListFeedData.get(getPosition()).getCommentsCount();
                    mFeedCallback.getModelFeed(getPosition());
                }
            });

            mTextCommentCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticValues.mPostId = mListFeedData.get(getPosition()).getUuid();
                    StaticValues.mPostComment = mListFeedData.get(getPosition()).getCommentsCount();
                    mFeedCallback.getModelFeed(getPosition());

                }
            });


        }

        private void flipCardToLeft(FlipAnimation flipANimation, FrameLayout cardFace, FrameLayout mFramePost, RelativeLayout mRootLayout) {
            flipANimation = new FlipAnimation(cardFace, mFramePost);
            mRootLayout.startAnimation(flipANimation);
        }

        private void flipCardToLeftt(FlipAnimation flipANimation, FrameLayout cardFace, FrameLayout mFramePost, RelativeLayout mRootLayout) {
            flipANimation = new FlipAnimation(mFramePost, cardFace);
            mRootLayout.startAnimation(flipANimation);
        }

        private void flipCardToRight(FlipAnimation flipANimation, FrameLayout cardFace, FrameLayout mFramePost, RelativeLayout mRootLayout) {
            flipANimation = new FlipAnimation(mFramePost, cardFace);
            flipANimation.reverse();
            mRootLayout.startAnimation(flipANimation);
        }

        private void flipCardToRightt(FlipAnimation flipANimation, FrameLayout cardFace, FrameLayout mFramePost, RelativeLayout mRootLayout) {
            flipANimation = new FlipAnimation(cardFace, mFramePost);
            flipANimation.reverse();
            mRootLayout.startAnimation(flipANimation);
        }

        private void removeViews() {
            mSmilyLike.setVisibility(View.GONE);
            mSmilyLaugh.setVisibility(View.GONE);
            mSmilySad.setVisibility(View.GONE);
            mSmilyLove.setVisibility(View.GONE);
            mSmilyAngry.setVisibility(View.GONE);
        }


    }

    private class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bitmap = null;
        GifImageView mImageView;

        public DownloadTask(GifImageView mImageView) {
            this.mImageView = mImageView;

        }

        @Override
        protected Bitmap doInBackground(String... url) {
            try {
                bitmap = uihandle.downloadImageUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            /** Getting a reference to ImageView to display the
             * downloaded image
             */
            //     mImageView.setBytes(result);
            /***
             *  Displaying the downloaded image
             *  ***/
            //  mImageView.setImageURI();
            //    mImageView.setImageBitmap(result);
            // mImageView.startAnimation();

            /** Showing a message, on completion of download process */
            // Toast.makeText(context, "Image downloaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
