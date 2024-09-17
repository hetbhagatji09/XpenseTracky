package com.example.expensetracky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, phoneEditText;
    private Button continueWithEmailButton, continueWithPhoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        emailEditText = findViewById(R.id.continueWithEmail);
//        phoneEditText = findViewById(R.id.continueWithPhone);
        continueWithEmailButton = findViewById(R.id.continueWithEmail);
        continueWithPhoneButton = findViewById(R.id.continueWithPhone);

        continueWithEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle continue with email
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("method", "email");
                startActivity(intent);
            }
        });

        continueWithPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle continue with phone number
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("method", "phone");
                startActivity(intent);
            }
        });
    }
}
