import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotFoundComponent} from './not-found/not-found.component';
import {LoginComponent} from './login/login.component';
import {CommandeComponent} from './commande/commande.component';
import {FilmComponent} from './cinema/film/film.component';
import {PersonComponent} from './cinema/person/person.component';
import {MoviesComponent} from './cinema/movies.component';
import {ClientComponent} from './client/client.component';
import {AuthGuardService} from './services/authGuard/auth-guard.service';
import {MenuPlatsComponent} from './restaurant/menu-plats.component';
import {FactureClientComponent} from './facture-client/facture-client.component';
import {ValidationComponent} from './panier/validation/validation.component';

//ROUTER  canActivate: [AuthGuardService],
const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'commande', canActivate: [AuthGuardService], component: CommandeComponent},
    {path: 'client', canActivate: [AuthGuardService], component: ClientComponent},
    {path: 'cinéma/:page', canActivate: [AuthGuardService], component: MoviesComponent},
    {path: 'film/:id', canActivate: [AuthGuardService], component: FilmComponent},
    {path: 'person/:id', canActivate: [AuthGuardService], component: PersonComponent},
    {path: 'menu', canActivate: [AuthGuardService], component: MenuPlatsComponent},
    {path: 'valider-panier', canActivate: [AuthGuardService], component: ValidationComponent},

    {path: 'facture', canActivate: [AuthGuardService], component: FactureClientComponent},
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'not-found', component: NotFoundComponent},
    {path: '**', redirectTo: 'not-found'}


];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
