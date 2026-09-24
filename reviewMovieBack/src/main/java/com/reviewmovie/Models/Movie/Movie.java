package com.reviewmovie.Models.Movie;

public record Movie(
    String id,
    String image,
    String title,
    String type,
    String year,
    String runtime,
    String director,
    String plot,
    String actors
) {
    public Movie {
        if (type == null) {
            type = "movie";
        }
    }

    public Movie(String id, String image, String title, String year, String runtime, String director, String plot, String actors) {
        this(id, image, title, "movie", year, runtime, director, plot, actors);
    }

    public Movie(Long id, String image, String title, String type, String year, String runtime, String director, String plot, String actors) {
        this(id != null ? String.valueOf(id) : null, image, title, type, year, runtime, director, plot, actors);
    }
}
