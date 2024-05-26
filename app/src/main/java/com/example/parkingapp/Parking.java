package com.example.parkingapp;

public class Parking {
    private String name;
    private String update;
    private boolean taken;

    // Constructeur sans argument requis pour Firestore
    public Parking() {
        // Empty constructor required for Firestore
    }

    public Parking(String name, boolean taken, String update) {
        this.name = name;
        this.taken = taken;
        this.update = update;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
