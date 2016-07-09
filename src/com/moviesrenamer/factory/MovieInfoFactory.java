package com.moviesrenamer.factory;

import com.moviesrenamer.model.MovieInfo;
import info.movito.themoviedbapi.model.MovieDb;
import org.jetbrains.annotations.NotNull;

public class MovieInfoFactory {
    public static @NotNull MovieInfo fromMovieDb(MovieDb movieDb) {
        MovieInfo movieInfo = new MovieInfo();
        movieInfo.setTitle(movieDb.getTitle());
        movieInfo.setOriginalTitle(movieDb.getOriginalTitle());
        movieInfo.setReleaseDate(movieDb.getReleaseDate().split("-")[0]);
        movieInfo.setRuntime(movieDb.getRuntime());
        return movieInfo;
    }
}
