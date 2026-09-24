package com.reviewmovie.Models.Movie;

public record Movie(
    String id,
    String title,
    String year,
    String runtime,
    String director,
    String plot,
    String actors
) {

    public Movie(Long id, String title, String year, String runtime, String director, String plot, String actors) {
        this(id != null ? String.valueOf(id) : null, title, year, runtime, director, plot, actors);
    }
}
