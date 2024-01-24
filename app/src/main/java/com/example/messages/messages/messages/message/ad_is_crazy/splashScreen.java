package com.example.messages.messages.messages.message.ad_is_crazy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView txt;
        txt=findViewById(R.id.textView33);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.translate);
        txt.setAnimation(anim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent next=new Intent(splashScreen.this, MainActivity.class);
                startActivity(next);
                finish();

            }
        },2200);

    }
}
