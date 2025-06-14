package com.example.mobilenetworkapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilenetworkapp.R;
import com.example.mobilenetworkapp.models.MobileSubscriber;
import com.example.mobilenetworkapp.models.TariffContract;
import com.example.mobilenetworkapp.models.TariffPrepaid;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private TextView userNameTextView, balanceTextView, tariffTextView, priceTextView,
            internetTextView, phoneCallsTextView, smsTextView, additionalServicesTextView;
    private View additionalServiceBlock;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity створено");

        // Ініціалізація UI
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_main);
        userNameTextView = findViewById(R.id.userNameTextView);
        balanceTextView = findViewById(R.id.balance);
        tariffTextView = findViewById(R.id.tariff);
        priceTextView = findViewById(R.id.price);
        internetTextView = findViewById(R.id.internetDescription);
        phoneCallsTextView = findViewById(R.id.phoneCallsDescription);
        smsTextView = findViewById(R.id.smsDescription);
        additionalServicesTextView = findViewById(R.id.additionalServicesDescription);
        additionalServiceBlock = findViewById(R.id.additionalServiceBlock);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_main) return true;
            if (itemId == R.id.nav_tariffs) {
                Log.d(TAG, "Переходимо до TariffsActivity");
                startActivity(new Intent(this, TariffsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Log.d(TAG, "Переходимо до ProfileActivity");
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        loadDataFromFirebase();

        Button tariffsButton = findViewById(R.id.buttonTariffs);
        tariffsButton.setOnClickListener(v -> {
            Log.d(TAG, "Натиснуто кнопку 'Тарифи'");
            Intent intent = new Intent(MainActivity.this, TariffsActivity.class);
            startActivity(intent);
        });
    }

    private void loadDataFromFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String phone = currentUser.getPhoneNumber();
            Log.d(TAG, "Користувач авторизований, номер: " + phone);

            if (phone != null) {
                DatabaseReference userRef = FirebaseDatabase.getInstance("https://mobilenetworkapp-af52b-default-rtdb.europe-west1.firebasedatabase.app")
                        .getReference("users")
                        .child(phone);

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        if (userSnapshot.exists()) {
                            String surname = userSnapshot.child("surname").getValue(String.class);
                            String name = userSnapshot.child("name").getValue(String.class);
                            String patronymic = userSnapshot.child("patronymic").getValue(String.class);
                            String tariffId = userSnapshot.child("tariffId").getValue(String.class);
                            String connectionDate = userSnapshot.child("connection_date").getValue(String.class);
                            Long balance = userSnapshot.child("balance").getValue(Long.class);

                            if (tariffId != null) {
                                DatabaseReference tariffRef = FirebaseDatabase.getInstance("https://mobilenetworkapp-af52b-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("tariffs")
                                        .child(tariffId);

                                tariffRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot tariffSnapshot) {
                                        if (tariffSnapshot.exists()) {
                                            String tariffName = tariffSnapshot.child("name").getValue(String.class);
                                            Integer price = tariffSnapshot.child("price").getValue(Integer.class);
                                            Integer internet = tariffSnapshot.child("services").child("0").getValue(Integer.class);
                                            Integer calls = tariffSnapshot.child("services").child("1").getValue(Integer.class);
                                            Integer sms = tariffSnapshot.child("services").child("2").getValue(Integer.class);
                                            String type = tariffSnapshot.child("type").getValue(String.class);

                                            TariffPrepaid tariff;
                                            if ("contract".equalsIgnoreCase(type)) {
                                                String additionalServices = tariffSnapshot.child("services").child("3").getValue(String.class);
                                                tariff = new TariffContract(tariffName, price, internet, calls, sms, additionalServices);
                                            } else {
                                                tariff = new TariffPrepaid(tariffName, price, internet, calls, sms);
                                            }

                                            MobileSubscriber subscriber = new MobileSubscriber(
                                                    surname, name, patronymic, phone, tariff, connectionDate
                                            );

                                            userNameTextView.setText(subscriber.getName());
                                            balanceTextView.setText(String.valueOf(balance));
                                            tariffTextView.setText(tariff.getName());
                                            priceTextView.setText(String.valueOf(tariff.getMonthlyFee()));
                                            internetTextView.setText(internet + " ГБ мобільного інтернету");
                                            phoneCallsTextView.setText(calls + " хв дзвінки на інші мережі");
                                            smsTextView.setText(sms + " шт SMS по Україні");

                                            if (tariff instanceof TariffContract) {
                                                String services = ((TariffContract) tariff).getAdditionalServices();
                                                additionalServicesTextView.setText("Додаткові послуги:\n" + services);
                                                additionalServiceBlock.setVisibility(View.VISIBLE);
                                                Log.d(TAG, "Показуємо додаткові послуги: " + services);
                                            } else {
                                                additionalServiceBlock.setVisibility(View.GONE);
                                                Log.d(TAG, "Сховуємо додаткові послуги (не контракт)");
                                            }
                                        } else {
                                            Log.w(TAG, "Дані тарифу не знайдено");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e(TAG, "Помилка завантаження тарифу: " + error.getMessage());
                                    }
                                });
                            } else {
                                Log.w(TAG, "У користувача відсутній тариф");
                            }
                        } else {
                            Log.w(TAG, "Дані користувача не знайдено");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Помилка завантаження користувача: " + error.getMessage());
                    }
                });
            } else {
                Log.w(TAG, "Номер телефону користувача відсутній");
            }
        } else {
            Log.w(TAG, "Користувач не авторизований");
        }
    }
}