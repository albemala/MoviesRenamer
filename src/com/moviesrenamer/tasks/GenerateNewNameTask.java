package com.moviesrenamer.tasks;

import com.moviesrenamer.model.MovieFile;

public class GenerateNewNameTask extends Task {

    private String renamingRule;
    private MovieFile movieFile;

    public GenerateNewNameTask(String renamingRule, MovieFile movieFile) {
        this.renamingRule = renamingRule;
        this.movieFile = movieFile;
    }

    @Override
    public void execute() {

    }
}
