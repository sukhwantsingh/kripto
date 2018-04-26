package braille.kofefe.app.modules_.notification;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
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
import braille.kofefe.app.modules_.notification.callback.INotificationMarked;
import braille.kofefe.app.modules_.notification.model.ModelNotification;
import braille.kofefe.app.supports_.UiHandleMethods;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Snow-Dell-05 on 11/7/2017.
 */

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.MyViewHolder> {

    private Activity context;
    private boolean mFlagCheck = false;
    private UiHandleMethods uihandle;
    private List<ModelNotification> mListData;
    private INotificationMarked mCallback;

    public AdapterNotifications(Activity context, List<ModelNotification> mListData) {
        this.context = context;
        this.mListData = mListData;
        uihandle = new UiHandleMethods(context);

        if (context instanceof NotificationActivity) {
            mCallback = (INotificationMarked) context;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_notifications, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                ModelNotification mData = mListData.get(position);
                //    holder.mTextDescription.setText(mData.getMessage());
                holder.mTextReactedNTime.setText(mData.getNotifiedDate());
                holder.mTextName.setText(mData.getMessage());

                if (mData.getViewed()) {
                    holder.mTextName.setTypeface(null, Typeface.NORMAL);
                } else {
                    holder.mTextName.setTypeface(null, Typeface.BOLD);
                }

                uihandle.getImageWithVolley(mData.getBodyImage(), holder.mImgReacted);
                //   uihandle.getImageCircleWithVolley(mData.getTitleImage(), holder.mImageTitle);


            }
        });
    }


    @Override
    public int getItemCount() {
        return mListData.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private View mViewLine;
        private TextView mTextHeaderText;
        private TextView mTextName, mTextReactedNTime, mTextDescription;
        private SquareRoundCornerImageView mImgReacted;
        private CircleImageView mImageTitle;
        private RelativeLayout mRelativeRoot;

        public MyViewHolder(View itemView) {
            super(itemView);

            mRelativeRoot = itemView.findViewById(R.id.rel_content);
            mImageTitle = itemView.findViewById(R.id.img_reacted_emoji);
            mImgReacted = itemView.findViewById(R.id.img_user_on_reacted);
            mTextName = itemView.findViewById(R.id.txt_name);
            mTextReactedNTime = itemView.findViewById(R.id.textView_reacted_n_time);
            mTextDescription = itemView.findViewById(R.id.text_decription);
            mViewLine = itemView.findViewById(R.id.view_e_2);

            mRelativeRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.getUuidForPost(mListData.get(getPosition()), mTextName, mListData.get(getPosition()).getNotificationType(),
                            mListData.get(getPosition()).getNotificationId(), mListData.get(getPosition()).getUuid());

                }
            });
            mImgReacted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickDialog(mImgReacted);
                }
            });

        }

        private void onImageClickDialog(ImageView mImageView) {
            Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            d.setContentView(R.layout.dilaog_full_screen_image_view);
            ImageView mImage = d.findViewById(R.id.image);
            mImage.setImageBitmap(((NotificationActivity) context).getBitmapFromView(mImageView));
            d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            d.show();
        }
    }
}
