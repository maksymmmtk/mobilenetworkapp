package com.example.mobilenetworkapp.models;

public class TariffPrepaid {
    private String name;
    private Integer monthlyFee;
    private Integer internetGigabytesCount;
    private Integer callMinutesCount;
    private Integer smsCount;
    private String id;

    public TariffPrepaid() {}

    public TariffPrepaid(String name, Integer monthlyFee, Integer internetGigabytesCount, Integer callMinutesCount, Integer smsCount) {
        this.name = name;
        this.monthlyFee = monthlyFee;
        this.internetGigabytesCount = internetGigabytesCount;
        this.callMinutesCount = callMinutesCount;
        this.smsCount = smsCount;

    }


    public String getName() { return name; }
    public Integer getMonthlyFee() { return monthlyFee; }
    public Integer getInternetGigabytesCount() { return internetGigabytesCount; }
    public Integer getCallMinutesCount() { return callMinutesCount; }
    public Integer getSmsCount() { return smsCount; }

    public String getId() {
        return id;
    }

    public void setName(String name) { this.name = name; }
    public void setMonthlyFee(int price) { this.monthlyFee = price; }
    public void setInternetGigabytesCount(int internetGigabytesCount) { this.internetGigabytesCount = internetGigabytesCount; }
    public void setCallMinutesCount(int callMinutesCount) { this.callMinutesCount = callMinutesCount; }
    public void setSmsCount(int smsCount) { this.smsCount = smsCount; }

    public void setId(String id) {
        this.id = id;
    }
}
