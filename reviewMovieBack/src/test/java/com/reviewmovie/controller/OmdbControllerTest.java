package com.reviewmovie.controller;

import com.reviewmovie.Models.Movie.Movie;
import com.reviewmovie.Models.Serie.Serie;
import com.reviewmovie.Services.OmdbService;
import com.reviewmovie.dto.MovieResumenDTO;
import com.reviewmovie.dto.SerieResumenDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OmdbController.class)
class OmdbControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OmdbService omdbService;

    @Test
    void testBuscarPeliculas() throws Exception {
        MovieResumenDTO movie = new MovieResumenDTO(
                "tt0372784",
                "Batman Begins",
                "2005",
                "Action, Crime, Drama"
        );

        when(omdbService.buscarPeliculas(eq("Batman"), any())).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/omdb/peliculas").param("titulo", "Batman"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].imdbId").value("tt0372784"))
                .andExpect(jsonPath("$[0].title").value("Batman Begins"))
                .andExpect(jsonPath("$[0].year").value("2005"))
                .andExpect(jsonPath("$[0].genre").value("Action, Crime, Drama"));
    }

    @Test
    void testBuscarSeries() throws Exception {
        SerieResumenDTO serie = new SerieResumenDTO(
                "tt0103359",
                "Batman: The Animated Series",
                "1992–1995",
                "4"
        );

        when(omdbService.buscarSeries(eq("Batman"), any())).thenReturn(List.of(serie));

        mockMvc.perform(get("/api/omdb/series").param("titulo", "Batman"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].imdbId").value("tt0103359"))
                .andExpect(jsonPath("$[0].title").value("Batman: The Animated Series"))
                .andExpect(jsonPath("$[0].year").value("1992–1995"))
                .andExpect(jsonPath("$[0].totalSeasons").value("4"));
    }

    @Test
    void testBuscarPorId() throws Exception {
        Movie movieDetalle = new Movie(
                "tt0372784",
                "Batman Begins",
                "2005",
                "Action, Crime, Drama",
                "140 min",
                "Christopher Nolan",
                "Bruce Wayne trains...",
                "Christian Bale"
        );

        when(omdbService.buscarPorId("tt0372784")).thenReturn(movieDetalle);

        mockMvc.perform(get("/api/omdb/id/tt0372784"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("tt0372784"))
                .andExpect(jsonPath("$.title").value("Batman Begins"))
                .andExpect(jsonPath("$.genre").value("Action, Crime, Drama"))
                .andExpect(jsonPath("$.director").value("Christopher Nolan"))
                .andExpect(jsonPath("$.runtime").value("140 min"))
                .andExpect(jsonPath("$.plot").value("Bruce Wayne trains..."));
    }

    @Test
    void testBuscarPeliculasSinQueryParam() throws Exception {
        mockMvc.perform(get("/api/omdb/peliculas"))
                .andExpect(status().isBadRequest());
    }
}
