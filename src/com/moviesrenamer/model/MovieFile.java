package com.moviesrenamer.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.List;

public class MovieFile {

    public enum State {
        Normal,
        Updating
    }

    private File originalFile;
    private SimpleStringProperty originalName;
    private SimpleStringProperty newName;
    private MovieGuessedInfo guessedInfo;
    private ObservableList<MovieInfo> movieInfoList;
    private int selectedMovieInfo;
    private IntegerProperty state;

    public MovieFile() {
        originalName = new SimpleStringProperty();
        newName = new SimpleStringProperty();
        movieInfoList = FXCollections.observableArrayList();
        guessedInfo = new MovieGuessedInfo();
        selectedMovieInfo = 0;
        state = new SimpleIntegerProperty(State.Normal.ordinal());
    }

    public void setOriginalFile(File originalFile) {
        this.originalFile = originalFile;
        originalName.set(originalFile.getName());
        newName.set("");
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public String getOriginalName() {
        return originalName.get();
    }

    public void setNewName(String newName) {
        this.newName.set(newName);
    }

    public String getNewName() {
        return newName.get();
    }

    public void setGuessedInfo(MovieGuessedInfo guessedInfo) {
        if (!guessedInfo.title.equals(""))
            this.guessedInfo.title = guessedInfo.title;
        if (guessedInfo.year != 0)
            this.guessedInfo.year = guessedInfo.year;
        if (!guessedInfo.language.equals(""))
            this.guessedInfo.language = guessedInfo.language;
        if (guessedInfo.cd != 0)
            this.guessedInfo.cd = guessedInfo.cd;
        if (guessedInfo.cdCount != 0)
            this.guessedInfo.cdCount = guessedInfo.cdCount;
    }

    public MovieGuessedInfo getGuessedInfo() {
        return guessedInfo;
    }

    public void setMovieInfo(List<MovieInfo> movieInfo) {
        movieInfoList.setAll(movieInfo);
    }

    public ObservableList<MovieInfo> getMovieInfo() {
        return movieInfoList;
    }

    public int getSelectedMovieInfo() {
        return selectedMovieInfo;
    }

    public void setSelectedMovieInfo(int selectedMovieInfo) {
        this.selectedMovieInfo = selectedMovieInfo;
    }

    public IntegerProperty getStateProperty() {
        return state;
    }

    public State getState() {
        return State.values()[state.get()];
    }

    public void setState(State state) {
        this.state.set(state.ordinal());
    }
}
