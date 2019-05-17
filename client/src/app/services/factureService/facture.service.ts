import {Injectable} from '@angular/core';
import {FactureInterface} from '../../interface/facture/factureInterface';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class FactureService {

    private domParser: DOMParser = new DOMParser();
    private doc: Document;
    public Facture: FactureInterface;
    private url = environment.serveurAdd;

  constructor(private http: HttpClient ) { }

    //fonction pull des données du document XML

    async getData(url: string): Promise<FactureInterface> {
        return new Promise<FactureInterface>(((resolve, reject) => {
            this.http.get<FactureInterface>(url, {responseType: 'json'}).toPromise().then(
                res=>
                {
                   // this.doc = this.domParser.parseFromString(res, 'text/xml');

                    this.Facture=res;

                    resolve(this.Facture);
                }, rej => {
                    reject(rej);
                }
            );
        }));
    }
}
