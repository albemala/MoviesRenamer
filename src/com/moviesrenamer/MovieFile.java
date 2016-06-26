package com.moviesrenamer;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;

public class MovieFile {

    private File originalFile;
    private File renamedFile;
    private SimpleStringProperty originalName;
    private SimpleStringProperty newName;
    private MovieGuessedInfo guessedInfo;

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
}
