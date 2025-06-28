import React from 'react';
import { credits, movieDetails } from '../types/movieTypes';
import { TextGenerateEffect } from '../ui/text-generate-effect';
import { Button } from '../ui/button';

export const MovieDetails = ({
    credits,
    details,
}: {
    credits: credits;
    details: movieDetails;
}) => {
    return (
        <div className="relative h-screen w-screen">
            <div
                className="absolute inset-0 bg-cover bg-center"
                style={{
                    backgroundImage: `url(${details?.backDropUrl})`,
                    filter: 'blur(0px)',
                }}
            />

            <div className="absolute inset-0 bg-black/40" />
            <div className="flex flex-row items-start px-8 py-6 gap-6 relative z-10">
                <img
                    className="h-120 w-auto rounded-lg shadow-lg m-5"
                    src={details?.posterUrl}
                    alt="Film posztere"
                />

                <div className="max-w-md text-white mt-6">
                    <h1 className="font-poppins text-4xl font-bold text-white">
                        {details?.title}
                    </h1>
                    <h2 className="text-xs text-gray-300 font-light">
                        Directed by:&nbsp;
                        {credits?.crew && credits.crew.length > 1
                            ? credits.crew.map((director, index) => (
                                  <span key={index}>
                                      {director.name}
                                      {index < credits.crew.length - 1
                                          ? ', '
                                          : ''}
                                  </span>
                              ))
                            : credits?.crew?.[0]?.name}
                    </h2>

                    <p className="text-lg text-gray-100 font-light mt-8">
                        {details?.releaseDate.split('-')[0]} •{' '}
                        {details?.runtime}m
                    </p>
                    <p className="text-sm text-gray-400 font-light">
                        {details?.genres.map((genre) => genre.name).join(', ')}
                    </p>

                    <Button variant="default" className="mt-8 text-black">
                        Download
                    </Button>
                </div>
            </div>

            <div className="absolute top-20 right-8 max-w-md text-white bg-gradient-to-r from-black/70 via-black/30 to-black/70 backdrop-blur-lg p-6 rounded-lg shadow-xl">
                {details?.overview && (
                    <div>
                        <p className="text-sm uppercase tracking-widest text-gray-400">
                            Overview
                        </p>
                        <TextGenerateEffect
                            duration={3}
                            filter={false}
                            words={details?.overview}
                            className="font-serif text-lg text-gray-300 leading-relaxed"
                        />
                    </div>
                )}
            </div>
        </div>
    );
};
