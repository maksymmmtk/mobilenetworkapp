package com.example.mobilenetworkapp.models;

public class MobileSubscriber {
    private String surname;
    private String name;
    private String patronymic;
    private String phoneNumber;
    private TariffPrepaid tariff;
    private String tariffConnectionDate;

    public MobileSubscriber(String surname, String name, String patronymic, String phoneNumber,
                            TariffPrepaid tariff, String tariffConnectionDate) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.tariff = tariff;
        this.tariffConnectionDate = tariffConnectionDate;
    }

    public String getSurname() { return surname; }
    public String getName() { return name; }
    public String getPatronymic() { return patronymic; }
    public String getPhoneNumber() { return phoneNumber; }
    public TariffPrepaid getTariff() { return tariff; }
    public String getTariffConnectionDate() { return tariffConnectionDate; }

    public void setSurname(String surname) { this.surname = surname; }
    public void setName(String name) { this.name = name; }
    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setTariff(TariffPrepaid tariff) { this.tariff = tariff; }
    public void setTariffConnectionDate(String tariffConnectionDate) { this.tariffConnectionDate = tariffConnectionDate; }
}