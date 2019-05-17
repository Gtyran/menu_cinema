import { Injectable } from '@angular/core';
import { CanActivate,  Router } from '@angular/router';
import { Observable } from 'rxjs';
import * as firebase from 'firebase/app';
import { MatSnackBar } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(public snackBar: MatSnackBar, private router: Router) { }

	//une guard de router permettant de restreinte les accès au page si on n'est pas connecté
  canActivate(): Observable<boolean> | Promise<boolean> | boolean {
		return new Promise(
		  (resolve, reject) => {
			firebase.auth().onAuthStateChanged(
			  (user) => {

					if(user) {				  
						resolve(true); 
					} 
					else {
						//si il y a aucun user, redirection vers la page login
						this.router.navigate(['login']); 
						this.snackBar.open("Accès refusé", 'OK',{
							duration: 2000 ,
							panelClass: ['error-snackbar']
						});
						reject(false);
					}
					
			  }
			);
		  }
		);
  }
  

}
