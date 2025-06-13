package com.example.mobilenetworkapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginSecondActivity extends AppCompatActivity {

    private static final String TAG = "LoginSecondActivity";

    private EditText codeEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String phoneNumber;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private TextView resendTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_second);

        Log.d(TAG, "LoginSecondActivity created");

        codeEditText = findViewById(R.id.smsCodeField);
        loginButton = findViewById(R.id.loginButton);
        resendTextView = findViewById(R.id.textViewResend);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        phoneNumber = prefs.getString("phone_number", "");
        verificationId = prefs.getString("verification_id", "");

        Log.d(TAG, "Отримано збережений номер телефону: " + phoneNumber);
        Log.d(TAG, "Отримано збережений verificationId: " + verificationId);

        TextView phoneText = findViewById(R.id.textViewPhone);
        phoneText.setText(phoneNumber);

        loginButton.setOnClickListener(v -> {
            String code = codeEditText.getText().toString().trim();
            Log.d(TAG, "Натиснуто кнопку 'Увійти'. Введений код: " + code);

            if (code.length() < 4) {
                Toast.makeText(this, "Введіть код", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Введено занадто короткий код");
                return;
            }
            verifyCode(code);
        });

        resendTextView.setOnClickListener(v -> resendVerificationCode());
    }

    private void verifyCode(String code) {
        Log.d(TAG, "Починається перевірка коду...");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        Log.d(TAG, "Спроба авторизації з переданим credential");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Авторизація пройшла успішно");
                        Intent intent = new Intent(LoginSecondActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e(TAG, "Не вдалося авторизуватись: ", task.getException());
                        Toast.makeText(this, "Код невірний", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resendVerificationCode() {
        Log.d(TAG, "Повторне надсилання коду для: " + phoneNumber);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(resendCallbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks resendCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    Log.d(TAG, "onVerificationCompleted (resend): Автоматично підтверджено");
                    signInWithCredential(credential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Log.e(TAG, "onVerificationFailed (resend): " + e.getMessage(), e);
                    Toast.makeText(LoginSecondActivity.this, "Помилка при повторному надсиланні", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String newVerificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken newToken) {
                    Log.d(TAG, "Код повторно надіслано. Новий verificationId: " + newVerificationId);
                    verificationId = newVerificationId;
                    resendToken = newToken;
                    Toast.makeText(LoginSecondActivity.this, "Код повторно надіслано", Toast.LENGTH_SHORT).show();
                }
            };
}