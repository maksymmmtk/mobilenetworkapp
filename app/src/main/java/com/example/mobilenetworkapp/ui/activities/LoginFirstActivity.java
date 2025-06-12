package com.example.mobilenetworkapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilenetworkapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginFirstActivity extends AppCompatActivity {

    private static final String TAG = "LoginFirstActivity";

    private EditText phoneEditText;
    private Button nextButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_first);

        phoneEditText = findViewById(R.id.editPhoneNumber);
        nextButton = findViewById(R.id.exitButton);
        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "LoginFirstActivity created");

        nextButton.setOnClickListener(v -> {
            String phone = phoneEditText.getText().toString().trim();
            Log.d(TAG, "Next button clicked with phone: " + phone);

            if (phone.isEmpty()) {
                Toast.makeText(this, "Введіть номер телефону", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Phone number is empty");
            } else {
                startPhoneNumberVerification(phone);
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Log.d(TAG, "Starting phone number verification for: " + phoneNumber);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    Log.d(TAG, "onVerificationCompleted: Автоматично підтверджено");
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.e(TAG, "onVerificationFailed: " + e.getMessage(), e);
                    Toast.makeText(LoginFirstActivity.this, "Помилка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    Log.d(TAG, "onCodeSent: Код надіслано. verificationId: " + verificationId);

                    SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                    prefs.edit()
                            .putString("verification_id", verificationId)
                            .putString("phone_number", phoneEditText.getText().toString().trim())
                            .putString("resend_token", token.toString())  // NOTE: token can't be saved this way directly
                            .apply();

                    Log.d(TAG, "Verification ID та номер збережено в SharedPreferences");

                    Intent intent = new Intent(LoginFirstActivity.this, LoginSecondActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "Перехід до LoginSecondActivity");
                }
            };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d(TAG, "Спроба входу з отриманим credential");

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Авторизація успішна");
                        Toast.makeText(this, "Успішний вхід!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Не вдалося увійти", task.getException());
                        Toast.makeText(this, "Не вдалося увійти", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}