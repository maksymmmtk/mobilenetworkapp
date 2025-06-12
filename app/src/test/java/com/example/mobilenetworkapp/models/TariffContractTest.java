package com.example.mobilenetworkapp.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TariffContractTest {

    private TariffContract tariffContract;

    @Before
    public void setUp() {
        tariffContract = new TariffContract(
                "Contract Plus",
                300,
                30,
                1500,
                200,
                "Voicemail, Roaming"
        );
        tariffContract.setId("contract001");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("Contract Plus", tariffContract.getName());
        assertEquals(Integer.valueOf(300), tariffContract.getMonthlyFee());
        assertEquals(Integer.valueOf(30), tariffContract.getInternetGigabytesCount());
        assertEquals(Integer.valueOf(1500), tariffContract.getCallMinutesCount());
        assertEquals(Integer.valueOf(200), tariffContract.getSmsCount());
        assertEquals("Voicemail, Roaming", tariffContract.getAdditionalServices());
        assertEquals("contract001", tariffContract.getId());
    }

    @Test
    public void testSetters() {
        tariffContract.setName("Business Max");
        tariffContract.setMonthlyFee(500);
        tariffContract.setInternetGigabytesCount(50);
        tariffContract.setCallMinutesCount(2000);
        tariffContract.setSmsCount(300);
        tariffContract.setAdditionalServices("Conference Calls, Priority Support");
        tariffContract.setId("contract002");

        assertEquals("Business Max", tariffContract.getName());
        assertEquals(Integer.valueOf(500), tariffContract.getMonthlyFee());
        assertEquals(Integer.valueOf(50), tariffContract.getInternetGigabytesCount());
        assertEquals(Integer.valueOf(2000), tariffContract.getCallMinutesCount());
        assertEquals(Integer.valueOf(300), tariffContract.getSmsCount());
        assertEquals("Conference Calls, Priority Support", tariffContract.getAdditionalServices());
        assertEquals("contract002", tariffContract.getId());
    }
}