import { providerType } from '@/components/types/providerGenreTypes';
import {
    getMoviesFromProviderById,
    getStreamingProviderNames,
} from '@/services/movieService/movieService';
import { GalleryForProviders } from '@/components/ui/GalleryForProviders';
import React, { useEffect, useState } from 'react';
import { topMoviesGeneral } from '@/components/types/movieTypes';
import { GalleryForMovies } from '@/components/ui/GalleryForMovies';
import { GalleryForAllMovies } from '@/components/ui/GalleryForAllMovies';

export const StreamingProvidersPage = () => {
    const watchRegion = 'HU';
    const [providers, setProviders] = useState<providerType[]>([]);
    const [isSelected, setIsSelected] = useState<boolean>(false);
    const [selectedProvider, setSelectedProvider] =
        useState<providerType | null>();
    const [moviesOfproviders, setMoviesOfProviders] =
        useState<topMoviesGeneral>({
            page: 1,
            movies: [],
        });

    useEffect(() => {
        const fetchProviders = async () => {
            const providerData = await getStreamingProviderNames('HU');
            setProviders(providerData);
        };

        fetchProviders();
    }, []);

    useEffect(() => {
        if (selectedProvider) {
            const fetchMoviesOfProvider = async () => {
                const movieData = await getMoviesFromProviderById(
                    selectedProvider.providerId,
                    watchRegion,
                );
                setMoviesOfProviders(movieData);
            };
            fetchMoviesOfProvider();
        }
    }, [selectedProvider]);

    const handleSelection = (selectedProvider: providerType): void => {
        setSelectedProvider((provider) =>
            provider && provider.providerId === selectedProvider.providerId
                ? null
                : selectedProvider,
        );

        setIsSelected(selectedProvider !== null);
    };

    return (
        <div>
            <div>
                <GalleryForProviders
                    providers={providers}
                    onSelectProvider={handleSelection}
                />
            </div>
            {isSelected && selectedProvider && (
                <div>
                    <GalleryForAllMovies
                        movies={moviesOfproviders}
                        title={`Movies > ${selectedProvider.providerName}`}
                    />
                </div>
            )}
        </div>
    );
};
