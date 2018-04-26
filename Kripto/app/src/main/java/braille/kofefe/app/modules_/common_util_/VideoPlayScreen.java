package braille.kofefe.app.modules_.common_util_;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import braille.kofefe.app.R;
import braille.kofefe.app.supports_.UiHandleMethods;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Snow-Dell-05 on 07-Feb-18.
 */

public class VideoPlayScreen extends CommonActivity {

    @InjectView(R.id.videoView)
    protected VideoView mVideoView;
    private Activity mContext = this;
    private UiHandleMethods uihandle;
    private TextView mTextViewHeading;
    private ImageView mImageViewBack, img_action_overflow;
    private String mVideoPath;
    private String mPostId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_screen);
        ButterKnife.inject(this);

        // calling init for initialisations
        init();
        implementListeners();
        mVideoPath = StaticValues.mVideoPathToPlay;
        mPostId = StaticValues.mPostId;

        /*** check whether temp file is created or not if created then use from temp file if not then load from url directly
         *  ***/
        if (getSessionInstance().getTempVideoPath(mPostId).isEmpty()) {
            getVideoFromUrl(mVideoPath);
        } else {
            videoSetUp(mVideoView, getSessionInstance().getTempVideoPath(mPostId));
        }
    }





    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
     /*
        mVideoPath = StaticValues.mVideoPathToPlay;
        mPostId = StaticValues.mPostId;
        *//*** check whether temp file is created or not if created then use from temp file if not then load from url directly
         *  ***//*
        if (getSessionInstance().getTempVideoPath(mPostId).isEmpty()) {
            getVideoFromUrl(mVideoPath);
        } else {
            videoSetUp(mVideoView, getSessionInstance().getTempVideoPath(mPostId));
        }*/

    }

    private void init() {
        uihandle = new UiHandleMethods(mContext);
    }

    private void implementListeners() {

    /*    // setting toolbar on header
        mTextViewHeading.setText("Playing");
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying()) {
                    mVideoView.stopPlayback();
                }
                mContext.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
*/


    }

    //video setup
    public void videoSetUp(VideoView mVideoView, String path) {
        //   Uri video = Uri.parse("http://www.servername.com/projects/projectname/videos/1361439400.mp4");
        //  mVideoView.setVideoURI(video);
        try {
            mVideoView.setVideoPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaController mc = new MediaController(mContext);

        mVideoView.setMediaController(mc);
        mVideoView.start();
        mc.show(0);
        mc.setEnabled(true);
        mVideoView.requestFocus();

    }

    //   filter video from url to display in the videoView
    private void getVideoFromUrl(final String pathVideo) {
        class UploadVideo extends AsyncTask<Void, Void, String> {
            /*
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (mKProgressHUD.isShowing()) {
                        mKProgressHUD.dismiss();
                    }*/
            ProgressDialog uploading;
            private KProgressHUD mKProgressHUD;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mKProgressHUD = KProgressHUD.create(mContext)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Buffering...")
                        .setCancellable(true)
                        .setAnimationSpeed(1)
                        .setDimAmount(.2f)
                        .show();

            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                try {
                    if (mKProgressHUD.isShowing()) {
                        mKProgressHUD.dismiss();
                    }
                    if (s != null) {
                        // jetPlay(s);
                        getSessionInstance().storeVideoTempPath(mPostId, s);
                        videoSetUp(mVideoView, s);
                    }
                } catch (Exception e) {
                    if (mKProgressHUD.isShowing()) {
                        mKProgressHUD.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    return getDataSource(pathVideo);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    // Datasource for the video to view
    private String getDataSource(String path) throws IOException {
        if (!URLUtil.isNetworkUrl(path)) {
            return path;
        } else {
            URL url = new URL(path);
            URLConnection cn = url.openConnection();
            cn.connect();
            InputStream stream = cn.getInputStream();
            if (stream == null)
                throw new RuntimeException("stream is null");
            File temp = File.createTempFile("mediaplayertmp", "dat");
            temp.deleteOnExit();
            String tempPath = temp.getAbsolutePath();
            FileOutputStream out = new FileOutputStream(temp);
            byte buf[] = new byte[128];
            do {
                int numread = stream.read(buf);
                if (numread <= 0)
                    break;
                out.write(buf, 0, numread);
            } while (true);
            try {
                stream.close();
            } catch (IOException ex) {
                Log.e("TAG", "error: " + ex.getMessage(), ex);
            }
            return tempPath;
        }
    }


    @Override
    public void onBackPressed() {
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
        uihandle.goBack();
        super.onBackPressed();


    }
}
