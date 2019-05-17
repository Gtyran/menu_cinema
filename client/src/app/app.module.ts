import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {A11yModule} from '@angular/cdk/a11y';
import {AngularFireAuthModule} from '@angular/fire/auth';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
} from '@angular/material';
import {CdkTableModule} from '@angular/cdk/table';
import {FlexLayoutModule} from '@angular/flex-layout';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {TmdbService} from './services/tmdbService/tmdb.service';
import {HttpClientModule} from '@angular/common/http';
import {AngularFireModule} from '@angular/fire';
import {environment} from '../environments/environment';
import {LoginComponent} from './login/login.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {CommandeComponent} from './commande/commande.component';
import {FilmComponent} from './cinema/film/film.component';
import {ShareDialogComponent} from './share-dialog/share-dialog.component';
import {ListFilmComponent} from './cinema/list-film/list-film.component';
import {PersonComponent} from './cinema/person/person.component';

import {CarouselModule} from 'primeng/carousel';
import {PaginatorModule} from 'primeng/paginator';
import {ToastModule} from 'primeng/toast';
import {MessageService} from 'primeng/api';
import {OrderListModule} from 'primeng/orderlist';
import {FileUploadModule} from 'primeng/fileupload';
import {PanelModule} from 'primeng/panel';
import {ButtonModule} from 'primeng/button';
import {ListboxModule} from 'primeng/listbox';

import {MoviesComponent} from './cinema/movies.component';
import {ClientComponent} from './client/client.component';
import {AuthGuardService} from './services/authGuard/auth-guard.service';
import {AuthService} from './services/authService/authService.service';
import {MenuPlatsComponent} from './restaurant/menu-plats.component';
import {PlatComponent} from './restaurant/plats/plat.component';
import {FactureClientComponent} from './facture-client/facture-client.component';
import {FooterComponent} from './footer/footer.component';
import {SuggestionDialogComponent} from './suggestion-dialog/suggestion-dialog.component';
import {DialogInscriptionComponent} from './dialog-inscription/dialog-inscription.component';
import {PanierComponent} from './panier/panier.component';
import {ValidationComponent} from './panier/validation/validation.component';
import {ListPersonComponent} from './cinema/list-person/list-person.component';


@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        NotFoundComponent,
        CommandeComponent,
        FilmComponent,
        ShareDialogComponent,
        ListFilmComponent,
        PersonComponent,
        MoviesComponent,
        ClientComponent,
        MenuPlatsComponent,
        PlatComponent,
        FactureClientComponent,
        FooterComponent,
        SuggestionDialogComponent,
        DialogInscriptionComponent,
        PanierComponent,
        ValidationComponent,
        ListPersonComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        AngularFireAuthModule,
        AngularFireModule.initializeApp(environment.firebase),
        BrowserAnimationsModule,
        FlexLayoutModule,
        MatAutocompleteModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatCardModule,
        MatCheckboxModule,
        MatChipsModule,
        MatDatepickerModule,
        MatDialogModule,
        MatDividerModule,
        MatExpansionModule,
        MatGridListModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatNativeDateModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRadioModule,
        MatRippleModule,
        MatSelectModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatSortModule,
        MatStepperModule,
        MatTableModule,
        MatTabsModule,
        MatToolbarModule,
        MatTooltipModule,
        CdkTableModule,
        NgbModule,
        MatFormFieldModule,
        /*PrimeNG*/
        ListboxModule,
        CarouselModule,
        PaginatorModule,
        ToastModule,
        DataViewModule,
        DialogModule,
        PanelModule,
        ButtonModule,
        OrderListModule,
        FileUploadModule,
        /*Fin PrimeNG*/
        A11yModule, FormsModule,
        ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'})
    ],
    providers: [TmdbService, MessageService, AuthGuardService, AuthService,],
    bootstrap: [AppComponent],
    entryComponents: [ShareDialogComponent, DialogInscriptionComponent, SuggestionDialogComponent]
})
export class AppModule { }
