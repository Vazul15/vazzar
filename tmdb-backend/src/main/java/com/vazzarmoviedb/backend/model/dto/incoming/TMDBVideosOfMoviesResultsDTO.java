package com.vazzarmoviedb.backend.model.dto.incoming;

public record TMDBVideosOfMoviesResultsDTO(String iso_639_1, String name, String key, String site,Integer size, String type, Boolean official, String published_at, String id) {
}
