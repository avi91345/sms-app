package com.example.messages.messages.messages.message.ad_is_crazy;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SmsStorageManager {

    private static final String PREFS_NAME = "SmsPrefs";
    private static final String KEY_SMS_NUMBER = "smsNumber";
    private static final String KEY_SMS_BODY = "smsBody";

    public static void saveSmsData(Context context, ArrayList<String> smsNumber, ArrayList<String> smsBody) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Serialize ArrayLists to JSON strings
        Gson gson = new Gson();
        String jsonSmsNumber = gson.toJson(smsNumber);
        String jsonSmsBody = gson.toJson(smsBody);

        // Save JSON strings to SharedPreferences
        editor.putString(KEY_SMS_NUMBER, jsonSmsNumber);
        editor.putString(KEY_SMS_BODY, jsonSmsBody);
        editor.apply();
    }

    public static void getSmsNumber(Context context, ArrayList<String> smsNumber) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Retrieve JSON string from SharedPreferences
        String jsonSmsNumber = preferences.getString(KEY_SMS_NUMBER, "[]");

        // Deserialize JSON string to ArrayList and update main array
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        smsNumber.clear(); // Clear the existing data
        smsNumber.addAll(gson.fromJson(jsonSmsNumber, type));
    }

    public static void getSmsBody(Context context, ArrayList<String> smsBody) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Retrieve JSON string from SharedPreferences
        String jsonSmsBody = preferences.getString(KEY_SMS_BODY, "[]");

        // Deserialize JSON string to ArrayList and update main array
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        smsBody.clear(); // Clear the existing data
        smsBody.addAll(gson.fromJson(jsonSmsBody, type));
    }
}

