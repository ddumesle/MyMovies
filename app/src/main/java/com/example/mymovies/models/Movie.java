package com.example.mymovies.models;

/**
 * A class the models a movie object.
 */
public class Movie {

    private String year;
    private String title;
    private String poster;
    private String imdbID;

    /**
     * Parameterized Constructor.
     * @param imdbID imdb ID of the movie
     */
    public Movie(String imdbID) {
        this.imdbID = imdbID;
    }

    /**
     * A function to set the movie's release year.
     * @param year year of movie release
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * A function to set the movie's title.
     * @param title title of the movie
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A function to set the movie's poster.
     * @param poster poster url of the movie
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * A function that returns the movie's release
     * year
     * @return year of the movie's release
     */
    public String getYear() {
        return year;
    }

    /**
     * A function that returns the movie's title.
     * @return title of the movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * A function that returns the movie's poster
     * url
     * @return url of the movie poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     * A function that returns the movie's imdb ID.
     * @return imdb ID of the movie
     */
    public String getImdbID() {
        return imdbID;
    }
}
