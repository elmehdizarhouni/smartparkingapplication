package com.example.parkingapp;

public class User {
    private String nom;
    private double solde;  // Utilisation de double pour solde
    private String tel;

    // Constructeur sans argument requis pour Firestore
    public User() {}

    public User(String nom, double solde, String tel) {
        this.nom = nom;
        this.solde = solde;
        this.tel = tel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
