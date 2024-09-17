package com.example.expensetracky;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneSignupActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_signup);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    phoneNumberEditText.setError("Phone number is required");
                } else {
                    // Proceed with phone number registration logic
                    Toast.makeText(PhoneSignupActivity.this, "Phone number registered: " + phoneNumber, Toast.LENGTH_SHORT).show();
                    // Optionally start a new activity or verification process
                }
            }
        });
    }
}

