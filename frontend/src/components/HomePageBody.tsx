import { SparklesCore } from "./ui/sparkles";
import { HomePageNavBar } from "./navbar/HomePageNavBar";
import { Spotlight } from "./ui/Spotlight";

export const HomePageBody = () => {
  return (
    <>
      <div className="relative h-screen w-full bg-black flex flex-col items-center justify-center overflow-hidden">
        <div className="absolute top-0 left-0 w-full h-16 bg-gray-700">
          <HomePageNavBar />
        </div>
        <Spotlight className="absolute left-[25%] top-[15%] w-[20%] h-[80%] blur-[30px]" fill="white" />
        <h1 className="md:text-7xl text-3xl lg:text-9xl font-bold text-center text-white relative z-20">
          Vazzar
        </h1>
        <div className="w-[40rem] h-40 relative">
          <div className="absolute inset-x-20 top-0 bg-gradient-to-r from-transparent via-indigo-500 to-transparent h-[2px] w-3/4 blur-sm" />
          <div className="absolute inset-x-20 top-0 bg-gradient-to-r from-transparent via-indigo-500 to-transparent h-px w-3/4" />
          <div className="absolute inset-x-60 top-0 bg-gradient-to-r from-transparent via-sky-500 to-transparent h-[5px] w-1/4 blur-sm" />
          <div className="absolute inset-x-60 top-0 bg-gradient-to-r from-transparent via-sky-500 to-transparent h-px w-1/4" />

          <SparklesCore
            background="transparent"
            minSize={0.4}
            maxSize={1}
            particleDensity={1200}
            className="w-full h-full"
            particleColor="#FFFFFF"
          />

          <div className="absolute inset-0 w-full h-full bg-black [mask-image:radial-gradient(350px_200px_at_top,transparent_20%,white)]"></div>
        </div>
      </div>
    </>
  );
};
