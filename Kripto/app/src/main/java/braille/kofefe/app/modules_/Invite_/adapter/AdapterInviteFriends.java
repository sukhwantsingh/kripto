package braille.kofefe.app.modules_.Invite_.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.Invite_.InviteFriendsScreen;
import braille.kofefe.app.modules_.Invite_.callback.IInviteCallback;
import braille.kofefe.app.modules_.Invite_.model.ModelInviteFriends;
import braille.kofefe.app.modules_.common_util_.ModelContacts;
import braille.kofefe.app.supports_.UiHandleMethods;

/**
 * Created by Snow-Dell-05 on 15-Jan-18.
 */

public class AdapterInviteFriends extends RecyclerView.Adapter<AdapterInviteFriends.MyViewHolder> {

    List<ModelContacts> mListContacts;
    private Context context;
    private UiHandleMethods uihandle;
    private List<ModelInviteFriends> mListNumbers;
    private IInviteCallback mCallback;

    public AdapterInviteFriends(Activity context, List<ModelInviteFriends> mListNumbers, List<ModelContacts> mListContacts) {

        this.context = context;

        this.uihandle = new UiHandleMethods(context);
        this.mListNumbers = mListNumbers;
        this.mListContacts = mListContacts;

        if (context instanceof InviteFriendsScreen) {
            mCallback = (IInviteCallback) context;
        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_contacts_invite_, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ModelInviteFriends mData = mListNumbers.get(position);

        /* ***
         * *  setting values to views
         * ***/
        //    holder.mTextName.setText(UiHandleMethods.capitalizeString(getName(mData.getmNumber())));

        for (int j = 0; j < mListContacts.size(); j++) {
            if ((mData.getmNumber().trim()).contains(mListContacts.get(j).getmPhoneNumber().trim())) {
                holder.mTextName.setText(mListContacts.get(j).getmName());
                holder.mTextNum.setText(mListContacts.get(j).getmPhoneNumber());
                Log.e("hereIn", mListContacts.get(j).getmName());

            } else {
                Log.e("here", mListContacts.get(j).getmName());
            }
        }

        //  holder.mTextName.setText(mData.getmNumber());
        //    uihandle.getImageWithVolley(mData.getProfilePic().trim(), holder.mImgUser);

        holder.mInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.fireInviteFriend(mData, holder.mTextNum.getText().toString().trim());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListNumbers.size();
    }

    public String getName(String numb) {
        for (int i = 0; i < mListContacts.size(); i++) {
            if ((mListContacts.get(i).getmPhoneNumber().trim()).contains(numb.trim())) {
                return mListContacts.get(i).getmName();
            }

        }
        return null;

    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextName, mInviteBtn, mTextNum;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTextNum = itemView.findViewById(R.id.txt_num);
            mTextName = itemView.findViewById(R.id.txt_name);
            mInviteBtn = itemView.findViewById(R.id.btn_follow);
        }
    }


}


