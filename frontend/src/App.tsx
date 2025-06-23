import { Route, Routes } from "react-router-dom";
import { HomePage } from "./pages/HomePage";
import { BrowsePage } from "./pages/BrowsePage";
import { MoviesPage } from "./pages/MoviesPage";
import { StreamingProvidersPage } from "./pages/StreamingProvidersPage";
import { MoviesByGenrePage } from "./pages/MoviesByGenrePage";
import { MovieDetailsPage } from "./pages/MovieDetailsPage";

function App() {
  return (
    <div className="bg-background min-h-screen">
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/browse" element={<BrowsePage />}>
          <Route path="/browse/movies" element={<MoviesPage />} />
          <Route path="/browse/movies/by-genre/:genreId" element={<MoviesByGenrePage />} />
          <Route path={"/browse/movies/:movieId"} element={< MovieDetailsPage />} />
          <Route path="/browse/streaming-providers" element={<StreamingProvidersPage />} />
        </Route>
      </Routes>
    </div>
  );
}

export default App;
