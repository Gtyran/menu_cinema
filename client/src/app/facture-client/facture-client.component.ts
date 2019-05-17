import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FactureInterface} from '../interface/facture/factureInterface';
import {ClientService} from '../services/clientService/client.service';
import {ClientInterface} from '../interface/client-data/client';
import {forfaitFilm, MovieResponse} from '../interface/tmdb-data/Movie';
import {TmdbService} from '../services/tmdbService/tmdb.service';
import {environment} from '../../environments/environment';
import {PlatInterface} from '../interface/commande-data/plat';
import {MenuService} from '../services/menuService/menu.service';
//import * as jspdf from 'jspdf';
//import html2canvas from 'html2canvas';



/**
 * Calcule de nombre d'occurences de chaque element de la liste
 * @param  arr tableau contenant les elements à compter
 * @return tableau de 2 dimension le premier contient les occurences
 * et le 2e contient le nombre d'occurences
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
  selector: 'app-facture-client',
  templateUrl: './facture-client.component.html',
  styleUrls: ['./facture-client.component.scss']
})
export class FactureClientComponent implements OnInit {

    @Output() onDialogHide = new EventEmitter(true);
    allPlatsBD: PlatInterface[];
    facture: FactureInterface;
    client: ClientInterface;
    filmsDetails: MovieResponse[];
    forfaitFilm = forfaitFilm;
    platsDetails: PlatInterface[];
    occPlats;
    @Input() factureXML;
    testData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
        "<facture>\n" +
        "<idcommande>12</idcommande>\n" +
        "<client>QyWtUgTnVdadkNJZdFTfOqJ8sqo2</client>\n" +
        "<date>2019-05-09 08:36:06.517</date>\n" +
        "<films>\n" +
        "<film>284054</film>\n" +
        "<film>445629</film>\n" +
        "<film>457799</film>\n" +
        "<film>449562</film>\n" +
        "</films>\n" +
        "<plats>\n" +
        "<plat id=\"2\">\n" +
        "<quantite>1</quantite>\n" +
        "<prix>10.0</prix>\n" +
        "</plat>\n" +
        "<plat id=\"3\">\n" +
        "<quantite>1</quantite>\n" +
        "<prix>8.0</prix>\n" +
        "</plat>\n" +
        "</plats>\n" +
        "<points>0</points>\n" +
        "<reduction>0.0</reduction>\n" +
        "<sous-total>33.0</sous-total>\n" +
        "<total>33.0</total>\n" +
        "</facture>";
    private domParser: DOMParser = new DOMParser();
    private doc: Document;

    constructor(private movieService: TmdbService, private clientService: ClientService, private platService: MenuService) {
    }


    ngOnInit() {
        this.movieService.init(environment.tmdbKey);
        this.client = this.clientService.getData();
        //this.factureXML = this.testData;// for test purposes
        this.init();
    }

    init() {
        let plat;
        this.doc = this.domParser.parseFromString(this.factureXML, 'text/xml');

        this.facture = {
            date: this.doc.querySelector('date').textContent,
            films: [],
            idClient: this.doc.querySelector('client').textContent,
            idCommande: this.doc.querySelector('idcommande').textContent,
            plats: [],
            sousTotal: +this.doc.querySelector('sous-total').textContent,
            reduction: +this.doc.querySelector('reduction').textContent,
            prixTotal: +this.doc.querySelector('total').textContent,
            points: +this.doc.querySelector('points').textContent,
        };

        const allFilms = Array.from(this.doc.querySelectorAll('films > film'));
        const allPlats = Array.from(this.doc.querySelectorAll('plats > plat'));

        // on recupère tous les id films du xml
        this.facture.films = [];
        allFilms.map(att => {
            this.facture.films.push(att.textContent);
        });

        //Et on va chercher les films dans TMDB
        this.filmsDetails = [];
        for (let i of this.facture.films) {
            console.log('i du film: ' + i);
            this.movieService.getMovie(+i).then(res => {
                this.filmsDetails.push(res);
            }, err => {
                console.log('Ooops!!');
            });
        }

        this.facture.plats = [];
        this.occPlats = [];
        allPlats.map(att => {
            this.facture.plats.push(att.getAttribute('id'));
            this.occPlats.push(att.querySelector('quantite').textContent);
        });


        this.platService.getData().then(res => {
            this.allPlatsBD = res;
            this.platsDetails = [];
            for (let i = 0; i < this.facture.plats.length; i++) {
                this.platsDetails.push(this.searchPlat('' + this.facture.plats[i]));
            }
        }, err => {
            console.log('error recup plats');
        });
    }

    searchPlat(id: string): PlatInterface {
        return this.allPlatsBD.find(p => p.id === id);
    }

    onHide(): void {
        this.onDialogHide.emit();
    }



    /*dPDF(){

        let data = document.getElementById('contentToConvert');
        html2canvas(data).then(canvas => {
            // Few necessary setting options
            let imgWidth = 208;
            let pageHeight = 295;
            let imgHeight = canvas.height * imgWidth / canvas.width;
            let heightLeft = imgHeight;

            const contentDataURL = canvas.toDataURL('image/png')
            let pdf = new jspdf('p', 'mm', 'a4'); // A4 size page of PDF
            let position = 0;
            pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight)
            pdf.save('Facture.pdf'); // Generated PDF
        });


    }*/

}






