package braille.kofefe.app.supports_;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by matrix on 1/7/16.
 */
public class ScrollText extends ScrollingMovementMethod {


    @Override
    protected boolean home(TextView widget, Spannable buffer) {
        return top(widget, buffer);
    }

    @Override
    protected boolean end(TextView widget, Spannable buffer) {
        return bottom(widget, buffer);
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        return Touch.onTouchEvent(widget, buffer, event);
    }

    @Override
    public void onTakeFocus(TextView widget, Spannable text, int dir) {
        Layout layout = widget.getLayout();

        if (layout != null && (dir & View.FOCUS_FORWARD) != 0) {
            widget.scrollTo(widget.getScrollX(),
                    layout.getLineTop(0));
        }
        if (layout != null && (dir & View.FOCUS_BACKWARD) != 0) {
            int padding = widget.getTotalPaddingTop() +
                    widget.getTotalPaddingBottom();
            int line = layout.getLineCount() - 1;

            widget.scrollTo(widget.getScrollX(),
                    layout.getLineTop(line+1) -
                            (widget.getHeight() - padding));
        }
    }

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new ScrollingMovementMethod();

        return sInstance;
    }

    private static ScrollingMovementMethod sInstance;

}
