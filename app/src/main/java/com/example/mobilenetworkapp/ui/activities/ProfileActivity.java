package com.example.mobilenetworkapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilenetworkapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private TextView userSurnameTextView, userNameTextView, userPatronymicTextView, userTariffTextView, userPhoneNumber, tarrifConnectionDate;

    private DatabaseReference usersRef;
    private DatabaseReference tariffsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "ProfileActivity створено");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        userSurnameTextView = findViewById(R.id.surname);
        userNameTextView = findViewById(R.id.name);
        userPatronymicTextView = findViewById(R.id.patronymic);
        userTariffTextView = findViewById(R.id.userTariff);
        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        tarrifConnectionDate = findViewById(R.id.tariffConnectionDate);

        Log.d(TAG, "UI елементи ініціалізовано");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_main) {
                Log.d(TAG, "Перехід до MainActivity");
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_tariffs) {
                Log.d(TAG, "Перехід до TariffsActivity");
                startActivity(new Intent(this, TariffsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }

            return false;
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        loadDataFromFirebase();

        findViewById(R.id.stopTariffButton).setOnClickListener(v -> {
            Log.d(TAG, "Натиснуто кнопку 'Зупинити тариф'");
            stopTariff();
        });

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            Log.d(TAG, "Користувач виходить з акаунту");

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginFirstActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadDataFromFirebase() {
        if (currentUser == null) {
            Log.w(TAG, "Користувач не авторизований");
            return;
        }

        String phone = currentUser.getPhoneNumber();
        Log.d(TAG, "Завантаження даних для користувача з номером: " + phone);
        userPhoneNumber.setText(phone);

        usersRef = FirebaseDatabase.getInstance("https://mobilenetworkapp-af52b-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users")
                .child(phone);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.w(TAG, "Дані користувача не знайдено");
                    return;
                }

                Log.d(TAG, "Дані користувача завантажено");

                String surname = snapshot.child("surname").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String patronymic = snapshot.child("patronymic").getValue(String.class);
                String connectionDate = snapshot.child("connection_date").getValue(String.class);
                String tariffId = snapshot.child("tariffId").getValue(String.class);

                userSurnameTextView.setText(surname);
                userNameTextView.setText(name);
                userPatronymicTextView.setText(patronymic);
                tarrifConnectionDate.setText(connectionDate);

                Log.d(TAG, "Ім'я: " + name + ", Тариф ID: " + tariffId);

                if (tariffId != null) {
                    tariffsRef = FirebaseDatabase.getInstance("https://mobilenetworkapp-af52b-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference("tariffs")
                            .child(tariffId);

                    tariffsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot tariffSnapshot) {
                            if (!tariffSnapshot.exists()) {
                                Log.w(TAG, "Дані тарифу не знайдено");
                                return;
                            }

                            String tariffName = tariffSnapshot.child("name").getValue(String.class);
                            userTariffTextView.setText(tariffName);

                            Log.d(TAG, "Назва тарифу: " + tariffName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Помилка завантаження тарифу: " + error.getMessage());
                        }
                    });
                } else {
                    Log.w(TAG, "У користувача немає тарифу");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Помилка завантаження користувача: " + error.getMessage());
            }
        });
    }

    private void stopTariff() {
        if (currentUser == null) {
            Log.w(TAG, "Користувач не авторизований для зупинки тарифу");
            return;
        }

        String phone = currentUser.getPhoneNumber();
        Log.d(TAG, "Зупинення тарифу для номера: " + phone);

        DatabaseReference userRef = FirebaseDatabase.getInstance("https://mobilenetworkapp-af52b-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users")
                .child(phone);

        Map<String, Object> updates = new HashMap<>();
        updates.put("tariffId", "tariff0");
        updates.put("connection_date", "Не підключено");

        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Тариф успішно зупинено");
                userTariffTextView.setText("Немає тарифу");
                tarrifConnectionDate.setText("Не підключено");
            } else {
                Log.e(TAG, "Не вдалося зупинити тариф: " + task.getException());
            }
        });
    }
}