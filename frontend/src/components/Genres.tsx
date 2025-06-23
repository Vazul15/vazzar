import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { genresType } from "./types/providerGenreTypes";

interface genresProps {
  genres: genresType[];
  title: string;
}

export const Genres = ({ genres, title }: genresProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();

  const handleSelection = (genreId: number) => {
    navigate(`/browse/movies/by-genre/${genreId}`);
  };

  return (
    <div className="relative w-fit">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="text-white bg-gray-700 px-4 py-2 rounded hover:bg-gray-600 cursor-pointer"
      >
        {title}
      </button>
      {isOpen && (
        <ul
          className="absolute left-0 mt-2 bg-gray-800 text-white rounded shadow-md overflow-y-auto max-h-48"
          style={{ minWidth: `${Math.max(...genres.map((genre) => genre.name.length))}ch` }}
        >
          {genres.map((genre) => (
            <li key={genre.id} className="px-4 py-2 hover:bg-gray-600 cursor-pointer" onClick={() => handleSelection(genre.id)}>
              {genre.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};
