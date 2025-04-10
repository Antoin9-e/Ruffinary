package com.example.project;

import java.time.LocalDate;

public class Entity {

    private String title;
    private String director;
    private int year;
    private String editor;
    private String genre;
    private String format;
    private  String date_ajout ;

    public Entity(String title, String director, int year, String editor, String genre, String format, String date_ajout) {

        this.title = title;
        this.director = director;
        this.year = year;
        this.genre = genre;
        this.format = format;
        this.date_ajout = date_ajout;
        this.editor = editor;
    }

    public Entity(String format){
        this.title = "Unknown";
        this.director = "Unknown";
        this.year = 0;
        this.genre = "Unknown";
        this.format = format;
        this.date_ajout = LocalDate.now().toString();
    }


    public int getFormatId() {
        return switch (format) {
            case "DVD" -> 3;
            case "Blu-Ray", "BluRay", "BRD" -> 1;
            case "UMD" -> 4;
            case "Laser-Disc" -> 2;
            case "BluRay 4K","Blu-Ray 4k" -> 5;
            default -> 0; // Unknown format

        };
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getFormat() {
        return format;
    }
    public String getDateAjout() {
        return date_ajout;
    }
}
