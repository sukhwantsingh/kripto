package braille.kofefe.app.modules_.profile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.common_util_.LinearSmoothScrollingCustom;
import braille.kofefe.app.modules_.profile.ProfileActivity;
import braille.kofefe.app.modules_.profile.adapter.AdapterPost;
import braille.kofefe.app.modules_.profile.callback_.IFollowersFollowingCallback;
import braille.kofefe.app.modules_.profile.model.ModelRecentPosts;

/**
 * Created by Snow-Dell-07 on 07-Nov-17.
 */

public class PostsFragment extends Fragment {
    IFollowersFollowingCallback mCallback;
    private RecyclerView mRecyclerPosts;
    private Activity mContext = null;
    private List<ModelRecentPosts> mListData;
    private AdapterPost mAdapterPost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_items_tabs, container, false);
      /*  Bundle bundle = this.getArguments();

        if (bundle != null) {
            mListData = bundle.getParcelableArrayList("mRecentPosts");
        }*/

        /***
         * init views
         ***/

        initViews(v);

        return v;
    }

    private void initViews(View v) {
        mRecyclerPosts = v.findViewById(R.id.lst_profile);
        mRecyclerPosts.setLayoutManager(new LinearSmoothScrollingCustom(mContext));
        mCallback.getRecentPosts(mRecyclerPosts,v);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mContext == null) {
            mContext = activity;

        }
        if (activity instanceof ProfileActivity) {
            mCallback = (IFollowersFollowingCallback) activity;
        }

    }
}
