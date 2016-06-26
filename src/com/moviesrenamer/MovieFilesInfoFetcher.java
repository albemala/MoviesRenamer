package com.moviesrenamer;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.jetbrains.annotations.NotNull;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MovieFilesInfoFetcher {

    public interface MovieFilesInfoFetcherListener {

        void onInfoFetched(MovieFile movieFile);

        void onInfoFetchingFinished();
    }

    private Stack<MovieFile> movieFileStack;
    private Thread[] threads;
    private final Object lock = new Object();

    public MovieFilesInfoFetcher() {
        movieFileStack = new Stack<>();
        threads = new Thread[3];
    }

    @NotNull
    private Thread getFetchInfoThread() {
        return new Thread(() -> {
            while (movieFileStack.size() > 0) {
                MovieFile movieFile;
                synchronized (lock) {
                    movieFile = movieFileStack.pop();
                }
                fetchGuessitInfo(movieFile);
                fetchTmdbInfo(movieFile);
            }
        });
    }

    public void fetchMovieFilesInfo(List<MovieFile> movieFiles) {
        for (MovieFile movieFile : movieFiles) {
            movieFileStack.push(movieFile);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i] = getFetchInfoThread();
            threads[i].start();
        }
    }

    private void fetchGuessitInfo(MovieFile movieFile) {
        Resty resty = new Resty();
        String originalMovieNameEncoded;
        try {
            originalMovieNameEncoded = URLEncoder.encode(movieFile.getOriginalName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        JSONResource jsonResponse;
        try {
            jsonResponse = resty.json("http://albemala.pythonanywhere.com/?filename=" + originalMovieNameEncoded);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        JSONObject jsonObject;
        try {
            jsonObject = jsonResponse.toObject();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return;
        }
        MovieGuessedInfo movieGuessedInfo = MovieGuessedInfoFactory.fromJSON(jsonObject);
        movieFile.setGuessedInfo(movieGuessedInfo);
    }

    private void fetchTmdbInfo(MovieFile movieFile) {

        TmdbApi tmdbApi = new TmdbApi("25be8b4eb94ac1d6a4991b76947327ca");

        MovieGuessedInfo guessedInfo = movieFile.getGuessedInfo();
        MovieResultsPage resultsPage = tmdbApi.getSearch().searchMovie(guessedInfo.title, guessedInfo.year, "", false, 0);
        List<MovieDb> results = resultsPage.getResults();

        List<MovieDb> movieDbs = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            int id = results.get(i).getId();
            MovieDb movieDb = tmdbApi.getMovies().getMovie(id, "");
            movieDbs.add(movieDb);
        }
        movieFile.setMovieInfo(movieDbs);
    }
}
