import React from 'react';
import { videoOfMovies } from '../types/movieTypes';

const MovieTrailer = ({ trailer }: { trailer: videoOfMovies }) => {
    return (
        <div
            style={{ position: 'relative', paddingBottom: '56.25%', height: 0 }}
        >
            <iframe
                src={`${trailer.trailerUrl}?loop=1&playlist=${
                    trailer.trailerUrl.split('/embed/')[1]
                }`}
                allow="autoplay; encrypted-media"
                allowFullScreen
                style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '60%',
                    outline: 'none',
                }}
            ></iframe>
        </div>
    );
};

export default MovieTrailer;
