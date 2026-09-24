package com.reviewmovie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieResumenDTO(
    String imdbId,
    String poster,
    String title,
    String year
){
    @JsonProperty("id")
    public String id() {
        return imdbId;
    }
}