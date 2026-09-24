package com.reviewmovie.Services;

import com.reviewmovie.Models.Movie.Movie;
import com.reviewmovie.Models.Serie.Serie;
import com.reviewmovie.dto.MovieResumenDTO;
import com.reviewmovie.dto.OmdbResponseDTO;
import com.reviewmovie.dto.OmdbSearchItemDTO;
import com.reviewmovie.dto.OmdbSearchResponseDTO;
import com.reviewmovie.dto.SerieResumenDTO;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class OmdbServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OmdbService omdbService = new OmdbService(objectMapper);

    @Test
    void testMapearPelicula() {
        OmdbResponseDTO dto = new OmdbResponseDTO(
                "Inception",
                "2010",
                "PG-13",
                "16 Jul 2010",
                "148 min",
                "Action, Sci-Fi",
                "Christopher Nolan",
                "Christopher Nolan",
                "Leonardo DiCaprio, Joseph Gordon-Levitt",
                "A thief who steals corporate secrets...",
                "English",
                "USA",
                "Won 4 Oscars",
                "https://image.poster/inception.jpg",
                "8.8",
                "2,000,000",
                "tt1375666",
                "movie",
                null,
                "True",
                null
        );

        Movie movie = omdbService.mapearPelicula(dto);

        assertNotNull(movie);
        assertEquals("tt1375666", movie.id());
        assertEquals("Inception", movie.title());
        assertEquals("2010", movie.year());
        assertEquals("Action, Sci-Fi", movie.genre());
        assertEquals("148 min", movie.runtime());
        assertEquals("Christopher Nolan", movie.director());
        assertEquals("A thief who steals corporate secrets...", movie.plot());
        assertEquals("Leonardo DiCaprio, Joseph Gordon-Levitt", movie.actors());
    }

    @Test
    void testMapearSerie() {
        OmdbResponseDTO dto = new OmdbResponseDTO(
                "Breaking Bad",
                "2008–2013",
                "TV-MA",
                "20 Jan 2008",
                "49 min",
                "Crime, Drama, Thriller",
                "Vince Gilligan",
                "Vince Gilligan",
                "Bryan Cranston, Aaron Paul",
                "A chemistry teacher diagnosed with cancer...",
                "English",
                "USA",
                "Won 16 Emmys",
                "https://image.poster/bb.jpg",
                "9.5",
                "2,200,000",
                "tt0903747",
                "series",
                "5",
                "True",
                null
        );

        Serie serie = omdbService.mapearSerie(dto);

        assertNotNull(serie);
        assertEquals("tt0903747", serie.id());
        assertEquals("Breaking Bad", serie.title());
        assertEquals("2008–2013", serie.year());
        assertEquals("Crime, Drama, Thriller", serie.genre());
        assertEquals("Vince Gilligan", serie.director());
        assertEquals("A chemistry teacher diagnosed with cancer...", serie.plot());
        assertEquals("Bryan Cranston, Aaron Paul", serie.actors());
    }

    @Test
    void testMapearPeliculaResumen() {
        OmdbSearchItemDTO item = new OmdbSearchItemDTO(
                "Batman Begins",
                "2005",
                "tt0372784",
                "movie",
                "https://image.poster/batman.jpg"
        );

        MovieResumenDTO movie = omdbService.mapearPeliculaResumen(item);

        assertNotNull(movie);
        assertEquals("tt0372784", movie.imdbId());
        assertEquals("Batman Begins", movie.title());
        assertEquals("2005", movie.year());
        assertNull(movie.genre(), "genre viene null desde la búsqueda general de OMDb");
    }

    @Test
    void testMapearSerieResumen() {
        OmdbSearchItemDTO item = new OmdbSearchItemDTO(
                "Batman: The Animated Series",
                "1992–1995",
                "tt0103359",
                "series",
                "https://image.poster/batman-series.jpg"
        );

        SerieResumenDTO serie = omdbService.mapearSerieResumen(item);

        assertNotNull(serie);
        assertEquals("tt0103359", serie.imdbId());
        assertEquals("Batman: The Animated Series", serie.title());
        assertEquals("1992–1995", serie.year());
        assertNull(serie.totalSeasons(), "totalSeasons viene null desde la búsqueda general de OMDb");
    }

    @Test
    void testDeserializacionOmdbSearchResponse() throws Exception {
        String json = """
                {
                    "Search": [
                        {
                            "Title": "The Dark Knight",
                            "Year": "2008",
                            "imdbID": "tt0468569",
                            "Type": "movie",
                            "Poster": "https://image.poster/tdk.jpg"
                        }
                    ],
                    "totalResults": "1",
                    "Response": "True"
                }
                """;

        OmdbSearchResponseDTO response = objectMapper.readValue(json, OmdbSearchResponseDTO.class);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("1", response.totalResults());
        assertEquals(1, response.search().size());

        OmdbSearchItemDTO item = response.search().getFirst();
        assertEquals("The Dark Knight", item.title());
        assertEquals("2008", item.year());
        assertEquals("tt0468569", item.imdbId());
        assertEquals("movie", item.type());
        assertEquals("https://image.poster/tdk.jpg", item.poster());
    }
}
