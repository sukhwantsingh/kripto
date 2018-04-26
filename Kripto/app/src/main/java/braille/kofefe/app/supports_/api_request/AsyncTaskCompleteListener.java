package braille.kofefe.app.supports_.api_request;

/**
 * Created by Snow-Dell-07 on 05-Dec-17.
 */

public interface AsyncTaskCompleteListener {
    void onTaskCompleted(String response, int serviceCode);
    void onErrorFound(String errorResponse, int serviceCode);
}
