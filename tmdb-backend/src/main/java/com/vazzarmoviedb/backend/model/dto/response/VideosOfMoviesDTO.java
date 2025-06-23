package com.vazzarmoviedb.backend.model.dto.response;

public record VideosOfMoviesDTO(String iso_639_1, String name, Integer size, String publishedAt, String trailerUrl) {
}