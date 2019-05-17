import { Component, OnInit, Inject } from '@angular/core';
import { MatSnackBar, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TmdbService } from '../services/tmdbService/tmdb.service';
import { MenuService } from '../services/menuService/menu.service';
import { environment } from 'src/environments/environment';
import { PanierService } from '../services/PanierService/panier.service';

@Component({
  selector: 'app-suggestion-dialog',
  templateUrl: './suggestion-dialog.component.html',
  styleUrls: ['./suggestion-dialog.component.scss']
})
export class SuggestionDialogComponent implements OnInit {

  titreDialog: string = '';
  itemName : string;
  listSuggestion : Array<any> = [];

  constructor(private panier : PanierService, private tmdb : TmdbService, private menuService : MenuService, private snackBar: MatSnackBar, 
    public dialogRef: MatDialogRef<SuggestionDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
  }
  
  async ngOnInit() {        
    if(this.data.suggest === 'film'){
      await this.suggestionDesPlatsPourUnFilm();
    }
    if(this.data.suggest === 'plat'){
      await this.suggestionDesFilmsPourUnPlat();
    }
    console.log(this.listSuggestion);
    this.titreDialog = "Le" + this.itemName + this.data.nom + " a bien été ajouté dans votre panier.";
  }

  async suggestionDesPlatsPourUnFilm(){    
    this.itemName = " film ";    
    
    await this.menuService.getData().then( res => {
      this.data.listItem.forEach( element => {
          let plats = res.filter( p => {
            return element == p.id;
          });
          //console.log(plats);
          plats.forEach( element => {
            let plat = {
              id : element.id,
              nom : element.nom,
              photo : 'assets/images/menu/'+element.id+'.png',
              prix :element.prix
            };
            this.listSuggestion.push(plat);
          });
       });

    });
  }

  async suggestionDesFilmsPourUnPlat(){
    this.itemName = " plat ";  
    
    this.tmdb.init( environment.tmdbKey );
    await this.data.listItem.forEach(element => {
      this.tmdb.getMovie(element).then( res => {
        let film = {
          id : res.id,
          original_title : res.original_title,
          poster_path : res.poster_path
        }
        this.listSuggestion.push(film);
      });      
    });
  }

  //fermeture du dialog
  public onNoClick(): void {
    this.dialogRef.close();
  }

  pushInPanier(item: any){
    if(this.data.suggest === 'plat'){
      this.panier.addMovie(item);
      this.snackBar.open('Le film ' + item.original_title + ' a bien été ajouté dans votre panier', 'OK', {
        duration: 3000,
        panelClass: ['info-snackbar']
      });
    }
    if(this.data.suggest === 'film'){
      this.panier.addPlat(item);
      this.snackBar.open('Le plat ' + item.nom + ' a bien été ajouté dans votre panier', 'OK', {
        duration: 3000,
        panelClass: ['info-snackbar']
      });
    }
  }

}
