package com.moviesrenamer;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class MovieFileFactory {

    public static void addMovieFileToList(File file, ObservableList<MovieFile> movieFiles) {
        if (MovieFileUtils.isMovieFile(file)) {
            movieFiles.add(new MovieFile(file));
        }
    }

    public static void addMovieFilesToList(List<File> files, ObservableList<MovieFile> movieFiles) {
        for (File file : files) {
            addMovieFileToList(file, movieFiles);
        }
    }

    public static void addMovieFilesInDirectoryToList(File directory, ObservableList<MovieFile> movieFiles) {
        List<File> files = FileUtils.getFilesInDirectory(directory);
        addMovieFilesToList(files, movieFiles);
    }

    public static void addMovieFilesInDirectoryTreeToList(File directory, final ObservableList<MovieFile> movieFiles) {
        try {
            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    addMovieFileToList(file.toFile(), movieFiles);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
