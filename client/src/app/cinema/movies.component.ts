import {Component, OnInit} from '@angular/core';
import {TmdbService} from '../services/tmdbService/tmdb.service';
import {environment} from 'src/environments/environment';
import {MovieQuery, MovieResponse} from '../interface/tmdb-data/Movie';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../services/authService/authService.service';
import {Location} from '@angular/common';
import {MatSnackBar} from '@angular/material';
import {SearchMovieResponse} from '../interface/tmdb-data/searchMovie';
import {PersonResponse} from '../interface/tmdb-data/Person';

@Component({
    selector: 'app-movies',
    templateUrl: './movies.component.html',
    styleUrls: ['./movies.component.scss']
})
export class MoviesComponent implements OnInit {
    listUpcoming: MovieResponse;
    listPopular: MovieResponse;
    listTopRated: MovieResponse;
    affichageList: MovieResponse;
    affichage: string;
    affResPerson: string;
    isLoading: boolean;
    page: number;

    //resultats de recherche
    listSeach: SearchMovieResponse;
    listPersonSearch: PersonResponse;
    searched: boolean;


    constructor(private snackBar: MatSnackBar, private location: Location, private tmdb: TmdbService,
                private router: Router, public authService: AuthService, private route: ActivatedRoute) {

    }

    ngOnInit() {
        this.isLoading = true;
        //récupération du numéro de page
        this.page = this.route.snapshot.params['page'];

        this.init();
    }

    public async init() {
        //initalisation des données themoviedb avec la key
        this.tmdb.init(environment.tmdbKey);

        //affectation de la liste des films qui viennent de sortir
        await this.pullUpComingMovies(this.page);

        //affectation de la liste des films populaires
        await this.pullPopularMovies(this.page);
        this.isLoading = false;
        //par défaut on affiche les sorties récents
        this.affichage = 'Les sorties récents';
    }

    //récuperation des films populaires
    public async pullPopularMovies(page: number, options?: MovieQuery) {
        await this.tmdb.getPopular(page, options).then(data => {
            this.listPopular = data;
        }, error => {
            this.snackBar.open('Une erreur est survenue lors de la récuperation des films de la page : ' + error, 'OK', {
                duration: 5000,
                panelClass: ['error-snackbar']
            });
        });
    }

    //récuperation des films récents
    public async pullUpComingMovies(page: number, options?: MovieQuery) {
        await this.tmdb.getUpComing(page).then(data => {
            this.listUpcoming = data;
            this.affichageList = this.listUpcoming;
        }, error => {
            this.snackBar.open('Une erreur est survenue lors de la récuperation des films de la page : ' + error, 'OK', {
                duration: 5000,
                panelClass: ['error-snackbar']
            });
        });
    }

    //revenir à la page précédente
    public back() {
        this.location.back();
    }

    //Filtrage de l'affichage des films
    public sortMovies(option: string) {
        if (option === 'date') {
            this.affichageList = this.listUpcoming;
            this.affichage = 'Les sorties récents';
        }
        if (option === 'popularité') {
            this.affichageList = this.listPopular;
            this.affichage = 'Les films populaires';
        }
    }

    //redirection vers la page info du film
    public ficheFilm(id: number) {
        if (id !== null) {
            this.router.navigate(['film/' + id]);
        }
    }

    //recherche des films spécifiques
    public async searchMovies(queries: string) {
        this.isLoading = true;
        await this.tmdb.searchMovie({
            language: 'fr-FR',
            query: queries,
            region: 'DE',
        }).then(res => {
            this.isLoading = false;
            this.searched = true;
            this.listSeach = res;
            this.affichage = this.listSeach.total_results + ' resultats pour votre recherche "' + queries + '".';
        }, err => {
            this.isLoading = false;
            this.snackBar.open('Une erreur est survenue lors de la recherche des films', 'OK', {
                duration: 5000,
                panelClass: ['error-snackbar']
            });
        });

        //recherche les acteurs correspondants
        this.tmdb.searchPerson({
            language: 'fr-FR',
            query: queries,
            region: 'DE',
        }).then(res => {
            this.listPersonSearch = res;
            this.affResPerson = 'Acteurs correspondants à "' + queries + '"';
        }, err => {
            this.snackBar.open('Une erreur est survenue lors de la recherche des acteurs', 'OK', {
                duration: 5000,
                panelClass: ['error-snackbar']
            });
        });

        //recherche des genres correspondants
    }


// to set the paginator to a specific page https://stackoverflow.com/a/44797920
    public paginate(event) {
        let pageRequested = event.page + 1; //commence à compter à partir de 0
        //console.log("paginate fired ==> " + pageRequested);
        this.pullUpComingMovies(pageRequested).then(res => {
            this.isLoading = false;
            this.location.go(`/cinema/${pageRequested}`);
        }, err => {
            console.log(err);
        });
    }
}
