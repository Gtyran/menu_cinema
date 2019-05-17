import {Injectable} from '@angular/core';
import {ClientInterface} from '../../interface/client-data/client';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  public client: ClientInterface;
  private url = environment.serveurAdd;

  constructor( private http: HttpClient) {
  }

  /**
  * Methode servant à authentifier/enregistrer un utilisateur  
  */
  authentificate(params: {[key: string]: string}): Promise<HttpResponse<string>> {
    const P = new HttpParams( {fromObject: params} );
    return this.http.post( `${this.url}authentification`, P, {
      observe: 'response',
      responseType: 'text',
      headers: {'content-type': 'application/x-www-form-urlencoded'}
    }).toPromise();
  }

  /**
  * Appelée pour mettre à jour les informations du client
  */
  update(params: {[key: string]: string}): Promise<HttpResponse<string>> {
    const P = new HttpParams( {fromObject: params} );
    return this.http.post(`${this.url}client`, P, {
      observe: 'response',
      responseType: 'text',
      headers: {'content-type': 'application/x-www-form-urlencoded'}
    }).toPromise();
  }

  //reçoit les données de l'utilisateur stocké dans la base de données envoyé par le serveur
  getData(): ClientInterface {
    let client: ClientInterface;
    client = JSON.parse(localStorage.getItem('currentUser'));

    client.photo = client.photo === 'null' ? '' : client.photo;
    client.tel = client.tel === 'null' ? '' : client.tel;
    client.adresse = client.adresse === 'null' ? '' : client.adresse;
    client.email = client.email === 'null' ? '' : client.email;
    
    console.log(client);
    return client;
  }
}
