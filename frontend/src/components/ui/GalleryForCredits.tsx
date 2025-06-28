import { ArrowLeft, ArrowRight } from 'lucide-react';
import { useEffect, useState } from 'react';

import { Button } from '@/components/ui/button';
import {
    Carousel,
    CarouselApi,
    CarouselContent,
    CarouselItem,
} from '@/components/ui/carousel';
import { cast, credits } from '../types/movieTypes';

export interface CreditsProps {
    credits: credits;
    title: string;
}

export const GalleryForCredits = ({ credits }: CreditsProps) => {
    const [carouselApiActors, setCarouselApiActors] = useState<CarouselApi>();
    const [carouselApiDirectors, setCarouselApiDirectors] =
        useState<CarouselApi>();
    const [canScrollPrevActors, setCanScrollPrevActors] = useState(false);
    const [canScrollNextActors, setCanScrollNextActors] = useState(false);
    const [canScrollPrevDirectors, setCanScrollPrevDirectors] = useState(false);
    const [canScrollNextDirectors, setCanScrollNextDirectors] = useState(false);

    useEffect(() => {
        if (!carouselApiActors) return;
        const updateSelectionActors = () => {
            setCanScrollPrevActors(carouselApiActors.canScrollPrev());
            setCanScrollNextActors(carouselApiActors.canScrollNext());
        };
        updateSelectionActors();
        carouselApiActors.on('select', updateSelectionActors);
        return () => {
            carouselApiActors.off('select', updateSelectionActors);
        };
    }, [carouselApiActors]);

    useEffect(() => {
        if (!carouselApiDirectors) return;
        const updateSelectionDirectors = () => {
            setCanScrollPrevDirectors(carouselApiDirectors.canScrollPrev());
            setCanScrollNextDirectors(carouselApiDirectors.canScrollNext());
        };
        updateSelectionDirectors();
        carouselApiDirectors.on('select', updateSelectionDirectors);
        return () => {
            carouselApiDirectors.off('select', updateSelectionDirectors);
        };
    }, [carouselApiDirectors]);

    return (
        <section className="py-18">
            <div className="w-full pl-6">
                <div className="mb-8 flex items-end justify-between md:mb-14 lg:mb-16">
                    <h2 className="text-2xl md:text-4xl font-extrabold text-gray-100 font-mono">
                        Cast
                    </h2>
                    <div className="hidden shrink-0 gap-2 md:flex">
                        <Button
                            size="icon"
                            variant="ghost"
                            onClick={() => carouselApiActors?.scrollPrev()}
                            disabled={!canScrollPrevActors}
                        >
                            <ArrowLeft className="size-5" />
                        </Button>
                        <Button
                            size="icon"
                            variant="ghost"
                            onClick={() => carouselApiActors?.scrollNext()}
                            disabled={!canScrollNextActors}
                        >
                            <ArrowRight className="size-5" />
                        </Button>
                    </div>
                </div>
                <div className="w-full">
                    <Carousel
                        setApi={setCarouselApiActors}
                        opts={{ dragFree: true }}
                    >
                        <CarouselContent className="ml-0">
                            {credits.cast.map((actor) => (
                                <CarouselItem
                                    key={actor.id}
                                    className="max-w-[200px] px-2"
                                >
                                    <div className="group relative flex flex-col items-center">
                                        <img
                                            src={actor.profileUrl}
                                            className="w-40 h-40 rounded-full object-cover border border-gray-700"
                                        />
                                        <div className="text-white text-mg font-semibold mt-3">
                                            {actor.name}
                                        </div>
                                        <div className="text-gray-400 text-sm">
                                            {actor.character}
                                        </div>
                                    </div>
                                </CarouselItem>
                            ))}
                        </CarouselContent>
                    </Carousel>
                </div>

                {/* Directors Section */}
                <div className="mt-12 mb-8 flex items-end justify-between md:mb-14 lg:mb-16">
                    <h2 className="text-2xl md:text-4xl font-extrabold text-gray-100 font-mono">
                        Director(s)
                    </h2>
                    <div className="hidden shrink-0 gap-2 md:flex">
                        <Button
                            size="icon"
                            variant="ghost"
                            onClick={() => carouselApiDirectors?.scrollPrev()}
                            disabled={!canScrollPrevDirectors}
                        >
                            <ArrowLeft className="size-5" />
                        </Button>
                        <Button
                            size="icon"
                            variant="ghost"
                            onClick={() => carouselApiDirectors?.scrollNext()}
                            disabled={!canScrollNextDirectors}
                        >
                            <ArrowRight className="size-5" />
                        </Button>
                    </div>
                </div>
                <div className="w-full">
                    <Carousel
                        setApi={setCarouselApiDirectors}
                        opts={{ dragFree: true }}
                    >
                        <CarouselContent className="ml-0">
                            {credits.crew.map((director) => (
                                <CarouselItem
                                    key={director.id}
                                    className="max-w-[200px] px-2"
                                >
                                    <div className="group relative flex flex-col items-center">
                                        <img
                                            src={director.profileUrl}
                                            className="w-40 h-40 rounded-full object-cover border border-gray-700"
                                        />
                                        <div className="text-white text-mg font-semibold mt-3">
                                            {director.name}
                                        </div>
                                    </div>
                                </CarouselItem>
                            ))}
                        </CarouselContent>
                    </Carousel>
                </div>
            </div>
        </section>
    );
};
