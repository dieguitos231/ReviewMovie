import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { card } from '../models/card';
import { Movie } from '../models/movie';
import { Serie } from '../models/serie';
import { Review } from '../models/review';

@Service()
export class ApiService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080';

  //Buscar peliculas
  buscarPeliculas(busqueda: string): Observable<card[]> {
    return this.http.get<card[]>(`${this.apiUrl}/api/omdb/peliculas?titulo=${busqueda}`);
  }

  buscarInfoPelicula(id: string): Observable<Movie> {
    return this.http.get<Movie>(`${this.apiUrl}/api/omdb/id/${id}`);
  }

  //Buscar series
  buscarSeries(busqueda: string): Observable<card[]> {
    return this.http.get<card[]>(`${this.apiUrl}/api/omdb/series?titulo=${busqueda}`);
  }

  buscarInfoSeries(id: string): Observable<Serie> {
    return this.http.get<Serie>(`${this.apiUrl}/api/omdb/id/${id}`);
  }

  //Realizar review 
  realizarReview(review:Review):Observable<Review>{
    return this.http.post<Review>(`${this.apiUrl}/api/review`,review)
  }

  //reviews Realizadas
  listaReviews():Observable<Review[]>{
    return this.http.get<Review[]>(`${this.apiUrl}/api/review`)
  }

  //Eliminar Review
  eliminarReview(idReview:number):Observable<any>{
    return this.http.delete(`${this.apiUrl}/api/review/${idReview}`,{})
  }
  
}
