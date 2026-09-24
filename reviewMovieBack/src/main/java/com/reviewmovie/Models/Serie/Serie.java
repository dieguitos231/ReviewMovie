package com.reviewmovie.Models.Serie;

public record Serie(
    String id,
    String image,
    String title,
    String type,
    String year,
    String genre,
    String director,
    String plot,
    String actors
) {
    public Serie {
        if (type == null) {
            type = "serie";
        }
    }

    public Serie(String id, String image, String title, String genre, String director, String plot, String actors) {
        this(id, image, title, "serie", null, genre, director, plot, actors);
    }

    public Serie(String id, String image, String title, String year, String genre, String director, String plot, String actors) {
        this(id, image, title, "serie", year, genre, director, plot, actors);
    }
}
