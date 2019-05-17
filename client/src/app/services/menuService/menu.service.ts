import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { PlatInterface } from 'src/app/interface/commande-data/plat';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

	//l'url pour accèder au serveur pour le menu
	private url = environment.serveurAdd + 'menu';
	ingredients : string[] = [];
	types : string[] = [];

  constructor(private http: HttpClient) { }

  /**
  * Retourne tous les plats disponibles
  */
	async getData(): Promise<PlatInterface[]> {		
		const id = JSON.parse(localStorage.getItem('currentUser')).id;
		return new Promise<PlatInterface[]>( (resolve, reject) => {
			this.http.get(`${this.url}?id=${id}`, {responseType: 'text'}).toPromise().then(
				res => {
				//traiter les données reçues pour constituer un MenuInterface				
				//console.log("la liste des plats: " + res);
				let menu = JSON.parse(res);
				let i : number;

				for(i=0;i<menu.length; i++){					
					let trouvetype = this.types.find( e => {
						return e === menu[i].type;
					});
					if(!trouvetype){
						this.types.push(menu[i].type);
					}
					
					menu[i].ingredients.forEach( elt => {
						const trouveIngredient = this.ingredients.find( i => {
							return i === elt;
						})

						if(!trouveIngredient){
							this.ingredients.push(elt);
						}
					});
					
				}
				//console.log(this.ingredients);
				//console.log(this.types);

				resolve(menu);
			}, rej => {
				reject(rej);
			});
		
		});
	}
	
}
