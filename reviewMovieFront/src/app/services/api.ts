import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { card } from '../models/card';
import { Movie } from '../models/movie';
import { Serie } from '../models/serie';

@Service()
export class ApiService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/omdb';

  //Buscar peliculas
  buscarPeliculas(busqueda: string): Observable<card[]> {
    return this.http.get<card[]>(`${this.apiUrl}/peliculas?titulo=${busqueda}`);
  }

  buscarInfoPelicula(id: string): Observable<Movie> {
    return this.http.get<Movie>(`${this.apiUrl}/id/${id}`);
  }

  //Buscar series
  buscarSeries(busqueda: string): Observable<card[]> {
    return this.http.get<card[]>(`${this.apiUrl}/series?titulo=${busqueda}`);
  }

  buscarInfoSeries(id: string): Observable<Serie> {
    return this.http.get<Serie>(`${this.apiUrl}/id/${id}`);
  }
  
}
