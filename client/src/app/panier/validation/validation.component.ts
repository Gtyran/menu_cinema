import {Component, OnInit} from '@angular/core';
import {FilmInterface, PlatQteInterface} from 'src/app/interface/commande-data/panier';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PanierService} from 'src/app/services/PanierService/panier.service';
import {ClientInterface} from 'src/app/interface/client-data/client';
import {ClientService} from 'src/app/services/clientService/client.service';
import {Adresse} from 'src/app/interface/client-data/adresse';
import {CommandeService} from 'src/app/services/commandeService/commande.service';
import {MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';

@Component({
  selector: 'app-validation',
  templateUrl: './validation.component.html',
  styleUrls: ['./validation.component.scss']
})
export class ValidationComponent implements OnInit {
  tittle : string = 'MenuCinéma';
  isLoading:boolean;
  showFacture = false;
  factureToInject;

  listFilms : Array<FilmInterface> = [];
  listPlats : Array<PlatQteInterface> = [];
  total : number = 0.0;
  user : ClientInterface;
  adresse : Adresse = {
    ville:  null,
    codePostal:  null,
    rue:  null,
    compl: null,
  };  
  points : number = 0;
  totalFinal :number = 0;
  
  itemCarte = [ 
    { name : "fab fa-cc-visa", color:"blue"},
    { name :"fab fa-cc-mastercard", color:"blue"},
    { name :"fab fa-cc-paypal", color:"blue" },
    { name :"far fa-credit-card" , color:"blue"}
  ];
  mois =[];
  annee =[];

  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;

  constructor(private snackBar: MatSnackBar,private formBuilder: FormBuilder, private commande : PanierService,
    private client : ClientService, private enregistrerCommande :CommandeService, private router : Router) {   
    this.user = this.client.getData(); 
    this.points = parseInt(this.user.points);        
    this.dateExp();
  }

  ngOnInit() {
    this.isLoading = true;
    this.getPanierItem();
    this.totalFinal = this.total;

    if(this.user.adresse === ''){
      this.firstFormGroup = this.formBuilder.group({      
        'nom': ['', Validators.required],
        'prenom': ['', Validators.required],
        'tel': ['', Validators.required],
        'adresse': [{value: '', disabled: true}],
        'email': ['', Validators.required],
        'rue':   ['', Validators.required],
        'codepostal':   ['', Validators.required],
        'ville':  ['', Validators.required],
        'compl': ['']
      });
    }
    else{
      this.firstFormGroup = this.formBuilder.group({      
        'nom': ['', Validators.required],
        'prenom': ['', Validators.required],
        'tel': ['', Validators.required],
        'adresse': [{value: this.user.adresse, disabled: true}],
        'email': ['', Validators.required],
        'rue':   [''],
        'codepostal':   [''],
        'ville':  [''],
        'compl': ['']
      });
    }
     
    this.secondFormGroup = this.formBuilder.group({      
      'numero': ['', Validators.required],
      'crypto': ['', Validators.required]
    });

    this.isLoading = false;
  }
  
  /**
   * récupération des valeurs du paniers
   */
  getPanierItem(){
    const panier = this.commande.getPanier();
    this.listFilms = panier.films;
    this.listPlats = panier.plats
    this.total = panier.total;
  }

  /**
   * affectation des dates pour la date expiration de la carte bancaire
   */
  dateExp(){
    let i :number;
    for(i=1;i<=12;i++){
      this.mois.push(i);
    }

    const date = new Date();
    i = date.getFullYear();
    while( i <= date.getFullYear() + 10 ){
      this.annee.push(i);
      i++;
    }

  }

  //concatenation de l'adresse en une chaine de caractère de type string
  public concatenation(){
    if(this.adresse.rue !== '' && this.adresse.codePostal !== null && this.adresse.ville !== ''){
      this.user.adresse = this.adresse.rue + " " + this.adresse.codePostal + " " + this.adresse.ville ;
      if(this.adresse.compl !== null){ this.user.adresse += " " + this.adresse.compl; }
    }
  }

  /**
   * Calcule du prix final avec ajout ou non de la réduction
   */
  public calculTotal(){
    this.totalFinal == this.total ?  (this.totalFinal =  this.total - Math.floor(this.points/10)) : (this.totalFinal = this.total);
    if(this.totalFinal < 0){
      this.totalFinal = 0;
    }
  }
  
   /**
    * supprilme un item plat
    * @param indexPlat 
    */
  public deleteP(indexPlat : number){
    this.commande.removePlat(indexPlat);
    this.listPlats.splice(indexPlat,1);
    this.ngOnInit();    
  }

  /**
   * supprime un item film
   * @param indexFilm 
   */
  public deleteF(indexFilm : number){
    this.commande.removeFilm(indexFilm);
    this.listFilms.splice(indexFilm,1);    
    this.ngOnInit();
  }

  /**
   * finalisation de la commande avec post vers le serveru qui renvoie un get facture
   */
  public async finaliser(){
    const verif = this.verificationPanier();
    if(verif){    
      let id = JSON.parse(localStorage.getItem("currentUser")).id;    

      let idfilms : string[] = [];
      this.listFilms.forEach(element => {
        idfilms.push(element.id);
      });
      
      let idplats : string[] = [];
      let i : number;
      this.listPlats.forEach(element => {
        i=1;
        while(i<=element.quantite){        
          idplats.push(element.id);
          i++;
        }
      });
      let points = this.totalFinal == this.total ? 'false' : 'true';
      await this.enregistrerCommande.validate(
        {
          idclient : id,
          idsFilm : idfilms.join(','),    
          idsPlat : idplats.join(','),      
          adresseLivraison : this.user.adresse,
          point_utilise : points
        }).then( res => {
          //localStorage.setItem('currentUser', JSON.stringify(this.user));
          //vidage du panier
          localStorage.removeItem('Films');
          localStorage.removeItem('Plats');
          console.log(res.body);

          this.snackBar.open("Votre commande est en préparation.", 'OK',{
            duration: 7000 ,
            panelClass: ['success-snackbar']
          });
          this.afficherFacture(res.body);

        },err => {
          this.snackBar.open("Une erreur est survenue lors de la finalisation de votre commande", 'OK',{
            duration: 7000 ,
            panelClass: ['error-snackbar']
          });
          console.log(err);
        }
        );
    }
  }

    /**
     * Affiche la facture après la validation de la commande en lisant le XML retourné
     */
    afficherFacture(fInj) {
        this.showFacture = true;
        this.factureToInject = fInj;
    }

    /**
     * Appelé lors de la fermeture du dialog
     */
    onDialogHide() {
        this.showFacture = false;
        this.factureToInject = '';
        this.router.navigate(['commande']);
    }

    /**
     * verifie si le panier comporte un film et un plat
     */
    verificationPanier() : boolean{
      let reponse : boolean;
      if(this.listFilms.length === 0 || this.listPlats.length === 0){
        reponse = false;
        this.snackBar.open("Votre panier doit comporter au moins un film et un plat", 'OK',{
          duration: 7000,
          panelClass: ['warn-snackbar']
        });
      }
      else{
        reponse = true;
      }
      
      return reponse;
    }
}
