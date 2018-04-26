package braille.kofefe.app.modules_.profile.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.modules_.profile.callback_.IFollowersFollowingCallback;
import braille.kofefe.app.modules_.profile.model.ModelFollowers;
import braille.kofefe.app.supports_.SessionKofefeApp;
import braille.kofefe.app.supports_.UiHandleMethods;

/**
 * Created by Snow-Dell-05 on 12/4/2017.
 */

public class AdapterFollowers extends RecyclerView.Adapter<AdapterFollowers.MyViewHolder> {

    private Context context;
    private UiHandleMethods uihandle;
    private List<ModelFollowers> mListFollower;
    private IFollowersFollowingCallback mCallback;

    public AdapterFollowers(Activity context, List<ModelFollowers> mListFollower) {
        this.context = context;
        this.uihandle = new UiHandleMethods(context);
        this.mListFollower = mListFollower;

        if (context instanceof ProfileActivity) {
            mCallback = (IFollowersFollowingCallback) context;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_followers, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         ModelFollowers mData = mListFollower.get(position);
        /***
         * *  setting values to views
         * ***/
        //   holder.mTextName.setText(UiHandleMethods.capitalizeString(mData.getName()));
        //   uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

        holder.mTextName.setText(mData.getName());
        // holder.mTextuserFollow.setText("@" + mData.getUserName() + " Follows you");
        holder.mTextUsername.setText("Joined " + mData.getJoined());
        //      Todo: change dynamic values
        //      holder.mTextUserBio.setText(mData.getBio());
        holder.mTextUserBio.setText(mData.getLoc_city() + ", " + mData.getLoc_state() + ", " + mData.getLoc_country());
        holder.mTextJoinedInfo.setText(mData.getFollowersCount() + " Followers  \u273b  " + mData.getFollowingCount() + " Following");

        uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

        /***
         * switch case for the relationship
         * ***/
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
                holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));

                if ((new SessionKofefeApp(context).getAuthorizationsetUUID()).equals(StaticValues.mUuidForFollowers)) {
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                } else {
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName() + "<font  color='#00c6fb'> Follows you</font>"));
                }

     //         holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName() + "<font  color='#00c6fb'> Follows you</font>"));
                holder.mFollowBtn.setEnabled(true);
                break;

            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWED:
                holder.mFollowBtn.setEnabled(true);
                holder.mFollowBtn.setText("   Following   ");
                holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                break;
            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER_AND_FOLLOWED:
                holder.mFollowBtn.setEnabled(true);

                if ((new SessionKofefeApp(context).getAuthorizationsetUUID()).equals(StaticValues.mUuidForFollowers)) {
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                } else {
                    holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName() + "<font  color='#00c6fb'> Follows you</font>"));
                }

                //     holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName() + "<font  color='#00c6fb'> Follows you</font>"));

                holder.mFollowBtn.setText("   Following   ");
                break;
            default:
                holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName()));
                break;
        }

        holder.mFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   mCallbackRelationClicked.onRelationshipClick(mData, holder.mFollowBtn);
                mCallback.onFollowerBtnClick(position, mListFollower.get(position), holder.mFollowBtn);
            }
        });

        holder.mLinearlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.onFollowerUserClick(position, mListFollower.get(position));
            }
        });
        holder.mImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickDialog(holder.mImgUser);
            }
        });


         /*** * ***/
        if (position == mListFollower.size() - 1) {
            mCallback.getFollowersAfter(position, mData);
        }

    }

    @Override
    public int getItemCount() {
        return mListFollower.size();
    }

    private void onImageClickDialog(ImageView mImageView) {
        Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.dilaog_full_screen_image_view);
        ImageView mImage = d.findViewById(R.id.image);
        mImage.setImageBitmap(((ProfileActivity) context).getBitmapFromView(mImageView));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.show();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private SquareRoundCornerImageView mImgUser;
        private TextView mTextName, mTextUsername, mTextUserBio, mTextJoinedInfo, mFollowBtn;
        private TextView mTextuserFollow;
        private LinearLayout mLinearlay;


        public MyViewHolder(View itemView) {
            super(itemView);


            mImgUser = itemView.findViewById(R.id.circleImageView2);

            mTextuserFollow = itemView.findViewById(R.id.txt_username);

            mTextUsername = itemView.findViewById(R.id.txt_joined);
            mTextUserBio = itemView.findViewById(R.id.txt_userProfesion);
            mTextJoinedInfo = itemView.findViewById(R.id.txt_address);
            mTextName = itemView.findViewById(R.id.txt_name);
            mFollowBtn = itemView.findViewById(R.id.btn_follow);

            mLinearlay = itemView.findViewById(R.id.linear_users_lay);
        }
    }


}

