package braille.kofefe.app.modules_.Invite_.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.AddFriendsScreen;
import braille.kofefe.app.modules_.Invite_.callback.IAddFriendsCallback;
import braille.kofefe.app.modules_.Invite_.model.ModelAddFriends;
import braille.kofefe.app.modules_.common_util_.SquareRoundCornerImageView;
import braille.kofefe.app.supports_.UiHandleMethods;

/**
 * Created by Snow-Dell-05 on 15-Jan-18.
 */

public class AdapterAddFriends extends RecyclerView.Adapter<AdapterAddFriends.MyViewHolder> {

    private Context context;
    private UiHandleMethods uihandle;
    private List<ModelAddFriends> mListAddFriends;

    private IAddFriendsCallback mCallback;

    public AdapterAddFriends(Activity context, List<ModelAddFriends> mListAddFriends) {

        this.context = context;
        this.uihandle = new UiHandleMethods(context);
        this.mListAddFriends = mListAddFriends;

        if (context instanceof AddFriendsScreen) {
            mCallback = (IAddFriendsCallback) context;
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_contacts_add_friends, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ModelAddFriends mData = mListAddFriends.get(position);

        /***
         **  setting values to views
         ***/
        holder.mTextName.setText(UiHandleMethods.capitalizeString(mData.getName()));
        holder.mTextUsername.setText(mData.getUserName());
        holder.mTextJoined.setText("Joined " + mData.getJoined());

        uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

        //in some cases, it will prevent unwanted situations
        holder.mCheckRegisteredUser.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.mCheckRegisteredUser.setChecked(mData.isSelected());


        holder.mCheckRegisteredUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                mData.setSelected(isChecked);
                mCallback.getUncheckedUsers(isChecked, position, mData.getUuid());
            }
        });


        holder.mLinearLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    StaticValues.mSearechedUserUUID = mData.getUuid();
                //   uihandle.goForNext(ProfileActivity.class);
                uihandle.showToast("position: " + position);
            }
        });
        holder.mImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickDialog(holder.mImgUser);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListAddFriends.size();
    }

    private void onImageClickDialog(ImageView mImageView) {
        Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.dilaog_full_screen_image_view);
        ImageView mImage = d.findViewById(R.id.image);
        mImage.setImageBitmap(((AddFriendsScreen) context).getBitmapFromView(mImageView));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.show();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private SquareRoundCornerImageView mImgUser;
        private TextView mTextName, mTextUsername, mTextJoined;
        private LinearLayout mLinearLay;
        private CheckBox mCheckRegisteredUser;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImgUser = itemView.findViewById(R.id.circleImageView2);
            mTextName = itemView.findViewById(R.id.txt_name);
            mTextUsername = itemView.findViewById(R.id.txt_username);
            mTextJoined = itemView.findViewById(R.id.txt_created);
            mLinearLay = itemView.findViewById(R.id.rel_users_lay);
            mCheckRegisteredUser = itemView.findViewById(R.id.checkBox_selection);


        }


    }
}

