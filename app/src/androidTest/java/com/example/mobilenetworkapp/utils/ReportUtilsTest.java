package com.example.mobilenetworkapp.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.os.Environment;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.mobilenetworkapp.models.MobileSubscriber;
import com.example.mobilenetworkapp.models.TariffContract;
import com.example.mobilenetworkapp.models.TariffPrepaid;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ReportUtilsTest {

    @Test
    public void testGenerateUserReport_createsFile() {
        Context context = ApplicationProvider.getApplicationContext();

        TariffPrepaid tariff = new TariffPrepaid("Смарт", 200, 10, 300, 100);
        tariff.setId("tariff123");

        MobileSubscriber subscriber = new MobileSubscriber(
                "Іванов", "Іван", "Іванович",
                "+380991234567", tariff, "13.06.2025"
        );

        ReportUtils.generateUserReport(context, subscriber);

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloads, "user_report_+380991234567.xlsx");

        assertTrue("Файл звіту користувача не знайдено", file.exists());
    }

    @Test
    public void testGenerateTariffsReport_createsFile() {
        Context context = ApplicationProvider.getApplicationContext();

        List<TariffPrepaid> tariffs = new ArrayList<>();
        tariffs.add(new TariffPrepaid("Передплата 1", 100, 5, 100, 50));
        tariffs.add(new TariffContract("Контракт 1", 250, 15, 400, 200, "Netflix + Deezer"));

        ReportUtils.generateTariffsReport(context, tariffs);

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = downloads.listFiles((dir, name) -> name.startsWith("tariffs_report_") && name.endsWith(".xlsx"));

        assertNotNull("Файли звітів тарифів не знайдені", files);
        assertTrue("Жодного звіту не створено", files.length > 0);
    }
}