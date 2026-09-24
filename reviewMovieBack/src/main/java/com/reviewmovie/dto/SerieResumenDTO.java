package com.reviewmovie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SerieResumenDTO(
    String imdbId,
    String poster,
    String title,
    String year,
    String totalSeasons
) {
    @JsonProperty("id")
    public String id() {
        return imdbId;
    }
}
