import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {AngularFireAuth} from '@angular/fire/auth';
import * as firebase from 'firebase/app';
import {ClientService} from '../clientService/client.service';
import {ClientInterface} from 'src/app/interface/client-data/client';
import { MatSnackBar } from '@angular/material';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
	//par défaut donnée d'un utilisateur/personnel
	isAuth : boolean;

	constructor( private snackBar : MatSnackBar, private clientService: ClientService, private afAuth: AngularFireAuth, private router: Router) { 
		this.infoUser();
	}

	infoUser(){
		this.afAuth.user.subscribe( 
			u => { 
				if(u){
					this.isAuth = true;
					//console.log(u);
				}
				else{
					this.isAuth = false;
					//initalise l'idToken à null
          localStorage.clear();
					//redirection vers la page login
					this.router.navigate(['login']);
				}

			}
		);	
	}

  /**
	 * Ouvre une nouvelle fenetre pour la connexion 
	 * Puis enregistre les info après une connexion reussie
	 */
    loginGoogle() {
      let i;
      return new Promise( (resolve, reject) => {
					this.afAuth.auth.signInWithPopup(new firebase.auth.GoogleAuthProvider()).then(res => {
								i = res.user.displayName.indexOf(" "); // decouper la chaine de nom en deux. L'user pourra changer plus tard
								
								// Appel pour enregistrer ls info user dans la BD
								this.clientService.authentificate({
										id: res.user.uid,
										nom: res.user.displayName.substr(0, i),
										prenom: res.user.displayName.substr(i),
										photo: res.user.photoURL,
										email: res.user.email,
										tel: res.user.phoneNumber,
										adresse: "",
										points: "0",
						}).then(res => {
							/*Creer une instance de client et stocker les info user retournées par le serveur*/
								resolve();
										localStorage.setItem('currentUser', res.body);
									}, 								
									err =>{
								/*Avertir l'user que le serveur n'a pas pu etre joint*/
								reject(err);
						});
				}, err => {
					/*Echec de l'authentification: soit popup fermée, soit connexion interrompue ou mauvais login*/
					console.log("erreur login google ==> \n" + err); // ou popup d'erreur
					reject(err);
				});
		});
	}


	loginMail(email: string, password: string) {
		let i, nom, prenom;
		return new Promise( (resolve, reject) => {
			this.afAuth.auth.signInWithEmailAndPassword(email, password)
				.then(res => {
						if (res.user.displayName != null ) {
							i = res.user.displayName.indexOf(" ");
							nom = 	res.user.displayName.substr(0, i);
							prenom = res.user.displayName.substr(i);
						} else {
							nom = "";
							prenom = "";
						}
						
						// Appel pour enregistrer ls info user dans la BD
						this.clientService.authentificate({
								id: res.user.uid,
								nom: nom,
								prenom: prenom,
								photo: res.user.photoURL,
								email: res.user.email,
								tel: res.user.phoneNumber,
								adresse: "",
								points: "0",
						}).then(res => {
							resolve();
								localStorage.setItem('currentUser', res.body);
						}, 								
						err =>{
							reject(err);
							console.log("erreur login mail ==> \n" + err);
						});

        }, err => {
						reject(err);
						this.snackBar.open(err , 'OK', {
							duration: 3000,
							panelClass: ['error-snackbar']
					});
				});
			});
	
	}
	
	// deconnection de l'utilisateur
	async doLogout() {
		await this.afAuth.auth.signOut().then( 
			res =>{			
				this.infoUser();
			}, err => {
				console.log(err);
			}
		);
	}

	//redirection vers la connexion correspondante
	async doLogin(button : string){
		if(button == "GOOGLE"){
			await this.loginGoogle();
		}
		this.infoUser();
	}

	inscription(client: ClientInterface, password: string) {
	    return new Promise((resolve, reject) => {
			let i;
	    firebase.auth().createUserWithEmailAndPassword(client.email, password).then(res => {
					// Appel pour enregistrer ls info user dans la BD
					this.clientService.authentificate({
							id: res.user.uid,
							nom: client.nom,
							prenom: client.prenom,
							photo: res.user.photoURL,
							email: res.user.email,
							tel: res.user.phoneNumber,
							adresse: "",
							points: "0",
					}).then(res => {
						resolve(true);
							localStorage.setItem('currentUser', res.body);
					}, 								
					err =>{
						reject(err);
						console.log("erreur login mail ==> \n" + err);
					});
			
			}, err => {
					this.snackBar.open(err , 'OK', {
						duration: 3000,
						panelClass: ['error-snackbar']
					});
			});	
		});		
	}

}
