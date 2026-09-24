package com.reviewmovie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OmdbSearchItemDTO(
    @JsonProperty("Title") String title,
    @JsonProperty("Year") String year,
    @JsonProperty("imdbID") String imdbId,
    @JsonProperty("Type") String type,
    @JsonProperty("Poster") String poster
) {}
