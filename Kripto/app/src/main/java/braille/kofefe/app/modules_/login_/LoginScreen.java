package braille.kofefe.app.modules_.login_;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import braille.kofefe.app.R;
import braille.kofefe.app.supports_.UiHandleMethods;

public class LoginScreen extends AppCompatActivity {

    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private TextView mTextForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initViews();

    }

    private void initViews() {
        uihandle = new UiHandleMethods(mContext);
        mTextForgot = (TextView) findViewById(R.id.textView2);
        uihandle.setUnderLine(mTextForgot);
    }

    public void goForLogin(View v) {
        uihandle.showToast("Coming soon");
    }


}
