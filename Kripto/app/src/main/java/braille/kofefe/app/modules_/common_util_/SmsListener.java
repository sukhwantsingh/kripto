package braille.kofefe.app.modules_.common_util_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Snow-Dell-05 on 12/25/2017.
 */

public class SmsListener extends BroadcastReceiver {

//  Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

//       Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for(int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().split(":")[i];

                    message = message.substring(0, message.length()-1);
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);

                } //       end for loop

            } //           bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}