import { Injectable } from '@angular/core';
import { MovieResponse } from 'src/app/interface/tmdb-data/Movie';
import { environment } from 'src/environments/environment';
import { SuggestionDialogComponent } from 'src/app/suggestion-dialog/suggestion-dialog.component';
import { PlatInterface } from 'src/app/interface/commande-data/plat';
import { HttpClient } from '@angular/common/http';
import { MatDialog, MatSnackBar } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class SuggestionService {

  constructor(private http: HttpClient, public dialog: MatDialog, private snackBar : MatSnackBar) { }

  /**
   * suggestion des plats pour un film donnée
   * @param movie 
   */
  async suggestionPourFilm(movie : MovieResponse){
    const url = environment.serveurAdd + 'suggestions/plats';       
    const item = 'id_film='+movie.id;
    let platsSuggestion : string[];
    await this.getDataSuggestion(url, item).then( res => {
      platsSuggestion = res;
    });    

    if(platsSuggestion.length > 0){
      //dialog suggestion
      this.dialog.open(SuggestionDialogComponent, {
        panelClass: 'custom-dialog-suggestion' ,
        data: { id : movie.id, nom: movie.original_title, suggest : 'film', listItem : platsSuggestion }
      });
    }
    else{
      this.snackBar.open('Le film ' + movie.original_title + ' a bien été ajouté dans votre panier', 'OK', {
        duration: 3000,
        panelClass: ['info-snackbar']
      });
    }
  }

  /**
   * sugestion des films pour un plat donnée
   * @param plat 
   */
  async suggestionPourPlat(plat : PlatInterface){
    const url = environment.serveurAdd + 'suggestions/films';      
    const item = 'id_menu='+plat.id;
    let FilmsSuggestion : string[]
    await this.getDataSuggestion(url, item).then( res => {
      FilmsSuggestion = res;
    });  

    if(FilmsSuggestion.length > 0){
      //dialog suggestion
      this.dialog.open(SuggestionDialogComponent, {
        panelClass: 'custom-dialog-suggestion' ,
        data: { id : plat.id, nom: plat.nom, suggest : 'plat', listItem : FilmsSuggestion }
      });    
    }else{
      this.snackBar.open('Le plat ' + plat.nom + ' a bien été ajouté dans votre panier', 'OK', {
        duration: 3000,
        panelClass: ['info-snackbar']
      });
    }

  }

  /**
   * recupération de la liste des suggestion s des films ou des plats
   * @param id correspond à l'id client
   * @param url correspond à l'url
   * @param item correspond à l'id du film/plat dont on veut la liste de suggestion
   */
  async getDataSuggestion(url : string, item:string): Promise<string[]> {
    const id = JSON.parse(localStorage.getItem('currentUser')).id;  		
		return new Promise<string[]>( (resolve, reject) => {
			this.http.get(`${url}?id=${id}&${item}`, {responseType: 'text'}).toPromise().then(
				res => {
				resolve(JSON.parse(res));
			}, rej => {
				reject(rej);
			});
		
		});
	}

}
