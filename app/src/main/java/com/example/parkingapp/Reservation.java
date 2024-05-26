package com.example.parkingapp;

public class Reservation {
    private String client;
    private String parking;
    private boolean payed;

    // Constructeur sans argument requis pour Firestore
    public Reservation() {}

    public Reservation(String client, String parking, boolean payed) {
        this.client = client;
        this.parking = parking;
        this.payed = payed;
    }

    // Getters et Setters
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }
}
