import { useNavigate } from "react-router-dom";
import { topMoviesGeneral } from "../types/movieTypes";

export const GalleryForAllMovies = ({
  movies,
  title,
}: {
  movies: topMoviesGeneral;
  title: string;
}) => {
  const navigate = useNavigate();

  const handleSelection = (movieId: number) => {
    navigate(`/browse/movies/${movieId}`);
  };

  return (
    <section className="py-18">
      <div className="container mx-auto">
        <h2 className="text-2xl md:text-4xl font-extrabold tracking-wide text-gray-100 font-mono mb-6">
          {title}
        </h2>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-4">
        {movies.movies.map((movie) => (
          <div
            key={movie.id}
            className="group rounded-xl overflow-hidden w-full"
          >
            <div className="relative" onClick={() => handleSelection(movie.id)}>
              <img
                src={movie.posterUrl}
                alt={movie.title}
                className="w-full h-auto rounded-xl object-cover transition-transform duration-300 group-hover:scale-110"
              />

              <div className="absolute inset-x-0 bottom-0 translate-y-full opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all duration-300 bg-black/80 p-4 rounded-b-xl">
                <h3 className="text-sm md:text-base lg:text-lg font-semibold truncate text-white">
                  {movie.title}
                </h3>
                <p className="text-xs md:text-sm text-gray-300 line-clamp-2">
                  {movie.genres}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};
