package com.reviewmovie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OmdbSearchResponseDTO(
    @JsonProperty("Search") List<OmdbSearchItemDTO> search,
    @JsonProperty("totalResults") String totalResults,
    @JsonProperty("Response") String response,
    @JsonProperty("Error") String error
) {
    public boolean isSuccess() {
        return "True".equalsIgnoreCase(response);
    }
}
