package com.reviewmovie.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateReviewDTO(
    @JsonProperty("idReview")
    Long idReview,

    @JsonProperty("id")
    @JsonAlias({"imdbId", "imdbID", "movieId", "idMovie", "peliculaId", "itemId", "item_id"})
    String id,

    @JsonProperty("rating")
    Integer rating,

    @JsonProperty("description")
    String description
) {
    public CreateReviewDTO(String id, Integer rating, String description) {
        this(null, id, rating, description);
    }
}
