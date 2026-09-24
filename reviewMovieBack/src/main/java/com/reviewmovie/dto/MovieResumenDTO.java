package com.reviewmovie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieResumenDTO(
    String imdbId,
    String title,
    String year,
    String poster
){
    @JsonProperty("id")
    public String id() {
        return imdbId;
    }
}