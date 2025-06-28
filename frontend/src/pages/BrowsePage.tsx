import { BrowseNavBar } from '@/components/navbar/BrowseNavbar/BrowseNavBar';
import { Outlet } from 'react-router-dom';

export const BrowsePage = () => {
    return (
        <div className="bg-background">
            <BrowseNavBar />
            <Outlet />
        </div>
    );
};
