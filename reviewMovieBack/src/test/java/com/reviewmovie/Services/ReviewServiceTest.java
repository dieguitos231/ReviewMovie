package com.reviewmovie.Services;

import com.reviewmovie.Models.Review.Review;
import com.reviewmovie.dto.CreateReviewDTO;
import com.reviewmovie.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
    }

    @Test
    void testCrearReviewGeneraIdConGeneratedValue() {
        CreateReviewDTO dto1 = new CreateReviewDTO("tt0372784", 5, "Excelente película");
        Review review1 = reviewService.crearReview(dto1);

        assertNotNull(review1);
        assertNotNull(review1.getIdReview(), "El ID debe ser autogenerado por @GeneratedValue");
        assertEquals("tt0372784", review1.getId());
        assertEquals(5, review1.getRating());
        assertEquals("Excelente película", review1.getDescription());

        CreateReviewDTO dto2 = new CreateReviewDTO("tt0103359", 4, "Buena serie");
        Review review2 = reviewService.crearReview(dto2);

        assertNotNull(review2);
        assertNotNull(review2.getIdReview());
        assertNotEquals(review1.getIdReview(), review2.getIdReview(), "Cada review debe tener un ID diferente generado");
        assertEquals("tt0103359", review2.getId());
        assertEquals(4, review2.getRating());
    }

    @Test
    void testCrearReviewSinIdPeliculaGeneraIdentificador() {
        CreateReviewDTO dto = new CreateReviewDTO(null, 5, "Sin id de película previo");
        Review review = reviewService.crearReview(dto);

        assertNotNull(review);
        assertNotNull(review.getIdReview());
        assertNotNull(review.getId());
        assertTrue(review.getId().contains(String.valueOf(review.getIdReview())));
    }

    @Test
    void testCrearReviewSinRatingLanzaBadRequest() {
        CreateReviewDTO dto = new CreateReviewDTO("tt0372784", null, "Sin calificacion");
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> reviewService.crearReview(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("calificación es obligatoria"));
    }

    @Test
    void testCrearReviewDuplicadaParaMismaPeliculaLanzaConflict() {
        CreateReviewDTO dto1 = new CreateReviewDTO("tt0372784", 5, "Primera reseña");
        reviewService.crearReview(dto1);

        CreateReviewDTO dto2 = new CreateReviewDTO("tt0372784", 4, "Segunda reseña para la misma");
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> reviewService.crearReview(dto2));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testBuscarPorIdYPorPeliculaId() {
        CreateReviewDTO dto = new CreateReviewDTO("tt0372784", 5, "Batman Begins");
        Review creada = reviewService.crearReview(dto);

        Review encontrada = reviewService.buscarPorId(creada.getIdReview());
        assertEquals(creada.getIdReview(), encontrada.getIdReview());

        List<Review> porPelicula = reviewService.buscarPorPeliculaId("tt0372784");
        assertEquals(1, porPelicula.size());
        assertEquals("tt0372784", porPelicula.get(0).getId());
    }

    @Test
    void testActualizarReview() {
        CreateReviewDTO dto = new CreateReviewDTO("tt0372784", 4, "Buena");
        Review creada = reviewService.crearReview(dto);

        CreateReviewDTO updateDto = new CreateReviewDTO("tt0372784", 5, "Increíble tras volver a verla");
        Review actualizada = reviewService.actualizarReview(creada.getIdReview(), updateDto);

        assertEquals(5, actualizada.getRating());
        assertEquals("Increíble tras volver a verla", actualizada.getDescription());
        assertEquals(creada.getIdReview(), actualizada.getIdReview());
    }

    @Test
    void testEliminarReview() {
        CreateReviewDTO dto = new CreateReviewDTO("tt0372784", 5, "Para borrar");
        Review creada = reviewService.crearReview(dto);

        reviewService.eliminarReview(creada.getIdReview());

        assertThrows(ResponseStatusException.class, () -> reviewService.buscarPorId(creada.getIdReview()));
    }
}
