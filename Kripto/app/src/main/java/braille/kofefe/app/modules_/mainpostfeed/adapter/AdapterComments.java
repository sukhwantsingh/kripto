package braille.kofefe.app.modules_.mainpostfeed.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.modules_.mainpostfeed.CommentScreen;
import braille.kofefe.app.modules_.mainpostfeed.callback_.ICommentCallback;
import braille.kofefe.app.modules_.mainpostfeed.model.ModelComments;
import braille.kofefe.app.supports_.UiHandleMethods;

/**
 * Created by Snow-Dell-05 on 12/1/2017.
 **/

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder> {

    private Activity context;
    private UiHandleMethods uihandle;
    private List<ModelComments> mListComments;
    private ICommentCallback mCallback;

    public AdapterComments(Activity context, List<ModelComments> mListComments) {
        this.context = context;
        this.uihandle = new UiHandleMethods(context);
        this.mListComments = mListComments;

        if (context instanceof CommentScreen) {
            mCallback = (ICommentCallback) context;
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_comments, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ModelComments mData = mListComments.get(position);

        holder.mTextName.setText(mData.getName());
        holder.mTextComment.setText(mData.getComment_());
        holder.mTextCommentTime.setText(mData.getCommented_date());

        if (!mData.getMedia_url().equals("")) {
            holder.mImgMedia.setVisibility(View.VISIBLE);
            uihandle.getImageWithVolley(mData.getMedia_url(), holder.mImgMedia);
        } else {
            holder.mImgMedia.setVisibility(View.GONE);
        }

        uihandle.getImageWithVolley(mData.getProfilePic(), holder.mImgUser);


        if (position == mListComments.size() - 1) {
            mCallback.getCommentsAfter(position, mData);
        }

    }


    public void removeItem(int position) {

        mListComments.remove(position);
        notifyItemRemoved(position);
        //    notifyItemRangeChanged(position, mListComments.size());
       /* if (position == 0) {
            mContext.finish();
            mContext.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }*/
        // notifyItemRangeChanged(position, feedItemList.size());
    }

    public void addItem(ModelComments data, RecyclerView mRecyclerView) {
        mListComments.add(0, data);
        notifyItemInserted(0);
       // mRecyclerView.scrollToPosition(mListComments.size() - 1);

    }

    public void replaceItem(final ModelComments newItem, final int position) {
        mListComments.set(position, newItem);
        notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return mListComments.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private SquareRoundCornerImageView mImgUser;
        private TextView mTextName, mTextComment, mTextCommentTime;
        private SquareRoundCornerImageView mImgMedia;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImgMedia = itemView.findViewById(R.id.imageView4);
            mTextCommentTime = itemView.findViewById(R.id.textView8);
            mTextComment = itemView.findViewById(R.id.textView7);
            mTextName = itemView.findViewById(R.id.txt_name);
            mImgUser = itemView.findViewById(R.id.circleImageView2);

            uihandle.changeColorToAppGradient(mTextCommentTime);


            mImgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickDialog(mImgUser);
                }
            });
            mImgMedia.setOnClickListener(new View.OnClickListener() {
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
            mImage.setImageBitmap(((CommentScreen) context).getBitmapFromView(mImageView));
            d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            d.show();
        }
    }
}


