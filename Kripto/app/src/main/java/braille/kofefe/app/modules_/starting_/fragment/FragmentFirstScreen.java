package braille.kofefe.app.modules_.starting_.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import braille.kofefe.app.R;
import braille.kofefe.app.supports_.UiHandleMethods;


/**
 * Created by Snow-Dell-05 on 11/6/2017.
 */

public class FragmentFirstScreen extends Fragment {

    private View mView = null;
    private UiHandleMethods uihandle;


    private TextView mTextKofefe;
    private Activity mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uihandle = new UiHandleMethods(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_first_screen, container, false);
        }

        init(mView);
        return mView;
    }

    private void init(View v) {
        mTextKofefe = v.findViewById(R.id.textView_app_name);
        //  int[] color = {Color.DKGRAY,Color.CYAN};
        uihandle.changeColorToAppGradient(mTextKofefe);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
}

