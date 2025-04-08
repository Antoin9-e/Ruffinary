package com.example.project;

public class Entity {

    private String title;
    private String director;
    private int year;
    private String genre;
    private String format;
    private String date_ajout;

    public Entity(String title, String director, int year, String genre, String format, String date_ajout) {

        this.title = title;
        this.director = director;
        this.year = year;
        this.genre = genre;
        this.format = format;
        this.date_ajout = date_ajout;
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
