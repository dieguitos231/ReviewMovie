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
    @PostMapping
    public ResponseEntity<Review> crearReview(@RequestBody CreateReviewDTO request) {
        Review reviewCreada = reviewService.crearReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewCreada);
    }

    /**
     * Endpoint para consultar todas las reseñas o filtrar por id de película (?id=... o ?movieId=...).
     */
    @GetMapping
    public ResponseEntity<List<Review>> obtenerReviews(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "movieId", required = false) String movieId) {
        String queryId = id != null ? id : movieId;
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
            @RequestBody CreateReviewDTO request) {
        return ResponseEntity.ok(reviewService.actualizarReview(idReview, request));
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
