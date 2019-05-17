import {Component, OnInit, ViewChild} from '@angular/core';
import {AuthService} from '../services/authService/authService.service';
import {CommandeInterface} from '../interface/commande-data/commande';
import {CommandeService} from '../services/commandeService/commande.service';
import {ClientInterface} from '../interface/client-data/client';
import {SelectItem} from 'primeng/api';
import {Router} from '@angular/router';
import {TmdbService} from '../services/tmdbService/tmdb.service';
import {environment} from '../../environments/environment';
import {forfaitFilm, MovieResponse} from '../interface/tmdb-data/Movie';
import {PlatInterface} from '../interface/commande-data/plat';
import {Dialog} from 'primeng/dialog';
import {MenuService} from '../services/menuService/menu.service';
import {MatSnackBar} from '@angular/material';

/**
 * https://stackoverflow.com/questions/5667888/counting-the-occurrences-frequency-of-array-elements
 * Compte le nombre d'occurence d'un element dans un array et retourne 2 tableaux: le premier contient les elements
 * du tableau et le second ne nombre d'occurence de chaque element du premier
 *
 * @param arr tableau dont les occurences doivent etre comptées
 */
function occurences(arr) {
    let a = [], b = [], prev;

    arr.sort();
    for (let i = 0; i < arr.length; i++) {
        if (arr[i] !== prev) {
            a.push(arr[i]);
            b.push(1);
        } else {
            b[b.length - 1]++;
        }
        prev = arr[i];
    }

    return [a, b];
}

@Component({
    selector: 'app-commande',
    templateUrl: './commande.component.html',
    styleUrls: ['./commande.component.scss']
})
export class CommandeComponent implements OnInit {

    @ViewChild(Dialog) dialog;
    allPlatsBD: PlatInterface[];
    commandes: CommandeInterface[];
    films: MovieResponse[];
    plats: PlatInterface[];
    platsArray = [];
    client: ClientInterface;
    isLoading = true;
    forfaitFilm = forfaitFilm;

    selectedCommande: CommandeInterface;

    displayDialog: boolean;

    sortOptions: SelectItem[];

    sortKey: string;
    filterBy: string;
    sortField: string;

    sortOrder: number;

    constructor(public authService: AuthService, private commandeService: CommandeService, private router: Router,
                private movieService: TmdbService, private platService: MenuService, private snackBar: MatSnackBar,) {

    }

    /**
     * Pour commencer on initialise la clé API de TMDB,
     * On recupère le client depuis le localStorage
     * Puis la commande et mes informations sur les plats
     */
    ngOnInit() {
        this.client = JSON.parse(localStorage.getItem('currentUser'));
        this.movieService.init(environment.tmdbKey);
        this.getCommandesClient(this.client.id);
        this.platService.getData().then(res => {
            this.allPlatsBD = res;
        }, err => {
            this.snackBar.open('Impossible d\'obtenir les informations des plats.\n' + err,
                'OK', {
                    duration: 3000,
                    panelClass: ['warn-snackbar']
                });
        });

        this.sortOptions = [
            {label: 'Moins ancien', value: '!date'},
            {label: 'Plus ancien', value: 'date'},
            {label: 'Prix', value: 'total'}
        ];

        this.filterBy = 'total,date,adresseLivraison';
    }

    /**
     * Recupère les commandes du client dont l'id est passé en paramètre
     * @param id identifiant du client dont les commandes doivent etre recuperées
     */
    getCommandesClient(id: string) {
        this.commandeService.getCommandes(id).then(res => {
            this.commandes = res;
            //console.log('la commande recuperée: ' + JSON.stringify(res));
            this.isLoading = false;
            sessionStorage.setItem('commmandes', JSON.stringify(res));
        }, err => {
            this.isLoading = false;
            this.snackBar.open('Vous êtes maintenant déconnecté(e)', 'OK', {
                duration: 3000,
                panelClass: ['info-snackbar']
            });
        });
    }

    /**
     * Invoquée au click du bouton details, cette methode recupère les details des films et des plats de la commande
     * @param event
     * @param commande Instance de commande dont les details doivent etre affichés
     */
    selectCommande(event: Event, commande: CommandeInterface) {
        let filmsArray = commande.idFilms.toString().split(',');
        let plat;

        this.films = [];
        this.plats = [];
        this.selectedCommande = commande;

        for (let i = 0; i < filmsArray.length; i++) {
            this.movieService.getMovie(+filmsArray[i]).then(res => {
                this.films.push(res);
            }, err => {
                this.snackBar.open('Une erreur est survenue lors de la recuperation des details du film\n' + err,
                    'OK', {
                        duration: 3000,
                        panelClass: ['error-snackbar']
                    });
            });
        }

        for (let i of commande.idPlats) {
            plat = {
                id: '',
                quantite: 0,
            };
            plat.id = i.id;
            plat.quantite = i.quantite;
            this.platsArray.push(plat);
            this.plats.push(this.searchPlat('' + i.id));
        }

        this.displayDialog = true;
        event.preventDefault();
    }

    /**
     * Declenché par le changement d'ordre de tri pour reevaluer le critère de tri
     * @param event evenement declencheur à partir duquel le nouveau critère est recuperé
     */
    onSortChange(event) {
        let value = event.value;

        if (value.indexOf('!') === 0) {
            this.sortOrder = -1;
            this.sortField = value.substring(1, value.length);
        } else {
            this.sortOrder = 1;
            this.sortField = value;
        }
    }

    /**
     * Invoquée pour nottoyer à la fermeture dialog des details
     */
    onDialogHide() {
        this.selectedCommande = null;
    }

    /**
     * Recherche le plat dont l'id est donné en paramètre dans le catologue des plats et le retourne
     * @param id identifiant du plat à trouver
     */
    searchPlat(id: string): PlatInterface {
        return this.allPlatsBD.find(p => p.id === id);
    }
}
