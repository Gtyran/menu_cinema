import {Component, Input, OnInit} from '@angular/core';
import {MovieResponse} from 'src/app/interface/tmdb-data/Movie';
import {Router} from '@angular/router';
import {PanierService} from 'src/app/services/PanierService/panier.service';
import {MatDialog, MatSnackBar} from '@angular/material';
import { SuggestionService } from 'src/app/services/suggestionService/suggestion.service';

@Component({
  selector: 'app-list-film',
  templateUrl: './list-film.component.html',
  styleUrls: ['./list-film.component.scss']
})
export class ListFilmComponent implements OnInit {
  @Input() movieList : MovieResponse;

  constructor(private route: Router,private snackBar: MatSnackBar, public dialog: MatDialog,
    private panier : PanierService, private suggestion : SuggestionService) {
  }

  ngOnInit() {
  }  

  //redirection vers la page info du film
  public ficheFilm(id:number){
    if(id !== null){
      this.route.navigate(['film/'+id]);
    }
  }

   //ajout du films dans le panier + affichage d'un dialog sur la suggestion des plats
   pushMovieInPanier(movie: MovieResponse ) {
    const reponse : boolean = this.panier.addMovie(movie);
    if(!reponse){
      this.snackBar.open("Le film " + movie.title + " a déjà été ajouté dans votre panier.", 'OK', {
        duration: 3000,
        panelClass: ['warn-snackbar']
      });
    }
    else{
      this.suggestion.suggestionPourFilm(movie);
    }


  }

}
