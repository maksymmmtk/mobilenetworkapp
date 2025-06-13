package com.example.mobilenetworkapp.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.mobilenetworkapp.models.MobileSubscriber;
import com.example.mobilenetworkapp.models.TariffContract;
import com.example.mobilenetworkapp.models.TariffPrepaid;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportUtils {

    private static final String TAG = "ReportUtils";

    public static void generateUserReport(Context context, MobileSubscriber subscriber) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Звіт користувача");

        int rowIndex = 0;

        Row row1 = sheet.createRow(rowIndex++);
        row1.createCell(0).setCellValue("Звіт про підключення тарифу");

        Row row2 = sheet.createRow(rowIndex++);
        row2.createCell(0).setCellValue("ПІБ");
        row2.createCell(1).setCellValue(subscriber.getSurname() + " " + subscriber.getName() + " " + subscriber.getPatronymic());

        Row row3 = sheet.createRow(rowIndex++);
        row3.createCell(0).setCellValue("Номер телефону");
        row3.createCell(1).setCellValue(subscriber.getPhoneNumber());

        Row row4 = sheet.createRow(rowIndex++);
        row4.createCell(0).setCellValue("Дата підключення");
        row4.createCell(1).setCellValue(subscriber.getTariffConnectionDate());

        TariffPrepaid tariff = subscriber.getTariff();

        Row row5 = sheet.createRow(rowIndex++);
        row5.createCell(0).setCellValue("Назва тарифу");
        row5.createCell(1).setCellValue(tariff.getName());

        Row row6 = sheet.createRow(rowIndex++);
        row6.createCell(0).setCellValue("Щомісячна плата");
        row6.createCell(1).setCellValue(tariff.getMonthlyFee());

        Row row7 = sheet.createRow(rowIndex++);
        row7.createCell(0).setCellValue("Інтернет (ГБ)");
        row7.createCell(1).setCellValue(tariff.getInternetGigabytesCount());

        Row row8 = sheet.createRow(rowIndex++);
        row8.createCell(0).setCellValue("Хвилини дзвінків");
        row8.createCell(1).setCellValue(tariff.getCallMinutesCount());

        Row row9 = sheet.createRow(rowIndex++);
        row9.createCell(0).setCellValue("SMS");
        row9.createCell(1).setCellValue(tariff.getSmsCount());

        if (tariff instanceof TariffContract) {
            Row row10 = sheet.createRow(rowIndex);
            row10.createCell(0).setCellValue("Додаткові послуги");
            row10.createCell(1).setCellValue(((TariffContract) tariff).getAdditionalServices());
        }

        saveWorkbook(context, workbook, "user_report_" + subscriber.getPhoneNumber() + ".xlsx");
    }

    public static void generateTariffsReport(Context context, List<TariffPrepaid> tariffs) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Тарифи");

        int rowIndex = 0;
        Row header = sheet.createRow(rowIndex++);
        header.createCell(0).setCellValue("Назва тарифу");
        header.createCell(1).setCellValue("Тип");
        header.createCell(2).setCellValue("Щомісячна плата");
        header.createCell(3).setCellValue("Інтернет (ГБ)");
        header.createCell(4).setCellValue("Дзвінки (хв)");
        header.createCell(5).setCellValue("SMS");
        header.createCell(6).setCellValue("Додаткові послуги");

        for (TariffPrepaid tariff : tariffs) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(tariff.getName());
            row.createCell(1).setCellValue(tariff instanceof TariffContract ? "Контракт" : "Передплата");
            row.createCell(2).setCellValue(tariff.getMonthlyFee());
            row.createCell(3).setCellValue(tariff.getInternetGigabytesCount());
            row.createCell(4).setCellValue(tariff.getCallMinutesCount());
            row.createCell(5).setCellValue(tariff.getSmsCount());
            row.createCell(6).setCellValue(tariff instanceof TariffContract ? ((TariffContract) tariff).getAdditionalServices() : "-");
        }

        saveWorkbook(context, workbook, "tariffs_report_" + System.currentTimeMillis() + ".xlsx");
    }

    private static void saveWorkbook(Context context, Workbook workbook, String fileName) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

            Toast.makeText(context, "Звіт збережено: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Файл створено: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Помилка при збереженні XLSX: " + e.getMessage());
            Toast.makeText(context, "Помилка при збереженні звіту", Toast.LENGTH_SHORT).show();
        }
    }
}