package com.example.mobilenetworkapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilenetworkapp.R;
import com.example.mobilenetworkapp.models.MobileSubscriber;
import com.example.mobilenetworkapp.models.TariffContract;
import com.example.mobilenetworkapp.models.TariffPrepaid;
import com.example.mobilenetworkapp.ui.adapters.TariffAdapter;
import com.example.mobilenetworkapp.utils.ReportUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TariffsActivity extends AppCompatActivity {

    private static final String TAG = "TariffsActivity";

    private RecyclerView recyclerView;
    private TariffAdapter adapter;
    private List<TariffPrepaid> tariffList = new ArrayList<>();

    private DatabaseReference tariffsRef;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariffs);

        Log.d(TAG, "TariffsActivity створено");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_tariffs);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_main) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        recyclerView = findViewById(R.id.rvTariffs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TariffAdapter(tariffList);
        recyclerView.setAdapter(adapter);

        adapter.setOnTariffSelectListener(tariff -> {
            String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            if (phoneNumber == null) {
                Toast.makeText(this, "Не вдалося отримати номер телефону користувача", Toast.LENGTH_SHORT).show();
                return;
            }

            usersRef = FirebaseDatabase.getInstance("https://mobilenetworkapp-af52b-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("users").child(phoneNumber);

            String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

            Map<String, Object> updates = new HashMap<>();
            updates.put("tariffId", tariff.getId());
            updates.put("connection_date", currentDate);

            usersRef.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Тариф \"" + tariff.getName() + "\" обрано!", Toast.LENGTH_SHORT).show();

                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String surname = snapshot.child("surname").getValue(String.class);
                                String name = snapshot.child("name").getValue(String.class);
                                String patronymic = snapshot.child("patronymic").getValue(String.class);
                                String connectionDate = snapshot.child("connection_date").getValue(String.class);

                                MobileSubscriber subscriber = new MobileSubscriber(
                                        surname,
                                        name,
                                        patronymic,
                                        phoneNumber,
                                        tariff,
                                        connectionDate
                                );

                                ReportUtils.generateUserReport(TariffsActivity.this, subscriber);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "Не вдалося завантажити дані користувача: " + error.getMessage());
                            }
                        });

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Помилка при збереженні тарифу: " + e.getMessage());
                        Toast.makeText(this, "Помилка при збереженні тарифу: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        loadTariffsFromFirebase();

        Button prepaidButton = findViewById(R.id.prepaidButton);
        Button contractButton = findViewById(R.id.contractButton);
        Button exportButton = findViewById(R.id.btnExportTariffs);

        prepaidButton.setOnClickListener(v -> {
            prepaidButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple));
            prepaidButton.setTextColor(ContextCompat.getColor(this, R.color.white));

            contractButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            contractButton.setTextColor(ContextCompat.getColor(this, R.color.grey));

            List<TariffPrepaid> filtered = new ArrayList<>();
            for (TariffPrepaid tariff : tariffList) {
                if (!(tariff instanceof TariffContract)) {
                    filtered.add(tariff);
                }
            }
            adapter.updateList(filtered);
        });

        contractButton.setOnClickListener(v -> {
            prepaidButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            prepaidButton.setTextColor(ContextCompat.getColor(this, R.color.grey));

            contractButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple));
            contractButton.setTextColor(ContextCompat.getColor(this, R.color.white));

            List<TariffPrepaid> filtered = new ArrayList<>();
            for (TariffPrepaid tariff : tariffList) {
                if (tariff instanceof TariffContract) {
                    filtered.add(tariff);
                }
            }
            adapter.updateList(filtered);
        });

        exportButton.setOnClickListener(v -> {
            if (tariffList.isEmpty()) {
                Toast.makeText(this, "Список тарифів порожній", Toast.LENGTH_SHORT).show();
            } else {
                ReportUtils.generateTariffsReport(this, tariffList);
            }
        });
    }

    private void loadTariffsFromFirebase() {
        tariffsRef = FirebaseDatabase.getInstance("https://mobilenetworkapp-af52b-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("tariffs");

        tariffsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tariffList.clear();
                for (DataSnapshot tariffSnapshot : snapshot.getChildren()) {
                    String tariffId = tariffSnapshot.getKey();
                    String name = tariffSnapshot.child("name").getValue(String.class);

                    if ("Немає тарифу".equalsIgnoreCase(name)) {
                        continue;
                    }

                    Integer price = tariffSnapshot.child("price").getValue(Integer.class);
                    Integer internet = tariffSnapshot.child("services").child("0").getValue(Integer.class);
                    Integer calls = tariffSnapshot.child("services").child("1").getValue(Integer.class);
                    Integer sms = tariffSnapshot.child("services").child("2").getValue(Integer.class);
                    String type = tariffSnapshot.child("type").getValue(String.class);

                    if ("contract".equalsIgnoreCase(type)) {
                        String extraService = tariffSnapshot.child("extraService").getValue(String.class);
                        TariffContract contract = new TariffContract(name, price, internet, calls, sms, extraService);
                        contract.setId(tariffId);
                        tariffList.add(contract);
                    } else {
                        TariffPrepaid prepaid = new TariffPrepaid(name, price, internet, calls, sms);
                        prepaid.setId(tariffId);
                        tariffList.add(prepaid);
                    }
                }

                adapter.updateList(tariffList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Помилка завантаження тарифів: " + error.getMessage());
            }
        });
    }
}