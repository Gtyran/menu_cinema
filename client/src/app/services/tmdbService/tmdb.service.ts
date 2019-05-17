import { Injectable } from '@angular/core';
import {MovieQuery, MovieResponse} from '../../interface/tmdb-data/Movie';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {PersonQuery, PersonResponse, MovieCreditsModel, MovieCastPerson} from '../../interface/tmdb-data/Person';
import {SearchMovieQuery, SearchMovieResponse} from '../../interface/tmdb-data/searchMovie';
import {SearchPeopleQuery, SearchPeopleResponse} from '../../interface/tmdb-data/SearchPeople';
import {TVQuery, TVResponse} from '../../interface/tmdb-data/TV';
import {SearchTVQuery, SearchTVResponse} from '../../interface/tmdb-data/SearchTV';
import { MovieVideos } from 'src/app/interface/tmdb-data/Video';

//lien générique de l'api thmoviedb
const tmdbApi = 'https://api.themoviedb.org/3';
type HTTP_METHOD = 'GET' | 'POST' | 'DELETE' | 'PUT';
const lang = 'fr-FR';

function AlxToObjectString(data?: object): {[key: string]: string} {
  const res = {};
  for (const k of Object.keys(data || {}) ) {
    const v = data[k];
    res[k] = typeof v === 'string' ? v : JSON.stringify(v);
  }
  return res;
}

@Injectable({
  providedIn: 'root'
})
export class TmdbService {
  private apiKey: string;

  private async get<T>(url: string, data: object): Promise<HttpResponse<T>> {
    return this.http.get<T>( url, {
      observe: 'response',
      params: {...AlxToObjectString(data), api_key: this.apiKey}
    }).toPromise();
  }

  constructor(private http: HttpClient) {}

  //initalisation de la clé
  init(key: string): this {
    this.apiKey = key;
    return this;
  }

  // _______________________________________________________________________________________________________________________________________
  // Movies ________________________________________________________________________________________________________________________________
  // _______________________________________________________________________________________________________________________________________
  async getMovie(id: number, options?: MovieQuery): Promise<MovieResponse> {
    const url = `${tmdbApi}/movie/${id}?language=${lang}`;
    const res = await this.get<MovieResponse>(url, options);
    return res.body;
  }

  async searchMovie(query: SearchMovieQuery): Promise<SearchMovieResponse> {
    const url = `${tmdbApi}/search/movie`;
    const res = await this.get<SearchMovieResponse>(url, query);
    return res.body;
  }

  async getPopular(page: number, options?: MovieQuery): Promise<MovieResponse> {
    const url = `${tmdbApi}/discover/movie?language=${lang}&sort_by=popularity.desc&page=${page}`;
    const res = await this.get<MovieResponse>(url, options);
    return res.body;
  }

  async getUpComing(page : number, options?: MovieQuery): Promise<MovieResponse> {
    const url = `${tmdbApi}/movie/upcoming?language=${lang}&page=${page}`;
    const res = await this.get<MovieResponse>(url, options);
    return res.body;
  }

  async getGenre(page : number, genreId: number, options?: MovieQuery): Promise<MovieResponse> {
    const url = `${tmdbApi}/genre/${genreId}/movies?language=${lang}&page=${page}`;
    const res = await this.get<MovieResponse>(url, options);
    return res.body;
  }
  
  async getVideoMovie(id: number, options?: MovieQuery): Promise<MovieVideos> {
    const url = `${tmdbApi}/movie/${id}/videos?language=${lang}`;
    const res = await this.get<MovieVideos>(url, options);
    return res.body;
  }

  async getSimilarMovies(id: number, options?: MovieQuery): Promise<MovieResponse> {
    const url = `${tmdbApi}/movie/${id}/similar?language=${lang}`;
    const res = await this.get<MovieResponse>(url, options);
    return res.body;
  }


  // _______________________________________________________________________________________________________________________________________
  // Person / People _______________________________________________________________________________________________________________________
  // _______________________________________________________________________________________________________________________________________
  async getPerson(id: number, options?: PersonQuery): Promise<PersonResponse> {
    const url = `${tmdbApi}/person/${id}?language=${lang}`;
    const res = await this.get<PersonResponse>(url, options);
    return res.body;
  }

  async searchPerson(query: SearchPeopleQuery): Promise<SearchPeopleResponse> {
    const url = `${tmdbApi}/search/person?language=${lang}`;
    const res = await this.get<SearchPeopleResponse>(url, query);
    return res.body;
  }

  async getCreditsMovie(id: number, options?: MovieQuery): Promise<MovieCreditsModel> {
    const url = `${tmdbApi}/movie/${id}/credits?language=${lang}`;
    const res = await this.get<MovieCreditsModel>(url, options);
    return res.body;
  }

  async getPersonMovie(id: number, options?: MovieQuery): Promise<MovieCreditsModel> {
    const url = `${tmdbApi}/person/${id}/movie_credits?language=${lang}`;
    const res = await this.get<MovieCreditsModel>(url, options);
    return res.body;
  }

  async getPersonTV(id: number, options?: MovieQuery): Promise<PersonResponse> {
    const url = `${tmdbApi}/person/${id}/tv_credits?language=${lang}`;
    const res = await this.get<PersonResponse>(url, options);
    return res.body;
  }

  // _______________________________________________________________________________________________________________________________________
  // TV ____________________________________________________________________________________________________________________________________
  // _______________________________________________________________________________________________________________________________________
  async getTV(id: number, options?: TVQuery): Promise<TVResponse> {
    const url = `${tmdbApi}/tv/${id}`;
    const res = await this.get<TVResponse>(url, options);
    return res.body;
  }

  async searchTV(query: SearchTVQuery): Promise<SearchTVResponse> {
    const url = `${tmdbApi}/search/tv`;
    const res = await this.get<SearchTVResponse>(url, query);
    return res.body;
  }

  async getPopularTV(options?: MovieQuery): Promise<MovieResponse> {
    const url = `${tmdbApi}/discover/tv?language=${lang}&sort_by=popularity.desc`;
    const res = await this.get<MovieResponse>(url, options);
    return res.body;
  }

  async getUpComingTV(page : number, options?: MovieQuery): Promise<MovieResponse> {
    const url = `${tmdbApi}/tv/upcoming?language=${lang}&page=${page}`;
    const res = await this.get<MovieResponse>(url, options);
    return res.body;
  }


  // _______________________________________________________________________________________________________________________________________
  // Page ____________________________________________________________________________________________________________________________________
  // _______________________________________________________________________________________________________________________________________
  

}
