package com.moviesrenamer.tasks;

import com.moviesrenamer.model.MovieFile;
import com.moviesrenamer.model.MovieGuessedInfo;
import com.moviesrenamer.factory.MovieGuessedInfoFactory;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GuessInfoTask extends Task {

    public interface GuessInfoTaskListener {
        void onGuessInfoTaskFinished(MovieFile movieFile);
    }

    private MovieFile movieFile;
    private GuessInfoTaskListener listener;
    private String searchString;

    public GuessInfoTask(MovieFile movieFile, String searchString) {
        this.movieFile = movieFile;
        this.searchString = searchString;
    }

    public void setListener(GuessInfoTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void execute() {
        Resty resty = new Resty();
        String originalMovieNameEncoded;
        try {
            originalMovieNameEncoded = URLEncoder.encode(searchString, "UTF-8");
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

        if (listener != null) listener.onGuessInfoTaskFinished(movieFile);
    }
}
