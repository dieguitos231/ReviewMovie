package com.reviewmovie.dto;

public record CreateReviewDTO(
    Long idReview,
    String id,
    Integer rating,
    String description
) {
    public CreateReviewDTO(String id, Integer rating, String description) {
        this(null, id, rating, description);
    }
}
