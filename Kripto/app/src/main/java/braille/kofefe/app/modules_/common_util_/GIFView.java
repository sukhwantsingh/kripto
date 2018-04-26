package braille.kofefe.app.modules_.common_util_;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import braille.kofefe.app.R;

/**
 * Created by Snow-Dell-05 on 03-Feb-18.
 */

public class GIFView  extends android.support.v7.widget.AppCompatImageView {

    public static final String RESOURCE_PREFIX_URL = "url:";
    public static final String RESOURCE_PREFIX_ASSET = "asset:";

    private static final int DEF_VAL_DELAY_IN_MILLIS = 33;

    // the gif instance
    private GIF gif;

    // keeps track if the view is in the middle of setting the gif
    private boolean settingGif;

    private GIF.OnFrameReadyListener gifOnFrameReadyListener;

    private OnSettingGifListener onSettingGifListener;

    //   delay in millis between frames
    private int delayInMillis;

    /**
     * Creates a new instance in the passed context.
     *
     * @param context the context
     */
    public GIFView(Context context) {
        super(context);
        init(null);
    }

    /**
     * Creates a new instance in the passed context with the specified set of attributes.
     *
     * @param context the context
     * @param attrs   the attributes
     */
    public GIFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    // inits the view
    private void init(AttributeSet attrs) {
        this.gifOnFrameReadyListener = new GIF.OnFrameReadyListener() {
            @Override
            public void onFrameReady(Bitmap bitmap) {
                setImageBitmap(bitmap);
            }
        };

        setDelayInMillis(DEF_VAL_DELAY_IN_MILLIS);

        if (attrs != null)
            initAttrs(attrs);
    }

    // inits the view with the specified attributes
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.gif_view,
                0, 0);
        try {
            // gets and sets the delay in millis.
            int delayInMillis = typedArray.getInt(R.styleable.gif_view_delay_in_millis,
                    DEF_VAL_DELAY_IN_MILLIS);
            if (delayInMillis != DEF_VAL_DELAY_IN_MILLIS)
                setDelayInMillis(delayInMillis);

            // gets the source of the gif and sets it
            String string = typedArray.getString(R.styleable.gif_view_gif_src);
            if (string != null)
                setGifResource(typedArray.getString(R.styleable.gif_view_gif_src));

        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Register callbacks to be invoked when the view finished setting a gif.
     *
     * @param onSettingGifListener the listener to attach
     */
    public void setOnSettingGifListener(OnSettingGifListener onSettingGifListener) {
        this.onSettingGifListener = onSettingGifListener;
    }

    /**
     * Sets the delay in millis between every calculation of the next frame to be set.
     *
     * @param delayInMillis the delay in millis
     * @throws IllegalArgumentException if delayInMillis is non-positive
     */
    public void setDelayInMillis(int delayInMillis) {
        if (delayInMillis <= 0)
            throw new IllegalArgumentException("delayInMillis must be positive");

        this.delayInMillis = delayInMillis;

        if (gif != null)
            gif.setDelayInMillis(delayInMillis);
    }

    /**
     * Returns true if the view is in the process of setting the gif, false otherwise.
     *
     * @return true if the view is in the process of setting the gif, false otherwise
     */
    public boolean isSettingGif() {
        return settingGif;
    }

    /**
     * Sets the gif of this view and starts it.
     * <p>
     * Note that every exception while setting the gif is only sent to the
     * OnSettingGifListener instance attached to this view.
     * <p>
     * If the view has already begun setting another gif, does nothing.
     * You can query this state with isSettingGif().
     * <p>
     * The string passed must be in the following format:
     * <p>
     * - If you want to get the gif from a url
     * concatenate the string "url:" with the full url.
     * <p>
     * - if you want to get the gif from the assets directory
     * concatenate the string "asset:" with the full path of the gif
     * within the assets directory. You can exclude the .gif extension.
     * <p>
     * You can use the Constants:
     * <p>
     * GIFView.RESOURCE_PREFIX_URL = "url:"
     * GIFView.RESOURCE_PREFIX_ASSET = "asset:"
     * <p>
     * for example if you have a gif in the path "assets/ex_dir/ex_gif.gif"
     * invoke the method like this: setGifResource(GIFView.RESOURCE_PREFIX_ASSET + "ex_dir/ex_gif");
     *
     * @param string the string
     * @throws IllegalArgumentException if the string format is invalid
     */
    public void setGifResource(String string) {
        if (settingGif)
            return;

        // stops the gif if it is running
        if (gif != null)
            gif.stopGif();

        // defines some finals for readability
        final int URL_START_INDEX = RESOURCE_PREFIX_URL.length();
        final int ASSET_START_INDEX = RESOURCE_PREFIX_ASSET.length();
        final String GIF_EXTENSION = ".gif";

        if (string.startsWith(RESOURCE_PREFIX_URL)) {

            // notifies setting gif has started
            settingGif = true;

            // gets the url
            String url = string.substring(URL_START_INDEX);

            new AsyncSettingOfGif() {
                @Override
                protected InputStream getGifInputStream(String url) throws Exception {
                    // gets the input stream from the url
                    return (InputStream) new URL(url).getContent();
                }
            }.execute(url);

        } else if (string.startsWith(RESOURCE_PREFIX_ASSET)) {

            // notifies setting gif has started
            settingGif = true;

            // gets the asset path
            String assetPath = string.substring(ASSET_START_INDEX)
                    .replaceAll("[\\\\/]", File.separator); // replacing file separators
            if (!assetPath.endsWith(GIF_EXTENSION))
                assetPath += GIF_EXTENSION;

            new AsyncSettingOfGif() {
                @Override
                protected InputStream getGifInputStream(String assetPath) throws Exception {
                    // gets the input stream from the assets directory
                    return GIFView.this.getResources().getAssets().open(assetPath);
                }
            }.execute(assetPath);

            // if string format is invalid
        } else {
            throw new IllegalArgumentException("string format is invalid");
        }
    }

    /**
     * Called when the view finished to set the gif
     * or an exception has occurred.
     * If there are no exceptions e is null.
     * <p>
     * Note that the gif can be initialized properly
     * and one or more exceptions can be caught in the way.
     *
     * @param e the Exception
     */
    protected void onFinishSettingGif(Exception e) {
        // notifies setting the gif has finished
        settingGif = false;

        if (gif != null)
            onSuccess();
        else
            onFailure(e);
    }

    // on finish setting the gif
    private void onSuccess() {
        gif.setOnFrameReadyListener(gifOnFrameReadyListener, getHandler());
        gif.setDelayInMillis(delayInMillis);
        startGif();

        if (onSettingGifListener != null)
            onSettingGifListener.onSuccess(this);
    }

    // when an exception has occurred while trying to set the gif
    private void onFailure(Exception e) {
        if (onSettingGifListener != null)
            onSettingGifListener.onFailure(this, e);
    }

    /**
     * Starts the gif.
     * If the gif is already running does nothing.
     *
     * @throws IllegalStateException if the gif has not been initialized yet
     */
    public void startGif() {
        if (gif == null || settingGif)
            throw new IllegalStateException("the gif has not been initialized yet");

        gif.startGif();
    }

    /**
     * Stops the gif.
     * If the gif is not running does nothing.
     *
     * @throws IllegalStateException if the gif has not been initialized yet
     */
    public void stopGif() {
        if (gif == null || settingGif)
            throw new IllegalStateException("the gif has not been initialized yet");

        gif.stopGif();
    }

    /**
     * Interface definition for callbacks to be invoked when setting a gif.
     */
    public interface OnSettingGifListener {

        /**
         * Called when a gif has successfully set.
         *
         * @param view the GIFView
         */
        void onSuccess(GIFView view);

        /**
         * Called when a gif cannot be set.
         *
         * @param view the GIFView
         * @param e    the Exception
         */
        void onFailure(GIFView view, Exception e);
    }

    /**
     * Definition of an Exception class to throw when the view cannot initialize the gif.
     */
    public static class CannotInitGifException extends Exception {

        /**
         * Creates a new instance.
         */
        public CannotInitGifException() {
            super();
        }

        /**
         * * Creates a new instance with a message.
         *
         * @param message the message
         */
        public CannotInitGifException(String message) {
            super(message);
        }
    }

    /**
     * A sub-class of AsyncTask to easily perform an async task of setting a gif.
     * <p>
     * The default implementation of AsyncSettingOfGif.doInBackground() is to try and init the gif
     * from the input stream returned from AsyncSettingOfGif.getGifInputStream() and notify
     * GIFView.onFinishSettingGif() sending to it the exception, if occurred, or null.
     * <p>
     * Implementations of this class should override AsyncSettingOfGif.getGifInputStream()
     * to return the right input stream for the gif based on the string argument.
     * The string argument can be, for example, a url to retrieve the input stream from.
     */
    protected abstract class AsyncSettingOfGif extends AsyncTask<String, Void, Exception> {

        @Override
        protected Exception doInBackground(String... string) {
            CannotInitGifException exceptionToSend = null;

            try (InputStream in = getGifInputStream(string[0])) {
                // tries to init the gif
                gif = new GIF(in);

            } catch (Exception e) {
                // prepares the message of the exception
                String message = e.getMessage();
                if (e instanceof FileNotFoundException)
                    message = "file not found: " + message;

                // prepares the exception to send back
                exceptionToSend = new CannotInitGifException(message);
            }

            return exceptionToSend;
        }

        /**
         * Override this method to return the right input stream for the gif based on the string argument.
         * The string argument can be, for example, a url to retrieve the input stream from.
         *
         * @param string the string
         * @return an InputStream of a gif
         * @throws Exception if an exception has occurred
         */
        protected abstract InputStream getGifInputStream(String string) throws Exception;

        @Override
        protected void onPostExecute(Exception e) {
            onFinishSettingGif(e);
        }
    }
}