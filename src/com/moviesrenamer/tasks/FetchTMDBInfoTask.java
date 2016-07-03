package com.moviesrenamer.tasks;

import com.moviesrenamer.factory.MovieInfoFactory;
import com.moviesrenamer.model.MovieFile;
import com.moviesrenamer.model.MovieGuessedInfo;
import com.moviesrenamer.model.MovieInfo;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

import java.util.ArrayList;
import java.util.List;

public class FetchTMDBInfoTask extends Task {

    public interface FetchTMDBInfoTaskListener {

        void onFetchTMDBInfoTaskFinished(MovieFile movieFile);
    }

    private MovieFile movieFile;
    private FetchTMDBInfoTaskListener listener;

    public FetchTMDBInfoTask(MovieFile movieFile) {
        this.movieFile = movieFile;
    }

    public void setListener(FetchTMDBInfoTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void execute() {
        TmdbApi tmdbApi = new TmdbApi("25be8b4eb94ac1d6a4991b76947327ca");

        MovieGuessedInfo guessedInfo = movieFile.getGuessedInfo();
        MovieResultsPage resultsPage = tmdbApi.getSearch().searchMovie(guessedInfo.title, guessedInfo.year, "", false, 0);
        List<MovieDb> results = resultsPage.getResults();

        List<MovieInfo> movieInfoList = new ArrayList<>();
        for (MovieDb result : results) {
            int id = result.getId();
            MovieDb movieDb = tmdbApi.getMovies().getMovie(id, "");
            MovieInfo movieInfo = MovieInfoFactory.fromMovieDb(movieDb);
            movieInfoList.add(movieInfo);
        }
        movieFile.setMovieInfo(movieInfoList);

        if (listener != null) listener.onFetchTMDBInfoTaskFinished(movieFile);
    }
}