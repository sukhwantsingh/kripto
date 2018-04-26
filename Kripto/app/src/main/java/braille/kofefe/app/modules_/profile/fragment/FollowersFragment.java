package braille.kofefe.app.modules_.profile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.modules_.profile.callback_.IFollowersFollowingCallback;

/**
 * Created by Snow-Dell-07 on 07-Nov-17.
 */

public class FollowersFragment extends Fragment {
    private RecyclerView mRecyclerPosts;
    private Activity mContext;
    private IFollowersFollowingCallback mCallbackGetFollowing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_followers, container, false);

        init(v);
        return v;
    }

    private void init(View v) {
        mRecyclerPosts = v.findViewById(R.id.recycle_followers);
        mRecyclerPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCallbackGetFollowing.getFollowers(mRecyclerPosts,v);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;

        if (mContext instanceof ProfileActivity) {
            mCallbackGetFollowing = (IFollowersFollowingCallback) mContext;
        }

    }
}
