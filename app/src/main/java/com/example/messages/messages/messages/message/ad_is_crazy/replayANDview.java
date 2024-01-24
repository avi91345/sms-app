package com.example.messages.messages.messages.message.ad_is_crazy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;




public class replayANDview extends AppCompatActivity {
    private String number,body;
    Toolbar toolbar;
    ScrollView scrollView;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_andview);
        Intent intent=getIntent();
       number= intent.getStringExtra("NUMBER1");
       body= intent.getStringExtra("BODY1");



        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        txt=findViewById(R.id.textView35);
        txt.setText(body);
        scrollView=findViewById(R.id.scrollview);

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Ensure the TextView content is large enough to scroll
                        if (txt.getHeight() > scrollView.getHeight()) {
                            // Scroll to the bottom
                            scrollView.fullScroll(View.FOCUS_DOWN);

                            // Remove the listener to avoid unnecessary scrolls
                            scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

    }

}