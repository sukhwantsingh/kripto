package braille.kofefe.app.modules_.common_util_;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import braille.kofefe.app.R;


/**
 * Created by HGS on 12/11/2015.
 */
public abstract class BaseActivity extends AppCompatActivity implements Handler.Callback {
    public Context _context = null;
    public Handler _handler = null;
    private ProgressDialog _progressDlg;
    private Vibrator _vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;
        _vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        _handler = new Handler(this);
    }

    @Override
    protected void onDestroy() {
        closeProgress();
        try {
            if (_vibrator != null)
                _vibrator.cancel();
        } catch (Exception e) {
        }
        _vibrator = null;

        super.onDestroy();
    }

    public void showProgress(boolean cancelable) {
        closeProgress();
        _progressDlg = new ProgressDialog(_context, R.style.MyTheme);
        _progressDlg.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        _progressDlg.setCancelable(cancelable);
        _progressDlg.show();

    }

    public void showProgress() {
        showProgress(false);
    }

    public void closeProgress() {

        if (_progressDlg == null) {
            return;
        }
        _progressDlg.dismiss();
        _progressDlg = null;
    }

    public void showAlertDialog(String msg) {


        AlertDialog.Builder customBuilder = new AlertDialog.Builder(_context);

        customBuilder.setTitle(getString(R.string.app_name));
        customBuilder.setMessage(msg);
        customBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // MyActivity.this.finish();
            }
        });
        customBuilder.setIcon(R.mipmap.ic_alphabet_k);
        customBuilder.setCancelable(false);
        AlertDialog dialog = customBuilder.create();
        dialog.show();

        Button b = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        if (b != null) {
            b.setBackgroundColor(getResources().getColor(R.color.white_80));
        }
        dialog.show();
    }

    /**
     * show toast
     *
     * @param toast_string
     */
    public void showToast(String toast_string) {
        Toast.makeText(_context, toast_string, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String toast_string) {
        Toast.makeText(_context, toast_string, Toast.LENGTH_LONG).show();
    }

    public void vibrate() {

        if (_vibrator != null)
            _vibrator.vibrate(500);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            default:
                break;
        }
        return false;
    }

}
