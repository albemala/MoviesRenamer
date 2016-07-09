package com.moviesrenamer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MovieInfo {
    public SimpleStringProperty title;
    public SimpleStringProperty originalTitle;
    public SimpleStringProperty releaseDate;
    public SimpleIntegerProperty runtime;

    public MovieInfo() {
        title = new SimpleStringProperty();
        originalTitle = new SimpleStringProperty();
        releaseDate = new SimpleStringProperty();
        runtime = new SimpleIntegerProperty();
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getOriginalTitle() {
        return originalTitle.get();
    }

    public SimpleStringProperty originalTitleProperty() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle.set(originalTitle);
    }

    public String getReleaseDate() {
        return releaseDate.get();
    }

    public SimpleStringProperty releaseDateProperty() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate.set(releaseDate);
    }

    public int getRuntime() {
        return runtime.get();
    }

    public SimpleIntegerProperty runtimeProperty() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime.set(runtime);
    }
}
