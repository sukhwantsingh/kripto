package braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.PostLockActivityList;
import braille.kofefe.app.modules_.mainpostfeed.lock_unlock_module_.callback.IPostLockActivityListCallback;


/**
 * Created by Snow-Dell-05 on 12/1/2017.
 */

public class FragmentUnlock extends Fragment {

    private RecyclerView mRecyclerPosts;
    private IPostLockActivityListCallback mCallback;

    public FragmentUnlock() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_unlock, container, false);
        // Inflate the layout for this fragment
        mRecyclerPosts = (RecyclerView) v.findViewById(R.id.rv_reactions);
        mRecyclerPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCallback.getUnlockedUsers(mRecyclerPosts);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof PostLockActivityList) {
            mCallback = (IPostLockActivityListCallback) activity;

        }


    }
}

