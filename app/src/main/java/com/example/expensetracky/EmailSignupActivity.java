package com.example.expensetracky;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailSignupActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button continueButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        continueButton = findViewById(R.id.continueButton);
        progressBar = findViewById(R.id.registerProgressBar);

        // Hide progress bar initially
        progressBar.setVisibility(View.GONE);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    emailEditText.setError("Email address is required");
                } else if (password.isEmpty()) {
                    passwordEditText.setError("Password is required");
                } else if (password.length() < 6) {
                    passwordEditText.setError("Password must be at least 6 characters long");
                } else {
                    // Show progress bar
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Hide progress bar after task completion
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(EmailSignupActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                        // Start LoginActivity or go to another screen
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Display error message if registration fails
                                        Toast.makeText(EmailSignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "onComplete: Failed=" + task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });

    }
}
