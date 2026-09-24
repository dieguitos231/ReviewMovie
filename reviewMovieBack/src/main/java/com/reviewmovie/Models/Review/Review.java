package com.reviewmovie.Models.Review;

public record Review(
    Long idReview,
    Integer rating,
    String id,
    String description
) {
    public Review(Long idReview, String id, Integer rating, String description) {
        this(idReview, rating, id, description);
    }
}
