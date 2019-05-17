import {Component, OnInit} from '@angular/core';
import {MovieResponse} from 'src/app/interface/tmdb-data/Movie';
import {TmdbService} from 'src/app/services/tmdbService/tmdb.service';
import {environment} from 'src/environments/environment';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog, MatSnackBar} from '@angular/material';
import {Location} from '@angular/common';
import {ShareDialogComponent} from 'src/app/share-dialog/share-dialog.component';
import {MovieCastPerson} from 'src/app/interface/tmdb-data/Person';
import {DomSanitizer} from '@angular/platform-browser';
import {PanierService} from 'src/app/services/PanierService/panier.service';
import { SuggestionService } from 'src/app/services/suggestionService/suggestion.service';

@Component({
  selector: 'app-film',
  templateUrl: './film.component.html',
  styleUrls: ['./film.component.scss']
})
export class FilmComponent implements OnInit {  
  isLoading: boolean;

  id : number;
  movie: MovieResponse;
  casting : MovieCastPerson[];
 // crew: MovieCrewPerson[];
  trailerUrl: any;
  videos:  Array<any>;
  movieSimilar : MovieResponse;

  constructor( private sanitizer: DomSanitizer, public dialog: MatDialog, 
    private snackBar: MatSnackBar, private panier : PanierService,
    private location: Location, private tmdb: TmdbService, private route: ActivatedRoute,
    private suggestion : SuggestionService) {  
  
  }

  ngOnInit() {
    this.isLoading = true;
    this.init();    
  }  

  public async init() {
    this.tmdb.init( environment.tmdbKey );

    //récuperation de l'id passé sur le router
    this.id =this.route.snapshot.params['id'];

    //récuperation des données du film selon id passé en paramètre
    this.movie = await this.tmdb.getMovie(this.id);
    //console.log(this.movie);

    //récuperation du casting du film
    await this.tmdb.getCreditsMovie(this.id).then( data => {
      this.casting = data.cast.slice(0, 10);
      //this.crew = data.crew.slice(0,10);
    }, error => {
      console.log(error);
    });

    //récuperation des urls des videos du films
    await this.tmdb.getVideoMovie(this.id).then( data => {
      this.videos = data.results;
      if(this.videos.length > 0){
        //console.log(this.videos[0].results.key);
        this.getMovieVideoUrl(this.videos[0].key);
      }
    }, error => {
      console.log(error);
    });

    //récuperation des films similaires
    await this.tmdb.getSimilarMovies(this.id).then( data => {
      this.movieSimilar = data;
    }, error => {
      console.log(error);
    });
    
    this.isLoading = false;    
    //return this.movie;
  }

  //revenir à la page précédente
  public back() {
    this.location.back();
  }

  //obtention du la video youtube (trailer) 
  public getMovieVideoUrl(id: any) {
    return this.trailerUrl = this.sanitizer.bypassSecurityTrustResourceUrl('https://www.youtube.com/embed/' + id);
  }

  //dialog copie link
  public shareDialog(movie: MovieResponse): void {
    const lien = window.location.href;
    this.dialog.open(ShareDialogComponent, {
      panelClass: 'custom-dialog-container' ,
      data: { url: lien, original_title: movie.original_title }
    });
  }
  
  //ajout du films dans le panier + affichage d'un dialog sur la suggestion des plats
  public pushMovieInPanier(movie: MovieResponse ) {
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
