import {MovieResponse} from '../tmdb-data/Movie';
import {PlatResponse} from './plat';


export interface CommandeInterface {
    date: string;
    id: string;
    idClient: string;
    idPlats: PlatResponse[];
    idFilms: MovieResponse[];
    prix: number;
    adresseLivraison: string;
}
