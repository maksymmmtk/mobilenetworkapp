package com.example.mobilenetworkapp.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MobileSubscriberTest {

    private MobileSubscriber subscriber;
    private TariffPrepaid tariff;

    @Before
    public void setUp() {
        tariff = new TariffPrepaid("Super Tariff", 200, 20, 1000, 100);
        subscriber = new MobileSubscriber(
                "Shevchenko",
                "Taras",
                "Hryhorovych",
                "+380931112233",
                tariff,
                "2024-05-01"
        );
    }

    @Test
    public void testGetters() {
        assertEquals("Shevchenko", subscriber.getSurname());
        assertEquals("Taras", subscriber.getName());
        assertEquals("Hryhorovych", subscriber.getPatronymic());
        assertEquals("+380931112233", subscriber.getPhoneNumber());
        assertEquals(tariff, subscriber.getTariff());
        assertEquals("2024-05-01", subscriber.getTariffConnectionDate());
    }

    @Test
    public void testSetters() {
        TariffPrepaid newTariff = new TariffPrepaid("New Tariff", 150, 10, 500, 50);

        subscriber.setSurname("Kovalenko");
        subscriber.setName("Ivan");
        subscriber.setPatronymic("Ivanovych");
        subscriber.setPhoneNumber("+380991234567");
        subscriber.setTariff(newTariff);
        subscriber.setTariffConnectionDate("2025-01-01");

        assertEquals("Kovalenko", subscriber.getSurname());
        assertEquals("Ivan", subscriber.getName());
        assertEquals("Ivanovych", subscriber.getPatronymic());
        assertEquals("+380991234567", subscriber.getPhoneNumber());
        assertEquals(newTariff, subscriber.getTariff());
        assertEquals("2025-01-01", subscriber.getTariffConnectionDate());
    }
}