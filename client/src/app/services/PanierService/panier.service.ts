import {Injectable} from '@angular/core';
import {MovieResponse} from 'src/app/interface/tmdb-data/Movie';
import {PlatInterface} from 'src/app/interface/commande-data/plat';
import {FilmInterface, PlatQteInterface} from 'src/app/interface/commande-data/panier';


@Injectable({
  providedIn: 'root'
})
export class PanierService {
  
  constructor() {
   }
  
  trouveItemDansPanier(id : string, list : any[]){
    const trouve = list.find( v => {
      return v.id === id;
    })
    //console.log(trouve);
    return trouve;
  }

  //ajout des films dans le panier
  addMovie(movie : MovieResponse) : boolean{
    let reponse : boolean;      
    let id = movie.id +"";
    let f : FilmInterface = {
      "id" : id,
      "nom" : movie.original_title,
      "photo" : movie.poster_path,
      "prix" : 3.75
    };
    let existing = localStorage.getItem('Films');
    let films = [];

    if(existing){
      films = JSON.parse(existing);    

      if(this.trouveItemDansPanier(id,films)){
        reponse = false;      
      }else{
        films.push(f);
        this.setFilm(films);
        reponse = true;
      }
    }
    else{
      films.push(f);
      this.setFilm(films);
      reponse = true;
    }    
    
    return reponse;
  }

  //modifie la quantite du plat dont l'id est passé en paramètre
  setQuantitePlatInPanier(id : string, qtite : number) {
    let i : number;
    let plats = this.getPlats();
    for(i=0; i<plats.length; i++){
      if(plats[i].id === id){
        plats[i].quantite = qtite;
      }
    } 
    this.setPlats(plats);
  }

  //ajout du plat dans le panier
  addPlat(plat : PlatInterface) : number{
    let existing = localStorage.getItem('Plats');
    let plats = [];
    let count : number
    let id = plat.id;
    let pl : PlatQteInterface = {
      "id" : id,
      "nom" : plat.nom,
      "quantite" : 1,
      "photo" : plat.photo,   
      "prix" : plat.prix,
      "type" : plat.type
    }
    
    if(existing){
      plats = JSON.parse(existing);
      //console.log(plats[id]);
      let trouve = this.trouveItemDansPanier(id,plats);
      if(trouve){
          let count = trouve.quantite++;   
          this.setQuantitePlatInPanier(id, count);
      }else{
        plats.push(pl);        
        count = 1;
      }
      this.setPlats(plats);
    }
    else{
      plats.push(pl);        
      count = 1;
      this.setPlats(plats);
    }        

    return count;
  }

   //supprime le plat dans le panier
   removePlat(index : number) {
    let plats = this.getPlats();   
    if(plats){
      const count = plats[index].quantite - 1;
      if(count > 0){
        plats[index].quantite = count;               
      }else{          
        plats.splice(index,1);            
        console.log('le plat a été supprimé du panier ');       
      }    
    }          
    this.setPlats(plats);   
  }

  //supprime le plat dans le panier
  removeFilm(index : number) {
    let films = this.getFilm();
    films.splice(index,1);        
    this.setFilm(films);
  }

  getFilm(){
    let existing = localStorage.getItem('Films');
    let films = JSON.parse(existing);
    return films;
  }

  setFilm(films : any){
    let existing = JSON.stringify(films);
    localStorage.setItem('Films', existing);
  }

  getPlats(){
    let existing = localStorage.getItem('Plats');
    let plats = JSON.parse(existing);
    return plats;
  }

  setPlats(plats : any){
    let existing = JSON.stringify(plats);
    localStorage.setItem('Plats', existing);
  }


  /**
   * récupération des films et les plats situés dans le localStorrage
   */
  getPanier() : any {
    let i : number;
    let filmList : Array<FilmInterface> = [];
    let platList : Array<PlatQteInterface> = [];
    let total : number = 0.0;
    let panier : Object = {};

    let films = JSON.parse( localStorage.getItem('Films') );
    if(films !== null && films.length > 0){      
      for(i = 0; i<films.length;i++){
        filmList.push(films[i]);
        total += films[i].prix;
      };   
    }
    
    let plats = JSON.parse( localStorage.getItem('Plats') );
    if(plats !== null && plats.length > 0){      
      for(i = 0; i<plats.length;i++){
        platList.push(plats[i]);
        total += parseFloat(plats[i].prix) * plats[i].quantite; 
      };   
    }
    
    panier['films'] = filmList;
    panier['plats'] = platList;
    panier['total'] = total;
    return panier;
  }

}
