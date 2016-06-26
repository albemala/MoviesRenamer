package com.moviesrenamer;

import org.jetbrains.annotations.NotNull;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        System.out.println(movieGuessedInfo.title);
        movieFile.setGuessedInfo(movieGuessedInfo);
    }

    private void fetchTmdbInfo(MovieFile movieFile) {

//        TmdbApi tmdbApi = new TmdbApi("25be8b4eb94ac1d6a4991b76947327ca");
//
//        MovieGuessedInfo guessedInfo = movieFile.getGuessedInfo();
//        System.out.println(guessedInfo.title+", "+ guessedInfo.year);
//        MovieResultsPage resultsPage = tmdbApi.getSearch().searchMovie(guessedInfo.title, guessedInfo.year, "", false, 0);
//        for (MovieDb movieDb : resultsPage.getResults()) {
//            System.out.println(movieDb.toString());
//        }


//        TheMovieDbApi tmdb;
//        try {
//            tmdb = new TheMovieDbApi("25be8b4eb94ac1d6a4991b76947327ca");
//        } catch (MovieDbException e) {
//            e.printStackTrace();
//            return;
//        }
//        MovieGuessedInfo guessedInfo = movieFile.getGuessedInfo();
//        try {
//            ResultList<MovieInfo> resultList = tmdb.searchMovie(guessedInfo.title, 0, "English", false, guessedInfo.year, 0, null);
//            for (int i = 0; i < resultList.getResults().size(); i++) {
//                MovieInfo movieInfo = resultList.getResults().get(i);
//                System.out.println(movieInfo.toString());
//                System.out.println(movieInfo.getReleaseDate());
//                System.out.println(movieInfo.getRuntime());
//                int id = movieInfo.getId();
//            }
//        } catch (MovieDbException e) {
//            e.printStackTrace();
//            return;
//        }
    }
}
