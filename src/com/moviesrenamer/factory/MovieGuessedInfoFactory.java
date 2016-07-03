package com.moviesrenamer.factory;

import com.moviesrenamer.model.MovieGuessedInfo;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

public class MovieGuessedInfoFactory {

    public static MovieGuessedInfo fromJSON(JSONObject jsonObject) {
        MovieGuessedInfo movieGuessedInfo = new MovieGuessedInfo();
        try {
            movieGuessedInfo.title = jsonObject.getString("title");
        } catch (JSONException ignored) {
        }
        try {
            movieGuessedInfo.year = jsonObject.getInt("year");
        } catch (JSONException ignored) {
        }
        try {
            movieGuessedInfo.language = jsonObject.getString("language");
        } catch (JSONException ignored) {
        }
        try {
            movieGuessedInfo.cd = jsonObject.getInt("cd");
        } catch (JSONException ignored) {
        }
        try {
            movieGuessedInfo.cdCount = jsonObject.getInt("cd_count");
        } catch (JSONException ignored) {
        }
        return movieGuessedInfo;
    }
}
