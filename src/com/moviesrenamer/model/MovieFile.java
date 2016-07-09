package com.moviesrenamer.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.List;

public class MovieFile {

    private File originalFile;
    private SimpleStringProperty originalName;
    private SimpleStringProperty newName;
    private MovieGuessedInfo guessedInfo;
    private ObservableList<MovieInfo> movieInfoList;
    private int selectedMovieInfo;

    public MovieFile() {
        originalName = new SimpleStringProperty();
        newName = new SimpleStringProperty();
        movieInfoList = FXCollections.observableArrayList();
        selectedMovieInfo = 0;
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
        this.guessedInfo = guessedInfo;
    }

    public MovieGuessedInfo getGuessedInfo() {
        return guessedInfo;
    }

    public void setMovieInfo(List<MovieInfo> movieInfo) {
        movieInfoList.addAll(movieInfo);
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
}
