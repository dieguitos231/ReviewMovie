package com.reviewmovie.repository;

import com.reviewmovie.Models.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE LOWER(r.id) = LOWER(:peliculaId)")
    List<Review> findByMovieOrSerieId(@Param("peliculaId") String peliculaId);

    @Query("SELECT (COUNT(r) > 0) FROM Review r WHERE LOWER(r.id) = LOWER(:peliculaId)")
    boolean existsByMovieOrSerieId(@Param("peliculaId") String peliculaId);
}
