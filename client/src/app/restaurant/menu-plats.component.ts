import {Component, OnInit} from '@angular/core';
import {Location} from '@angular/common';
import {MenuService} from '../services/menuService/menu.service';
import {PlatInterface} from '../interface/commande-data/plat';
import {MatSnackBar} from '@angular/material';
import {PanierService} from '../services/PanierService/panier.service';
import { CommandeService } from '../services/commandeService/commande.service';

@Component({
    selector: 'app-menu-plats',
    templateUrl: './menu-plats.component.html',
    styleUrls: ['./menu-plats.component.scss']
})
export class MenuPlatsComponent implements OnInit {

    listPopular: PlatInterface[];
    listAllPlats: PlatInterface[];
    listToShow: PlatInterface[];

    bestVente: PlatInterface[] = [];
    prixCroissant: PlatInterface[];
    ordre = true;

    searched: boolean;

    isLoading: boolean;
    affichage = 'Plats disponibles';

    constructor(private commande: CommandeService, private location: Location, private menuService: MenuService,
                private snackBar: MatSnackBar) {

        //console.log(this.ListTypes);
    }

    ngOnInit() {
        this.isLoading = true;
        this.init();
    }

    async init() {
        await this.menuService.getData().then(res => {
            this.listAllPlats = res;
            this.listToShow = this.listAllPlats;
            //console.log("la liste des plats: " + res);
        }, err => {
            this.snackBar.open('Une erreur est survenue lors de la récuperation des plats : ' + err, 'OK', {
                duration: 3000,
                panelClass: ['error-snackbar']
            });
        });

        this.getBestSellers();       
        //this.trierCroissant(this.listAllPlats, this.ordre);

        this.isLoading = false;
    }

    back() {
        this.location.back();
    }

    /**
     * Affiche les plats selon le critère choisi
     */
    sortPlats(option: string) {
        if (option === 'prix') {
            this.ordre = !this.ordre;
            this.listToShow = this.trierCroissant(this.listAllPlats, this.ordre);
            this.affichage = 'Les plats par prix';
            if (this.ordre) {
                this.affichage += ' croissant';
            } else {
                this.affichage += ' decroissant';
            }
        }
        if (option === 'popularité') {
            //recuperer les plats les plus demandés depuis la BD et les afficher
            this.listToShow = this.bestVente;
            this.affichage = 'Les meilleurs ventes';
        }

    }

    /**
     * Rechercher les plats qui ont:    
     * un nom correpondant
     * des ingredient correspondants
     *  des types correspondants
     * et les retourner
     */
    searchPlat(query: string) {
        query = query.toLowerCase();
        let ingredient = [];
        let results: PlatInterface[];

        results = this.listAllPlats.filter(p => {
            if (p.nom.toLowerCase().includes(query) || p.type.toLowerCase().includes(query)) {
                return p;
            }

            for (let ing of p.ingredients) {
                if (ing.toLowerCase().includes(query)) {
                    return p;
                }
            }
        });

        this.affichage = 'Resultats de la recherche pour "' + query + '"';
        this.listToShow = results;
    }

    /**
     * Trie la liste affichée en ordre croissant de prix
     */
    trierCroissant(listeAtrier, bool) {
        this.prixCroissant = listeAtrier;
        if (bool) {
            this.prixCroissant.sort(function (a, b) {
                return parseFloat(a.prix) - parseFloat(b.prix);
            });
        } else {
            this.prixCroissant.sort(function (a, b) {
                return parseFloat(b.prix) - parseFloat(a.prix);
            });
        }

        return this.prixCroissant;
    }

    /**
     * Recupère les meilleures ventes pour les afficher
     */
    async getBestSellers() {
        const idBestSeller = await this.commande.getBestSellerPlats();
        idBestSeller.forEach( element => {
            const plat = this.listAllPlats.filter( p => {
                return p.id === element.id;
            });
            if(plat.length > 0){
                this.bestVente.push(plat[0]);
            }
        });
        //console.log(this.bestVente)
    }

}
