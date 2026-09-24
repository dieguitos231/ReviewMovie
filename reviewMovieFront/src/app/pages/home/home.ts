import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { ApiService } from '../../services/api';
import { Movie } from '../../models/movie';
import { card } from '../../models/card';
import { Serie } from '../../models/serie';

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
    this.infoMovie=null
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
      },
    });
  }
 mostrarInfoSerie = false;
  abrirSerie(): void {
    this.mostrarInfoSerie = true;
  }
  cerrarSerie(): void {
    this.mostrarInfoSerie = false;
    this.infoSerie=null
  }
  
  

  /**
   * PANEL DE REALIZAR REVIEW
   */
  mostrarVentanaReview = false;
  abrirVentanaReview(): void {
    this.mostrarVentanaReview = true;
  }
  cerrarVentanaReview(): void {
    this.mostrarVentanaReview = false;
    this.limpiarCampos();
  }

  //Calificacion
  estrellas = [1, 2, 3, 4, 5];

  calificacion = 0;
  calificacionHover = 0;

  seleccionarCalificacion(valor: number): void {
    this.calificacion = valor;
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

  enviarReview() {
    this.descripcion.markAllAsTouched();

    if (this.descripcion.invalid) {
      return;
    }
    console.log('Calificacion:', this.calificacion);
    console.log('Descripcion', this.descripcion.value);
    this.cerrarVentanaReview();
  }
}
