package com.moviesrenamer.factory;

import com.moviesrenamer.model.MovieInfo;
import info.movito.themoviedbapi.model.MovieDb;
import org.jetbrains.annotations.NotNull;

public class MovieInfoFactory {
    public static @NotNull MovieInfo fromMovieDb(MovieDb movieDb) {
        MovieInfo movieInfo = new MovieInfo();
        movieInfo.title = movieDb.getTitle();
        movieInfo.originalTitle = movieDb.getOriginalTitle();
        movieInfo.releaseDate = movieDb.getReleaseDate().split("-")[0];
        movieInfo.runtime = movieDb.getRuntime();
        return movieInfo;
    }
}
