package com.example.expensetracky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhoneSignupActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private EditText phoneNumber, otp;
    private Button generateOtpButton, verifyOtpButton;
    private String generatedOtp; // Store generated OTP here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_signup);

        phoneNumber = findViewById(R.id.phoneNumber);
        otp = findViewById(R.id.otp);
        generateOtpButton = findViewById(R.id.getOtpButton);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);

        requestSmsPermission();

        generateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(PhoneSignupActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    String formattedPhoneNumber = "+91" + phone; // Adding country code for India
                    generatedOtp = generateRandomOtp(); // Generate OTP
                    sendSms(formattedPhoneNumber, "Your OTP code is: " + generatedOtp); // Send OTP
                    // Do not set OTP in the EditText
                    Toast.makeText(PhoneSignupActivity.this, "OTP sent to " + phone, Toast.LENGTH_SHORT).show();
                }
            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpCode = otp.getText().toString().trim();
                if (TextUtils.isEmpty(otpCode)) {
                    Toast.makeText(PhoneSignupActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (otpCode.equals(generatedOtp)) {
                        // OTP verified successfully
                        Toast.makeText(PhoneSignupActivity.this, "OTP verified successfully!", Toast.LENGTH_SHORT).show();
                        // Navigate to TransactionActivity
                        Intent intent = new Intent(PhoneSignupActivity.this, Transactions.class);
                        startActivity(intent);
                        finish(); // Optional: finish this activity so it doesn't stay in the back stack
                    } else {
                        Toast.makeText(PhoneSignupActivity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateRandomOtp() {
        // Generate a 6-digit OTP
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}
