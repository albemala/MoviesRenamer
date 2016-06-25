package com.moviesrenamer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class MovieFilesManager {

    private ObservableList<MovieFile> movieFiles;

    public MovieFilesManager() {
        movieFiles = FXCollections.observableArrayList(
                new MovieFile(new File("/Users/albertomalagoli/Downloads/Night of the Living Dead[1968]DvDrip[Eng]-Stealthmaster.avi")),
                new MovieFile(new File("/Users/albertomalagoli/Downloads/Paura e delirio a Las Vegas (1998, 118).avi")),
                new MovieFile(new File("/Users/albertomalagoli/Downloads/Ricomincio Da Capo.avi"))
        );
    }

    public ObservableList<MovieFile> getMovieFiles() {
        return movieFiles;
    }

    public void clearMovieFiles() {
        movieFiles.clear();
    }

    public void removeMovieFiles(List<MovieFile> files) {
        movieFiles.removeAll(files);
    }

    public void addMovieFileToList(File file) {
        if (MovieFileUtils.isMovieFile(file)) {
            movieFiles.add(new MovieFile(file));
        }
    }

    public void addMovieFilesToList(List<File> files) {
        for (File file : files) {
            addMovieFileToList(file);
        }
    }

    public void addMovieFilesInDirectoryToList(File directory) {
        List<File> files = FileUtils.getFilesInDirectory(directory);
        addMovieFilesToList(files);
    }

    public void addMovieFilesInDirectoryTreeToList(File directory) {
        try {
            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    addMovieFileToList(file.toFile());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
