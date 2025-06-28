import React, { useState } from 'react';
import { Search } from 'lucide-react';

export const NavBarSearch = () => {
    const [query, setQuery] = useState('');

    const handleSearch = () => {
        console.log('Searching for:', query);
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    };

    return (
        <div className="w-1/7 relative ml-auto">
            <input
                type="search"
                placeholder="Search"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                onKeyDown={handleKeyDown}
                className="pr-10 bg-zinc-800 text-white w-full rounded-full focus:placeholder-transparent h-11"
            />
            <button
                type="button"
                onClick={handleSearch}
                className="absolute right-3 top-1/2 transform -translate-y-1/2"
            >
                <Search className="h-5 w-8 text-gray-500 hover:text-gray-700" />
            </button>
        </div>
    );
};
