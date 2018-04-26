package braille.kofefe.app.modules_.mainpostfeed.services_;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Snow-Dell-05 on 11/23/2017.
 */

public class MyServiceRunning extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("info", "started");

        return START_STICKY;
    }
}
