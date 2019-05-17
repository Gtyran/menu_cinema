import {Component, OnInit} from '@angular/core';
import {MovieCastPerson, PersonResponse} from 'src/app/interface/tmdb-data/Person';
import {TmdbService} from 'src/app/services/tmdbService/tmdb.service';
import {ActivatedRoute} from '@angular/router';
import {environment} from 'src/environments/environment';
import {Location} from '@angular/common';

@Component({
  selector: 'app-person',
  templateUrl: './person.component.html',
  styleUrls: ['./person.component.scss']
})
export class PersonComponent implements OnInit {
  id :number;
  person: PersonResponse;
  movies: MovieCastPerson[];
  isLoading: boolean;

  constructor(private location: Location, private tmdb: TmdbService, private route: ActivatedRoute) { 
   }

  ngOnInit() {
    this.isLoading = true;
    this.init();   
  }

  public async init() {
    this.tmdb.init( environment.tmdbKey );
    //récuperation de l'id passé sur le router
    this.id =this.route.snapshot.params['id'];

    //récuperation de la fiche profil de l'acteur/l'actrice
    this.person = await this.tmdb.getPerson(this.id);

    //récuperation des films dont l'acteur/l'actrice a joué
    await this.tmdb.getPersonMovie(this.id).then( data => {
      this.movies = data.cast.slice(0, 10);;
    }, error => {
      console.log(error);
    });

    this.isLoading = false;    
  }

  //revenir à la page précédent
  public back() {
    this.location.back();
  }

}
