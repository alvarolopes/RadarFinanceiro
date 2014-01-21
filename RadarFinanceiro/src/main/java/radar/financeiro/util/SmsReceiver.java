package radar.financeiro.util;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import radar.financeiro.Debitos_Detalhes;
import radar.financeiro.MainActivity;
import radar.financeiro.Model.Debito;

public class SmsReceiver extends BroadcastReceiver {
    // All available column names in SMS table
    // [_id, thread_id, address, 
    // person, date, protocol, read,
    // status, type, reply_path_present,
    // subject, body, service_center,
    // locked, error_code, seen]

    public static final String SMS_EXTRA_NAME = "pdus";
    public static final String SMS_URI = "content://sms";

    public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";

    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;

    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;

    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;

    // Change the password here or give a user possibility to change it
    public static final byte[] PASSWORD = new byte[]{0x20, 0x32, 0x34, 0x47, (byte) 0x84, 0x33, 0x58};

    public void onReceive(Context context, Intent intent) {
        // Get SMS map from Intent

        Bundle extras = intent.getExtras();

        String messages = "";

        if (extras != null) {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);

            // Get ContentResolver object for pushing encrypted SMS to incoming folder
            DatabaseHelper db = new DatabaseHelper(context);

            for (int i = 0; i < smsExtra.length; ++i) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

                String msg = sms.getMessageBody().toString();
                String address = sms.getOriginatingAddress();
                if (address.equals("27181"))
                {
                    Debito debito = Debito.CreateInstace(msg);

                    if (debito != null)
                    {
                        db.createDebito(debito);

                        MainActivity.PlaceholderFragment.recarregarDebitos();
                    }
                }
            }

            // Display SMS message
            Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
        }

        // WARNING!!! 
        // If you uncomment next line then received SMS will not be put to incoming.
        // Be careful!
        // this.abortBroadcast();
    }
}

