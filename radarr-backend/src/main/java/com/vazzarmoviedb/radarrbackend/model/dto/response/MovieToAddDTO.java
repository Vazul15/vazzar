package com.vazzarmoviedb.radarrbackend.model.dto.response;

import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.AddMovieRequestDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.AddOptionsDTO;


//Here I'm still thinking if I should give here the default values or in the service instead
public record MovieToAddDTO(
        int tmdbId,
        String title,
        int year
) {
    public AddMovieRequestDTO toRadarrRequest(String rootFolderPath, int qualityProfileId) {
        AddOptionsDTO optionsDTO = new AddOptionsDTO(true);
        String titleSlug = title.toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + year;
        String path = rootFolderPath + "/" + title + " (" + year + ")";
        return new AddMovieRequestDTO(
                tmdbId,
                title,
                year,
                qualityProfileId,
                rootFolderPath,
                true,
                optionsDTO,
                titleSlug,
                path
        );
    }
}
