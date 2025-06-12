package com.example.mobilenetworkapp.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TariffPrepaidTest {

    private TariffPrepaid tariff;

    @Before
    public void setUp() {
        tariff = new TariffPrepaid("Basic", 100, 10, 300, 50);
        tariff.setId("tariff001");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("Basic", tariff.getName());
        assertEquals(Integer.valueOf(100), tariff.getMonthlyFee());
        assertEquals(Integer.valueOf(10), tariff.getInternetGigabytesCount());
        assertEquals(Integer.valueOf(300), tariff.getCallMinutesCount());
        assertEquals(Integer.valueOf(50), tariff.getSmsCount());
        assertEquals("tariff001", tariff.getId());
    }

    @Test
    public void testSetters() {
        tariff.setName("Premium");
        tariff.setMonthlyFee(200);
        tariff.setInternetGigabytesCount(20);
        tariff.setCallMinutesCount(500);
        tariff.setSmsCount(100);
        tariff.setId("tariff002");

        assertEquals("Premium", tariff.getName());
        assertEquals(Integer.valueOf(200), tariff.getMonthlyFee());
        assertEquals(Integer.valueOf(20), tariff.getInternetGigabytesCount());
        assertEquals(Integer.valueOf(500), tariff.getCallMinutesCount());
        assertEquals(Integer.valueOf(100), tariff.getSmsCount());
        assertEquals("tariff002", tariff.getId());
    }

    @Test
    public void testEmptyConstructor() {
        TariffPrepaid emptyTariff = new TariffPrepaid();
        assertNull(emptyTariff.getName());
        assertNull(emptyTariff.getMonthlyFee());
        assertNull(emptyTariff.getInternetGigabytesCount());
        assertNull(emptyTariff.getCallMinutesCount());
        assertNull(emptyTariff.getSmsCount());
        assertNull(emptyTariff.getId());
    }
}