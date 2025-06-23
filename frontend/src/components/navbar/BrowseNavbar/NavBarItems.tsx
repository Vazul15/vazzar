import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { motion } from "framer-motion";
import { Home, User, Briefcase, FileText } from "lucide-react";

const navbarItems = [
  {name: "Streaming Provider Movies", path: "/browse/streaming-providers", icon: FileText},
    { name: "My List", path: "/browse/my-list", icon: FileText },
    { name: "New & Popular", path: "/browse/new-popular", icon: FileText },
    { name: "Series", path: "/browse/series", icon: Briefcase },
    { name: "Movies", path: "/browse/movies", icon: User },
    { name: "Home", path: "/", icon: Home },
];

export const NavBarItems = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState(navbarItems[0].name);

  useEffect(() => {
    const currentItem = navbarItems.find(item => item.path === location.pathname);
    if (currentItem) {
      setActiveTab(currentItem.name);
    }
  }, [location.pathname]);

  const handleNavigateAndHighlight = (path: string, itemName: string) => {
    setActiveTab(itemName);
    navigate(path);
  };

  return (
    <div className="flex gap-3 flex-row-reverse pl-10">
      {navbarItems.map((item) => {
        const Icon = item.icon;
        const isActive = activeTab === item.name;

        return (
          <div
            key={item.name}
            onClick={() => handleNavigateAndHighlight(item.path, item.name)}
            className={`relative cursor-pointer text-sm font-semibold px-4 py-2 rounded-full transition-colors text-foreground/80 hover:text-primary ${
              isActive ? "bg-muted !text-black" : ""
            }`}
          >
            <span className="hidden md:inline">{item.name}</span>
            <span className="md:hidden">
              <Icon size={18} strokeWidth={2.5} />
            </span>

            {isActive && (
              <motion.div
                layoutId="lamp"
                className="absolute inset-0 w-full bg-primary/5 rounded-full -z-10"
                initial={false}
                transition={{
                  type: "spring",
                  stiffness: 300,
                  damping: 30,
                }}
              >
                <div className="absolute -top-2 left-1/2 -translate-x-1/2 w-8 h-1 bg-primary rounded-t-full">
                  <div className="absolute w-12 h-6 bg-primary/20 rounded-full blur-md -top-2 -left-2" />
                  <div className="absolute w-8 h-6 bg-primary/20 rounded-full blur-md -top-1" />
                  <div className="absolute w-4 h-4 bg-primary/20 rounded-full blur-sm top-0 left-2" />
                </div>
              </motion.div>
            )}
          </div>
        );
      })}
    </div>
  );
};
