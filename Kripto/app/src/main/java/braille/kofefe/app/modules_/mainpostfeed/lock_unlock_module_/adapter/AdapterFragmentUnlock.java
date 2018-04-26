package braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.common_util_.StaticValues;
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.PostLockActivityList;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.callback.IPostLockActivityListCallback;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.model.ModelPostActivityList;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.supports_.UiHandleMethods;

/**
 * Created by Snow-Dell-05 on 12/6/2017.
 */

public class AdapterFragmentUnlock extends RecyclerView.Adapter<AdapterFragmentUnlock.MyViewHolder> {

    private Context context;
    private UiHandleMethods uihandle;
    private List<ModelPostActivityList> mListUnlocked;
    private IPostLockActivityListCallback mCallback;

    public AdapterFragmentUnlock(Activity context, List<ModelPostActivityList> mListUnlocked) {
        this.context = context;
        this.uihandle = new UiHandleMethods(context);
        this.mListUnlocked = mListUnlocked;
        if (context instanceof PostLockActivityList) {
            mCallback = (IPostLockActivityListCallback) context;

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_unlock, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ModelPostActivityList mData = mListUnlocked.get(position);

        /***
         * * setting values to views
         * ***/
        holder.mTextName.setText(mData.getName());
        holder.mTextTimeAgo.setText(mData.getActivityDate());

        /***
         * switch case for the relationship
         * **/
        switch (mData.getRelationship()) {
            case ConstantStatusInAPI.RELATIONSHIP_SELF:
                holder.mFollowBtn.setEnabled(false);
                holder.mFollowBtn.setText("    You    ");
                break;
            case ConstantStatusInAPI.RELATIONSHIP_NONE:
                holder.mFollowBtn.setText("    Follow    ");
                holder.mFollowBtn.setEnabled(true);

                break;
            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER:
                holder.mFollowBtn.setText("    Follow    ");
                holder.mFollowBtn.setEnabled(true);
                break;
            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWED:
                holder.mFollowBtn.setEnabled(true);
                holder.mFollowBtn.setText("   Following   ");

                break;
            case ConstantStatusInAPI.RELATIONSHIP_FOLLOWER_AND_FOLLOWED:
                holder.mFollowBtn.setEnabled(true);
                holder.mFollowBtn.setText("   Following   ");
                break;
        }


        uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);


        holder.mFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onRelationshipClickUnlocked(mListUnlocked.get(position).getRelationship(), mListUnlocked.get(position), holder.mFollowBtn);
            }
        });

    }

    private void onImageClickDialog(ImageView mImageView) {
        Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.dilaog_full_screen_image_view);
        ImageView mImage = d.findViewById(R.id.image);
        mImage.setImageBitmap(((PostLockActivityList) context).getBitmapFromView(mImageView));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.show();
    }

    @Override
    public int getItemCount() {
        return mListUnlocked.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private SquareRoundCornerImageView mImgUser;
        private TextView mTextName, mTextTimeAgo, mFollowBtn;
        private RelativeLayout mRootLayout;

        public MyViewHolder(View itemView) {
            super(itemView);


            mImgUser = itemView.findViewById(R.id.circleImageView2);
            mTextTimeAgo = itemView.findViewById(R.id.textView7);

            mTextName = itemView.findViewById(R.id.txt_name);
            mFollowBtn = itemView.findViewById(R.id.btn_follow);

            mRootLayout = itemView.findViewById(R.id.root_);
            mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StaticValues.mSearechedUserUUID = mListUnlocked.get(getPosition()).getUuid();
                    uihandle.goForNextScreen(ProfileActivity.class);

                } });

            mImgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickDialog(mImgUser);
                }
            });

        }
    }
}

