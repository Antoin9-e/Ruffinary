package com.example.project;

public class Ld extends Entity{
    private String country;
    private String prix;
    private String code;

    public Ld(String title, String director, int year, String editor, String genre, String format, String date_ajout, String country, String prix, String code) {
        super(title, director, year, editor, genre, format, date_ajout);
        this.country = country;
        this.prix = prix;
        this.code = code;
    }
    public String getCountry() {
        return country;
    }

    public String getPrix() {
        return prix;
    }
    public String getCode() {
        return code;
    }
}
