package com.example.expensetracky;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer (3-4 seconds)
    private static final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);  // Use the layout you just created

        // Delaying the transition to the main activity by SPLASH_TIME_OUT (4 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your app's main activity here after the delay
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();  // Close the splash activity so the user can't go back to it
            }
        }, SPLASH_TIME_OUT);
    }
}