package com.example.messages.messages.messages.message.ad_is_crazy;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> smsNumber = new ArrayList<>();
    private ArrayList<String> smsBody = new ArrayList<>();
    private recAdoptor adapter;
    private smsReceiver smsm = new smsReceiver();
    private Toolbar tb;
    Context context = this;
    public static int on = 1;

    private final BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(smsm.SMS_RECEIVED_ACTION)) {
                ArrayList<String> newSmsNumber = intent.getStringArrayListExtra("smsNumber");
                ArrayList<String> newSmsBody = intent.getStringArrayListExtra("smsBody");

                // Loop through the new messages and add them to the adapter
                for (int i = 0; i < Objects.requireNonNull(newSmsNumber).size(); i++) {
                    assert newSmsBody != null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        adapter.addNewMessage(newSmsNumber.get(i), newSmsBody.get(i));
                    }
                }
                SmsStorageManager.saveSmsData(context, smsNumber, smsBody);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            // Permissions are granted, continue with your logic
                            recyclerView = findViewById(R.id.hii);

                            recyclerView.setLayoutManager(new LinearLayoutManager(context));

                            // Register the broadcast receiver
                            LocalBroadcastManager.getInstance(context).registerReceiver(smsReceiver, new IntentFilter(smsm.SMS_RECEIVED_ACTION));

                            // Load SMS data from SharedPreferences
                            SmsStorageManager.getSmsNumber(context, smsNumber);
                            SmsStorageManager.getSmsBody(context, smsBody);


                            loadSmsData();
                            int size = smsNumber.size();
                            for (int k = 0; k < size; k++) {
                                String h = smsNumber.get(k);
                                for (int f = k + 1; f < size; f++) {
                                    String d = smsNumber.get(f);
                                    if (h.equals(d)) {
                                        String mesbd = smsBody.get(f);
                                        smsNumber.remove(f);
                                        smsBody.remove(f);

                                        String existingBdy = smsBody.get(k);


                                        String totalbdy = mesbd + "\n\n\n\n\nNEXT MESSAGE:\n" + existingBdy + "\n";
                                        smsBody.set(k, totalbdy);
                                        f--;
                                        size--;

                                    }
                                }
                            }
                            SmsStorageManager.saveSmsData(context, smsNumber, smsBody);
                            clearSmsData();


                            adapter = new recAdoptor(context, smsNumber, smsBody, recyclerView);
                            recyclerView.setAdapter(adapter);






                            //TOOLBAR
                            tb=findViewById(R.id.toolbar);
                            tb.inflateMenu(R.menu.main_menu);
                            tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (item.getItemId() == R.id.send) {
                                      Intent intt=new Intent(context, replay.class);
                                        startActivity(intt);
                                    }

                                    return false;
                                }
                            });



                        } else {
                            // Handle denied permissions
                            Toast.makeText(context, "PLEASE ALLOW THE PERMISSIONS FIRST", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    @Override
    protected void onPause() {
        clearSmsData();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Unregister the broadcast receiver to avoid memory leaks
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);

        // Check if the activity is finishing (not just being temporarily paused)
        if (isFinishing()) {
            // Save SMS data when the activity is finishing
            SmsStorageManager.saveSmsData(context, smsNumber, smsBody);
            clearSmsData();
        }

        super.onDestroy();
    }


    // Retrieve SMS data from SharedPreferences
    private void loadSmsData() {
        SharedPreferences preferences = context.getSharedPreferences("smsPreferences", Context.MODE_PRIVATE);
        String smsNumberString = preferences.getString("offnumber", "");
        String smsBodyString = preferences.getString("offbody", "");

        // Convert comma-separated strings to ArrayLists
        if (!smsNumberString.equals("")) {
            smsNumber.addAll(0, stringToArray(smsNumberString));
            smsBody.addAll(0, stringToArray(smsBodyString));
        }
    }

    private ArrayList<String> stringToArray(String commaSeparatedString) {
        // Convert comma-separated String to ArrayList
        String[] items = commaSeparatedString.split("_#3%");
        ArrayList<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(items));
        return result;
    }

    private void clearSmsData() {
        SharedPreferences preferences = context.getSharedPreferences("smsPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Clear only the data related to the broadcast receiver
        editor.remove("offnumber");
        editor.remove("offbody");

        editor.apply();
    }

}

