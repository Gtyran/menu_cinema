import {Injectable} from '@angular/core';
import {CommandeInterface} from '../../interface/commande-data/commande';
import {MovieQuery} from '../../interface/tmdb-data/Movie';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import { PlatInterface } from 'src/app/interface/commande-data/plat';


function AlxToObjectString(data?: object): { [key: string]: string } {
    const res = {};
    for (const k of Object.keys(data || {})) {
        const v = data[k];
        res[k] = typeof v === 'string' ? v : JSON.stringify(v);
    }
    return res;
}

interface bestIdPlats{
    id: string,
    nombre_vente : string
}

@Injectable({
    providedIn: 'root'
})

export class CommandeService {
    private api_url = environment.serveurAdd;

    constructor(private http: HttpClient) {
    }

    /**
     * retourne les commande d'un client donnée
     * @param id_client correspond à l'id du client
     * @param options 
     */
    async getCommandes(id_client: string, options?: MovieQuery): Promise<CommandeInterface[]> {
        const url = `${this.api_url}commande?idclient=${id_client}`;
        const res = await this.get<CommandeInterface[]>(url, options);
        return res.body;
    }

    /**
     * post une commande vers le serveur pour son enregistrement
     * @param params correspond aux données d'une commande
     */
    validate(params: {[key: string]: string}): Promise<HttpResponse<string>> {
        const P = new HttpParams( {fromObject: params} );
        return this.http.post( `${this.api_url}commande`, P, {
          observe: 'response',
          responseType: 'text',
          headers: {'content-type': 'application/x-www-form-urlencoded'}
        }).toPromise();
    }

    private async get<T>(url: string, data: object): Promise<HttpResponse<T>> {
        return this.http.get<T>(url, {
            observe: 'response',
        }).toPromise();
    }

    /**
     * récupération des meilleures ventes des plats
     * @param id_client correspond à l'id du client
     * @param options 
     */
    async getBestSellerPlats(options?: MovieQuery): Promise<bestIdPlats[]> {
        const id = JSON.parse(localStorage.getItem('currentUser')).id;
        const url = `${this.api_url}commande?idclient=${id}&bestsellerplats=1`;
        const res = await this.get<bestIdPlats[]>(url, options);
        //console.log(res.body);
        return res.body;
    }
}
