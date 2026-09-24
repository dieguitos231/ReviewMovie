package com.reviewmovie.controller;

import com.reviewmovie.Models.Review.Review;
import com.reviewmovie.Services.ReviewService;
import com.reviewmovie.dto.CreateReviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Test
    void testCrearReviewRetorna201ConIdGenerado() throws Exception {
        Review review = new Review(1L, 5, "tt0372784", "Excelente película");
        when(reviewService.crearReview(any(CreateReviewDTO.class))).thenReturn(review);

        String jsonPayload = """
                {
                    "id": "tt0372784",
                    "rating": 5,
                    "description": "Excelente película"
                }
                """;

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReview").value(1))
                .andExpect(jsonPath("$.id").value("tt0372784"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.description").value("Excelente película"));
    }

    @Test
    void testCrearReviewEnRutaSingularRetorna201() throws Exception {
        Review review = new Review(2L, 4, "tt0103359", "Buena serie");
        when(reviewService.crearReview(any(CreateReviewDTO.class))).thenReturn(review);

        String jsonPayload = """
                {
                    "id": "tt0103359",
                    "rating": 4,
                    "description": "Buena serie"
                }
                """;

        mockMvc.perform(post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReview").value(2))
                .andExpect(jsonPath("$.id").value("tt0103359"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void testCrearReviewSinRatingLanzaBadRequest() throws Exception {
        when(reviewService.crearReview(any(CreateReviewDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calificación es obligatoria"));

        String jsonPayload = """
                {
                    "id": "tt0372784",
                    "description": "Sin rating"
                }
                """;

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerReviews() throws Exception {
        Review review = new Review(1L, 5, "tt0372784", "Excelente");
        when(reviewService.obtenerTodas()).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReview").value(1))
                .andExpect(jsonPath("$[0].id").value("tt0372784"))
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void testObtenerReviewPorId() throws Exception {
        Review review = new Review(1L, 5, "tt0372784", "Excelente");
        when(reviewService.buscarPorId(1L)).thenReturn(review);

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReview").value(1))
                .andExpect(jsonPath("$.id").value("tt0372784"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testActualizarReview() throws Exception {
        Review reviewActualizada = new Review(1L, 4, "tt0372784", "Actualizada");
        when(reviewService.actualizarReview(eq(1L), any(CreateReviewDTO.class))).thenReturn(reviewActualizada);

        String jsonPayload = """
                {
                    "rating": 4,
                    "description": "Actualizada"
                }
                """;

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.description").value("Actualizada"));
    }
}
