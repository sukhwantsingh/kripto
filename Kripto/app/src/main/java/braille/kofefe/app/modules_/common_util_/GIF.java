package braille.kofefe.app.modules_.common_util_;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.os.SystemClock;

import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Snow-Dell-05 on 05-Feb-18.
 */

public class GIF {


    private static final Bitmap.Config DEF_VAL_CONFIG = Bitmap.Config.RGB_565;

    private static final int DEF_VAL_DELAY_IN_MILLIS = 33;

    // the gif's frames are stored in a movie instance
    private Movie movie;

    // the canvas of this gif
    private Canvas canvas;

    // the bitmap of this gif
    private Bitmap bitmap;

    // the start time of the gif
    private long gifStartTime;

    // the executor of the gif's thread
    private ScheduledExecutorService executor;

    // the main runnable of the gif
    private Runnable mainRunnable;

    // delay in millis between frames
    private int delayInMillis;

    private OnFrameReadyListener onFrameReadyListener;

    private Handler listenerHandler;

    private Runnable listenerRunnable;

    /**
     * Creates Gif instance based on the passed InputStream.
     *
     * @param in the InputStream
     * @throws InputStreamIsNull                        if in is null
     * @throws InputStreamIsEmptyOrUnavailableException if in is empty or unavailable
     */
    public GIF(InputStream in) {
        this(in, DEF_VAL_CONFIG);
    }

    /**
     * Creates Gif instance based on the passed InputStream and the config.
     *
     * @param in     the InputStream
     * @param config the Config
     * @throws NullPointerException                     if config is null
     * @throws InputStreamIsNull                        if in is null
     * @throws InputStreamIsEmptyOrUnavailableException if in is empty or unavailable
     */
    public GIF(InputStream in, Bitmap.Config config) {
        if (in == null)
            throw new InputStreamIsNull("the input stream is null");

        this.movie = Movie.decodeStream(in);

        if (movie == null)
            throw new InputStreamIsEmptyOrUnavailableException("the input steam is empty or unavailable");

        this.bitmap = Bitmap.createBitmap(movie.width(), movie.height(), config);

        // associates the canvas with the bitmap
        this.canvas = new Canvas(bitmap);

        this.mainRunnable = new Runnable() {
            @Override
            public void run() {
                draw();
                invokeListener();
            }
        };

        setDelayInMillis(DEF_VAL_DELAY_IN_MILLIS);
    }

    /**
     * Register a callback to be invoked when the gif changed a frame.
     * Invokes methods from a special thread.
     *
     * @param onFrameReadyListener the listener to attach
     */
    public void setOnFrameReadyListener(OnFrameReadyListener onFrameReadyListener) {
        setOnFrameReadyListener(onFrameReadyListener, null);
    }

    /**
     * Register a callback to be invoked when the gif changed a frame.
     * Invokes methods from the specified handler.
     *
     * @param onFrameReadyListener the listener to attach
     * @param handler              the handler
     */
    public void setOnFrameReadyListener(OnFrameReadyListener onFrameReadyListener, Handler handler) {
        this.onFrameReadyListener = onFrameReadyListener;
        listenerHandler = handler;

        if (listenerHandler != null)
            listenerRunnable = new Runnable() {
                @Override
                public void run() {
                    GIF.this.onFrameReadyListener.onFrameReady(bitmap);
                }
            };

        else
            listenerRunnable = null;
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
    }

    /**
     * Starts the gif.
     * If the gif is already running does nothing.
     */
    public void startGif() {
        if (executor != null)
            return;

        executor = Executors.newSingleThreadScheduledExecutor();

        final int INITIAL_DELAY = 0;
        executor.scheduleWithFixedDelay(mainRunnable, INITIAL_DELAY,
                delayInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the gif.
     * If the gif is not running does nothing.
     */
    public void stopGif() {
        if (executor == null)
            return;

        executor.shutdown();

        // waits until the thread is finished
        while (true) {
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
                break;
            } catch (InterruptedException ignored) {
            }
        }

        executor = null;
    }

    // calculates the frame and draws it to the bitmap through the canvas
    private void draw() {
        // if gifStartTime == 0 inits it for the first time
        if (gifStartTime == 0)
            gifStartTime = SystemClock.uptimeMillis();

        long timeElapsed = SystemClock.uptimeMillis() - gifStartTime;

        int timeInGif = (int) (timeElapsed % movie.duration());
        movie.setTime(timeInGif);

        movie.draw(canvas, 0, 0);
    }

    // invokes the listener
    private void invokeListener() {
        if (onFrameReadyListener == null)
            return;

        // if handler was given invokes from it, otherwise invokes from this thread
        if (listenerHandler != null)
            listenerHandler.post(listenerRunnable);
        else
            onFrameReadyListener.onFrameReady(bitmap);
    }

    /**
     * Interface definition for a callback to be invoked when the gif changed a frame.
     */
    public interface OnFrameReadyListener {
        /**
         * Called when the gif changed a frame.
         * <p>
         * Note: If a handler was given with the listener this method
         * invokes from the handler, otherwise this method
         * invokes from a special thread.
         * <p>
         * Note: This bitmap is mutable and used by the gif instance
         * thus it is not recommended to mutate it.
         *
         * @param bitmap the new bitmap of the gif
         */
        void onFrameReady(Bitmap bitmap);
    }

    /**
     * Definition of a runtime exception class to throw when the inputStream is null.
     */
    public static class InputStreamIsNull extends NullPointerException {

        /**
         * Creates a new instance.
         */
        public InputStreamIsNull() {
            super();
        }

        /**
         * * Creates a new instance with a message.
         *
         * @param message the message
         */
        public InputStreamIsNull(String message) {
            super(message);
        }
    }

    /**
     * Definition of a runtime exception class to throw when the inputStream is empty or unavailable.
     */
    public static class InputStreamIsEmptyOrUnavailableException extends RuntimeException {

        /**
         * Creates a new instance.
         */
        public InputStreamIsEmptyOrUnavailableException() {
            super();
        }

        /**
         * * Creates a new instance with a message.
         *
         * @param message the message
         */
        public InputStreamIsEmptyOrUnavailableException(String message) {
            super(message);
        }
    }
}

