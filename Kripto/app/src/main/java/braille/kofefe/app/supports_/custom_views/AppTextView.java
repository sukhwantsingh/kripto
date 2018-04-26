package braille.kofefe.app.supports_.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by matrix on 10/5/16.
 */
public class AppTextView extends AppCompatTextView {

    public AppTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway_Regular.ttf");
        setTypeface(tf);
    }

}
