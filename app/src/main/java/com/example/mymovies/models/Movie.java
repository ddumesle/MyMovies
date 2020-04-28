package com.example.mymovies.models;

public class Movie {

    private String year;
    private String title;
    private String poster;
    private String imdbID;
    private String documentID;

    public Movie(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getDocumentID() {
        return documentID;
    }
}
