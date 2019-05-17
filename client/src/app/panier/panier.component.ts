import { Component, OnInit, Input } from '@angular/core';
import { PanierService } from '../services/PanierService/panier.service';
import { PlatQteInterface, FilmInterface } from '../interface/commande-data/panier';

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss']
})
export class PanierComponent implements OnInit {

  @Input() listFilms : Array<FilmInterface> = [];
  @Input() listPlats : Array<PlatQteInterface> = [];
  @Input() total : number = 0.0;

  constructor(private commande : PanierService) {
  }

  ngOnInit() {
  }

  public deleteP(indexPlat : number){
    this.commande.removePlat(indexPlat);
    this.listPlats.splice(indexPlat,1);
    
  }

  public deleteF(indexFilm : number){
    this.commande.removeFilm(indexFilm);
    this.listFilms.splice(indexFilm,1);
  }

}
