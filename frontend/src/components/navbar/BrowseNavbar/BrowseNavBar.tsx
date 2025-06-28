import { useEffect, useState } from 'react';
import { NavBarItems } from './NavBarItems';
import { NavBarSearch } from './NavBarSearch';

export function BrowseNavBar() {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const handleResize = () => {
            setIsMobile(window.innerWidth < 768);
        };

        handleResize();
        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    return (
        <div className="flex items-center justify-between w-full bg-background/5 border border-border backdrop-blur-lg py-2 px-4 rounded-full shadow-lg">
            <NavBarItems />
            <NavBarSearch />
        </div>
    );
}
