package com.example.mobilenetworkapp.models;

public class TariffContract extends TariffPrepaid {
    private String additionalServices;

    public TariffContract(String name, Integer monthlyFee, Integer internetGigabytesCount,
                          Integer callMinutesCount, Integer smsCount, String additionalServices) {
        super(name, monthlyFee, internetGigabytesCount, callMinutesCount, smsCount);
        this.additionalServices = additionalServices;
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }
}

