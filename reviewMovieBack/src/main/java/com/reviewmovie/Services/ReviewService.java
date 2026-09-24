package com.reviewmovie.Services;

import com.reviewmovie.Models.Review.Review;
import com.reviewmovie.dto.CreateReviewDTO;
import com.reviewmovie.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Crea una nueva reseña donde el idReview es generado automáticamente
     * por la anotación @GeneratedValue(strategy = GenerationType.IDENTITY)
     * al persistir la entidad con JPA.
     * Reglas de negocio:
     * - La calificación es obligatoria.
     * - Solo se puede crear una reseña por película o serie.
     */
    public Review crearReview(CreateReviewDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos de la reseña son obligatorios");
        }

        if (dto.rating() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calificación es obligatoria");
        }

        String peliculaId = (dto.id() != null && !dto.id().isBlank())
                ? dto.id().trim()
                : null;

        // Validar que no exista ya una reseña para esta película o serie
        if (peliculaId != null) {
            boolean yaExiste = reviewRepository.existsByMovieOrSerieId(peliculaId);
            if (yaExiste) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Solo se puede crear una reseña por película o serie. Ya existe una reseña para el id: " + peliculaId
                );
            }
        }

        // Se instancia con idReview = null para que @GeneratedValue genere el identificador
        Review review = new Review();
        review.setRating(dto.rating());
        review.setDescription(dto.description());
        review.setId(peliculaId);

        Review guardada = reviewRepository.save(review);

        // Si no venía id de película, se asigna uno basado en el id autogenerado
        if (guardada.getId() == null) {
            guardada.setId("item-" + guardada.getIdReview());
            guardada = reviewRepository.save(guardada);
        }

        return guardada;
    }

    public List<Review> obtenerTodas() {
        return reviewRepository.findAll();
    }

    public Review buscarPorId(Long idReview) {
        return reviewRepository.findById(idReview)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reseña no encontrada con id: " + idReview
                ));
    }

    public List<Review> buscarPorPeliculaId(String peliculaId) {
        if (peliculaId == null || peliculaId.isBlank()) {
            return Collections.emptyList();
        }
        return reviewRepository.findByMovieOrSerieId(peliculaId.trim());
    }

    public Review buscarUnaPorPeliculaId(String peliculaId) {
        return buscarPorPeliculaId(peliculaId).stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró reseña para la película o serie con id: " + peliculaId
                ));
    }

    public Review actualizarReview(Long idReview, CreateReviewDTO dto) {
        Review reviewExistente = buscarPorId(idReview);

        if (dto.rating() != null) {
            reviewExistente.setRating(dto.rating());
        }
        if (dto.description() != null) {
            reviewExistente.setDescription(dto.description());
        }

        return reviewRepository.save(reviewExistente);
    }

    public void eliminarReview(Long idReview) {
        Review review = buscarPorId(idReview);
        reviewRepository.delete(review);
    }
}
