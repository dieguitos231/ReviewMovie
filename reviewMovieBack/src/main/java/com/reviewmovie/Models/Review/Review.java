package com.reviewmovie.Models.Review;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReview;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "item_id")
    private String id;

    @Column(length = 2000)
    private String description;

    public Review() {
    }

    public Review(Long idReview, Integer rating, String id, String description) {
        this.idReview = idReview;
        this.rating = rating;
        this.id = id;
        this.description = description;
    }

    public Review(Long idReview, String id, Integer rating, String description) {
        this(idReview, rating, id, description);
    }

    public Review(String id, Integer rating, String description) {
        this(null, rating, id, description);
    }

    public Review(Integer rating, String id, String description) {
        this(null, rating, id, description);
    }

    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long idReview() {
        return idReview;
    }

    public Integer rating() {
        return rating;
    }

    public String id() {
        return id;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(idReview, review.idReview) &&
                Objects.equals(rating, review.rating) &&
                Objects.equals(id, review.id) &&
                Objects.equals(description, review.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReview, rating, id, description);
    }

    @Override
    public String toString() {
        return "Review{" +
                "idReview=" + idReview +
                ", rating=" + rating +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
