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
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.modules_.profile.callback_.IFollowersFollowingCallback;
import braille.kofefe.app.modules_.profile.model.ModelFollowing;
import braille.kofefe.app.supports_.UiHandleMethods;


/**
 * Created by Snow-Dell-05 on 12/4/2017.
 */

public class AdapterFollowings extends RecyclerView.Adapter<AdapterFollowings.MyViewHolder> {

    private Context context;
    private UiHandleMethods uihandle;
    private List<ModelFollowing> mListFollowings;

    private IFollowersFollowingCallback mCallback;

    public AdapterFollowings(Activity context, List<ModelFollowing> mListFollowings) {
        this.context = context;
        this.uihandle = new UiHandleMethods(context);
        this.mListFollowings = mListFollowings;

        if (context instanceof ProfileActivity) {
            mCallback = (IFollowersFollowingCallback) context;
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_following, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         ModelFollowing mData = mListFollowings.get(position);

        /***
         * * setting values to views...
         * ***/
        //     holder.mTextName.setText(UiHandleMethods.capitalizeString(mData.getName()));
        //   uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

        holder.mTextName.setText(mData.getName());
        holder.mTextuserFollow.setText(mData.getUserName());
        holder.mTextUsername.setText("Joined " + mData.getJoined());
        //      Todo: change dynamic values
        //      holder.mTextUserBio.setText(mData.getBio());
        holder.mTextUserBio.setText(mData.getLoc_city() + ", " + mData.getLoc_state() + ", " + mData.getLoc_country());
        holder.mTextJoinedInfo.setText(mData.getFollowersCount() + " Followers  \u273b  " + mData.getFollowingCount() + " Following");

        uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);


        /***
         * switch case for the relationship...
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
                holder.mTextuserFollow.setText(Html.fromHtml("@" + mData.getUserName() + "<font  color='#00c6fb'> Follows you</font>"));
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
                mCallback.onFollowingBtnClick(position, mListFollowings.get(position), holder.mFollowBtn);
            }
        });

        holder.mLinearlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFollowingUserClick(position, mListFollowings.get(position));
            }
        });
        holder.mImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickDialog(holder.mImgUser);
            }
        });

        /***
         * go for next 50 results in the folowings
         * * ***/
        if (position == mListFollowings.size() - 1) {
            mCallback.getFollowingsAfter(position, mData);
        }

    }


    @Override
    public int getItemCount() {
        return mListFollowings.size();
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
        private LinearLayout mLinearlay;
        private TextView mTextuserFollow;

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

