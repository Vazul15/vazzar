import { GalleryForMovies } from "@/components/ui/GalleryForMovies";
import { topMoviesGeneral } from "@/components/types/movieTypes";
import {
  getGenres,
  getPopularMoviesGeneral,
  getTopMoviesGeneral,
} from "@/services/movieService/movieService";
import { useEffect, useState } from "react";
import { Genres } from "@/components/Genres";
import { genresType } from "@/components/types/providerGenreTypes";

export const MoviesPage = () => {
  const [topMovies, setTopMovies] = useState<topMoviesGeneral>({
    page: 1,
    movies: [],
  });
  const [popularMovies, setPopularMovies] = useState<topMoviesGeneral>({
    page: 1,
    movies: [],
  });
  const [genres, setGenres] = useState<genresType[]>([]);

  useEffect(() => {
    const fetchMovies = async () => {
      const movieData = await getTopMoviesGeneral();
      const popularMovieData = await getPopularMoviesGeneral();
      const fetchGenres = await getGenres();
      setTopMovies(movieData);
      setPopularMovies(popularMovieData);
      setGenres(fetchGenres);
    };

    fetchMovies();
  }, []);

  return (
    <div>
      <div className=" w-1/4 p-4 mb-6">
        <Genres genres={genres} title="Genres:" />
      </div>
      <div className="flex flex-col gap-2 mt-8">
        <GalleryForMovies movies={popularMovies} title="Popular Movies" />
        <GalleryForMovies movies={topMovies} title="Top Rated Movies" />
      </div>
    </div>
  );
};
