export interface PersonQuery {
  language?: string; // default "en-US"
}

export interface PersonResponse {
  birthday?: string;
  known_for_department?: string;
  deathday?: string;
  id?: number;
  name?: string;
  also_known_as?: string[];
  gender?: number; // 0, 1, 2, default 0
  biography?: string;
  popularity?: number;
  place_of_birth?: string;
  profile_path?: string;
  adult?: boolean;
  imdb_id?: string;
  homepage?: string;
}

export interface MovieCreditsModel {
    id: number;
    cast: MovieCastPerson[];
    crew: MovieCrewPerson[];
}

export interface MovieCastPerson {
  cast_id: number;
  character: string;
  credit_id: string;
  gender: number;
  id: number;
  name: string;
  order: number;
  profile_path: string;
}

export interface MovieCrewPerson {
  credit_id: number;
  department: string;
  gender: number;
  id: number;
  job: string;
  name: string;
  profile_path: string;
}
