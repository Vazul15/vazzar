package com.vazzarmoviedb.radarrbackend.exception;

public class MovieNotFoundByIdException extends RuntimeException {
    public MovieNotFoundByIdException(String message) {
        super(message);
    }
}
