import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { ApiService } from '../../services/api';
import { Movie } from '../../models/movie';
import { card } from '../../models/card';
import { Serie } from '../../models/serie';
import { Review } from '../../models/review';

@Component({
  imports: [ReactiveFormsModule, MatIcon],
  selector: 'app-home',
  styleUrl: './home.css',
  templateUrl: './home.html',
})
export class Home {
  private apiService = inject(ApiService);
  private cdr = inject(ChangeDetectorRef);
  /**
   * FUNCIONES DEL BUSCADOR DE UNA PELICULA O SERIE
   */

  resultado: card[] = [];
  tipoBusqueda: string = 'movie';
  busqueda = new FormControl('', {
    nonNullable: true,
    validators: [Validators.minLength(2)],
  });

  seleccionarTipo(tipo: string) {
    this.tipoBusqueda = tipo;
  }

  buscar() {
    
    if (this.tipoBusqueda === 'movie') {
      this.apiService.buscarPeliculas(this.busqueda.value).subscribe({
        next: (datos) => {
          console.log(datos)
          this.resultado = datos;
          this.busqueda.reset();
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.log(`Error consultando datos . ${error}`);
        },
      });
    }
    if (this.tipoBusqueda === 'series') {
      this.apiService.buscarSeries(this.busqueda.value).subscribe({
        next: (datos) => {
          console.log(datos)
          this.resultado = datos;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.log(`Error consultando datos . ${error}`);
        },
      });
    }
  }
  //Pelicula
  infoMovie: Movie | null = null;
  buscarInfoPelicula(id: string) {
    this.apiService.buscarInfoPelicula(id).subscribe({
      next: (datos) => {
        console.log(datos)
        this.infoMovie = datos;
        this.abrirMovie();
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
  mostrarInfoMovie = false;
  abrirMovie(): void {
    this.mostrarInfoMovie = true;
  }
  cerrarMovie(): void {
    this.mostrarInfoMovie = false;
  }
  infoSerie:Serie | null=null;
   buscarInfoSerie(id: string) {
    this.apiService.buscarInfoSeries(id).subscribe({
      next: (datos) => {
        console.log(datos)
        this.infoSerie = datos;
        this.abrirSerie();
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error(error);
      }
    });
  }
 mostrarInfoSerie = false;
  abrirSerie(): void {
    this.mostrarInfoSerie = true;
  }
  cerrarSerie(): void {
    this.mostrarInfoSerie = false;
  }
  
  

  /**
   * PANEL DE REALIZAR REVIEW
   */
  mostrarReviewMovie = false;
  abrirReviewMovie(): void {
    this.mostrarReviewMovie = true;
  }
  cerrarReviewMovie(): void {
    this.mostrarReviewMovie = false;
    this.limpiarCampos();
  }
  mostrarReviewSerie = false;
  abrirReviewSerie(): void {
    this.mostrarReviewSerie = true;
  }

  cerrarReviewSerie(): void {
    this.mostrarReviewSerie = false;
    this.limpiarCampos();
  }

  //Calificacion
  estrellas = [1, 2, 3, 4, 5];

  calificacion = 0;
  calificacionHover = 0;

  seleccionarCalificacion(estrella: number): void {
    this.calificacion = estrella;
  }

  //Descripcion
  descripcion = new FormControl('', {
    nonNullable: true,
    validators: [
      Validators.minLength(4),
      Validators.maxLength(200),
      Validators.pattern(/^[\p{L}\p{N}\s.,;:!?'"()\-/%]+$/u),
    ],
  });

  limpiarCampos() {
    this.calificacion = 0;
    this.descripcion.reset();
  }

  esDescripcionInvalida(): boolean {
    return this.descripcion.invalid;
  }

  enviarReviewMovie(id:string) {
    this.descripcion.markAllAsTouched();

    if (this.descripcion.invalid) {
      return;
    }
    const newReview:Review={
      id:id,
      rating:this.calificacion,
      description:this.descripcion.value
    }
    this.apiService.realizarReview(newReview).subscribe({
      next:(datos)=>{

      },
      error:(error)=>{
        console.error(error)
      }
    })
    console.log('Calificacion:', this.calificacion);
    console.log('Descripcion', this.descripcion.value);
    this.cerrarReviewMovie();
  }
  enviarReviewSerie(id:string) {

    this.descripcion.markAllAsTouched();

    if (this.descripcion.invalid) {
      return;
    }
    const newReview:Review={
      id:id,
      rating:this.calificacion,
      description:this.descripcion.value
    }
    this.apiService.realizarReview(newReview).subscribe({
      next:()=>{
      },
      error:(error)=>{
        console.error(error)
      }
    })
    console.log('Calificacion:', this.calificacion);
    console.log('Descripcion', this.descripcion.value);
    
  }

  
}
