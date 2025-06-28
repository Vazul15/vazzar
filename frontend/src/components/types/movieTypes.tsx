import { genresType } from './providerGenreTypes';

export type generalMovie = {
    id: number;
    title: string;
    genres: number[];
    overview: string;
    backdropUrl: string;
    posterUrl: string;
};

export interface topMoviesGeneral {
    page: number;
    movies: generalMovie[];
}

export type videoOfMovies = {
    iso_639_1: string;
    name: string;
    size: number;
    publishedAt: string;
    trailerUrl: string;
};

export type movieDetails = {
    backDropUrl: string;
    budget: number;
    genres: genresType[];
    homepage: string;
    id: number;
    originCountry: string[];
    originalLanguage: string;
    originalTitle: string;
    overview: string;
    posterUrl: string;
    productionCompanies: productionCompanies;
    releaseDate: string;
    revenue: number;
    runtime: number;
    status: string;
    title: string;
};

export type productionCompanies = {
    id: number;
    logoUrl: string;
    name: string;
    originCountry: string;
};

export type crew = {
    id: number;
    knownForDepartment: string;
    name: string;
    originalName: string;
    profileUrl: string;
    department: string;
    job: string;
};

export type cast = {
    id: number;
    knownForDepartment: string;
    name: string;
    originalName: string;
    profileUrl: string;
    character: string;
};

export interface credits {
    cast: cast[];
    crew: crew[];
}

export type movieDetailsAndCredits = {
    movieDetails: movieDetails;
    credits: credits;
};
