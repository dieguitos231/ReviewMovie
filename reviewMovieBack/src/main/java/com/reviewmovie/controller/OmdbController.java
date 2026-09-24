package com.reviewmovie.controller;

import com.reviewmovie.Services.OmdbService;
import com.reviewmovie.dto.MovieResumenDTO;
import com.reviewmovie.dto.SerieResumenDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/omdb")
public class OmdbController {

    private final OmdbService omdbService;

    public OmdbController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    /**
     * Endpoint para la búsqueda de películas mediante el parámetro 's' de OMDb.
     * Retorna una lista de películas que coinciden con el nombre buscado.
     * Admite parámetros 'titulo' o 'nombre' y opcionalmente 'page'.
     * Ejemplo: GET /api/omdb/peliculas?titulo=Batman
     */
    @GetMapping({"/peliculas", "/pelicula"})
    public ResponseEntity<List<MovieResumenDTO>> buscarPeliculas(
            @RequestParam(name = "titulo", required = false) String titulo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "s", required = false) String s,
            @RequestParam(name = "page", required = false) Integer page) {
        String query = titulo != null ? titulo : (nombre != null ? nombre : s);
        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(omdbService.buscarPeliculas(query.trim(), page));
    }

    /**
     * Endpoint para la búsqueda de series mediante el parámetro 's' de OMDb.
     * Retorna una lista de series que coinciden con el nombre buscado.
     * Admite parámetros 'titulo' o 'nombre' y opcionalmente 'page'.
     * Ejemplo: GET /api/omdb/series?titulo=Batman
     */
    @GetMapping({"/series", "/serie"})
    public ResponseEntity<List<SerieResumenDTO>> buscarSeries(
            @RequestParam(name = "titulo", required = false) String titulo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "s", required = false) String s,
            @RequestParam(name = "page", required = false) Integer page) {
        String query = titulo != null ? titulo : (nombre != null ? nombre : s);
        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(omdbService.buscarSeries(query.trim(), page));
    }

    /**
     * Endpoint para obtener toda la información específica de una película o serie
     * a partir de su ID de IMDb (ej: tt0372784).
     * Ideal para el botón "Más información" de cada carta.
     * Ejemplo: GET /api/omdb/id/tt0372784
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Record> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(omdbService.buscarPorId(id));
    }

    /**
     * Endpoint general: busca películas por coincidencia de nombre (parámetro 's').
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<MovieResumenDTO>> buscar(
            @RequestParam(name = "titulo", required = false) String titulo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "page", required = false) Integer page) {
        String query = titulo != null ? titulo : nombre;
        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(omdbService.buscarPeliculas(query.trim(), page));
    }
}