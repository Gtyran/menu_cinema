import {MediaMatcher} from '@angular/cdk/layout';
import {ChangeDetectorRef, Component, OnDestroy} from '@angular/core';
import {AuthService} from './services/authService/authService.service';
import {MatSnackBar} from '@angular/material';
import {FilmInterface, PlatQteInterface} from './interface/commande-data/panier';
import {PanierService} from './services/PanierService/panier.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
    tittle: String = 'MenuCinéma';

    public filmList: Array<FilmInterface> = [];
    public platList: Array<PlatQteInterface> = [];
    public total: number = 0.0;

    mobileQuery: MediaQueryList;

    private _mobileQueryListener: () => void;

    constructor(changeDetectorRef: ChangeDetectorRef, media: MediaMatcher,
                private snackBar: MatSnackBar, public authService: AuthService, private commande: PanierService) {
        this.mobileQuery = media.matchMedia('(min-width: 100px)');
        this._mobileQueryListener = () => changeDetectorRef.detectChanges();
        this.mobileQuery.addListener(this._mobileQueryListener);
    }

    public ngOnDestroy(): void {
        this.mobileQuery.removeListener(this._mobileQueryListener);
    }

    //déconnection
    public logout() {
        this.authService.doLogout().then(res => {
            sessionStorage.clear();
            this.snackBar.open('Vous êtes maintenant déconnecté(e)', 'OK', {
                duration: 3000,
                panelClass: ['info-snackbar']
            });

        }, err => {
            this.snackBar.open(err, 'OK', {
                duration: 3000,
                panelClass: ['error-snackbar']
            });

        });
    }

    getPanierItem() {
        const panier = this.commande.getPanier();
        this.filmList = panier.films;
        this.platList = panier.plats;
        this.total = panier.total;
        //console.log(this.total);
    }


}
