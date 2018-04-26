package braille.kofefe.app.modules_.reactions.adapter;

import android.app.Activity;
import android.app.Dialog;
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
import braille.kofefe.app.modules_.mainpostfeed.constants_status_in_api.ConstantStatusInAPI;
import braille.kofefe.app.modules_.reactions.ReactionsActivity;
import braille.kofefe.app.modules_.reactions.callback.IReactionCallback;
import braille.kofefe.app.modules_.reactions.model.ModelReactionCommon;
import braille.kofefe.app.supports_.UiHandleMethods;

/**
 * Created by Snow-Dell-05 on 16-Jan-18.
 */

public class AdapterLove extends RecyclerView.Adapter<AdapterLove.MyViewHolder> {

    private Activity context;
    private UiHandleMethods uihandle;
    private List<ModelReactionCommon> mListLiked;
    private IReactionCallback mCallback;
    public AdapterLove(Activity context, List<ModelReactionCommon> mListLiked) {
        this.context = context;
        this.uihandle = new UiHandleMethods(context);
        this.mListLiked = mListLiked;
        if (context instanceof ReactionsActivity) {
            mCallback = (IReactionCallback) context;
        }

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_reactions, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         ModelReactionCommon mData = mListLiked.get(position);

        /***
         **  setting values to views
         ***/
        //       holder.mTextName.setText(UiHandleMethods.capitalizeString(mData.getName()));
        //       uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

        holder.mTextName.setText(mData.getName() + " (" + mData.getUserName() + ")");
        holder.mTextReactedTime.setText(mData.getReacted_date() );
        //      Todo: change dynamic values

        uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

        /***
         * switch case for the relationship
         * ***/
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

        holder.mFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFollowerBtnClick("Lo",position, mListLiked.get(position), holder.mFollowBtn);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mListLiked.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private SquareRoundCornerImageView mImgUser;
        private TextView mTextName, mTextReactedTime, mFollowBtn;


        private RelativeLayout mRootLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            mRootLayout = itemView.findViewById(R.id.id_root_layout);
            mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.goForUserProfile(mListLiked.get(getPosition()).getUuid());
                }
            });

            mImgUser = itemView.findViewById(R.id.circleImageView2);

            mTextReactedTime = itemView.findViewById(R.id.textView7);

            mTextName = itemView.findViewById(R.id.txt_name);

            mFollowBtn = itemView.findViewById(R.id.btn_follow);

            mImgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickDialog(mImgUser);
                }
            });

        }

        private void onImageClickDialog(ImageView mImageView) {
            Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            d.setContentView(R.layout.dilaog_full_screen_image_view);
            ImageView mImage = d.findViewById(R.id.image);
            mImage.setImageBitmap(((ReactionsActivity) context).getBitmapFromView(mImageView));
            d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            d.show();
        }
    }
}



