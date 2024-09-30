package com.example.expensetracky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button continueWithPhoneButton, continueWithEmailButton;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the buttons

        continueWithEmailButton = findViewById(R.id.continueWithEmail);
        login=findViewById(R.id.loginButton);

        // Set click listener for continue with phone number


        // Set click listener for continue with email
        continueWithEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EmailSignupActivity
                Intent emailIntent = new Intent(MainActivity.this, EmailSignupActivity.class);
                startActivity(emailIntent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
