import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { topMoviesGeneral } from '@/components/types/movieTypes';
import {
    getGenres,
    getMoviesByGenre,
} from '@/services/movieService/movieService';
import { GalleryForAllMovies } from '@/components/ui/GalleryForAllMovies';
import { genresType } from '@/components/types/providerGenreTypes';

export const MoviesByGenrePage = () => {
    const { genreId } = useParams();
    const watchRegion = 'HU';

    const [moviesByGenre, setMoviesByGenre] = useState<topMoviesGeneral>({
        page: 1,
        movies: [],
    });
    const [genresNameId, setGenresNameId] = useState<genresType[]>([]);
    const [genreName, setGenreName] = useState<string>('');

    const getnameById = (genreId: number) => {
        const genreFound = genresNameId.find(
            (genreMap) => genreMap.id === genreId,
        );
        if (genreFound) {
            setGenreName(genreFound.name);
        }
    };

    useEffect(() => {
        const fetchMoviesByGenreAndGenreName = async () => {
            if (genreId) {
                const movieData = await getMoviesByGenre(
                    Number(genreId),
                    watchRegion,
                );
                const genreNames = await getGenres();
                setMoviesByGenre(movieData);
                setGenresNameId(genreNames);
            }
        };

        fetchMoviesByGenreAndGenreName();
    }, [genreId]);

    useEffect(() => {
        if (genresNameId.length > 0 && genreId) {
            getnameById(Number(genreId));
        }
    }, [genresNameId, genreId]);

    return (
        <div>
            <GalleryForAllMovies
                movies={moviesByGenre}
                title={`Movies > ${genreName}`}
            />
        </div>
    );
};
