package com.example.messages.messages.messages.message.ad_is_crazy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class replay extends AppCompatActivity {
    Toolbar tbb;
    EditText no,sub,body;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        tbb = findViewById(R.id.toolbar1);
        setSupportActionBar(tbb);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        no=findViewById(R.id.editTextText5);
        sub=findViewById(R.id.editTextText4);
        body=findViewById(R.id.editTextText7);
        bt=findViewById(R.id.buttonx);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number=no.getText().toString();
                String subj=sub.getText().toString();
                String bdy=body.getText().toString();
                Log.d("send",number+subj+bdy);
                if (!number.isEmpty() && !bdy.isEmpty()) {
                    // Use SmsManager to send the SMS
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, subj+bdy, null, null);

                    // Show a success message
                    Toast.makeText(replay.this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Show an error message if phone number or message body is empty
                    Toast.makeText(replay.this, "Phone number or message body cannot be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}