package com.moviesrenamer.model;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.util.List;

public class MovieFile {

    private File originalFile;
    private SimpleStringProperty originalName;
    private SimpleStringProperty newName;
    private MovieGuessedInfo guessedInfo;
    private List<MovieInfo> movieInfo;

    public MovieFile() {
        originalName = new SimpleStringProperty();
        newName = new SimpleStringProperty();
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
        this.movieInfo = movieInfo;
    }

    public List<MovieInfo> getMovieInfo() {
        return movieInfo;
    }
}
