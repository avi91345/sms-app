package com.example.messages.messages.messages.message.ad_is_crazy;

import static com.example.messages.messages.messages.message.ad_is_crazy.SmsStorageManager.saveSmsData;
import static com.example.messages.messages.messages.message.ad_is_crazy.smsReceiver.SMS_RECEIVED_ACTION;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SmsJobIntentService extends JobIntentService {
    public static final int JOB_ID = 1001;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SmsJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Extract arrays of SMS data from the intent
        ArrayList<String> mobileNumbers = intent.getStringArrayListExtra("mobile_numbers");
        ArrayList<String> smsBodies = intent.getStringArrayListExtra("sms_bodies");

        if (mobileNumbers != null && smsBodies != null) {
            // Save SMS data
            saveSmsData(getApplicationContext(), mobileNumbers, smsBodies);

            // Broadcast the received SMS data
            Intent broadcastIntent = new Intent(SMS_RECEIVED_ACTION);
            broadcastIntent.putStringArrayListExtra("smsNumber", mobileNumbers);
            broadcastIntent.putStringArrayListExtra("smsBody", smsBodies);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
        }
    }
    private void saveSmsData(Context context, ArrayList<String> smsNumber, ArrayList<String> smsBody) {
        Collections.reverse(smsNumber);
        Collections.reverse(smsBody);
        // Get SharedPreferences editor
        SharedPreferences preferences = context.getSharedPreferences("smsPreferences", Context.MODE_PRIVATE);

        // Retrieve existing data
        String existingNumberString = preferences.getString("offnumber", "");
        String existingBodyString = preferences.getString("offbody", "");
        ArrayList<String> existingNumber;
        ArrayList<String> existingBody;
        if(!existingNumberString.equals("")) {

            // Convert existing data to ArrayList
            existingNumber = stringToArray(existingNumberString);
            existingBody = stringToArray(existingBodyString);
            // Append new data
            existingNumber.addAll(0,smsNumber);
            existingBody.addAll(0,smsBody);
        }
        else{
            existingNumber=smsNumber;
            existingBody=smsBody;
        }




        // Convert ArrayLists to String
        String updatedNumberString = arrayToString(existingNumber);
        String updatedBodyString = arrayToString(existingBody);

        // Save updated data to SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("offnumber", updatedNumberString);
        editor.putString("offbody", updatedBodyString);
        editor.apply();
    }


    private String arrayToString(ArrayList<String> arrayList) {
        // Convert ArrayList to comma-separated String
        StringBuilder builder = new StringBuilder();
        for (String item : arrayList) {
            builder.append(item).append("_#3%");
        }
        return builder.toString();
    }
    private ArrayList<String> stringToArray(String commaSeparatedString) {
        // Convert separator-separated String to ArrayList
        String[] items = commaSeparatedString.split("_#3%");
        return new ArrayList<>(Arrays.asList(items));
    }


}

