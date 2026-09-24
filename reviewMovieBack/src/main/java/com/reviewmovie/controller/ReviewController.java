package com.reviewmovie.controller;

import com.reviewmovie.Models.Review.Review;
import com.reviewmovie.Services.ReviewService;
import com.reviewmovie.dto.CreateReviewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/reviews", "/api/review"})
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Endpoint para la creación de una nueva review.
     * El ID (idReview) es generado automáticamente por el generador de IDs.
     * Ejemplo de uso: POST /api/reviews
     * Body: { "id": "tt0372784", "rating": 5, "description": "Excelente película" }
     */


    /*
    @PostMapping({})
    public ResponseEntity<Review> crearReview(
            @PathVariable(name = "peliculaId", required = false) String pathPeliculaId,
            @RequestParam(name = "id", required = false) String queryId,
            @RequestParam(name = "movieId", required = false) String queryMovieId,
            @RequestParam(name = "imdbId", required = false) String queryImdbId,
            @RequestBody(required = false) CreateReviewDTO request) {
        String finalId = null;
        if (request != null && request.id() != null && !request.id().isBlank()) {
            finalId = request.id();
        } else if (pathPeliculaId != null && !pathPeliculaId.isBlank()) {
            finalId = pathPeliculaId;
        } else if (queryId != null && !queryId.isBlank()) {
            finalId = queryId;
        } else if (queryMovieId != null && !queryMovieId.isBlank()) {
            finalId = queryMovieId;
        } else if (queryImdbId != null && !queryImdbId.isBlank()) {
            finalId = queryImdbId;
        }

        CreateReviewDTO finalRequest = new CreateReviewDTO(
                request != null ? request.idReview() : null,
                finalId,
                request != null ? request.rating() : null,
                request != null ? request.description() : null
        );

        Review reviewCreada = reviewService.crearReview(finalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewCreada);
    }
     */

    /**
     * Endpoint para la creacion de una nueva review
     * 
     * 
     * 
     * 
    */

    /**
     * Endpoint para consultar todas las reseñas o filtrar por id de película (?id=..., ?movieId=... o ?imdbId=...).
     */
    @GetMapping
    public ResponseEntity<List<Review>> obtenerReviews(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "movieId", required = false) String movieId,
            @RequestParam(name = "imdbId", required = false) String imdbId) {
        String queryId = id != null ? id : (movieId != null ? movieId : imdbId);
        if (queryId != null && !queryId.isBlank()) {
            return ResponseEntity.ok(reviewService.buscarPorPeliculaId(queryId.trim()));
        }
        return ResponseEntity.ok(reviewService.obtenerTodas());
    }

    /**
     * Endpoint para consultar una reseña por su id autogenerado (idReview).
     */
    @GetMapping("/{idReview}")
    public ResponseEntity<Review> obtenerPorId(@PathVariable Long idReview) {
        return ResponseEntity.ok(reviewService.buscarPorId(idReview));
    }

    /**
     * Endpoint para consultar la reseña de una película o serie específica.
     */
    @GetMapping("/pelicula/{id}")
    public ResponseEntity<Review> obtenerPorPeliculaId(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.buscarUnaPorPeliculaId(id));
    }

    /**
     * Endpoint para actualizar la calificación o descripción de una reseña ya creada.
     */
    @PutMapping("/{idReview}")
    public ResponseEntity<Review> actualizarReview(
            @PathVariable Long idReview,
            @RequestParam(name = "id", required = false) String queryId,
            @RequestParam(name = "movieId", required = false) String queryMovieId,
            @RequestParam(name = "imdbId", required = false) String queryImdbId,
            @RequestBody CreateReviewDTO request) {
        String finalId = null;
        if (request != null && request.id() != null && !request.id().isBlank()) {
            finalId = request.id();
        } else if (queryId != null && !queryId.isBlank()) {
            finalId = queryId;
        } else if (queryMovieId != null && !queryMovieId.isBlank()) {
            finalId = queryMovieId;
        } else if (queryImdbId != null && !queryImdbId.isBlank()) {
            finalId = queryImdbId;
        }

        CreateReviewDTO finalRequest = new CreateReviewDTO(
                request != null ? request.idReview() : null,
                finalId,
                request != null ? request.rating() : null,
                request != null ? request.description() : null
        );

        return ResponseEntity.ok(reviewService.actualizarReview(idReview, finalRequest));
    }

    /**
     * Endpoint para eliminar una reseña existente.
     */
    @DeleteMapping("/{idReview}")
    public ResponseEntity<Void> eliminarReview(@PathVariable Long idReview) {
        reviewService.eliminarReview(idReview);
        return ResponseEntity.noContent().build();
    }
}
