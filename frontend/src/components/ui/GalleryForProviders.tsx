import { providerType } from '../types/providerGenreTypes';

type GalleryProvidersProps = {
    providers: providerType[];
    onSelectProvider: (provider: providerType) => void;
};

export const GalleryForProviders = ({
    providers,
    onSelectProvider,
}: GalleryProvidersProps) => {
    return (
        <div className="flex flex-wrap justify-center gap-6 p-6">
            {providers.map((provider) => (
                <div
                    key={provider.providerId}
                    className="group relative flex flex-col items-center bg-gradient-to-b from-gray-800 to-gray-900 p-6 rounded-xl shadow-lg cursor-pointer transform transition-all duration-300 hover:-4xl"
                    onClick={() => onSelectProvider(provider)}
                >
                    <img
                        src={`${provider.logoPath}`}
                        alt={`${provider.providerName} Logo`}
                        className="w-15 h-15 object-contain transition-transform duration-300"
                    />
                    <p className="text-lg font-semibold mt-3 text-white tracking-wide">
                        {provider.providerName}
                    </p>

                    <div className="absolute inset-0 bg-white/20 opacity-0 transition-opacity duration-300 group-hover:opacity-50 rounded-xl"></div>
                </div>
            ))}
        </div>
    );
};
