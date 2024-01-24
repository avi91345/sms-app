package com.example.messages.messages.messages.message.ad_is_crazy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class smsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED_ACTION = "com.example.messages.SMS_RECEIVED";
    private Date currentDatea = new Date();

    // Define a format for the date and time including AM/PM
    private SimpleDateFormat formattera = new SimpleDateFormat("dd-MM-yyyy, EEEE \nhh:mm:ss a", Locale.getDefault());

    // Format the current date and time using the defined format
    private String formattedDateTimea = formattera.format(currentDatea);

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] smsobj = (Object[]) bundle.get("pdus");
            if (smsobj != null) {

                ArrayList<String> mno = new ArrayList<>();
                ArrayList<String> mbody = new ArrayList<>();

                for (Object obj : smsobj) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                    String mobno = message.getDisplayOriginatingAddress();
                    String bbody = message.getDisplayMessageBody();

                    // Check if the number is already in the list
                    int index = mno.indexOf(mobno);
                    if (index != -1) {
                        // Number already exists, concatenate the body
                        String existingBody = mbody.get(index);
                        mbody.set(index, existingBody +formattedDateTimea+bbody+"\n");
                    } else {
                        // Number doesn't exist, add it to the list
                        mno.add(mobno);
                        mbody.add(formattedDateTimea +"\n\n"+bbody+"\n");
                    }
                }

                enqueueWorkToService(context, mno, mbody);






                        // Save SMS data to SharedPreferences

            }
        }
    }









    private void enqueueWorkToService(Context context, ArrayList<String> mobileNumbers, ArrayList<String> smsBodies) {
        Intent serviceIntent = new Intent(context, SmsJobIntentService.class);
        serviceIntent.putStringArrayListExtra("mobile_numbers", mobileNumbers);
        serviceIntent.putStringArrayListExtra("sms_bodies", smsBodies);
        SmsJobIntentService.enqueueWork(context, serviceIntent);
    }


}
