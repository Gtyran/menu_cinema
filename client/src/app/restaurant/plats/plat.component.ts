import {Component, Input, OnInit} from '@angular/core';
import {PlatInterface} from 'src/app/interface/commande-data/plat';
import {MatSnackBar} from '@angular/material';
import {Location} from '@angular/common';
import {PanierService} from 'src/app/services/PanierService/panier.service';
import { SuggestionService } from 'src/app/services/suggestionService/suggestion.service';

@Component({
  selector: 'app-plat',
  templateUrl: './plat.component.html',
  styleUrls: ['./plat.component.scss']
})
export class PlatComponent implements OnInit {
  @Input() Plats : PlatInterface[];

  constructor(private snackBar: MatSnackBar,private location: Location, 
    private panier : PanierService,  private suggestion : SuggestionService) { }

  ngOnInit() {
  }

  back() {
    this.location.back();
  }

  /**
   * ajoute le plat dans le panier
   * @param plat 
   */
  public pushplatsInPanier(plat: PlatInterface) {
    this.panier.addPlat(plat);
    /* this.snackBar.open('Le plat ' + plat.nom + ' a bien été ajouté dans votre panier', 'OK', {
        duration: 3000,
        panelClass: ['success-snackbar']
    });*/

    //dialog suggestion
    this.suggestion.suggestionPourPlat(plat);

  }

}
