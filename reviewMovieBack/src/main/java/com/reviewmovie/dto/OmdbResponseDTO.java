package com.reviewmovie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OmdbResponseDTO(
    @JsonProperty("Title") String title,
    @JsonProperty("Year") String year,
    @JsonProperty("Rated") String rated,
    @JsonProperty("Released") String released,
    @JsonProperty("Runtime") String runtime,
    @JsonProperty("Genre") String genre,
    @JsonProperty("Director") String director,
    @JsonProperty("Writer") String writer,
    @JsonProperty("Actors") String actors,
    @JsonProperty("Plot") String plot,
    @JsonProperty("Language") String language,
    @JsonProperty("Country") String country,
    @JsonProperty("Awards") String awards,
    @JsonProperty("Poster") String poster,
    @JsonProperty("imdbRating") String imdbRating,
    @JsonProperty("imdbVotes") String imdbVotes,
    @JsonProperty("imdbID") String imdbId,
    @JsonProperty("Type") String type,
    @JsonProperty("totalSeasons") String totalSeasons,
    @JsonProperty("Response") String response,
    @JsonProperty("Error") String error
) {
    public boolean isSuccess() {
        return "True".equalsIgnoreCase(response);
    }
}
