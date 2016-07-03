package com.moviesrenamer;

import com.moviesrenamer.model.MovieFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class MovieFilesManager {

    private ObservableList<MovieFile> movieFiles;

    public MovieFilesManager() {
        movieFiles = FXCollections.observableArrayList(
                new MovieFile(),
                new MovieFile(),
                new MovieFile()
//                new MovieFile(),
//                new MovieFile(),
//                new MovieFile()
        );
        movieFiles.get(0).setOriginalFile(new File("/Users/albertomalagoli/Downloads/Paura e delirio a Las Vegas (1998, 118).avi"));
        movieFiles.get(1).setOriginalFile(new File("/Users/albertomalagoli/Downloads/Night of the Living Dead[1968]DvDrip[Eng]-Stealthmaster.avi"));
        movieFiles.get(2).setOriginalFile(new File("/Users/albertomalagoli/Downloads/Ricomincio Da Capo.avi"));
//        movieFiles.get(3).setOriginalFile(new File("/Users/albertomalagoli/Downloads/Collateral.2004.720p.BrRip.x264.YIFY.mp4"));
//        movieFiles.get(4).setOriginalFile(new File("/Users/albertomalagoli/Downloads/Jaws.1975.720p.BrRip.x264.bitloks.YIFY.mp4"));
//        movieFiles.get(5).setOriginalFile(new File("/Users/albertomalagoli/Downloads/L'Esorcista [Divx -ITA] [Anacletus] cd 1 of 2.avi"));
    }

    @NotNull
    public ObservableList<MovieFile> getMovieFiles() {
        return movieFiles;
    }

    public void clearMovieFiles() {
        movieFiles.clear();
    }

    public void removeMovieFiles(List<MovieFile> files) {
        movieFiles.removeAll(files);
    }

    @NotNull
    public List<MovieFile> addMovieFilesToList(List<File> files) {
        List<MovieFile> addedMovieFiles = new ArrayList<>(files.size());
        for (File file : files) {
            @Nullable MovieFile movieFile = addMovieFileToList(file);
            if (movieFile != null) {
                addedMovieFiles.add(movieFile);
            }
        }
        return addedMovieFiles;
    }

    @NotNull
    public List<MovieFile> addMovieFilesInDirectoryToList(File directory) {
        List<File> files = FileUtils.getFilesInDirectory(directory);
        return addMovieFilesToList(files);
    }

    @NotNull
    public List<MovieFile> addMovieFilesInDirectoryTreeToList(File directory) {
        List<MovieFile> addedMovieFiles = new ArrayList<>();
        try {
            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    @Nullable MovieFile movieFile = addMovieFileToList(file.toFile());
                    if (movieFile != null) {
                        addedMovieFiles.add(movieFile);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addedMovieFiles;
    }

    @Nullable
    private MovieFile addMovieFileToList(File file) {
        if (MovieFileUtils.isMovieFile(file)) {
            MovieFile movieFile = new MovieFile();
            movieFile.setOriginalFile(file);
            movieFiles.add(movieFile);
            return movieFile;
        }
        return null;
    }
}
