import {Component, OnInit} from '@angular/core';
import {AuthService} from '../services/authService/authService.service';
import {Router} from '@angular/router';
import * as firebase from 'firebase/app';
import {MatDialog, MatSnackBar} from '@angular/material';
import {Location} from '@angular/common';
import {DialogInscriptionComponent} from '../dialog-inscription/dialog-inscription.component';
import {ClientInterface} from '../interface/client-data/client';

interface choiceButton {
    name: string;
    color: string;
    icon: string
}

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    isLoading: boolean;
    choixbuttons : choiceButton =         
        {
            name : 'GOOGLE',
            color : '#a30000',
            icon: 'fab fa-google'
        };/*,
        {
            name : 'FACEBOOK',
            color : '#004a9f',
            icon: 'fab fa-facebook-f'
        },
        {
            name : 'TWITTER',
            color : '#4fc3f7',
            icon: 'fab fa-twitter'
        },
        {
            name : 'GIT',
            color : '#f57c00',
            icon: 'fab fa-github'
        }*/
    
    //compteMail : boolean;
    email : string;
    password: string;

    constructor(private router : Router, public authService : AuthService,  private location: Location,
        private snackBar: MatSnackBar,public dialog: MatDialog) {
    }

    async ngOnInit() {
        this.isLoading = true;
        //this.compteMail = true;
        await this.init();
        this.isLoading = false;
    }

    public async init() : Promise<boolean> {
        return new Promise( (resolve, reject) => {
            firebase.auth().onAuthStateChanged(
            (user) => {
                if(user){
                    this.authService.isAuth = true;
                }
                else{
                    this.authService.isAuth = false;
                }                
                resolve(true);
            }, err => {
                console.log(err);
                this.snackBar.open('Une erreur inconnue est survenue lors de l\'authentificcation firebase', 'OK', {
                    duration: 3000,
                    panelClass: ['error-snackbar']
                });
                
                reject(false);
            });
        });
    }


        //revenir à la page précédente
    public back() {
        this.location.back();
    }
  
    public async signIn(button : string, email : string, password : string){
        if(button===''){
            await this.authService.loginMail(email,password).then(res=>{
                this.snackBar.open('Vous êtes maintenant connecté(e)', 'OK', {
                    duration: 3000,
                    panelClass: ['info-snackbar']
                });
                this.router.navigate(['/cinéma',1]);
                this.authService.isAuth = true;
                //console.log("Vous êtes maintenant connecté");
            }, err => {
                this.snackBar.open('Erreur d\'authentification : ' + err , 'OK', {
                    duration: 3000,
                    panelClass: ['error-snackbar']
                });
            });

        }
        else{        
            await this.authService.doLogin(button).then(res=>{
                this.snackBar.open('Vous êtes maintenant connecté(e)', 'OK', {
                    duration: 3000,
                    panelClass: ['info-snackbar']
                });
                this.router.navigate(['/cinéma',1]);
                this.authService.isAuth = true;
                //console.log("Vous êtes maintenant connecté");
            }, err => {
                this.snackBar.open('Erreur d\'authentification : ' + err , 'OK', {
                    duration: 3000,
                    panelClass: ['error-snackbar']
                });
            });
        }
    }

     //fonction de déconnexion
    public logout(){
        this.authService.doLogout().then(res => {
            this.snackBar.open('Vous êtes maintenant déconnecté(e)', 'OK', {
                duration: 3000,
                panelClass: ['info-snackbar']
            });
            this.router.navigate(['login']);  
            this.authService.isAuth = false;
            sessionStorage.clear();
            localStorage.clear();
        }, err => {
            this.snackBar.open('Erreur de deconnexion : ' + err, 'OK', {
                duration: 3000,
                panelClass: ['error-snackbar']
            });            
        });
    }

    //inscription d'un nouveau utilisateur par mail
    public creeUnCompteAvecMail(){
        let newUser : ClientInterface ={
            id: null,    
            nom: null,
            prenom: null,
            email:null,
            adresse: null,
            photo: null,
            tel:null,
            points : "0"            
        }
        
        let motdepasse : string = null;

        //dialog inscription
        this.dialog.open(DialogInscriptionComponent, {
            panelClass: 'custom-dialog-inscription',
            data: { user: newUser, password : motdepasse }
        });

        //Fermeture dialog Inscription
       /* dialogRef.afterClosed().subscribe( result => {
            if (result) {
                console.log("L'inscription" + result.client);
                const client = result.user;
                const pwd = result.password
                
                this.authService.inscription(client, pwd).then( res => {
                    if(res === true){
                        this.snackBar.open('Bienvenue ' + client.prenom + " " + client.nom + ",\nvous êtes désormais inscrit.", 'OK', {
                            duration: 3000,
                            panelClass: ['success-snackbar']
                        });
                    }
                }); 

            }
        });*/
    }
}
