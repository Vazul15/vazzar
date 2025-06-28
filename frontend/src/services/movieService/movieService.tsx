import {
    movieDetailsAndCredits,
    topMoviesGeneral,
} from '@/components/types/movieTypes';
import {
    genresType,
    providerType,
} from '@/components/types/providerGenreTypes';

export async function getTopMoviesGeneral(): Promise<topMoviesGeneral> {
    const response = await fetch(`/api/movies/top-movies`);

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const moviesData: topMoviesGeneral = await response.json();

    console.log(moviesData);
    return moviesData;
}

export async function getPopularMoviesGeneral(): Promise<topMoviesGeneral> {
    const response = await fetch(`/api/movies/popular`);

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const moviesData: topMoviesGeneral = await response.json();
    return moviesData;
}

export async function getStreamingProviderNames(
    watchRegion: string,
): Promise<providerType[]> {
    const response = await fetch(
        `/api/movies/providers?watchRegion=${watchRegion}`,
    );

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    const providerData: providerType[] = await response.json();
    return providerData;
}

export async function getMoviesFromProviderById(
    providerId: number,
    watchRegion: string,
): Promise<topMoviesGeneral> {
    const response = await fetch(
        `/api/movies/id?providerId=${providerId}&watchRegion=${watchRegion}`,
    );

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    const providerMovies: topMoviesGeneral = await response.json();
    return providerMovies;
}

export async function getGenres(): Promise<genresType[]> {
    const response = await fetch('/api/movies/genres');

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    const genresData = await response.json();
    return genresData;
}

export async function getMoviesByGenre(
    genres: string[] | number,
    watchRegion: string,
) {
    let convertedGenres = '';

    if (Array.isArray(genres)) {
        convertedGenres = genres.join(',');
    } else {
        convertedGenres = genres.toString();
    }

    const response = await fetch(
        `/api/movies/genre?watchRegion=${watchRegion}&genres=${convertedGenres}`,
    );

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    console.log('Küldött genres paraméter:', convertedGenres);

    const genresData = await response.json();
    return genresData;
}

export async function getTrailerOfMovieByMovieId(movieId: number) {
    const response = await fetch(`/api/movies/trailer?movieId=${movieId}`);

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const videosData = await response.json();
    return videosData;
}

export async function getDetailsAndCreditsOfMovieByMovieId(
    movieId: number,
): Promise<movieDetailsAndCredits> {
    const response = await fetch(
        `/api/movies/movie-details?movieId=${movieId}`,
    );

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const videosData = await response.json();
    return videosData;
}
