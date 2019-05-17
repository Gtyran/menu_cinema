import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientInterface } from '../interface/client-data/client';
import { ClientService } from '../services/clientService/client.service';
import { Location } from '@angular/common';
import { MatSnackBar } from '@angular/material';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit {
 @ViewChild('formulaire') formulaire: NgForm;

  isLoading : boolean = true; 
  clientModifier : ClientInterface;

  imageUrl: string = null;
  fileToUpload: File = null;

  constructor( private snackBar: MatSnackBar, private location: Location,private clientService : ClientService) {
      
   }

  ngOnInit() {
    this.init();    
  }

  public init(){
    this.isLoading = true; 
    this.clientModifier = this.clientService.getData();
   this.isLoading = false; 
  }

  //retour à la page précedente
  public back() {
    this.location.back();
  }

  //modification des données de l'user
  public async modifierDonnees(params: {[key: string]: string}){
    //console.log(this.clientModifier.photo);
    await this.clientService.update({
        id: params.id,
        nom: params.nom,
        prenom: params.prenom,
        photo: params.photo,
        email: params.email,
        tel: params.tel,
        adresse: params.adresse,
        points : params.points
      }).then( res => {
          localStorage.setItem('currentUser', res.body);
          this.snackBar.open('Vos informations ont été mises à jour avec succès', 'OK', {
            duration: 3000,
            panelClass: ['info-snackbar']
          });
    }, err => {
      console.log(err);
      this.snackBar.open('Une erreur est survenue lors de la modification des données du client', 'OK', {
        duration: 3000,
        panelClass: ['error-snackbar']
      });
    });
  }

  //fonction de récuperation du nom de la photo et visualisation
  public fileInput(file: FileList) {
    this.fileToUpload = file.item(0);
    this.clientModifier.photo = this.fileToUpload.name;
    //visualisation
    var reader = new FileReader();
    reader.onload = (event:any) => {
      this.imageUrl = event.target.result;
    }
    reader.readAsDataURL(this.fileToUpload);
  }
}
