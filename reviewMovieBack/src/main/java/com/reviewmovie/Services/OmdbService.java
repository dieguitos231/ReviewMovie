package com.reviewmovie.Services;

import tools.jackson.databind.ObjectMapper;
import com.reviewmovie.Models.Movie.Movie;
import com.reviewmovie.Models.Serie.Serie;
import com.reviewmovie.dto.MovieResumenDTO;
import com.reviewmovie.dto.OmdbResponseDTO;
import com.reviewmovie.dto.OmdbSearchItemDTO;
import com.reviewmovie.dto.OmdbSearchResponseDTO;
import com.reviewmovie.dto.SerieResumenDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class OmdbService {

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OmdbService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    /**
     * Búsqueda de películas mediante el parámetro 's' de OMDb.
     * Retorna una lista de películas mapeadas al modelo Movie.
     */
    public List<MovieResumenDTO> buscarPeliculas(String titulo) {
        return buscarPeliculas(titulo, null);
    }

    public List<MovieResumenDTO> buscarPeliculas(String titulo, Integer page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("s", titulo)
                .queryParam("type", "movie");

        if (page != null && page > 0) {
            builder.queryParam("page", page);
        }

        URI uri = builder.build().encode().toUri();
        OmdbSearchResponseDTO response = consultarOmdbSearch(uri);

        if (!response.isSuccess()) {
            if (response.error() != null && response.error().toLowerCase().contains("key")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, response.error());
            }
            return Collections.emptyList();
        }

        if (response.search() == null) {
            return Collections.emptyList();
        }

        return response.search().parallelStream()
                .map(this::mapearPeliculaResumen)
                .toList();
    }

    /**
     * Búsqueda de series mediante el parámetro 's' de OMDb.
     * Retorna una lista de series mapeadas al modelo Serie.
     */
    public List<SerieResumenDTO> buscarSeries(String titulo) {
        return buscarSeries(titulo, null);
    }

    public List<SerieResumenDTO> buscarSeries(String titulo, Integer page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("s", titulo)
                .queryParam("type", "series");

        if (page != null && page > 0) {
            builder.queryParam("page", page);
        }

        URI uri = builder.build().encode().toUri();
        OmdbSearchResponseDTO response = consultarOmdbSearch(uri);

        if (!response.isSuccess()) {
            if (response.error() != null && response.error().toLowerCase().contains("key")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, response.error());
            }
            return Collections.emptyList();
        }

        if (response.search() == null) {
            return Collections.emptyList();
        }

        return response.search().parallelStream()
                .map(this::mapearSerieResumen)
                .toList();
    }

    /**
     * Busca en OMDb por ID de IMDb (ej: "tt1375666") y asigna los datos completos
     * al record Movie o Serie según corresponda. Ideal para el botón de "Más información".
     */
    public Record buscarPorId(String imdbId) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("i", imdbId)
                .build()
                .encode()
                .toUri();

        return consultarYMapear(uri);
    }

    /**
     * Busca en OMDb por título exacto (parámetro 't') y asigna automáticamente
     * al record Movie o Serie según el tipo devuelto.
     */
    public Record buscarPorTitulo(String titulo) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("t", titulo)
                .build()
                .encode()
                .toUri();

        return consultarYMapear(uri);
    }

    /**
     * Busca específicamente una película por título exacto (parámetro 't').
     */
    public Movie buscarPelicula(String titulo) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("t", titulo)
                .queryParam("type", "movie")
                .build()
                .encode()
                .toUri();

        OmdbResponseDTO omdb = consultarOmdb(uri);
        return mapearPelicula(omdb);
    }

    /**
     * Busca específicamente una serie por título exacto (parámetro 't').
     */
    public Serie buscarSerie(String titulo) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("t", titulo)
                .queryParam("type", "series")
                .build()
                .encode()
                .toUri();

        OmdbResponseDTO omdb = consultarOmdb(uri);
        return mapearSerie(omdb);
    }

    /**
     * Realiza la llamada HTTP a OMDb para búsquedas (parámetro 's')
     * y deserializa en OmdbSearchResponseDTO.
     */
    public OmdbSearchResponseDTO consultarOmdbSearch(URI uri) {
        try {
            String respuestaJson = restTemplate.getForObject(uri, String.class);
            System.out.println("Respuesta de OMDb (Búsqueda):");
            System.out.println(respuestaJson);

            return objectMapper.readValue(respuestaJson, OmdbSearchResponseDTO.class);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al comunicarse con la API de OMDb: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Realiza la llamada HTTP a OMDb para un elemento específico (por 'i' o 't')
     * y deserializa en OmdbResponseDTO.
     */
    public OmdbResponseDTO consultarOmdb(URI uri) {
        try {
            String respuestaJson = restTemplate.getForObject(uri, String.class);
            System.out.println("Respuesta de OMDb (Detalle):");
            System.out.println(respuestaJson);

            OmdbResponseDTO omdb = objectMapper.readValue(respuestaJson, OmdbResponseDTO.class);

            if (!omdb.isSuccess()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        omdb.error() != null ? omdb.error() : "Contenido no encontrado en OMDb"
                );
            }

            return omdb;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al comunicarse con la API de OMDb: " + e.getMessage(),
                    e
            );
        }
    }

    public OmdbResponseDTO consultarOmdb(String url) {
        return consultarOmdb(URI.create(url));
    }

    /**
     * Consulta OMDb y según el tipo ("series" o "movie") asigna a Serie o Movie.
     */
    private Record consultarYMapear(URI uri) {
        OmdbResponseDTO omdb = consultarOmdb(uri);

        if ("series".equalsIgnoreCase(omdb.type())) {
            return mapearSerie(omdb);
        } else {
            return mapearPelicula(omdb);
        }
    }

    /**
     * Asigna los datos de un item de búsqueda al modelo Movie para mostrar en cartas.
     * Consulta el detalle para obtener el género y evitar valores nulos.
     */
    public MovieResumenDTO mapearPeliculaResumen(OmdbSearchItemDTO item) {
        return new MovieResumenDTO(
                item.imdbId(),
                item.poster(),
                item.title(),
                item.year()
        );
    }

    public MovieResumenDTO mapearPeliculaResumen(OmdbSearchItemDTO item, String genre) {
        return new MovieResumenDTO(
                item.imdbId(),
                item.poster(),
                item.title(),
                item.year()
        );
    }

    /**
     * Asigna los datos de un item de búsqueda al modelo Serie para mostrar en cartas.
     * Consulta el detalle para obtener el total de temporadas y evitar valores nulos.
     */
    public SerieResumenDTO mapearSerieResumen(OmdbSearchItemDTO item) {
        String totalSeasons = obtenerTotalSeasonsPorId(item.imdbId());
        return new SerieResumenDTO(
                item.imdbId(),
                item.poster(),
                item.title(),
                item.year(),
                totalSeasons
        );
    }

    public SerieResumenDTO mapearSerieResumen(OmdbSearchItemDTO item, String totalSeasons) {
        return new SerieResumenDTO(
                item.imdbId(),
                item.poster(),
                item.title(),
                item.year(),
                totalSeasons
        );
    }

    /**
     * Obtiene el género de un contenido por su ID de IMDb.
     */
    public String obtenerGeneroPorId(String imdbId) {
        if (imdbId == null || imdbId.isBlank() || apiKey == null || apiKey.isBlank()) {
            return null;
        }
        try {
            URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("apikey", apiKey)
                    .queryParam("i", imdbId)
                    .build()
                    .encode()
                    .toUri();

            OmdbResponseDTO omdb = consultarOmdb(uri);
            return omdb != null ? omdb.genre() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene el total de temporadas de una serie por su ID de IMDb.
     */
    public String obtenerTotalSeasonsPorId(String imdbId) {
        if (imdbId == null || imdbId.isBlank() || apiKey == null || apiKey.isBlank()) {
            return null;
        }
        try {
            URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("apikey", apiKey)
                    .queryParam("i", imdbId)
                    .build()
                    .encode()
                    .toUri();

            OmdbResponseDTO omdb = consultarOmdb(uri);
            return omdb != null ? omdb.totalSeasons() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Asigna los valores detallados recibidos de OMDb a los campos del record Movie.
     */
    public Movie mapearPelicula(OmdbResponseDTO omdb) {
        return new Movie(
                omdb.imdbId(),
                omdb.title(),
                omdb.year(),
                omdb.genre(),
                omdb.runtime(),
                omdb.director(),
                omdb.plot(),
                omdb.actors()
        );
    }

    /**
     * Asigna los valores detallados recibidos de OMDb a los campos del record Serie.
     */
    public Serie mapearSerie(OmdbResponseDTO omdb) {
        return new Serie(
                omdb.imdbId(),
                omdb.title(),
                omdb.year(),
                omdb.genre(),
                omdb.director(),
                omdb.plot(),
                omdb.totalSeasons(),
                omdb.actors()
        );
    }
}