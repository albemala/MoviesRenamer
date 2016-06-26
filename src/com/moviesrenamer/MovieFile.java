package com.moviesrenamer;

import info.movito.themoviedbapi.model.MovieDb;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.util.List;

public class MovieFile {

    private File originalFile;
    private File renamedFile;
    private SimpleStringProperty originalName;
    private SimpleStringProperty newName;
    private MovieGuessedInfo guessedInfo;
    private List<MovieDb> movieInfo;

    public MovieFile(File originalFile) {
        this.originalFile = originalFile;
        renamedFile = new File("");
        originalName = new SimpleStringProperty(originalFile.getName());
//        newName=new SimpleStringProperty(renamedFile.getName());
        newName = new SimpleStringProperty("");
    }

    public String getOriginalName() {
        return originalName.get();
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

    public void setMovieInfo(List<MovieDb> movieInfo) {
        this.movieInfo = movieInfo;
    }

    public List<MovieDb> getMovieInfo() {
        return movieInfo;
    }
}
