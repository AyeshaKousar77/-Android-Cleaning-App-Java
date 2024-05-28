package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // HERE WE ARE TAKING THE REFERENCE OF OUR IMAGE
        // SO THAT WE CAN PERFORM ANIMATION USING THAT IMAGE
        ImageView backgroundImage = findViewById(R.id.SplashScreenImage);
        TextView tex = findViewById(R.id.text_go);

        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.splash_anim);

        backgroundImage.startAnimation(slideAnimation);
        tex.startAnimation(slide);


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, sign_in.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // 3000 is the delayed time in milliseconds.
    }

    }
