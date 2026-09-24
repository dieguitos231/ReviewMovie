package com.reviewmovie.Models.Movie;

public record Movie(
    String id,
    String title,
    String year,
    String genre,
    String runtime,
    String director,
    String plot,
    String actors
) {

    public Movie(String id, String title, String year, String runtime, String director, String plot, String actors) {
        this(id, title, year, null, runtime, director, plot, actors);
    }
    public Movie(Long id, String title, String year, String genre, String runtime, String director, String plot, String actors) {
        this(id != null ? String.valueOf(id) : null, title, year, genre, runtime, director, plot, actors);
    }
}
