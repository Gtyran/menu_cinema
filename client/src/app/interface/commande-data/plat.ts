export interface PlatInterface {
    id: string;
    nom: string;
    photo: string;
    type: string;
    prix: string;
    ingredients: string[];
}

export interface PlatResponse {
    id: string;
    quantite: number;
}
