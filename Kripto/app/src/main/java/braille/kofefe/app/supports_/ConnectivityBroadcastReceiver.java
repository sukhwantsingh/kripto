package braille.kofefe.app.supports_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Snow-Dell-05 on 7/26/2017.
 */

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

      //  Toast.makeText(context, "changed status: " + isOnline(context), Toast.LENGTH_SHORT).show();


        //     context.startActivity(new Intent(context, SignUpChooser.class));
        // context.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }


    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
