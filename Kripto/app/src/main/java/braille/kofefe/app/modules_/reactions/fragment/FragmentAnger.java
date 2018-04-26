package braille.kofefe.app.modules_.reactions.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import braille.kofefe.app.R;
import braille.kofefe.app.modules_.reactions.ReactionsActivity;
import braille.kofefe.app.modules_.reactions.callback.IReactionCallback;

/**
 * Created by Snow-Dell-05 on 11/8/2017.
 */

public class FragmentAnger extends Fragment {

    private RecyclerView mRecyclerPosts;
    private IReactionCallback mCallback;

    public FragmentAnger() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    //           Inflate the layout for this fragment
     View v = inflater.inflate(R.layout.fragment_wink, container, false);

        mRecyclerPosts = v.findViewById(R.id.rv_reactions);
        mRecyclerPosts.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCallback.startLoadingAnger(mRecyclerPosts);

        return v;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ReactionsActivity) {
            mCallback = (IReactionCallback) activity;
        }
    }
}
