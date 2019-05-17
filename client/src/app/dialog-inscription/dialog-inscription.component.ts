import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Adresse } from '../interface/client-data/adresse';
import { AuthService } from '../services/authService/authService.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dialog-inscription',
  templateUrl: './dialog-inscription.component.html',
  styleUrls: ['./dialog-inscription.component.scss']
})
export class DialogInscriptionComponent implements OnInit {
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  thirdFormGroup: FormGroup;
  
  titreDialog: string;
  adresse : Adresse = {
    ville:  null,
    codePostal:  null,
    rue:  null,
    compl: null,
  };  

  constructor(private router : Router, private snackBar: MatSnackBar, private authService : AuthService, private formBuilder: FormBuilder, 
    public dialogRef: MatDialogRef<DialogInscriptionComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
}

  ngOnInit() {    
    this.titreDialog = "Inscription"; 

    this.firstFormGroup = this.formBuilder.group({      
      'nom': ['', Validators.required],
      'prenom': ['', Validators.required],
      'tel': [''],
      'email': ['', Validators.required]
    });
     
    this.secondFormGroup = this.formBuilder.group({      
      'rue':   [''],
      'codepostal':   [''],
      'ville':  [''],
      'compl': [''],
    });

    this.thirdFormGroup = this.formBuilder.group({      
      'password': ['', Validators.required]
    });
  }

  //fermeture du dialog
  public onNoClick(): void {
    this.dialogRef.close();
  }

  //concatenation de l'adresse en une chaine de caractère de type string
  public concatenation(){
    this.data.user.adresse = this.adresse.rue + " " + this.adresse.codePostal + " " + this.adresse.ville + " " + this.adresse.compl;
  }

  //creation de compte
  public finisalition(){
    const client = this.data.user;
    const pwd = this.data.password;

    this.authService.inscription(client, pwd).then( res => {
      if(res === true){
        console.log(res)
          this.dialogRef.close();

          if(this.adresse.rue ==null && this.adresse.codePostal ==null && this.adresse.ville ==null) {            
            this.router.navigate(['client']);
          }
          else{
            this.router.navigate(['/cinéma',1]);
          }

          this.snackBar.open('Bienvenue ' + client.prenom + " " + client.nom + ",\nvous êtes désormais inscrit.", 'OK', {
              duration: 3000,
              panelClass: ['success-snackbar']
          });
      }
    }, err => {
          this.snackBar.open('Erreur lors de la creation de compte', 'OK', {
              duration: 3000,
              panelClass: ['error-snackbar']
          });
          console.log(err);
    }); 
  }
}
