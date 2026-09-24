import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ApiService } from '../../services/api';
import { Review } from '../../models/review';
import { MatIcon } from '@angular/material/icon';

@Component({
  imports: [MatIcon],
  selector: 'app-dashboard',
  styleUrl: './dashboard.css',
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit{
  private apiService = inject(ApiService);
  private cdr = inject(ChangeDetectorRef);
  ngOnInit(): void {
    this.obtenerReviews()
  }
  listaReviews:Review[]=[]
  obtenerReviews(){
    this.apiService.listaReviews().subscribe({
      next:(datos)=>{
        console.log(datos)
        this.listaReviews=datos
        this.cdr.detectChanges();
      },
      error:(error)=>{
        console.error(error)
      }
    })
  }
  eliminarReview(idReview:number){
    this.apiService.eliminarReview(idReview).subscribe({
      next:()=>{
        this.obtenerReviews();
        this.cdr.detectChanges();
      },
      error:(error)=>{
        console.error(error)
      }
    })
    
  }
}
