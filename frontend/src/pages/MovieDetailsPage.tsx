import { MovieDetails } from '@/components/movieDetailsComponents/MovieDetails';
import MovieTrailer from '@/components/movieDetailsComponents/MovieTrailer';
import {
    credits,
    movieDetails,
    videoOfMovies,
} from '@/components/types/movieTypes';
import { GalleryForCredits } from '@/components/ui/GalleryForCredits';
import {
    getDetailsAndCreditsOfMovieByMovieId,
    getTrailerOfMovieByMovieId,
} from '@/services/movieService/movieService';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

export const MovieDetailsPage = () => {
    const { movieId } = useParams();
    const [videosOfMovie, setVideosOfMovie] = useState<videoOfMovies[]>([]);
    const [trailer, setTrailer] = useState<videoOfMovies | null>(null);
    const [details, setDetails] = useState<movieDetails | null>(null);
    const [credits, setCredits] = useState<credits | null>(null);

    useEffect(() => {
        const fetchVideosOfMovie = async () => {
            if (movieId) {
                const videosResponse: videoOfMovies[] =
                    await getTrailerOfMovieByMovieId(Number(movieId));
                setVideosOfMovie(videosResponse);
                const oldest = videosResponse
                    .filter((video) => video.publishedAt)
                    .sort(
                        (a, b) =>
                            new Date(a.publishedAt).getTime() -
                            new Date(b.publishedAt).getTime(),
                    )[0];

                setTrailer(oldest);

                const detailsAndCreditsOfMovies =
                    await getDetailsAndCreditsOfMovieByMovieId(Number(movieId));
                setCredits(detailsAndCreditsOfMovies.credits);
                setDetails(detailsAndCreditsOfMovies.movieDetails);
            }
        };

        fetchVideosOfMovie();
    }, [movieId]);

    return (
        <div>
            {credits && details && (
                <MovieDetails credits={credits} details={details} />
            )}
            {credits && <GalleryForCredits credits={credits} title="Cast" />}
            {trailer && <MovieTrailer trailer={trailer} />}
        </div>
    );
};
